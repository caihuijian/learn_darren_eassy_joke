package com.example.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hjcai on 2021/3/4.
 */
public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {
    //此处使用泛型 外部传入的参数params有可能是AbsNavigationBar.Builder.NavigationParams的子类（在AbsNavigationBar的具体实现类中）
    private static final String TAG = "AbsNavigationBar";

    // 存储默认的配置参数
    private P mParams;

    // 存放navigationBar的布局
    private View mNavigationView;

    public AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBind();
    }

    //根据viewId查找导航条上显示的String
    public String getString(int viewId) {
        return this.mParams.mContext.getResources().getString(viewId);
    }

    //根据viewId查找导航条上显示的颜色
    public int getColor(int viewId) {
        return ContextCompat.getColor(this.mParams.mContext, viewId);
    }

    public P getParams() {
        return mParams;
    }


    // 设置文本
    public void setText(int viewId, CharSequence text) {
        // 每次都 findViewById   减少findViewById的次数
        View view = mNavigationView.findViewById(viewId);
        if (view != null) {
            try {
                //利用反射使得所有带有setText(CharSequence)的方法的控件都可以调用setText的方法
                Class clazz = view.getClass();
                Method setTextMethod = clazz.getMethod("setText", CharSequence.class);
                setTextMethod.invoke(view, text);
                view.setVisibility(View.VISIBLE);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                Log.e(TAG, "setText: error " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    // 设置点击事件
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = mNavigationView.findViewById(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }


    // 设置图片
    public void setImageResource(int viewId, int resourceId) {
        ImageView imageView = mNavigationView.findViewById(viewId);
        if (imageView instanceof ImageView) {
            imageView.setImageResource(resourceId);
        }else{
            Log.e(TAG, "设置图片失败");
        }
    }


    /**
     * 创建和绑定布局
     */
    public void createAndBind() {
        if(mParams.mParent == null){
            // 获取activity的根布局
            ViewGroup activityRoot = (ViewGroup) ((Activity)(mParams.mContext))
                    .findViewById(android.R.id.content);
            // 获取navigationBar的父容器viewGroup
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }
        if(mParams.mParent == null){
            Log.e(TAG, "导航条的布局为空");
            return;
        }
        // 填充navigationBar
        mNavigationView = LayoutInflater.from(mParams.mContext).
                inflate(bindLayoutId(), mParams.mParent, false);

        // 将布局添加到parent的头部
        mParams.mParent.addView(mNavigationView, 0);
        applyView();
    }

    // 用于构建导航条类 这个类只是定义默认的配置 具体功能的实现由具体的实现类决定
    public abstract static class Builder {
        // 构造方法
        public Builder(Context context, ViewGroup parent) {
        }

        // 创建导航条方法
        public abstract AbsNavigationBar builder();

        // NavigationParams用于存储默认的配置参数
        public static abstract class AbsNavigationParams {
            public Context mContext;
            // NavigationBar显示的父布局
            public ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                this.mContext = context;
                this.mParent = parent;
            }
        }
    }
}
