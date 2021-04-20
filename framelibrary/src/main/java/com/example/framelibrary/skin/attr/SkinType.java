package com.example.framelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framelibrary.skin.SkinManager;
import com.example.framelibrary.skin.SkinResource;

/**
 * Created by hjcai on 2021/4/14.
 * 需要换肤的类型
 * 主要有：android:src android:background android:textColor android:textColorHint等 没有列出的可以自己补充
 */
public enum SkinType {
    TEXT_COLOR("textColor") {
        @Override
        public void skin(View view, String resName) {
            if (view == null) {
                return;
            }
            SkinResource skinResource = getSkinResource();
            ColorStateList color = skinResource.getColorStateListByName(resName);
            if (color == null) {
                return;
            }
            TextView textView = (TextView) view;
            textView.setTextColor(color);
        }
    },

    BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {
            if (view == null) {
                return;
            }
            // background 是图片的情况
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if (drawable != null) {
                view.setBackground(drawable);
                return;
            }
            int color = skinResource.getColorByName(resName);
            if (color == 0) {
                return;
            }
            view.setBackgroundColor(color);
        }
    },

    TEXT_COLOR_HINT("textColorHint") {
        @Override
        public void skin(View view, String resName) {
            if (view == null) {
                return;
            }
            SkinResource skinResource = getSkinResource();
            ColorStateList color = skinResource.getColorStateListByName(resName);
            if (color == null) {
                return;
            }
            TextView textView = (TextView) view;
            textView.setHintTextColor(color);
        }
    },

    SRC("src") {
        @Override
        public void skin(View view, String resName) {
            if (view == null) {
                return;
            }
            SkinResource skinResource = getSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            ImageView imageView = (ImageView) view;
            if (drawable == null) {
                return;
            }
            imageView.setImageDrawable(drawable);
        }
    };

    private String mResName; // 存储资源的名称

    SkinType(String resName) { // 一个参数的构造方法
        this.mResName = resName;
    }

    // 换肤的方法 具体实现由各种类型自己实现
    public abstract void skin(View view, String resName);

    public String getResName() {
        return mResName;
    }

    public SkinResource getSkinResource() {
        return SkinManager.getInstance().getSkinResource();
    }
}
