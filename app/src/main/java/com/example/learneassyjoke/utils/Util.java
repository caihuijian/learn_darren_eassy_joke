package com.example.learneassyjoke.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

/**
 * Created by hjcai on 2021/4/8.
 */
public class Util {
    public static void checkAndRequestReadSDCardPermission(Activity activity) {
        if (activity == null) {
            return;
        }
        if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }
}
