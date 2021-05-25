package com.example.testjni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 这是一个TODO的project 本来想使用libjpeg-turbo学习引入外部so 但是遇到诸多问题
        // 先暂停吧 有空以后再看
    }
}