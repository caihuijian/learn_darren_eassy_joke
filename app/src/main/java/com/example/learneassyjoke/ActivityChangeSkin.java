package com.example.learneassyjoke;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.framelibrary.BaseSkinActivity;
import com.example.framelibrary.navigationbar.DefaultNavigationBar;

public class ActivityChangeSkin extends BaseSkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_skin);
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
                .setTitle("投稿")
                .setRightIcon(R.mipmap.ic_launcher)
                .setRightText("右边")
                .setRightClickListener(v -> Toast.makeText(ActivityChangeSkin.this, "右边", Toast.LENGTH_SHORT).show())
                .builder();
    }

    @Override
    protected void setContentView() {

    }

    public void changeSkin(View view) {
    }

    public void restoreSkin(View view) {
    }
}