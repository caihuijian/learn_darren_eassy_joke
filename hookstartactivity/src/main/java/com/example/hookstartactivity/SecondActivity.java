package com.example.hookstartactivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by hjcai on 2021/6/9.
 * <p>
 * 真正想要启动的Activity
 */
public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
