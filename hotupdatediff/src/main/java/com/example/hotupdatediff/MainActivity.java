package com.example.hotupdatediff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

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
        //ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

//    if (ContextCompat.checkSelfPermission(
//    CONTEXT, Manifest.permission.REQUESTED_PERMISSION) ==
//    PackageManager.PERMISSION_GRANTED) {
//        // You can use the API that requires the permission.
//        performAction(...);
//    } else if (shouldShowRequestPermissionRationale(...)) {
//        // In an educational UI, explain to the user why your app requires this
//        // permission for a specific feature to behave as expected. In this UI,
//        // include a "cancel" or "no thanks" button that allows the user to
//        // continue using your app without granting the permission.
//        showInContextUI(...);
//    } else {
//        // You can directly ask for the permission.
//        // The registered ActivityResultCallback gets the result of this request.
//        requestPermissionLauncher.launch(
//                Manifest.permission.REQUESTED_PERMISSION);
//    }

    public void diff(View view) {
        DiffUtils.diff(mOldApkPath, mNewApkPath, mPatchPath);
    }
}