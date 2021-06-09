package main.java.com.example.testfragment;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.testfragment.R;

/**
 * Created by hjcai on 2021/5/27.
 */
public class Fragment1 extends Fragment {
    private static final String TAG = "Fragment1";
    Bundle mBundle;
    LocaleList oldLocaleList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        Configuration config = getResources().getConfiguration();
        oldLocaleList = config.getLocales();
        return inflater.inflate(R.layout.fragment1, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onViewCreated: ");
        TextView tv = getView().findViewById(R.id.textView1);
        if (savedInstanceState != null && !TextUtils.isEmpty(savedInstanceState.getString("name"))) {
            tv.setText(savedInstanceState.getString("name"));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState: called ");
        outState.putString("name", "hjcai");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged: in fragment");
        if (oldLocaleList.equals(newConfig.getLocales())) {
            Log.e(TAG, "onConfigurationChanged: AAAAAAA");
        } else {
            Log.e(TAG, "onConfigurationChanged: BBBBBBBBBBB");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}