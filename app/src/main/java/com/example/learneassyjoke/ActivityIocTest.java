package com.example.learneassyjoke;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ioc.ViewById;
import com.example.ioc.ViewUtils;

import butterknife.BindView;

public class ActivityIocTest extends AppCompatActivity {
    @ViewById(R.id.tv)
    TextView mTextView;
    @ViewById(R.id.btn)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ioc_test);
        ViewUtils.inject(this);
        mTextView.setText("Tv text！！！！");
        mButton.setText("Button text！！！！");
    }
}