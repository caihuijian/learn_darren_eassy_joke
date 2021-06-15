package com.example.hookstartactivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Field;

import static com.example.hookstartactivity.HookInvocationHandler.REQUEST_TARGET_INTENT_KEY;

/**
 * Created by hjcai on 2021/6/11.
 * <p>
 * 在launchActivity前拦住 达到换柱的目的
 */
public class CallBackProxy implements Handler.Callback {

    private final Handler mHandler;

    public CallBackProxy(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == 100) {
            Object o = msg.obj;
            try {
                Field field = o.getClass().getDeclaredField("intent");
                field.setAccessible(true);
                //获取占坑Activity的Intent
                Intent intent = (Intent) field.get(o);
                //获取之前保存的插件Activity的Intent
                Intent targetIntent = intent.getParcelableExtra(REQUEST_TARGET_INTENT_KEY);
                //将占坑的Activity替换为插件Activity
                if (targetIntent == null) {
                    return false;
                }
                // 借尸还魂中的还魂
                intent.setComponent(targetIntent.getComponent());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        mHandler.handleMessage(msg);
        return true;
    }
}
