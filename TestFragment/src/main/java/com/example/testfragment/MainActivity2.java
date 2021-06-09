package com.example.testfragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testfragment.R;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 这是一个TODO的project 本来想使用libjpeg-turbo学习引入外部so 但是遇到诸多问题
        // 先暂停吧 有空以后再看
    }

    public void jump(View view) {
        Log.e("hjcai", "jump: self");
        Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
        startActivity(intent);

    }
}