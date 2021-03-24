package com.example.framelibrary.database;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;


/**
 * Created by hjcai on 2021/3/12.
 * 数据库操作辅助类
 */
class DaoUtil {
    private static final String TAG = "DaoUtil";

    // 获取数据库表名
    public static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    // 将java中的基本数据类型翻译为SQL语句中的数据类型
    // 此方法只适用于属性中的基本类型 如果存在复杂类型(如User Student)等 则不适用该方法
    public static String getColumnType(String type) {
        String value = null;
        if (type.contains("String")) {
            value = " text";
        } else if (type.contains("int")) {
            value = " integer";
        } else if (type.contains("boolean")) {
            value = " boolean";
        } else if (type.contains("float")) {
            value = " float";
        } else if (type.contains("double")) {
            value = " double";
        } else if (type.contains("char")) {
            value = " varchar";
        } else if (type.contains("long")) {
            value = " long";
        } else {
            Log.e(TAG, "getColumnType: invalid type!");
        }
        return value;
    }

    // 使得参数string开头字母大写
    public static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            // 前面把首字符大写 后面将剩余部分拼接上
            return string.substring(0, 1).toUpperCase(Locale.US) + string.substring(1);
        }
        return string == null ? null : "";
    }
}
