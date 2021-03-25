package com.example.framelibrary.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjcai on 2021/3/25.
 * 专门用来查询的类
 * 利用builder设计模式 设置各种参数
 */
public class QuerySupport<T> {

    private static final String TAG = "QuerySupport";
    // 查询的列
    private String[] mQueryColumns;
    // 查询的条件
    private String mQuerySelection;
    // 查询的参数
    private String[] mQuerySelectionArgs;
    // 查询分组
    private String mQueryGroupBy;
    // 查询对结果集进行过滤
    private String mQueryHaving;
    // 查询排序
    private String mQueryOrderBy;
    // 查询可用于分页
    private String mQueryLimit;

    private final Class<T> mClazz;
    private final SQLiteDatabase mSQLiteDatabase;

    public QuerySupport(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mClazz = clazz;
        this.mSQLiteDatabase = sqLiteDatabase;
    }

    public QuerySupport<T> columns(String... columns) {
        this.mQueryColumns = columns;
        return this;
    }

    public QuerySupport<T> selectionArgs(String... selectionArgs) {
        this.mQuerySelectionArgs = selectionArgs;
        return this;
    }

    public QuerySupport<T> having(String having) {
        this.mQueryHaving = having;
        return this;
    }

    public QuerySupport<T> orderBy(String orderBy) {
        this.mQueryOrderBy = orderBy;
        return this;
    }

    public QuerySupport<T> limit(String limit) {
        this.mQueryLimit = limit;
        return this;
    }

    public QuerySupport<T> groupBy(String groupBy) {
        this.mQueryGroupBy = groupBy;
        return this;
    }

    public QuerySupport<T> selection(String selection) {
        this.mQuerySelection = selection;
        return this;
    }

    public List<T> query() {
        Cursor cursor = mSQLiteDatabase.query(DaoUtil.getTableName(mClazz), mQueryColumns, mQuerySelection,
                mQuerySelectionArgs, mQueryGroupBy, mQueryHaving, mQueryOrderBy, mQueryLimit);
        clearQueryParams();
        return cursorToListByReflect(cursor);
    }

    private void clearQueryParams() {
        mQueryColumns = null;
        mQuerySelection = null;
        mQuerySelectionArgs = null;
        mQueryGroupBy = null;
        mQueryHaving = null;
        mQueryOrderBy = null;
        mQueryLimit = null;
    }

    // 多次调用反射 查询所有列表
    private List<T> cursorToListByReflect(Cursor cursor) {
        List<T> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            // 通过反射创建T对象 第一次反射 以Person为例 相当于调用Person p = new Person()
            T instance = null;// 要调用此方法 必须有无参构造方法
            try {
                instance = mClazz.newInstance();
                // 反射获取T对象所有的属性 第二次反射
                Field[] fields = mClazz.getDeclaredFields();
                // 遍历field 从数据库取出数据填充到instance
                for (Field field : fields) {
                    Object value = null;
                    field.setAccessible(true);
                    // 以Person为例 它有个属性String name， fieldName 则是 name
                    String fieldName = field.getName();
                    // 查询当前属性在数据库中所在的列 下面则相当于调用cursor.getColumnIndex("name")
                    int index = cursor.getColumnIndex(fieldName);
                    if (index != -1) {
                        // 根据对象T的属性类型推算cursor的方法 如cursor.getString cursor.getInt
                        Method cursorGetColumnMethod = convertType2CursorMethod(field.getType());// 在该方法进行第三次反射
                        // 通过反射获取 value 第四次反射 相当于cursor.getString(cursor.getColumnIndex("name"))//cursor.getColumnIndex("name")在上面已经调用
                        value = cursorGetColumnMethod.invoke(cursor, index);
                        if (value == null) {
                            continue;
                        }
                        // 处理一些特殊的部分
                        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                            // sqlite不支持bool类型 使用0代表false 1代表true
                            if ("0".equals(String.valueOf(value))) {
                                value = false;
                            } else if ("1".equals(String.valueOf(value))) {
                                value = true;
                            }
                        } else if (field.getType() == char.class || field.getType() == Character.class) {
                            // sqlite不支持char类型 取第0位即可
                            value = ((String) value).charAt(0);
                        } else if (field.getType() == Date.class) {
                            // sqlite不支持Date类型 存储的是时间戳
                            long date = (Long) value;
                            if (date <= 0) {
                                value = null;
                            } else {
                                value = new Date(date);
                            }
                        }
                    } else {
                        Log.e(TAG, "cursorToList: 该属性没有存储在数据库中");
                        continue;
                    }
                    // 反射注入属性的值(以person为例,类似调用person.setName(value))
                    // 第五次反射
                    field.set(instance, value);
                }
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            list.add(instance);
        }
        cursor.close();
        Log.e(TAG, "cursorToList: " + list.size());
        return list;
    }

    // 获取cursor的方法
    private Method convertType2CursorMethod(Class<?> type) throws NoSuchMethodException {
        // 根据数据类型 得到不同cursor方法
        String methodName = getColumnMethodName(type);
        // 第三次反射 根据方法名和参数类型调用
        return Cursor.class.getMethod(methodName, int.class);
    }

    // 根据数据类型 得到不同cursor方法 如getInt getString
    private String getColumnMethodName(Class<?> fieldType) {
        String typeName;
        if (fieldType.isPrimitive()) { // 如果是基本数据类
            // 将int boolean float等转换为对象的形式 即首字母大写
            /*
             * @see     java.lang.Boolean#TYPE
             * @see     java.lang.Character#TYPE
             * @see     java.lang.Byte#TYPE
             * @see     java.lang.Short#TYPE
             * @see     java.lang.Integer#TYPE
             * @see     java.lang.Long#TYPE
             * @see     java.lang.Float#TYPE
             * @see     java.lang.Double#TYPE
             * @see     java.lang.Void#TYPE
             */
            typeName = DaoUtil.capitalize(fieldType.getName());
        } else {
            typeName = fieldType.getSimpleName();
        }
        // 上面获取对象T的Java的get方法，如Integer String Boolean 下面需要转成SQLite里面的数据类型
        // 如getBoolen转换为数据库的getInt getChar转换为数据库的getString
        String methodName = "get" + typeName;
        switch (methodName) {
            case "getBoolean":
            case "getInteger":
                methodName = "getInt";
                break;
            case "getChar":
            case "getCharacter":
                methodName = "getString";
                break;
            case "getDate":
                methodName = "getLong";
                break;
        }
        return methodName;
    }
}
