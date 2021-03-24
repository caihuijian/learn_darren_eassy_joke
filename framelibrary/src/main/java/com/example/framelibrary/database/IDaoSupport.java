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

    // 查询所有
    List<T> queryAll();

    // 查询所有 使用反射
    List<T> queryAllByReflect();

    void deleteAll();

}
