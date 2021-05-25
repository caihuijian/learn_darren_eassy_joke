package com.example.testproxy;

import android.util.Log;

// 银行业务员 代理类
public class BusinessMan implements IBank {

    private final IBank mConsumer;

    public BusinessMan(IBank man) {
        this.mConsumer = man;
    }

    @Override
    public void applyBank() {
        //申请银行卡
        if (mConsumer != null) {
            Log.e("hjcai", "进行挂号等基础流程");
            mConsumer.applyBank();
            Log.e("hjcai", "流程结束");
        }
    }

    // 申请挂失
    @Override
    public void loseBank() {
        if (mConsumer != null) {
            Log.e("hjcai", "进行挂号等基础流程");
            mConsumer.loseBank();
            Log.e("hjcai", "流程结束");
        }
    }
}
