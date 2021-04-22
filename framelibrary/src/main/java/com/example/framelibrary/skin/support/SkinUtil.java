package com.example.framelibrary.skin.support;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * Created by hjcai on 2021/4/19.
 */
public class SkinUtil {
    private static SkinUtil mInstance;
    private final WeakReference<Context> contextWeakRef;

    private SkinUtil(Context context) {
        contextWeakRef = new WeakReference<>(context.getApplicationContext());
    }

    public static SkinUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SkinUtil.class) {
                if (mInstance == null) {
                    mInstance = new SkinUtil(context);
                }
            }
        }
        return mInstance;
    }

    // return /storage/emulated/0/light.skin
    public String getLightSkinPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "light.skin";
    }

    // 保存当前皮肤路径
    public void saveSkinPath(String skinPath) {
        contextWeakRef.get().getSharedPreferences(SkinConfig.SHARED_PREFERENCE_FILE_NAME_SKIN, Context.MODE_PRIVATE)
                .edit().putString(SkinConfig.SKIN_PATH_NAME_KEY, skinPath).apply();
    }

    // 清空皮肤路径
    public void clearSkinInfo() {
        saveSkinPath("");
    }

    public String getSkinPathFromSP() {
        return contextWeakRef.get().getSharedPreferences(SkinConfig.SHARED_PREFERENCE_FILE_NAME_SKIN, Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_PATH_NAME_KEY, "");
    }

    // 检查皮肤有效性
    public int checkSkinFileByPath(String skinPath) {
        File file = new File(skinPath);

        if (!file.exists()) {
            // 不存在，清空皮肤
            SkinUtil.getInstance(contextWeakRef.get()).clearSkinInfo();
            return SkinConfig.SKIN_FILE_NO_EXIST;
        }

        // 检查是否是apk
        String packageName = Objects.requireNonNull(contextWeakRef.get().getPackageManager().getPackageArchiveInfo(
                skinPath, PackageManager.GET_ACTIVITIES)).packageName;

        if (TextUtils.isEmpty(packageName)) {
            SkinUtil.getInstance(contextWeakRef.get()).clearSkinInfo();
            return SkinConfig.SKIN_FILE_ERROR;
        }
        return SkinConfig.SKIN_FILE_OK;
    }
}
