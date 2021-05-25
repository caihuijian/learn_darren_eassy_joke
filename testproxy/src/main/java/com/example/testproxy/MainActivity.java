package com.example.testproxy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Proxy;

public class MainActivity extends AppCompatActivity {

    BusinessMan mBusinessMan;// 静态代理对象
    IBank mConsumerProxy;// 动态代理对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 被代理对象
        Consumer mConsumer = new Consumer();

        // 静态代理对象
        mBusinessMan = new BusinessMan(mConsumer);

        // 动态代理对象
        mConsumerProxy = (IBank) Proxy.newProxyInstance(
                mConsumer.getClass().getClassLoader(),
                mConsumer.getClass().getInterfaces(),
                new DynamicProxyCallBack(mConsumer));
    }

    // 静态代理测试
    public void staticApply(View view) {
        // 调用的代理类的方法 但是核心内容调用的是真实类（消费者）的方法
        mBusinessMan.applyBank();
    }

    // 静态代理测试
    public void staticLose(View view) {
        // 调用的代理类的方法 但是核心内容调用的是真实类（消费者）的方法
        mBusinessMan.loseBank();
    }

    // 动态代理测试
    public void dynamicLose(View view) {
        mConsumerProxy.loseBank();
    }

    // 动态代理测试
    public void dynamicApply(View view) {
        mConsumerProxy.applyBank();
    }
}