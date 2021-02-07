package com.example.ioc;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by hjcai on 2021/2/5.
 */
public class ViewUtils {

    //Activity绑定
    public static void injectActivity(Activity activity) {
        injectActivity(new ViewFinder(activity), activity);
    }

    private static void injectActivity(ViewFinder finder, Object object) {
        injectActivityField(finder, object);
        injectActivityEvent(finder, object);
    }

    //注入属性
    private static void injectActivityField(ViewFinder finder, Object object) {
        //1 反射 获取class里面所有属性
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();//获取Activity的所有属性包括私有和共有
        for (Field field : fields) {
            //2 遍历fields 找到添加了注解ViewById的filed
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null) {
                //3 获取注解里面的id值
                int viewId = viewById.value();
                View view = finder.findViewById(viewId);//相当于调用Activity.findViewById
                if (view != null) {
                    field.setAccessible(true);
                    try {
                        //4 动态的注入找到的View
                        field.set(object, view);//利用反射 将object(activity)中的声明了ViewById注解的地方 替换成刚刚find的view
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void injectActivityEvent(ViewFinder finder, Object object) {
        //1 反射 获取class里面所有属性
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();//获取Activity的所有方法包括私有和共有
        for (Method method : methods) {
            //2 遍历fields 找到添加了注解OnClick的method
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                //3 获取注解里面的id值
                int[] viewIds = onClick.value();
                for (int viewId : viewIds) {
                    View view = finder.findViewById(viewId);//相当于调用Activity.findViewById
                    if (view != null) {
                        // 4. 给每个view设置点击事件
                        view.setOnClickListener(new DeclaredOnClickListener(method, object));
                    }
                }
            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {
        private Object mObject;
        private Method mMethod;//实际定义的方法 在这里是 ActivityIocTest.onItemClick

        public DeclaredOnClickListener(Method method, Object object) {
            this.mObject = object;
            this.mMethod = method;
        }

        @Override
        public void onClick(View v) {
            try {
                // 所有方法都可以 包括私有共有
                mMethod.setAccessible(true);
                // 反射执行方法
                // 当View被点击时执行
                mMethod.invoke(mObject, v);//通过反射 调用mObject的声明了onClick注解了的方法 参数为v
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject, null);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
