package com.example.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void bind(View view) {
        boolean res = ServerServiceWrapper.getInstance().bindService(this);
        Log.e(TAG, "bind: " + res);
    }

    public void unBind(View view) {
        ServerServiceWrapper.getInstance().unbindService(this);
        Log.e(TAG, "unBind");
    }

    public void getName(View view) {
        String name = ServerServiceWrapper.getInstance().getName();
        Log.e(TAG, "getName: " + name);
    }

    public void getPass(View view) {
        String pass = ServerServiceWrapper.getInstance().getPass();
        Log.e(TAG, "getPass: " + pass);
    }
}