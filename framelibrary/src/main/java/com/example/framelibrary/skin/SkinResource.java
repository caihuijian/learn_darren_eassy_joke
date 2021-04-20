package com.example.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by hjcai on 2021/4/14.
 * <p>
 * 皮肤资源包管理类
 */
public class SkinResource {
    // 通过加载不同path的Resources来加载不同皮肤包中的资源
    private Resources mSkinResource;
    private String mPackageName;
    private static final String TAG = "SkinResource";

    // 初始化构建mSkinResource对象
    public SkinResource(Context context, String skinPath) {
        try {
            // 读取本地的一个 .skin里面的资源
            Resources superRes = context.getResources();
            // 创建AssetManager
            AssetManager assetManager = AssetManager.class.newInstance();
            // 寻找hide的addAssetPath方法
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            // method.setAccessible(true); 如果是私有的
            Log.e(TAG, "loadImg from : " + skinPath);
            // 反射执行方法 assetManager指向指定的皮肤包 /storage/emulated/0/xxx.skin
            method.invoke(assetManager, skinPath);

            // 获取到加载资源的mSkinResource
            mSkinResource = new Resources(assetManager, superRes.getDisplayMetrics(),
                    superRes.getConfiguration());

            // 获取指定路径apk的packageName
            mPackageName = getPackageName(context, skinPath);
        } catch (Exception e) {
            Log.e(TAG, "loadImg: " + e.getStackTrace().toString());
            e.printStackTrace();
        }
    }

    private String getPackageName(Context context, String skinPath) {
        if (context.getApplicationContext() != null) {
            if (context.getApplicationContext().getPackageManager() != null) {
                Log.e(TAG, "loadImage: myPath ===" + skinPath);
                return context.getApplicationContext().getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
            } else {
                Log.e(TAG, "loadImage: getPackageManager==null");
                return "";
            }
        }
        return "";
    }

    public ColorStateList getColorStateListByName(String resName) {
        try {
            int resId = mSkinResource.getIdentifier(resName, "color", mPackageName);
            ColorStateList color = mSkinResource.getColorStateList(resId);
            return color;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过资源名字获取Drawable
     *
     * @param resName 资源名称
     * @return Drawable
     */
    public Drawable getDrawableByName(String resName) {
        try {
            int resId = mSkinResource.getIdentifier(resName, "drawable", mPackageName);
            Log.e(TAG, "resId -> " + resId + " mPackageName -> " + mPackageName + " resName -> " + resName);
            return mSkinResource.getDrawableForDensity(resId, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getColorByName(String resName) {
        try {
            int resId = mSkinResource.getIdentifier(resName, "color", mPackageName);
            return mSkinResource.getColor(resId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
