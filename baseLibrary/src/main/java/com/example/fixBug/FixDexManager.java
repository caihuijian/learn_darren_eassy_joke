package com.example.fixBug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by hjcai on 2021/2/23.
 */
public class FixDexManager {
    private final Context mContext;
    private final File mDexDir;//copy路径 /data/user/0/com.example.learneassyjoke/app_odex/
    private final static String TAG = "FixDexManager";

    public FixDexManager(Context context) {
        this.mContext = context;
        // 获取应用可以访问的dex目录
        this.mDexDir = context.getDir("odex", Context.MODE_PRIVATE);
    }

    public void fixBugByDex() throws Exception {
        //1.获取Android系统中原来的dex list
        Object applicationOriginDexElements = loadOriginDex();
        Log.e(TAG, "fixBugByDex: loadOriginDex end ");

        //2.准备dex解压路径 解压路径是拷贝路径的一个子文件夹
        File unzipDirectory = prepareUnzipDir();
        Log.e(TAG, "fixBugByDex: prepareUnzipDir end " + unzipDirectory);

        //3.查看是否有dex文件 如果存在 copy到指定目录,只有在app内部的目录才能解压和解析dex文件
        File srcFile = findDexFiles();
        File destFile = new File(mDexDir,srcFile.getName());
        copyFile(srcFile, destFile);

        //4.获取copy目录中的所有dex补丁包(*.dex)
        List<File> allFixDexFile = loadAllFixDex();
        if (allFixDexFile == null || allFixDexFile.size() == 0) {
            Log.e(TAG, "fixBugByDex: dex size incorrect, return!");
            return;
        } else {
            Log.e(TAG, "fixBugByDex: dex size is " + allFixDexFile.size());
        }
        //5.合并所有补丁dex包以及运行中的dex包
        applicationOriginDexElements = combineDex(allFixDexFile, unzipDirectory, applicationOriginDexElements);
        Log.e(TAG, "fixBugByDex: combineDex end");
        //6.将合并的dex包注入classLoader
        injectDexToClassLoader(applicationOriginDexElements);
        Log.e(TAG, "fixBugByDex: injectDexToClassLoader end");
    }

    /**
     * copy file
     *
     * @param src  source file
     * @param dest target file
     * @throws IOException
     */
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inFile = null;
        FileChannel outFile = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inFile = new FileInputStream(src).getChannel();
            outFile = new FileOutputStream(dest).getChannel();
            inFile.transferTo(0, inFile.size(), outFile);
        } finally {
            if (inFile != null) {
                inFile.close();
            }
            if (outFile != null) {
                outFile.close();
            }
        }
    }

    private File findDexFiles() throws Exception {
        File fixFile = new File(mContext.getExternalFilesDir(null), "fix2.dex");
        if (fixFile.exists()) {
            Log.e(TAG, "findDexFiles: " + fixFile.getAbsolutePath());
        } else {
            boolean b = fixFile.mkdir();
            if (b) {
                Log.e(TAG, "create success" + fixFile.getAbsolutePath());
            } else {
                Log.e(TAG, "create failed" + fixFile.getAbsolutePath());
                throw new FileNotFoundException();
            }
        }
        return fixFile;
    }

    private File prepareUnzipDir() {
        File unzipDirectory = new File(mDexDir, "odex");

        if (!unzipDirectory.exists()) {
            boolean res = unzipDirectory.mkdirs();
            if (!res) {
                Log.e(TAG, "prepareUnzipDir: mkdir failed");
            }
        }
        return unzipDirectory;
    }

    private void injectDexToClassLoader(Object applicationOriginDexElements) throws Exception {
        // 1.先获取 pathList
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(applicationClassLoader);

        // 2. pathList里面的dexElements
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, applicationOriginDexElements);
    }

    private Object combineDex(List<File> allFixDexFiles, File unzipDirectory, Object applicationOriginDexElements) throws Exception {
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        for (File fixDexFile : allFixDexFiles) {
            // dexPath  dex路径
            // optimizedDirectory  解压路径
            // libraryPath .so文件位置
            // parent 父ClassLoader
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(),// dex路径  必须要在应用目录下的odex文件中
                    unzipDirectory,// 解压路径
                    null,// .so文件位置
                    applicationClassLoader // 父ClassLoader
            );
            //5.1 解析当前fixDexFile为Elements对象
            Object fixDexElements = getDexElementsByClassLoader(fixDexClassLoader);

            //5.2 把补丁的dexElement 插到 已经运行的 dexElement 的最前面
            //即在applicationClassLoader数组前插入数次解析出的fixDexElements对象
            //核心是数组合并
            applicationOriginDexElements = combineArray(fixDexElements, applicationOriginDexElements);
        }
        return applicationOriginDexElements;
    }

    /**
     * 从classLoader中获取 dexElements
     */
    private Object getDexElementsByClassLoader(ClassLoader classLoader) throws Exception {

        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        return dexElementsField.get(pathList);
    }

    /**
     * 合并两个数组
     */
    private static Object combineArray(Object arrayHead, Object arrayTail) {
        Class<?> localClass = arrayHead.getClass().getComponentType();
        int i = Array.getLength(arrayHead);
        int j = i + Array.getLength(arrayTail);
        Log.d(TAG, "combineArray: first array length "+i+" second array length "+j);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayHead, k));
            } else {
                Array.set(result, k, Array.get(arrayTail, k - i));
            }
        }
        return result;
    }

    private List<File> loadAllFixDex() {
        File[] dexFiles = mDexDir.listFiles();
        if (dexFiles == null || dexFiles.length == 0) {
            Log.e(TAG, "file list is null or file size is 0");
            return null;
        }
        List<File> fixDexFiles = new ArrayList<>();
        for (File dexFile : dexFiles) {
            if (dexFile.getName().endsWith(".dex")) {
                fixDexFiles.add(dexFile);
            }
        }
        return fixDexFiles;
    }

    //TODO 可以和getDexElementsByClassLoader合并
    private Object loadOriginDex() throws Exception {
        ClassLoader applicationClassLoader = mContext.getClassLoader();
        // 1.1 先获取 pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(applicationClassLoader);

        if (pathList == null) {
            return null;
        }
        // 1.2 pathList里面的dexElements
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        return dexElementsField.get(pathList);
    }
}
