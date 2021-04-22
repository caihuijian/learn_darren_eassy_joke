package com.example.framelibrary.skin;

import android.content.Context;
import android.text.TextUtils;

import com.example.framelibrary.BaseSkinActivity;
import com.example.framelibrary.skin.attr.SkinView;
import com.example.framelibrary.skin.callback.ISkinChangeListener;
import com.example.framelibrary.skin.support.SkinConfig;
import com.example.framelibrary.skin.support.SkinUtil;

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
    private final Map<ISkinChangeListener, List<SkinView>> mAllSkinViewsInActivity = new HashMap<>();
    // 皮肤实际操作者
    private SkinResource mSkinResource;
    // 为了避免内存泄漏 这里应该使用Application的context
    private WeakReference<Context> mContextWeakRef;

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
    public void cache(ISkinChangeListener skinChangeListener, List<SkinView> skinViews) {
        mAllSkinViewsInActivity.put(skinChangeListener, skinViews);
    }

    public void unCache(ISkinChangeListener skinChangeListener) {
        mAllSkinViewsInActivity.remove(skinChangeListener);
    }

    // skinManager的初始化 在Application启动时初始化
    public void init(Context context) {
        mContextWeakRef = new WeakReference<>(context);

        // 每一次打开应用都会到这里来，防止皮肤被任意删除 检查皮肤有效性
        String currentSkinPath = SkinUtil.getInstance(context).getSkinPathFromSP();
        int checkRes = SkinUtil.getInstance(context).checkSkinFileByPath(currentSkinPath);
        if (checkRes != SkinConfig.SKIN_FILE_OK && checkRes != SkinConfig.SKIN_FILE_ERROR) {
            return;
        }

        // 最好校验签名  增量更新再说
        // 做一些初始化的工作
        mSkinResource = new SkinResource(context, currentSkinPath);
    }

    // 加载任意皮肤
    // 返回值表示是否成功换肤
    public int loadSkin(String skinPath) {
        // 1.加载皮肤前检查有效性
        if (SkinUtil.getInstance(mContextWeakRef.get()).checkSkinFileByPath(skinPath) != SkinConfig.SKIN_FILE_OK) {
            return SkinUtil.getInstance(mContextWeakRef.get()).checkSkinFileByPath(skinPath);
        }

        // 2.当前皮肤如果一样不要换
        String currentSkinPath = SkinUtil.getInstance(mContextWeakRef.get()).getSkinPathFromSP();
        if (skinPath.equals(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }

        // 3.初始化资源管理并换肤
        mSkinResource = new SkinResource(mContextWeakRef.get(), skinPath);
        changeSkin();
        // 4.保存皮肤的状态
        saveSkinStatus(skinPath);

        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    private void changeSkin() {
        Set<ISkinChangeListener> keys = mAllSkinViewsInActivity.keySet();
        // 遍历存储的所有需要换肤的Activity
        for (ISkinChangeListener key : keys) {
            List<SkinView> skinViews = mAllSkinViewsInActivity.get(key);
            // 更新所有Activity中的view
            for (SkinView skinView : skinViews) {
                skinView.applySkin();
            }
            // 通知Activity
            key.changeSkin(mSkinResource);
        }
    }

    private void saveSkinStatus(String skinPath) {
        SkinUtil.getInstance(mContextWeakRef.get()).saveSkinPath(skinPath);
    }

    // 恢复默认皮肤
    public int restoreDefault() {
        // 判断当前SP如果没有存储皮肤 什么都不做
        String currentSkinPath = SkinUtil.getInstance(mContextWeakRef.get()).getSkinPathFromSP();

        if (TextUtils.isEmpty(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }

        // 当前手机运行的app的路径apk路径
        String skinPath = mContextWeakRef.get().getPackageResourcePath();
        // 初始化资源管理
        mSkinResource = new SkinResource(mContextWeakRef.get(), skinPath);

        // 改变皮肤
        changeSkin();

        // 把皮肤信息清空
        SkinUtil.getInstance(mContextWeakRef.get()).clearSkinInfo();
        return SkinConfig.SKIN_CHANGE_SUCCESS;
    }

    // 获取当前皮肤资源
    public SkinResource getSkinResource() {
        if (mSkinResource == null) {
            return new SkinResource(mContextWeakRef.get(), mContextWeakRef.get().getPackageResourcePath());
        }
        return mSkinResource;
    }
}
