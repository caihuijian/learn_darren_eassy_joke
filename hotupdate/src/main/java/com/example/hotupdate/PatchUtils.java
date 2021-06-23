package com.example.hotupdate;

/**
 * Created by hjcai on 2021/6/18.
 */
public class PatchUtils {
    static {
        System.loadLibrary("bspatch");
    }

    /**
     * @param oldApkPath 旧版本的apk
     * @param newApkPath 合并后新的apk路径
     * @param patchPath  差分包路径
     */
    public static native void combine(String oldApkPath, String newApkPath, String patchPath);
}
