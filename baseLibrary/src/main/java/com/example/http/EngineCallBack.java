package com.example.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by hjcai on 2021/3/9.
 * 引擎回调
 */
public interface EngineCallBack {
    // 调用http接口时最开始回调的方法
    public void onPreExecute(Context context, Map<String, Object> params);

    // 错误时的回调
    public void onError(Exception e);

    // 成功时的回调
    // 为什么不返回对象而返回String 除非成功失败返回同一种对象，否则返回对象会出问题，例如成功返回data对象的json数组  失败返回字符串 那么解析的时候会出问题
    // 返回对象可以在子类中实现
    public void onSuccess(String result);

    // 默认的回调 什么都不处理(相当于没有回调)
    public final EngineCallBack DEFAULT_CALL_BACK = new EngineCallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
