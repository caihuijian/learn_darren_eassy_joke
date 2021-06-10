package com.example.hookstartactivity;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * Created by hjcai on 2021/6/9.
 */
public class HookStartActivityUtil {
    private static final String TAG = "HookStartActivityUtil";
    Context mContext;
    Class<?> mTempClazz;

    public HookStartActivityUtil(Context context, Class<?> tempClazz) {
        mContext = context;
        mTempClazz = tempClazz;
    }
    // 思路：在startActivity的时候 使用已经注册清单文件的空壳Activity通过startActivity时的检测
    // 在lunchActivity时 替换掉空壳activity 使用真正想要打开的activity

    public static void hookStartActivity() throws Exception {

        // 查看API28的API源码 发现启动activity时
        // 1.ActivityManager.getService().startActivity方法内部 进行了intent可用性检查
        // 2.而真正启动activity是在ActivityThread handleMessage(EXECUTE_TRANSACTION)的时候
        // 在第一步中 我们需要先使用临时的空壳activity的intent来通过检测
        // 在第二步中 我们需要取出handleMessage里面我们塞进去的壳activity的intent 替换为我们真正想要启动的activity的intent


        try {
            // 找到IActivityManager的原先的实现者 我们需要拦截它的startActivity方法 并替换他内部的的一个intent变量
            // 因此首先要拿到IActivityManager的原先的实现者

            // 反射android.util.Singleton对象
            Class<?> singletonClazz = Class.forName("android.util.Singleton");
            // 从其中获取mInstance的变量
            Field instanceField = singletonClazz.getDeclaredField("mInstance");
            instanceField.setAccessible(true);
            // 获取defaultSingleton中IActivityManager类型的mInstance成员变量
            Object defaultSingleton = getIActivityManagerSingleton();
            // 从mInstance中获取defaultSingleton 即IActivityManager的实现者
            Object activityManagerInstance = instanceField.get(defaultSingleton);// mInstance不是静态变量 需要从具体实例中获取对象 这里是defaultSingleton获取

            // Proxy 的三个参数
            // 1.class loader
            // 2.需要代理的接口类
            // 3.实现InvocationHandler的类 InvocationHandler中包含真正想要执行接口方法的对象
            // ActivityManager.getService().startActivity内部拦截intent 在InvocationHandler中 将其intent替换成我们空壳activity的intent
            Class<?> interfaceActivityManager = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(HookStartActivityUtil.class.getClassLoader(),
                    new Class[]{interfaceActivityManager},
                    new HookInvocationHandler(activityManagerInstance));

            //替换为代理类IActivityManagerProxy
            instanceField.set(defaultSingleton, proxy);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Object getIActivityManagerSingleton() {
        try {
            // 反射获取ActivityManager实例
            Class<?> activityManagerClazz = Class.forName("android.app.ActivityManager");
            // 拿到其中的静态变量 IActivityManagerSingleton
            Field field = activityManagerClazz.getDeclaredField("IActivityManagerSingleton");
            field.setAccessible(true);
            return field.get(null);// IActivityManagerSingleton是static变量 field.get(null)可以获取IActivityManagerSingleton对象
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
