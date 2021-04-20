package com.example.framelibrary.skin.attr;

import android.view.View;

import java.util.List;

/**
 * Created by hjcai on 2021/4/14.
 * 存储切换皮肤的一个view
 * 该view可能存在多个属性需要换肤
 */
public class SkinView {
    // 需要换肤的一个view
    private final View mView;
    // 需要换肤的属性用list保存 因为可能存在多个属性需要换肤
    private final List<SkinAttr> mSkinAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mSkinAttrs = skinAttrs;
    }

    // 换肤的方法
    public void applySkin() {
        // 遍历需要换肤view的中的可换肤属性们 进行换肤
        for (SkinAttr attr : mSkinAttrs) {
            attr.skin(mView);
        }
    }
}
