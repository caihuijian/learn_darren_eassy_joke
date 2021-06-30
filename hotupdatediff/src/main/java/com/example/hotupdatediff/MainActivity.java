package com.example.hotupdatediff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 2;
    private String mPatchPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            + File.separator + "1_2.patch";

    private String mNewApkPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            + File.separator + "2.0.apk";

    private String mOldApkPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            + File.separator + "1.0.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!checkPermission(READ_EXTERNAL_STORAGE)) {
            requestPermission(this, READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        if (!checkPermission(WRITE_EXTERNAL_STORAGE)) {
            requestPermission(this, WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    public void requestPermission(Activity activity, String permission, int requestCode) {
        //第一次申请权限被拒后每次进入activity就会调用，或者用户之前允许了，之后又在设置中去掉了该权限
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            Toast.makeText(activity, permission + " permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
        }
    }

    public boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void diff(View view) {
        if (!new File(mOldApkPath).exists()) {
            Toast.makeText(MainActivity.this, "hotUpdateDiff: mOldApkPath doesn't exist", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!new File(mNewApkPath).exists()) {
            Toast.makeText(MainActivity.this, "hotUpdateDiff: mNewApkPath doesn't exist", Toast.LENGTH_SHORT).show();
            return;
        }

        Thread thread = new Thread(() -> {
            DiffUtils.diff(mOldApkPath, mNewApkPath, mPatchPath);
            view.post(() -> {
                Toast.makeText(MainActivity.this, "patch 生成完毕", Toast.LENGTH_SHORT).show();
            });
        });
        thread.start();
    }
}