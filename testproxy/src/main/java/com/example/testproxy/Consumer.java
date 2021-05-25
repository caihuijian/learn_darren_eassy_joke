package com.example.testproxy;

import android.util.Log;

// 消费者
public class Consumer implements IBank {

    @Override
    public void applyBank() {
        Log.e("hjcai", "申请新的银行卡");
    }

    @Override
    public void loseBank() {
        Log.e("hjcai", "申请挂失");
    }

    // ......
}
