package com.example.learneassyjoke;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityButterKnifeTest extends AppCompatActivity {

    //3 ButterKnife属性初始化 注意View必须有id
    @BindView(R.id.tv)
    TextView mTextView;
    @BindView(R.id.btn)
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //2 初始化ButterKnife
        ButterKnife.bind(this);
        //4 ButterKnife属性使用
        mTextView.setText("ABC");
        mButton.setText("My Button");
    }

    //5 ButterKnife Event使用
    @OnClick({R.id.tv,R.id.btn})
    void onItemClick(View view){
        switch (view.getId()){
            case R.id.tv:
                Toast.makeText(this,"text clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn:
                Toast.makeText(this,"button clicked",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}