package com.example.learneassyjoke;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.framelibrary.BaseSkinActivity;
import com.example.framelibrary.navigationbar.DefaultNavigationBar;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hjcai on 2021/3/10.
 */
public class ActivityTestOKHttp extends BaseSkinActivity implements View.OnClickListener {
    private static final String TAG = "ActivityTestOKHttp";
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ok_http);
        findViewById(R.id.get).setOnClickListener(this);
        findViewById(R.id.post).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        new DefaultNavigationBar.Builder(this)
                .setTitle("OKHttp")
                .setRightIcon(R.mipmap.ic_launcher)
                .setRightText("OKHttp")
                .setRightClickListener(v -> Toast.makeText(ActivityTestOKHttp.this, "OKHttp", Toast.LENGTH_SHORT).show())
                .builder();
    }

    @Override
    protected void setContentView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:
                doOKHttpGet();
                break;
            case R.id.post:
                doOKHttpPost();
                break;
        }
    }

    private void doOKHttpGet() {
        Runnable runnable = () -> {
            String response = "";
            try {
                response = getRequest("https://raw.github.com/square/okhttp/master/README.md");
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                e.printStackTrace();
            }
            Log.d(TAG, "doOKHttpGet: " + response);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    String getRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private void doOKHttpPost() {
        Runnable runnable = () -> {
            String json = bowlingJson("Jesse", "Jake111");
            String response = "";
            try {
                response = post("http://www.roundsapp.com/post", json);
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                e.printStackTrace();
            }
            Log.d(TAG, "doOKHttpPost: " + response);
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    //模拟json数据
    String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }
}
