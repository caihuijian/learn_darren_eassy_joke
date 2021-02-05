package com.example.ioc;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by hjcai on 2021/2/5.
 */
public class ViewUtils {
    //注入Activity
    public static void inject(Activity activity) {
        inject(new ViewFinder(activity), activity);
    }

    //属性绑定
    public static void inject(View view) {
        inject(new ViewFinder(view), view);
    }

    //fragment使用
    public static void inject(View view, Object object) {
        inject(new ViewFinder(view), object);
    }

    //兼容 上面三个方法  object --> 反射需要执行的类(调用find的类)
    private static void inject(ViewFinder finder, Object object) {
        injectField(finder, object);
        injectEvent(finder, object);
    }

    //注入事件
    private static void injectEvent(ViewFinder finder, Object object) {

    }

    //注入属性
    private static void injectField(ViewFinder finder, Object object) {
        //1 反射 获取class里面所有属性
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();//获取所有属性包括私有和共有
        for (Field field : fields) {
            //2 遍历fields 找到添加了注解的ViewById
            ViewById viewById = field.getAnnotation(ViewById.class);
            if(viewById != null){
                //3 获取注解里面的id值
                int viewId = viewById.value();
                View view = finder.findViewById(viewId);
                if (view != null){
                    field.setAccessible(true);
                    try {
                        //4 动态的注入找到的View
                        field.set(object,view);//？？？？？？？？？？？？
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
