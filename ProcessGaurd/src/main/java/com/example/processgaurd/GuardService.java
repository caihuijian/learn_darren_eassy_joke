package com.example.processgaurd;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by hjcai on 2021/4/28.
 */
public class GuardService extends Service {
    private static final String TAG = "GuardService";
    private final int SERVICE_ID = 0x12;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "GuardService 创建 onCreate: ");
        new Thread(() -> {
            while (true) {
                Log.e(TAG, "GuardService 等待接收消息");
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
        Log.e(TAG, "GuardService启动 onStartCommand: ");
        // 提高进程优先级
        startForeground(SERVICE_ID, Util.getNotification(GuardService.this, GuardService.class, "222", "2222"));

        // 绑定建立连接
        bindService(new Intent(this, MainService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "GuardService onBind ");
        return new IRemoteServiceConn.Stub() {
        };
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "GuardService onServiceConnected ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "GuardService onServiceDisconnected ");
            // 连接异常断开 ,重新启动，重新绑定
            Intent intent = new Intent(GuardService.this, MainService.class);
            startService(intent);
            bindService(intent,
                    mServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}
