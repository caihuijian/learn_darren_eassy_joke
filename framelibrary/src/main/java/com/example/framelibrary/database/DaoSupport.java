package com.example.framelibrary.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by hjcai on 2021/3/12.
 */
class DaoSupport<T> implements IDaoSupport<T> {
    // 数据库对象
    private SQLiteDatabase mSqLiteDatabase;
    // 数据库需要操作的 表中存储的 对象类型
    private Class<T> mClazz;
    private String TAG = "DaoSupport";

    private static final Object[] mPutMethodArgs = new Object[2];//存储key value 如 name "hjcai"//个人觉得这个变量意义不大
    // 数据库优化 缓存数据类型 减少反射的调用次数
    private static final Map<String, Method> mPutMethods = new ArrayMap<>();

    public DaoSupport(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        init(sqLiteDatabase, clazz);
    }

    @Override
    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mSqLiteDatabase = sqLiteDatabase;
        this.mClazz = clazz;
        // 创建表的sql语句
        /*create table if not exists Person (id integer primary key autoincrement, name text, age integer, flag boolean)*/
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ")
                .append(DaoUtil.getTableName(mClazz))// 表名为类的名字
                .append("(id integer primary key autoincrement, ");// id 为 int的主键 自增长
        // 通过反射获取类中的属性
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {//遍历属性
            field.setAccessible(true);// 设置权限
            String name = field.getName();
            String type = field.getType().getSimpleName();// int String boolean
            //  type需要进行转换 int --> integer, String text;
            sb.append(name).append(DaoUtil.getColumnType(type)).append(", ");
        }
        // 将插入语句的最后两位", "替换为")"
        sb.replace(sb.length() - 2, sb.length(), ")");
        String createTableSql = sb.toString();
        Log.e(TAG, "建表语句--> " + createTableSql);
        // 执行建表语句
        mSqLiteDatabase.execSQL(createTableSql);
    }

    // 插入数据库 类型为任意类型
    @Override
    public long insert(T t) {
        /*
        通常我们可能直接调用
        ContentValues values = new ContentValues();
        values.put("name","wz");
        values.put("author","xx");
        values.put("price",1.0);
        db.insert("Book",null,values);

        但是这样其实不方便 如果我们删除或增加Book.java的字段 那么这段代码需要修改
        并且 对于不同的对象 我们还需要写不同的插入逻辑，利用反射 就可以直接绕开这两个问题
        因此 这里我们使用了反射
        */
        ContentValues values = contentValuesByObj(t);

        return mSqLiteDatabase.insert(DaoUtil.getTableName(mClazz), null, values);
    }

    @Override
    public void insert(List<T> data) {
        mSqLiteDatabase.beginTransaction();
        for (T datum : data) {
            insert(datum);
        }
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    @Override
    public void deleteAll() {
        mSqLiteDatabase.execSQL("DROP TABLE " + DaoUtil.getTableName(mClazz));
        //mSqLiteDatabase.execSQL("DELETE FROM " + DaoUtil.getTableName(mClazz));
    }

    // 查询

    // 修改

    // 删除

    // obj 转成 ContentValues
    // ContentValues实际作用类似与hashMap 只不过它的value只能push基本类型
    private ContentValues contentValuesByObj(T obj) {
        ContentValues contentValues = new ContentValues();
        // 通过反射获取mClazz定义的filed(以Person为例 返回的是age 和 name字段)
        Field[] fields = mClazz.getDeclaredFields();
        for (Field field : fields) {
            Method putMethod;
            try {
                // 设置权限，私有和共有都可以访问
                field.setAccessible(true);
                // 获取field的名称(如age)
                String key = field.getName();
                // 获取field的value(如30)
                Object value = field.get(obj);

                mPutMethodArgs[0] = key;
                mPutMethodArgs[1] = value;

                // 虽然使用反射会有一点性能的影响 但是影响很小 而且源码里面  activity实例的创建 View创建反射等都使用了反射 因此这里也会使用反射 获取put方法
                String filedTypeName = field.getType().getName();//获取filed的数据类型(如Integer)
                // filedTypeName的作用是作为存储mPutMethods的key
                // 因此使用的key类似 java.lang.String int boolean等
                putMethod = mPutMethods.get(filedTypeName);
                if (putMethod == null) {
                    putMethod = ContentValues.class.getDeclaredMethod("put",
                            String.class, value.getClass());
                    // 缓存PutMethods 下次遇到下面类似的一样的方法 就不需要再反射了
                    // put(String key, String value)
                    // put(String key, Integer value)
                    // put(String key, Boolean value)
                    mPutMethods.put(filedTypeName, putMethod);
                }
                // 通过反射执行ContentValues的putXXX方法
                // 相当于调用类似 contentValues.put("age",30);
                putMethod.invoke(contentValues, mPutMethodArgs);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethodArgs[0] = null;
                mPutMethodArgs[1] = null;
            }
        }
        return contentValues;
    }
}
