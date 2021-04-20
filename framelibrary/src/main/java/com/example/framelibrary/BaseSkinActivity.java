package com.example.framelibrary;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.ViewCompat;

import com.example.base.BaseActivity;
import com.example.framelibrary.skin.SkinManager;
import com.example.framelibrary.skin.attr.SkinAttr;
import com.example.framelibrary.skin.attr.SkinView;
import com.example.framelibrary.skin.support.SkinAppCompatViewInflater;
import com.example.framelibrary.skin.support.SkinAttrSupport;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjcai on 2021/2/18.
 */
public abstract class BaseSkinActivity extends BaseActivity {
    private static final String TAG = "BaseSkinActivity";
    private SkinAppCompatViewInflater mAppCompatViewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(layoutInflater, new LayoutInflater.Factory2() {
            @Nullable
            @Override
            public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                //在这里拦截view创建
                //可以在这里进行换肤
                Log.e(TAG, "onCreateView: 拦截了view " + name);
                // 换肤框架从这里开始搭建
                // 1.创建View 目的是替换原先的view的一些属性
                // createView走的代码流程和AppCompatDelegateImpl createView源码没有二致
                // 即先走源码的流程 让源码帮我们创建好view 我们再检查这些view中的属性 看是否需要换肤
                View view = createView(parent, name, context, attrs);

                // 在拦截后与返回前进行所有可以进行换肤view的存储
                // 2.解析属性  src  textColor  background textHintColor TODO 自定义属性先不考虑
                if (view != null) {
                    // 获取当前activity中一个view中所有换肤的属性
                    List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
                    // 创建skinView  skinView中可能包含多个需要换肤的属性
                    SkinView skinView = new SkinView(view, skinAttrs);
                    // 3.交给SkinManager统一存储管理
                    managerSkinView(skinView);
                }

                return view;
            }

            @Nullable
            @Override
            public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {//Factory的方法 可以忽略
                Log.e(TAG, "onCreateView: ");
                return null;
            }
        });
        super.onCreate(savedInstanceState);
    }

    // 在这里进行统一的皮肤存储
    private void managerSkinView(SkinView skinView) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if (skinViews == null) {
            // 之前没有存储过当前activity的view
            skinViews = new ArrayList<>();
            SkinManager.getInstance().cache(this, skinViews);
        }
        // 之前存储过当前activity的view
        if (!skinViews.contains(skinView)) {
            skinViews.add(skinView);
        }
    }

    // 拷贝自androidx.appcompat.app.AppCompatDelegateImpl 对view进行拦截处理
    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        if (mAppCompatViewInflater == null) {
            TypedArray a = context.obtainStyledAttributes(R.styleable.AppCompatTheme);
            String viewInflaterClassName =
                    a.getString(R.styleable.AppCompatTheme_viewInflaterClass);
            if (viewInflaterClassName == null) {
                // Set to null (the default in all AppCompat themes). Create the base inflater
                // (no reflection)
                mAppCompatViewInflater = new SkinAppCompatViewInflater();
            } else {
                try {
                    Class<?> viewInflaterClass = Class.forName(viewInflaterClassName);
                    mAppCompatViewInflater =
                            (SkinAppCompatViewInflater) viewInflaterClass.getDeclaredConstructor()
                                    .newInstance();
                } catch (Throwable t) {
                    Log.i(TAG, "Failed to instantiate custom view inflater "
                            + viewInflaterClassName + ". Falling back to default.", t);
                    mAppCompatViewInflater = new SkinAppCompatViewInflater();
                }
            }
        }

        boolean inheritContext = false;
        final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                true/* 原本为VectorEnabledTintResources.shouldBeUsed() 先改为always true */
                /* Only tint wrap the context if enabled */
        );
    }

    // 拷贝自androidx.appcompat.app.AppCompatDelegateImpl
    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = this.getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }
}
