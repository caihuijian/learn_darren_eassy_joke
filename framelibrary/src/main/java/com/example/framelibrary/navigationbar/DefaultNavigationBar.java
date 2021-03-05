package com.example.framelibrary.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.framelibrary.R;
import com.example.navigationbar.AbsNavigationBar;


public class DefaultNavigationBar<D extends
        DefaultNavigationBar.Builder.DefaultNavigationParams> extends
        AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParams> {

    public DefaultNavigationBar(Builder.DefaultNavigationParams params) {
        super(params);
    }


    @Override
    public int bindLayoutId() {
        // 默认title bar布局
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        // 应用效果
        setText(R.id.title, getParams().mTitle);
        setText(R.id.right_text, getParams().mRightText);

        setOnClickListener(R.id.right_text, getParams().mRightClickListener);
        // 默认左边button是返回button 作用为finishActivity
        setOnClickListener(R.id.back, getParams().mLeftClickListener);
    }


    public static class Builder extends AbsNavigationBar.Builder {

        DefaultNavigationParams P;


        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new DefaultNavigationParams(context, parent);
        }

        public Builder(Context context) {
            super(context, null);
            P = new DefaultNavigationParams(context, null);
        }

        @Override
        public DefaultNavigationBar builder() {
            return new DefaultNavigationBar(P);
        }

        // 存储所有效果到DefaultNavigationParams
        public Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        // 存储右边的文字
        public Builder setRightText(String rightText) {
            P.mRightText = rightText;
            return this;
        }

        // 存储右边的点击事件
        public Builder setRightClickListener(View.OnClickListener rightListener) {
            P.mRightClickListener = rightListener;
            return this;
        }

        // 存储左边的点击事件
        public Builder setLeftClickListener(View.OnClickListener rightListener) {
            P.mLeftClickListener = rightListener;
            return this;
        }

        // 存储右边的图片
        public Builder setRightIcon(int rightRes) {
            P.mRightImageId = rightRes;
            return this;
        }

        public static class DefaultNavigationParams extends
                AbsNavigationParams {
            // 存储所有可控制navigationBar的变量
            public String mTitle;
            public String mRightText;
            public View.OnClickListener mRightClickListener;
            public View.OnClickListener mLeftClickListener = v -> {
                // 关闭当前Activity
                ((Activity) mContext).finish();
            };
            public int mRightImageId;

            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
