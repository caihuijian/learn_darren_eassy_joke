package com.example.fixBug;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hjcai on 2021/2/18.
 * <p>
 * Description: 拦截应用的闪退信息
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "ExceptionCrashHandler";
    private static ExceptionCrashHandler mInstance;

    // 留下原来异常处理器的引用，便于开发的时候调试
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    // 上下文  用于获取版本信息和手机信息
    private WeakReference<Context>  mContextRef;

    public static ExceptionCrashHandler getInstance() {
        if (mInstance == null) {
            synchronized (ExceptionCrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        Log.e(TAG, " ExceptionCrashHandler init ");
        /*
         * 官方解释
         * Set the handler invoked when this thread abruptly terminates
         * due to an uncaught exception.
         *
         * 当前线程由于未捕获异常而终止的时候 调用当前的handler处理异常
         **/
        Thread.currentThread().setUncaughtExceptionHandler(this);
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.mContextRef = new WeakReference<>(context);
    }

    private ExceptionCrashHandler() {

    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Log.e(TAG, "拦截到闪退信息" + e.getMessage());
        // 1. 获取信息
        // 1.1 崩溃信息
        // 1.2 手机信息
        // 1.3 版本信息
        // 2.写入文件
        String crashFileName = saveInfoToCache(e);

        Log.e(TAG, "fileName --> " + crashFileName);
        // 让系统默认处理 否则应用发生crash后没有任何反应
        mDefaultHandler.uncaughtException(t, e);
    }

    /**
     * 得到获取的 软件信息，设备信息和出错信息
     * 保存在机身内存
     */
    private String saveInfoToCache(Throwable ex) {
        String fileName = null;
        StringBuilder stringBuffer = new StringBuilder();

        //遍历手机信息 格式化后输出
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContextRef.get())
                .entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuffer.append(key).append(" = ").append(value).append("\n");
        }
        stringBuffer.append(obtainExceptionInfo(ex));

        //这里原课程有问题 为什么获取外部存储的可用状态 最后存储在内部存储 因此我注释掉了最外层的判断
        //if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)) {
        // 获取crash文件夹
        File dir = new File(mContextRef.get().getFilesDir() + File.separator + "crash"
                + File.separator);

        // 先删除之前的异常信息
        if (dir.exists()) {
            deleteDir(dir);
        }

        // 再重新创建文件夹
        if (!dir.exists()) {
            boolean res = dir.mkdir();
            if (!res) {
                Log.e(TAG, "saveInfoToSD: create dir failed ");
                return "";
            }
        }
        // 创建文件
        try {
            fileName = dir.toString()
                    + File.separator
                    + getAssignTime() + ".txt";
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(stringBuffer.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //}
        return fileName;
    }

    /**
     * 返回当前日期根据格式
     **/
    private String getAssignTime() {
        DateFormat dataFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm", Locale.US);
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(currentTime);
    }

    public static void deleteDir(File file) {
        boolean res;
        if (file.isFile()) {
            res = file.delete();
            if (!res){
                Log.e(TAG, "deleteDir: failed when delete "+ file);
            }
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                res = file.delete();
                if (!res){
                    Log.e(TAG, "deleteDir: failed when delete "+ file);
                }
                return;
            }
            for (File f : childFile) {
                deleteDir(f);
            }
            res = file.delete();
            if (!res){
                Log.e(TAG, "deleteDir: failed when delete "+ file);
            }
        }
    }


    /**
     * 获取系统未捕捉的错误信息 将其转化为字符串
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     *
     */
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MOBLE_INFO", getMobileInfo());
        return map;
    }

    /**
     * 获取机器信息
     */
    public static String getMobileInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                stringBuilder.append(name).append("=").append(value).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
