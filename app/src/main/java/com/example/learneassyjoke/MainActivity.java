package com.example.learneassyjoke;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.framelibrary.BaseSkinActivity;
import com.example.ioc.OnClick;
import com.example.ioc.ViewById;
import com.example.ioc.ViewUtils;

import java.io.File;
import java.io.IOException;


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

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_ioc_test);
        ViewUtils.injectActivity(this);
        mTextView.setText("Tv text！！！！");
        mTextView.setOnClickListener(this);
        mButton.setText("Button text！！！！");
        mButton.setOnClickListener(this);
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
                int x = 2 / 0;
                Toast.makeText(this, "text view clicked" + x, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn:
                Toast.makeText(this, "button clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}