package com.example.hookstartactivity;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

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

    public static void hookStartActivity() throws Exception {

        // 查看API28的API源码 发现启动activity时
        // 1.ActivityManager.getService().startActivity方法内部 进行了intent可用性检查
        // 2.而真正启动activity是在ActivityThread handleMessage(EXECUTE_TRANSACTION)的时候
        // 在第一步中 我们需要先使用临时的空壳activity的intent来通过检测
        // 在第二步中 我们需要取出handleMessage里面我们塞进去的壳activity的intent 替换为我们真正想要启动的activity的intent

        /* 以下仅仅涉及第一步 大致思路
         * 我们需要拦截IActivityManager原先实现者的startActivity方法
         * IActivityManager的原先实现者是android.util.Singleton里面的mInstance对象
         * 因此我们的核心是取得mInstance对象
         * 取得过程：
         * 反射android.app.ActivityManager对象 取得其中的IActivityManagerSingleton对象
         * 再从IActivityManagerSingleton中取得mInstance对象
         */
        try {
            // 1.反射android.app.ActivityManager对象 取得其中的IActivityManagerSingleton对象
            Object defaultSingleton = getIActivityManagerSingleton();
            if (defaultSingleton == null) {
                Log.e(TAG, "hookStartActivity: failed");
                return;
            }
            // 反射android.util.Singleton对象
            Class<?> singletonClazz = Class.forName("android.util.Singleton");
            // 2.从IActivityManagerSingleton中取得mInstance对象
            Field instanceField = singletonClazz.getDeclaredField("mInstance");
            instanceField.setAccessible(true);
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

    public static void hookLaunchActivity() throws Exception {
        try {
            Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
            Field currentActivityThreadField = activityThreadClazz.getDeclaredField("sCurrentActivityThread");
            currentActivityThreadField.setAccessible(true);
            Object currentActivityThread = currentActivityThreadField.get(null);

            Field handlerField = activityThreadClazz.getDeclaredField("mH");
            handlerField.setAccessible(true);
            Handler mH = (Handler) handlerField.get(currentActivityThread);

            Field callbackField = Handler.class.getDeclaredField("mCallback");
            callbackField.setAccessible(true);
            //Handler的mCallback替换为CallBackProxy
            callbackField.set(mH, new CallBackProxy(mH));
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
