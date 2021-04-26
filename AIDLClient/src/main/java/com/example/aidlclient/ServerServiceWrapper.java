package com.example.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.aidlserver.IUserCalc;

/**
 * Created by hjcai on 2021/4/25.
 */
class ServerServiceWrapper {
    private static final String TAG = "ServerServiceWrapper";
    private IUserCalc mCalcAidl;
    public static ServerServiceWrapper instance;

    private ServerServiceWrapper() {

    }

    public static ServerServiceWrapper getInstance() {

        if (instance == null) {
            synchronized (ServerServiceWrapper.class) {
                if (instance == null) {
                    instance = new ServerServiceWrapper();
                }
            }
        }
        return instance;
    }

    public boolean bindService(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.example.aidl.server");
        // 需要明确指明server端包名 否则抛出如下异常
        // Service Intent must be explicit: Intent { act=com.example.aidl.server }
        intent.setPackage("com.example.aidlserver");
        return context.bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        if (mCalcAidl != null) {
            context.unbindService(mServiceConn);
        }
    }

    // 返回服务端的代理
    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: ");
            mCalcAidl = IUserCalc.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: ");
            mCalcAidl = null;
        }
    };

    public String getName() {
        try {
            return mCalcAidl == null ? "" : mCalcAidl.getUserName();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getPass() {
        try {
            return mCalcAidl == null ? "" : mCalcAidl.getUserPassword();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }
}
