package com.example.framelibrary.skin.attr;

import android.view.View;

/**
 * Created by hjcai on 2021/4/14.
 * <p>
 * 存储了某个view的一个可以换肤的属性
 * 存储的值为 当前资源名+属性类型
 */
public class SkinAttr {
    private final String mResName;
    private final SkinType mSkinType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName = resName;
        this.mSkinType = skinType;
    }

    // 对当前可以换肤的单独类型 进行换肤
    public void skin(View view) {
        mSkinType.skin(view, mResName);
    }
}
