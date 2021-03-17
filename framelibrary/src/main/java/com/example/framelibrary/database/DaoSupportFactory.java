package com.example.framelibrary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

/**
 * Created by hjcai on 2021/3/12.
 */
public class DaoSupportFactory {
    private static final String TAG = "DaoSupportFactory";
    // 单例对象
    private static DaoSupportFactory mFactoryInstance;

    private final SQLiteDatabase mSqLiteDatabase;

    private DaoSupportFactory(Context context) {
        // 把数据库放到内存卡里面  TODO 没有判断是否有存储卡 没有动态申请权限
        File dbRoot = new File(context.getExternalFilesDir(null)
                .getAbsolutePath() + File.separator + "nhdz" + File.separator + "database");
        if (!dbRoot.exists()) {
            if (!dbRoot.mkdirs()) {
                Log.e(TAG, "DaoSupportFactory: 创建db路径失败");
            }
            ;
        }

        File dbFile = new File(dbRoot, "nhdz.db");
        // 打开或者创建一个数据库 并存储数据库操作的引用
        Log.e(TAG, "DaoSupportFactory: 创建db路径==>" + dbRoot);
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public static DaoSupportFactory getFactoryInstance(Context context) {
        if (mFactoryInstance == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactoryInstance == null) {
                    mFactoryInstance = new DaoSupportFactory(context);
                }
            }
        }
        return mFactoryInstance;
    }

    // 获取DAO对象 用于操作数据库
    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        return new DaoSupport<>(mSqLiteDatabase, clazz);
    }
}
