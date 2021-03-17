package com.example.learneassyjoke;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.framelibrary.BaseSkinActivity;
import com.example.framelibrary.database.DaoSupportFactory;
import com.example.framelibrary.database.IDaoSupport;
import com.example.framelibrary.http.DefaultHttpCallBack;
import com.example.framelibrary.navigationbar.DefaultNavigationBar;
import com.example.http.HttpUtils;
import com.example.http.OkHttpEngine;
import com.example.ioc.ViewById;
import com.example.ioc.ViewUtils;
import com.example.learneassyjoke.model.Person;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;


public class MainActivity extends BaseSkinActivity implements View.OnClickListener {
    public static final String TAG = "MyActivity";
    @ViewById(R.id.tv)
    TextView mTextView;
    @ViewById(R.id.btn)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        IDaoSupport<Person> daoSupport = DaoSupportFactory.getFactoryInstance(MainActivity.this).getDao(Person.class);
        // 最少的知识原则
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            int totalNum = 1000;
            for (int i = 0; i < totalNum; i++) {
                daoSupport.inert(new Person("hjcai", i));
            }
            long endTime = System.currentTimeMillis();
            Log.e(TAG, " insert " + totalNum + " cost time -> " + (endTime - startTime));
        }).start();
        //AliFix();
    }

    private void AliFix() {
        // 测试 ,直接获取内部存储里面的 fix.aptach
        File fixFile = new File(MainActivity.this.getFilesDir(), "fix.apatch");

        if (fixFile.exists()) {
            // 修复Bug
            try {
                // 立马生效不需要重启
                BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Log.e(TAG, "initData: 修复成功");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "initData: 修复失败");
            }
        } else {
            Log.e(TAG, "没有找到apatch补丁文件 " + fixFile);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        new DefaultNavigationBar.Builder(this)
                .setTitle("投稿")
                .setRightIcon(R.mipmap.ic_launcher)
                .setRightText("右边")
                .setRightClickListener(v -> Toast.makeText(MainActivity.this, "右边", Toast.LENGTH_SHORT).show())
                .builder();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        ViewUtils.injectActivity(this);
        mTextView.setText("This is Bug text ");
        mTextView.setOnClickListener(this);
        mButton.setText("Button text！！！！");
        mButton.setOnClickListener(this);
        findViewById(R.id.testOKHttp).setOnClickListener(this::onClick);
        findViewById(R.id.testMyOKHttp).setOnClickListener(this::doMyHttpRequest);
        findViewById(R.id.clearData).setOnClickListener(this::clearData);
    }

    private void clearData(View view) {
        IDaoSupport<Person> daoSupport = DaoSupportFactory.getFactoryInstance(MainActivity.this).getDao(Person.class);
        daoSupport.deleteAll();
    }

    private void doMyHttpRequest(View view) {
        HttpUtils.init(new OkHttpEngine());
        HttpUtils.with(MainActivity.this)
                .exchangeEngine(new OkHttpEngine())
//                .addParam("iid", "6152551759")
//                .addParam("aid", "7")
//                .url("http://is.snssdk.com/2/essay/discovery/v3/")
                .url("https://www.baidu.com/")
//                .post()
                .execute(new DefaultHttpCallBack() {

                    @Override
                    public void onSuccess(Object result) {
                        Log.d(TAG, "onSuccess: " + result);
                    }

                    @Override
                    public void onPreExecute() {
                        Log.d(TAG, "onPreExecute: ");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "onError: " + e.getStackTrace());
                    }
                });
    }

//    @OnClick({R.id.tv, R.id.btn})
//    public void onItemClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv:
//                int x = 2 / 0;
//                Toast.makeText(this, "text view clicked" + x, Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btn:
//                Toast.makeText(this, "button clicked", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv:
                com.example.dialog.AlertDialog myDialog = new com.example.dialog.AlertDialog.Builder(this, android.R.style.Theme_Black)
                        .setContentView(R.layout.detail_comment_dialog)
//                        .setText(R.id.share_label, "评论哈哈")
                        .setTitle("这是标题")
                        .formBottom(true)
                        .fullWidth().show();

                // dialog去操作点击事件
                final EditText commentEt = myDialog.getView(R.id.comment_editor);
                myDialog.setOnclickListener(R.id.submit_btn, v12 -> Toast.makeText(MainActivity.this,
                        commentEt.getText().toString().trim(), Toast.LENGTH_LONG).show());
                myDialog.setText(R.id.share_label, "评论哈哈1111");
                break;
            case R.id.btn:
                Button button = new Button(MainActivity.this);
                button.setText("Button");
                button.setOnClickListener(v1 -> {
                    Toast.makeText(MainActivity.this, "Button点击了", Toast.LENGTH_SHORT).show();
                });
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("标题")
                        .setNegativeButton("取消", (dialog, which) -> {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                        })
                        .setPositiveButton("确定", (dialog, which) -> Toast.makeText(MainActivity.this, "点击了确定", Toast.LENGTH_SHORT).show())
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_launcher_background)
                        .setMessage("这是消息这是消息这是消息这是消息")
                        .setView(button)
                        .create();
                alertDialog.show();
                break;
            case R.id.testOKHttp:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ActivityTestOKHttp.class);
                startActivity(intent);
                break;
        }
    }
}