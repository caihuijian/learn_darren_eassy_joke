package com.example.dialog;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hjcai on 2021/3/2.
 */
class DialogViewHelper {
    private static final String TAG = "DialogViewHelper";
    //dialog的view
    private View mContentView = null;
    //存储了dialog上的各个view viewId+View的键值对 目的是减少findViewById的调用次数
    //为防止内存泄漏 使用WeakReference
    private final SparseArray<WeakReference<View>> mViews = new SparseArray<>();

    public DialogViewHelper(Context context, int layoutResId) {
        mContentView = LayoutInflater.from(context).inflate(layoutResId, null);
    }

    public DialogViewHelper() {
    }

    //根据view id操作该view的Text
    public void setText(int viewId, CharSequence text) {
        // 每次都 findViewById   减少findViewById的次数
        View view = getView(viewId);
        if (view != null) {
            try {
                //利用反射使得所有带有setText(CharSequence)的方法的控件都可以调用setText的方法
                Class clazz = view.getClass();
                Method setTextMethod = clazz.getMethod("setText", CharSequence.class);
                setTextMethod.invoke(view, text);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                Log.e(TAG, "setText:"+e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //获取view
    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewReference = mViews.get(viewId);
        //利用弱引用防止内存泄漏
        View view = null;
        if (viewReference != null) {
            view = viewReference.get();
        }

        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<>(view));
            }
        }
        return (T) view;
    }

    //操作view setOnClickListener
    public void setOnclickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    //存储dialog的view
    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    //获取dialog的view
    public View getContentView() {
        return mContentView;
    }
}
