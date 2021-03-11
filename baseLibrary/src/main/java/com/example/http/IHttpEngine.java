package com.example.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by hjcai on 2021/3/9.
 * 引擎规范
 */
interface IHttpEngine {
    // get请求
    void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    // post请求
    void post(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    // 优化空间
    // 1下载文件

    // 2上传文件

    // 3 https 添加证书
}
