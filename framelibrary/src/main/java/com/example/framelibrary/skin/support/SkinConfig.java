package com.example.framelibrary.skin.support;

import androidx.annotation.IntDef;

/**
 * Created by hjcai on 2021/4/21.
 */
public class SkinConfig {
    // SharedPreferences xml文件的文件名
    public static final String SHARED_PREFERENCE_FILE_NAME_SKIN = "skinInfo";
    // SharedPreferences中保存皮肤文件路径的key
    public static final String SKIN_PATH_NAME_KEY = "skinPath";

    // 不需要改变任何东西
    public static final int SKIN_CHANGE_NOTHING = -1;

    // 换肤成功
    public static final int SKIN_CHANGE_SUCCESS = 1;

    // 皮肤文件不存在
    public static final int SKIN_FILE_NO_EXIST = -2;

    // 皮肤文件有错误可能不是一个apk文件
    public static final int SKIN_FILE_ERROR = -3;

    // 皮肤文件状态OK
    public static final int SKIN_FILE_OK = 2;

}
