package com.example.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by hjcai on 2021/2/8.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置布局layout
        setContentView();

        // 初始化头部
        initTitle();

        // 初始化界面
        initView();

        // 初始化数据
        initData();
    }

    // 初始化数据
    protected abstract void initData();

    // 初始化界面
    protected abstract void initView();

    // 初始化头部
    protected abstract void initTitle();

    // 设置布局layout
    protected abstract void setContentView();


    /**
     * 启动Activity
     */
    protected void startActivity(Class<?> clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }

    /**
     * findViewById
     * @return View
     */
    protected <T extends View> T viewById(int viewId){
        return (T) findViewById(viewId);
    }
}
