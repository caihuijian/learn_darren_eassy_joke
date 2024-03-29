package com.example.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Created by hjcai on 2021/4/25.
 */
public class UserService extends Service {
    private static final String TAG = "UserService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    private final IUserCalc.Stub mBinder = new IUserCalc.Stub() {
        @Override
        public String getUserName() throws RemoteException {
            Log.e("hjcai", Log.getStackTraceString(new Throwable()));
            Log.e(TAG, "getUserName: ");
            return "hjcai";
        }

        @Override
        public String getUserPassword() throws RemoteException {
            Log.e(TAG, "getUserPassword: ");
            return "12341234";
        }
    };
}
