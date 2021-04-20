package com.example.framelibrary.skin.support;

import android.os.Environment;

import java.io.File;

/**
 * Created by hjcai on 2021/4/19.
 */
public class SkinPathUtil {
    // return /storage/emulated/0/light.skin
    public static String getLightSkinPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "light.skin";
    }
}
