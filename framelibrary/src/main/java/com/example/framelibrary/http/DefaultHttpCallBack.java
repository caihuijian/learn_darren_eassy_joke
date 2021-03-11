package com.example.framelibrary.http;

import android.content.Context;

import com.example.http.EngineCallBack;
import com.example.http.HttpUtils;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by hjcai on 2021/3/10.
 */
public abstract class DefaultHttpCallBack<T> implements EngineCallBack {
    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        // 固定参数部分 暂时写死
//        params.put("app_name","joke_essay");
//        params.put("version_name","5.7.0");
//        params.put("ac","wifi");
//        params.put("device_id","30036118478");
//        params.put("device_brand","Xiaomi");
//        params.put("update_version_code","5701");
//        params.put("manifest_version_code","570");
//        params.put("longitude","113.000366");
//        params.put("latitude","28.171377");
//        params.put("device_platform","android");
        onPreExecute();
    }

    @Override
    public void onSuccess(String result) {
//        Gson gson = new Gson();
//        // data:{"name","darren"}   data:"请求失败"
//        T objResult = (T) gson.fromJson(result,
//                HttpUtils.analysisClazzInfo(this));
//        onSuccess(objResult);
        T objResult = (T) result;
        onSuccess(objResult);
    }


    // 以下是三个子类必须实现的方法
    // 返回可以直接操作的对象
    public abstract void onSuccess(T result);

    // 开始执行了
    public abstract void onPreExecute();

    @Override
    public abstract void onError(Exception e);
}
