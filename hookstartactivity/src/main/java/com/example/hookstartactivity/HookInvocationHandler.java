package com.example.hookstartactivity;

import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by hjcai on 2021/6/10.
 */
public class HookInvocationHandler implements InvocationHandler {
    private static final String TAG = "HookInvocationHandler";
    private static final String REQUEST_TARGET_INTENT_KEY = "REQUEST_TARGET_INTENT_KEY";
    // 代理ActivityManager
    private final Object mActivityManager;

    public HookInvocationHandler(Object activityManagerServiceClass) {
        this.mActivityManager = activityManagerServiceClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.e(TAG, method.getName());
        if (method.getName().equals("startActivity")) {
            Log.e(TAG, "invoke: startActivity....");
            // 拦截startActivity
            Intent intent;
            int index = 0;
            // 查找intent在startActivity方法中是第几个参数 由于Android版本更新 参数index可能变化
            for (int i = 0, length = args.length; i < length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            //获取一开始设置进去的真正想要进入activity的intent
            intent = (Intent) args[index];

            //创建占坑Activity的Intent
            Intent subIntent = new Intent();
            subIntent.setClassName("com.example.hookstartactivity", "com.example.hookstartactivity.TempActivity");
            // 保存插件Activity的Intent
            subIntent.putExtra(REQUEST_TARGET_INTENT_KEY, intent);
            // 借尸还魂中的借尸 由于想要打开的activity没有注册 这里替换为已经注册的TempActivity 真正想要打开的activity的intent放在intent的bundle里面
            args[index] = subIntent;
        }
        return method.invoke(mActivityManager, args);
    }
}
