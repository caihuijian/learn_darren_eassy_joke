package com.example.framelibrary;


import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.*;
import androidx.core.view.LayoutInflaterCompat;

import com.example.base.BaseActivity;

/**
 * Created by hjcai on 2021/2/18.
 */
public abstract class BaseSkinActivity extends BaseActivity {
    private static final String TAG = "BaseSkinActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(layoutInflater, new LayoutInflater.Factory2() {
            @Nullable
            @Override
            public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                //在这里拦截view创建
                //可以在这里进行换肤
                Log.e(TAG, "onCreateView: 拦截了view " + name);
                View view = null;
                switch (name) {
                    case "TextView":
                        view = new AppCompatTextView(context, attrs);
                        break;
                    case "ImageView":
                        view = new AppCompatImageView(context, attrs);
                        break;
                    case "Button":
                        view = new AppCompatButton(context, attrs);
                        //((Button) view).setText("拦截");
                        break;
                    case "EditText":
                        view = new AppCompatEditText(context, attrs);
                        break;
                    case "Spinner":
                        view = new AppCompatSpinner(context, attrs);
                        break;
                    case "ImageButton":
                        view = new AppCompatImageButton(context, attrs);
                        break;
                    case "CheckBox":
                        view = new AppCompatCheckBox(context, attrs);
                        break;
                    case "RadioButton":
                        view = new AppCompatRadioButton(context, attrs);
                        break;
                    case "CheckedTextView":
                        view = new AppCompatCheckedTextView(context, attrs);
                        break;
                    case "AutoCompleteTextView":
                        view = new AppCompatAutoCompleteTextView(context, attrs);
                        break;
                    case "MultiAutoCompleteTextView":
                        view = new AppCompatMultiAutoCompleteTextView(context, attrs);
                        break;
                    case "RatingBar":
                        view = new AppCompatRatingBar(context, attrs);
                        break;
                    case "SeekBar":
                        view = new AppCompatSeekBar(context, attrs);
                        break;
                }
                return view;
            }

            @Nullable
            @Override
            public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {//Factory的方法 可以忽略
                Log.e(TAG, "onCreateView: ");
                return null;
            }
        });
        super.onCreate(savedInstanceState);
    }
}
