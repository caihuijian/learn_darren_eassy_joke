package com.example.framelibrary.skin;

import android.app.Activity;
import android.content.Context;

import com.example.framelibrary.BaseSkinActivity;
import com.example.framelibrary.skin.attr.SkinView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hjcai on 2021/4/14.
 * <p>
 * 皮肤管理类
 * 换肤公开接口 调用SkinResource进行换肤
 */
public class SkinManager {
    private static final SkinManager mInstance;
    // 缓存 存储的内容是 activity与它里面需要换肤的views的键值对
    private final Map<Activity, List<SkinView>> mAllSkinViewsInActivity = new HashMap<>();
    // 皮肤实际操作者
    private SkinResource mSkinResource;
    // 为了避免内存泄漏 这里应该使用Application的context
    private WeakReference<Context> contextWeakReference;

    //静态代码块初始化
    static {
        mInstance = new SkinManager();
    }


    public static SkinManager getInstance() {
        return mInstance;
    }

    // 根据activity获取需要换肤的所有view
    public List<SkinView> getSkinViews(BaseSkinActivity activity) {
        return mAllSkinViewsInActivity.get(activity);
    }

    // 缓存当前activity的所有换肤的view
    public void cache(BaseSkinActivity activity, List<SkinView> skinViews) {
        mAllSkinViewsInActivity.put(activity, skinViews);
    }

    // skinManager的初始化 在Application启动时初始化
    public void init(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    // 加载任意皮肤
    // 返回值表示是否成功换肤 后续会更新不同的返回值
    public int loadSkin(String skinPath) {
        // 初始化资源管理
        mSkinResource = new SkinResource(contextWeakReference.get(), skinPath);

        // 遍历存储的所有需要换肤的Activity
        Set<Activity> keys = mAllSkinViewsInActivity.keySet();
        for (Activity key : keys) {
            List<SkinView> skinViewsInOneActivity = mAllSkinViewsInActivity.get(key);
            // 更新所有Activity中的view
            for (SkinView skinView : skinViewsInOneActivity) {
                skinView.applySkin();
            }
        }
        return 1;
    }

    // 恢复默认皮肤
    public int restoreDefault() {
        return 0;
    }

    // 获取当前皮肤资源
    public SkinResource getSkinResource() {
        return mSkinResource;
    }
}
