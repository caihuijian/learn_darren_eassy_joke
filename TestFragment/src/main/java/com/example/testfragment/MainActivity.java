package com.example.testfragment;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testfragment.R;

import main.java.com.example.testfragment.*;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private LinearLayout content;
    private TextView tv1, tv2, tv3, tv4;
    private FragmentManager fm;
    private FragmentTransaction ft;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged: MainActivity");
    }

    /*  @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: ");
        content = (LinearLayout) findViewById(R.id.content);
        tv1 = (TextView) findViewById(R.id.tab1);
        tv2 = (TextView) findViewById(R.id.tab2);
        tv3 = (TextView) findViewById(R.id.tab3);
        tv4 = (TextView) findViewById(R.id.tab4);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv1.setBackgroundColor(Color.BLUE);
        fm = getFragmentManager();
        ft = fm.beginTransaction();

        if (savedInstanceState == null) {
            ft.replace(R.id.content, new Fragment1());
            ft.commit();
        }
    }

    @Override
    public void onClick(View v) {
        ft = fm.beginTransaction();
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tab1:
                tv1.setBackgroundColor(Color.BLUE);
                tv2.setBackgroundColor(Color.WHITE);
                tv3.setBackgroundColor(Color.WHITE);
                tv4.setBackgroundColor(Color.WHITE);
                ft.replace(R.id.content, new Fragment1());
                break;
            case R.id.tab2:
                tv1.setBackgroundColor(Color.WHITE);
                tv2.setBackgroundColor(Color.BLUE);
                tv3.setBackgroundColor(Color.WHITE);
                tv4.setBackgroundColor(Color.WHITE);
                ft.replace(R.id.content, new Fragment2());
                break;
            case R.id.tab3:
                tv1.setBackgroundColor(Color.WHITE);
                tv2.setBackgroundColor(Color.WHITE);
                tv3.setBackgroundColor(Color.BLUE);
                tv4.setBackgroundColor(Color.WHITE);
                ft.replace(R.id.content, new Fragment3());
                break;
            case R.id.tab4:
                tv1.setBackgroundColor(Color.WHITE);
                tv2.setBackgroundColor(Color.WHITE);
                tv3.setBackgroundColor(Color.WHITE);
                tv4.setBackgroundColor(Color.BLUE);
                ft.replace(R.id.content, new Fragment4());
                break;

            default:
                break;
        }
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}

/*{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 这是一个TODO的project 本来想使用libjpeg-turbo学习引入外部so 但是遇到诸多问题
        // 先暂停吧 有空以后再看

    }

    public void jump(View view) {
        Intent intent = new Intent(MainActivity.this,MainActivity2.class);
        startActivity(intent);
    }
}*/