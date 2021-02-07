package com.example.learneassyjoke;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ioc.OnClick;
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
        ViewUtils.injectActivity(this);
        mTextView.setText("Tv text！！！！");
        mButton.setText("Button text！！！！");
    }

    @OnClick({R.id.tv,R.id.btn})
    public void onItemClick(View view){
        switch (view.getId()){
            case R.id.tv:
                Toast.makeText(this,"text view clicked",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn:
                Toast.makeText(this,"button clicked",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}