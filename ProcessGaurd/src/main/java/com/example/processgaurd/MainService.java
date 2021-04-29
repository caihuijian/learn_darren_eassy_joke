package com.example.processgaurd;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by hjcai on 2021/4/28.
 */
public class MainService extends Service {
    private static final String TAG = "MainService";
    private static final int SERVICE_ID = 0x11;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, " MainService 创建 onCreate: ");
        new Thread(() -> {
            while (true) {
                Log.e(TAG, "MainService 等待接收消息");
                try {
                    Thread.sleep(2000 * 20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, " MainService启动 onStartCommand: ");
        // 提高进程优先级
        startForeground(SERVICE_ID, Util.getNotification(MainService.this, MainService.class, "111", "1111"));

        // 尝试bind remote GuardService
        Intent guardIntent = new Intent(MainService.this, GuardService.class);
        bindService(guardIntent,
                mServiceConnection, Context.BIND_IMPORTANT);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "MainService onBind ");
        return new IRemoteServiceConn.Stub() {
        };
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "MainService onServiceConnected ");
            // 建立连接
            Toast.makeText(MainService.this, "onServiceConnected MainService建立连接到守护Service成功", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接
            Log.e(TAG, "MainService onServiceDisconnected ");
            Intent guardIntent = new Intent(MainService.this, GuardService.class);
            startService(guardIntent);
            MainService.this.bindService(guardIntent,
                    mServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}
