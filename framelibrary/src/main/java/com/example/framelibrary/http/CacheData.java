package com.example.framelibrary.http;

/**
 * Description: 缓存的数据结构
 */
public class CacheData {
    // 请求链接
    private String mUrlKey;

    // 后台返回数据的Json
    private String mResultJson;

    public CacheData() {

    }

    public CacheData(String urlKey, String resultJson) {
        this.mUrlKey = urlKey;
        this.mResultJson = resultJson;
    }

    public String getResultJson() {
        return mResultJson;
    }
}
