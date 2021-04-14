package com.example.learneassyjoke;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.framelibrary.BaseSkinActivity;
import com.example.framelibrary.database.DaoSupportFactory;
import com.example.framelibrary.database.IDaoSupport;
import com.example.framelibrary.database.QuerySupport;
import com.example.framelibrary.http.DefaultHttpCallBack;
import com.example.framelibrary.http.OkHttpEngine;
import com.example.framelibrary.navigationbar.DefaultNavigationBar;
import com.example.http.HttpUtils;
import com.example.ioc.ViewById;
import com.example.ioc.ViewUtils;
import com.example.learneassyjoke.model.Person;
import com.example.learneassyjoke.utils.Util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;


public class MainActivity extends BaseSkinActivity implements View.OnClickListener {
    public static final String TAG = "MyActivity";
    @ViewById(R.id.tv)
    TextView mTextView;
    @ViewById(R.id.btn)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View layoutView = View.inflate(this,R.layout.activity_main,null);
//        layoutView = LayoutInflater.from(this).inflate(R.layout.activity_main,null);
//        layoutView = LayoutInflater.from(this).inflate(R.layout.activity_main,null,false);
    }

    @Override
    protected void initData() {
        IDaoSupport<Person> daoSupport = DaoSupportFactory.getFactoryInstance(MainActivity.this).getDao(Person.class);
        // 最少的知识原则
        /*new Thread(() -> {
            int totalNum = 10;
            List<Person> personList = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < totalNum; i++) {
                personList.add(new Person("whzhou", "shanghai" + i, i));
            }
            daoSupport.insert(personList);
            long endTime = System.currentTimeMillis();
            Log.e(TAG, " insert " + totalNum + " cost time -> " + (endTime - startTime));
        }).start();*/
        //AliFix();
    }

    private void AliFix() {
        // 测试 ,直接获取内部存储里面的 fix.aptach
        File fixFile = new File(MainActivity.this.getFilesDir(), "fix.apatch");

        if (fixFile.exists()) {
            // 修复Bug
            try {
                // 立马生效不需要重启
                BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Log.e(TAG, "initData: 修复成功");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "initData: 修复失败");
            }
        } else {
            Log.e(TAG, "没有找到apatch补丁文件 " + fixFile);
        }
    }

    @Override
    protected void initView() {
        Util.checkAndRequestReadSDCardPermission(this);
        loadImg();
    }

    private void loadImg() {
        try {
            ImageView img = findViewById(R.id.emptyImg);
            // 读取本地的一个 .skin里面的资源
            Resources superRes = getResources();
            // 创建AssetManager
            AssetManager assetManager = AssetManager.class.newInstance();
            // 寻找hide的方法
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            // method.setAccessible(true); 如果是私有的
            Log.e(TAG, "loadImg from : " + Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "test.skin");
            // 反射执行方法 assetManager指向指定的皮肤包 /storage/emulated/0/test.skin
            method.invoke(assetManager, Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "test.skin");

            Resources resource = new Resources(assetManager, superRes.getDisplayMetrics(),
                    superRes.getConfiguration());

            // 获取指定路径apk的packageName
            String packageName = "";
            if (this.getApplication() != null) {
                Log.e(TAG, "loadImage: getApplication!=null");
                if (this.getApplication().getPackageManager() != null) {
                    String myPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            File.separator + "test.skin";
                    Log.e(TAG, "loadImage: myPath ===" + myPath);
                    packageName = this.getApplication().getPackageManager().getPackageArchiveInfo(Environment.getExternalStorageDirectory().getAbsolutePath() +
                            File.separator + "test.skin", PackageManager.GET_ACTIVITIES).packageName;
                } else {
                    Log.e(TAG, "loadImage: getPackageManager==null");
                }
            }

            // 获取资源 id
            int drawableId = resource.getIdentifier("abc", "drawable", packageName);
            Drawable drawable = resource.getDrawable(drawableId);
            img.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.e(TAG, "loadImg: " + e.getStackTrace().toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void initTitle() {
        new DefaultNavigationBar.Builder(this)
                .setTitle("投稿")
                .setRightIcon(R.mipmap.ic_launcher)
                .setRightText("右边")
                .setRightClickListener(v -> Toast.makeText(MainActivity.this, "右边", Toast.LENGTH_SHORT).show())
                .builder();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        ViewUtils.injectActivity(this);
        mTextView.setText("This is Bug text ");
        mTextView.setOnClickListener(this);
        mButton.setText("Button text！！！！");
        mButton.setOnClickListener(this);
        findViewById(R.id.testOKHttp).setOnClickListener(this::onClick);
        findViewById(R.id.testMyOKHttp).setOnClickListener(this::doMyHttpRequest);
        findViewById(R.id.clearData).setOnClickListener(this::clearData);
        findViewById(R.id.queryAll).setOnClickListener(this::queryAll);
        findViewById(R.id.update).setOnClickListener(this::update);
        findViewById(R.id.deleteByArgs).setOnClickListener(this::deleteByArgs);
        findViewById(R.id.testChangeSkin).setOnClickListener(this::jumpChangeSkin);
    }

    private void jumpChangeSkin(View view) {
        Intent intent = new Intent(MainActivity.this, ActivityChangeSkin.class);
        startActivity(intent);
    }

    private void deleteByArgs(View view) {
        QuerySupport<Person> querySupport = DaoSupportFactory.getFactoryInstance(MainActivity.this).getDao(Person.class).querySupport();
        List<Person> persons = querySupport.query();
        Log.e(TAG, "删除前所有数据: " + persons);
        // 删除所有名字为hjcai的
        DaoSupportFactory.getFactoryInstance(MainActivity.this).getDao(Person.class).delete("name =?", "hjcai");

        Log.e(TAG, "删除后所有数据: " + DaoSupportFactory.getFactoryInstance(MainActivity.this).getDao(Person.class).querySupport().query());
    }

    private void update(View view) {
        new Thread(() -> {
            QuerySupport<Person> querySupport = DaoSupportFactory.getFactoryInstance(MainActivity.this).getDao(Person.class).querySupport();
            List<Person> persons = querySupport.orderBy("age").query();
            Log.e(TAG, "更新前: " + persons);
            if (persons != null && persons.size() > 0) {
                Person person = persons.get(0);
                // 把所有name=hjcai的 更新为age最小的样子
                DaoSupportFactory.getFactoryInstance(MainActivity.this).getDao(Person.class).update(person, "name = ?", "hjcai");
                Log.e(TAG, " 更新后 -> " + querySupport.query());
            }

        }).start();
    }

    private void queryAll(View view) {
        new Thread(() -> {
            QuerySupport<Person> querySupport = DaoSupportFactory.getFactoryInstance(MainActivity.this).getDao(Person.class).querySupport();
//            List<Person> people = querySupport.selection("name = ? AND age = ?").selectionArgs("hjcai","0").query();
//            List<Person> people = querySupport.selection("name = ?").selectionArgs("hjcai").columns("name","age").query();
//            List<Person> people = querySupport.selection("name = ?").selectionArgs("hjcai").query();
//            List<Person> people = querySupport.selection("name = ?").selectionArgs("hjcai").limit("2").query();
//            List<Person> people = querySupport.groupBy("name").query();
            // 查询所有
            List<Person> people = querySupport.query();
            Log.e(TAG, "queryAll: " + people);
        }).start();
    }

    private void clearData(View view) {
        IDaoSupport<Person> daoSupport = DaoSupportFactory.getFactoryInstance(MainActivity.this).getDao(Person.class);
        daoSupport.deleteAll();
    }

    private void doMyHttpRequest(View view) {
        HttpUtils.init(new OkHttpEngine());
        HttpUtils.with(MainActivity.this)
                .exchangeEngine(new OkHttpEngine())
//                .addParam("iid", "6152551759")
//                .addParam("aid", "7")
//                .url("http://is.snssdk.com/2/essay/discovery/v3/")
                .url("https://www.baidu.com/")
//                .post()
                .execute(new DefaultHttpCallBack() {

                    @Override
                    public void onSuccess(Object result) {
                        Log.d(TAG, "onSuccess: " + result);
                    }

                    @Override
                    public void onPreExecute() {
                        Log.d(TAG, "onPreExecute: ");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "onError: " + e.getStackTrace());
                    }
                });
    }

//    @OnClick({R.id.tv, R.id.btn})
//    public void onItemClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv:
//                int x = 2 / 0;
//                Toast.makeText(this, "text view clicked" + x, Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btn:
//                Toast.makeText(this, "button clicked", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv:
                com.example.dialog.AlertDialog myDialog = new com.example.dialog.AlertDialog.Builder(this, android.R.style.Theme_Black)
                        .setContentView(R.layout.detail_comment_dialog)
//                        .setText(R.id.share_label, "评论哈哈")
                        .setTitle("这是标题")
                        .formBottom(true)
                        .fullWidth().show();

                // dialog去操作点击事件
                final EditText commentEt = myDialog.getView(R.id.comment_editor);
                myDialog.setOnclickListener(R.id.submit_btn, v12 -> Toast.makeText(MainActivity.this,
                        commentEt.getText().toString().trim(), Toast.LENGTH_LONG).show());
                myDialog.setText(R.id.share_label, "评论哈哈1111");
                break;
            case R.id.btn:
                Button button = new Button(MainActivity.this);
                button.setText("Button");
                button.setOnClickListener(v1 -> {
                    Toast.makeText(MainActivity.this, "Button点击了", Toast.LENGTH_SHORT).show();
                });
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("标题")
                        .setNegativeButton("取消", (dialog, which) -> {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                        })
                        .setPositiveButton("确定", (dialog, which) -> Toast.makeText(MainActivity.this, "点击了确定", Toast.LENGTH_SHORT).show())
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_launcher_background)
                        .setMessage("这是消息这是消息这是消息这是消息")
                        .setView(button)
                        .create();
                alertDialog.show();
                break;
            case R.id.testOKHttp:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ActivityTestOKHttp.class);
                startActivity(intent);
                break;
        }
    }
}