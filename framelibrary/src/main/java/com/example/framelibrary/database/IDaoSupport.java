package com.example.framelibrary.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by hjcai on 2021/3/12.
 * 数据库支持类的定义
 */
public interface IDaoSupport<T> {
    // 初始化数据库表(Class<T> clazz代表T所对应的类)
    void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz);

    // 数据库插入数据
    long insert(T t);

    // 批量插入数据
    void insert(List<T> t);

    void deleteAll();

    // 获取专门查询的支持类 按照语句查询
    QuerySupport<T> querySupport();

    // 按照语句删除
    int delete(String whereClause, String... whereArgs);

    // 按照语句更新
    int update(T obj, String whereClause, String... whereArgs);

}
