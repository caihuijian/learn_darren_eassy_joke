package com.example.learneassyjoke;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.framelibrary.BaseSkinActivity;
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

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_ioc_test);
        ViewUtils.injectActivity(this);
        mTextView.setText("This is Bug text ");
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
        }
    }
}