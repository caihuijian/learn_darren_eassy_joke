package com.example.hotupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "hjcai";
    private String mPatchPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            + File.separator + "1_2.patch";

    private String mNewApkPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            + File.separator + "2.0.apk";

    protected void hotUpdate() {
        // 1.使用当前版本查询是否需要更新

        // 2.如果需要更新版本
        // a 部分更新 下载patch
        // b 下载全部apk

        // 3.下载完差分包之后, 调用native方法去合并生成新的apk
        // 是一个耗时操作，怎么弄 开线程+，Handler, AsyncTask , RXJava
        // 本地apk路径怎么来，已经被安装了  1.0
        // 获取本地的getPackageResourcePath()apk路径
        if (!new File(mPatchPath).exists()) {// TODO 这里有问题！！！！！！！
            Log.e(TAG, "hotUpdate: mPatchPath doesn't exist");
            return;
        }
        String oldApkPath = getPackageResourcePath();
        // 需要申请sdcard读写权限
        PatchUtils.combine(oldApkPath, mNewApkPath, mPatchPath);
        // 4.删除patch

        delFile(mPatchPath);
        Log.e(TAG, "hotUpdate: patch deleted");

        // 5.需要校验签名    就是获取本地apk的签名，与我们新版本的apk作对比

        // 6.安装最新版本

        installAPK();
        Log.e(TAG, "hotUpdate: installed APK");
        // 7.删除最新版本临时apk
        delFile(mNewApkPath);
        Log.e(TAG, "hotUpdate: new apk deleted");
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(new File(mNewApkPath)),
//                "application/vnd.android.package-archive");
//        startActivity(intent);
    }

    private void installAPK() {
        //File fileApkToInstall = new File(getExternalFilesDir("Download"), mNewApkPath);
        File fileApkToInstall = new File(mNewApkPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //final Uri uri = Uri.parse("file://" + mNewApkPath);
            Uri apkUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", fileApkToInstall);
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            MainActivity.this.startActivity(intent);
        } else {
            Uri apkUri = Uri.fromFile(fileApkToInstall);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.this.startActivity(intent);
        }
    }

    public String getRealPathFromURI(Uri contentURI) {
        return contentURI.getPath();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hotUpdate();
    }


    public static boolean delFile(String filePathAndName) {
        File file = new File(filePathAndName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("hjcai", "删除单个文件" + filePathAndName + "成功！");
                return true;
            } else {
                Log.e("hjcai", "删除单个文件" + filePathAndName + "失败！");
                return false;
            }
        } else {
            Log.e("hjcai", "删除单个文件失败：" + filePathAndName + "不存在！");
            return false;
        }
    }
}