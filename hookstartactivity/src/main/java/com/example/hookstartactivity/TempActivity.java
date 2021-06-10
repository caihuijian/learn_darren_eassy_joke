package com.example.hookstartactivity;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by hjcai on 2021/6/9.
 * 临时Activity 已经在清单文件注册
 * 用于糊弄intent的检测流程
 */
public class TempActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
    }
}
