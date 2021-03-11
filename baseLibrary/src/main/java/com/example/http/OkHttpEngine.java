package com.example.http;

import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hjcai on 2021/3/9.
 */
public class OkHttpEngine implements IHttpEngine {
    private static final String TAG = "OkHttpEngine";
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void post(Context context, String url, Map<String, Object> params, EngineCallBack callBack) {
        // 拼装参数
        final String jointUrl = HttpUtils.jointParams(url, params);
        Log.e("TAG", "post 请求Url" + jointUrl);

        RequestBody requestBody = appendBody(params);
        // OKHttp官网标准request构造request方法
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        // OKHttp官方标准调用方式
        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    // 注意，两个回调方法都不是在主线程中 不要在这里直接操作UI
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.e(TAG, "Post返回结果：" + jointUrl);
                        callBack.onSuccess(result);
                    }
                }
        );
    }

    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    // 添加参数
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    // 处理文件 --> Object File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file
                                    .getAbsolutePath())), file));
                } else if (value instanceof List) {
                    // 代表提交的是 List集合 //为什么说List里面一定是File?
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    @Override
    public void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack) {
        url = HttpUtils.jointParams(url, params);
        Log.e(TAG, "Get请求路径：" + url);

        Request request = new Request
                .Builder()
                .url(url)
                .tag(context)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() == null) {
                    Log.e(TAG, "Get返回结果是null");
                    return;
                }
                String resultJson = response.body().string();
                callBack.onSuccess(resultJson);
                Log.e("Get返回结果：", resultJson);
            }
        });
    }
}
