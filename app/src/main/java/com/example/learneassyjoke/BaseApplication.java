package com.example.learneassyjoke;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;
import com.example.cachebug.ExceptionCrashHandler;
import com.example.fixBug.FixDexManager;


public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    //阿里AndFix的使用
    public static PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化异常处理类
        ExceptionCrashHandler.getInstance().init(this);
        //AliFix();
        fixDexBug();
    }

    private void AliFix() {
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

    //自己写的dex fix热修复
    private void fixDexBug() {
        //准备工作 将修复好的dex 拷贝到 /storage/emulated/0/Android/data/app包名/files/
        FixDexManager fixDexManager = new FixDexManager(this);
        try {
            fixDexManager.fixBugByDex();
            Log.e(TAG, "fixDexBug: 修复成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "fixDexBug: 修复失败");
        }
    }
}
