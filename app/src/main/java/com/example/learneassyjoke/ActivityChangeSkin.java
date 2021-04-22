package com.example.learneassyjoke;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.framelibrary.BaseSkinActivity;
import com.example.framelibrary.navigationbar.DefaultNavigationBar;
import com.example.framelibrary.skin.SkinManager;
import com.example.framelibrary.skin.SkinResource;
import com.example.framelibrary.skin.support.SkinConfig;
import com.example.framelibrary.skin.support.SkinUtil;

public class ActivityChangeSkin extends BaseSkinActivity {

    private static final String TAG = "ActivityChangeSkin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_skin);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        new DefaultNavigationBar.Builder(this)
                .setTitle("投稿")
                .setRightIcon(R.mipmap.ic_launcher)
                .setRightText("右边")
                .setRightClickListener(v -> Toast.makeText(ActivityChangeSkin.this, "右边", Toast.LENGTH_SHORT).show())
                .builder();
    }

    @Override
    protected void setContentView() {

    }

    public void changeSkin(View view) {
        int changeSkinRes = SkinManager.getInstance().loadSkin(SkinUtil.getInstance(this).getLightSkinPath());
        if (changeSkinRes == SkinConfig.SKIN_CHANGE_SUCCESS) {
            Log.i(TAG, "changeSkin: success");
        }
    }

    public void restoreSkin(View view) {
        int changeSkinRes = SkinManager.getInstance().restoreDefault();
        if (changeSkinRes == SkinConfig.SKIN_CHANGE_SUCCESS) {
            Log.i(TAG, "restoreSkin: success");
        }
    }

    public void jumpActivity(View view) {
        Intent intent = new Intent(ActivityChangeSkin.this, ActivityChangeSkin.class);
        startActivity(intent);
    }
}