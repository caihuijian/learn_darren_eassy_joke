package com.example.http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hjcai on 2021/3/9.
 * 第三方http框架的封装类
 * 主体思想：链式调用
 */
public class HttpUtils {
    // 直接带参数 都存放在String中
    private String mUrl;
    // 请求方式
    private int mRequestType = GET_TYPE;
    private static final int POST_TYPE = 0x0001;
    private static final int GET_TYPE = 0x0002;

    // 存放参数
    private Map<String, Object> mParams;
    private Context mContext;

    private HttpUtils(Context context) {
        mContext = context;
        mParams = new HashMap<>();
    }

    // 存放 context
    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    //传入url
    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }

    // 传入请求的方式(两种方式)
    public HttpUtils post() {
        mRequestType = POST_TYPE;
        return this;
    }

    public HttpUtils get() {
        mRequestType = GET_TYPE;
        return this;
    }

    // 传入参数(两种方式)
    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    // http请求开始执行
    public void execute(EngineCallBack callBack) {
        if (callBack == null) {
            callBack = EngineCallBack.DEFAULT_CALL_BACK;
        }
        // 预执行部分
        callBack.onPreExecute(mContext, mParams);

        // 判断执行方法
        if (mRequestType == POST_TYPE) {
            post(mUrl, mParams, callBack);
        }

        if (mRequestType == GET_TYPE) {
            get(mUrl, mParams, callBack);
        }
    }

    public void execute() {
        execute(null);
    }

    // 默认OkHttpEngine
    private static IHttpEngine mHttpEngine;

    // 在Application初始化引擎
    public static void init(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
    }

    // 切换其他引擎
    public HttpUtils exchangeEngine(IHttpEngine httpEngine) {
        mHttpEngine = httpEngine;
        return this;
    }

    private void get(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.get(mContext, url, params, callBack);
    }

    private void post(String url, Map<String, Object> params, EngineCallBack callBack) {
        mHttpEngine.post(mContext, url, params, callBack);
    }

    /**
     * 将params中的参数拼接到url上
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuilder stringBuilder = new StringBuilder(url);
        // 如果url中没有? 在末尾加一个?
        if (!url.contains("?")) {
            stringBuilder.append("?");
        } else {
            // url中已经存在? 如果不是以?结尾 在末尾加上一个&
            if (!url.endsWith("?")) {
                stringBuilder.append("&");
            }
        }
        //上述操作之后 该url以?或者&结尾

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            //将参数一个个拼接到Url上
            stringBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        //删除最后一个&
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    //貌似是获取object的类型
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }
}
