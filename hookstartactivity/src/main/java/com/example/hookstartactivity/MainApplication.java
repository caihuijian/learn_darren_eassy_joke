package com.example.hookstartactivity;

import android.app.Application;

/**
 * Created by hjcai on 2021/6/9.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HookStartActivityUtil hookStartActivityUtil =
                new HookStartActivityUtil(this, TempActivity.class);
        try {
            hookStartActivityUtil.hookStartActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
