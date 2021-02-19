package com.example.learneassyjoke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.fixBug.ExceptionCrashHandler;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化异常处理类
        ExceptionCrashHandler.getInstance().init(this);
        try {
            // 初始化版本，获取当前应用的版本
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            String versionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
