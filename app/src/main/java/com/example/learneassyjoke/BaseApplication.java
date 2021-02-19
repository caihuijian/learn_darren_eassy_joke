package com.example.learneassyjoke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alipay.euler.andfix.patch.PatchManager;
import com.example.cachebug.ExceptionCrashHandler;


public class BaseApplication extends Application {
    //阿里AndFix的使用
    public static PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化异常处理类
        ExceptionCrashHandler.getInstance().init(this);

        mPatchManager = new PatchManager(this);
        try {
            // 初始化版本，获取当前应用的版本
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            mPatchManager.init(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // 加载之前的 apatch 包
        mPatchManager.loadPatch();
    }
}
