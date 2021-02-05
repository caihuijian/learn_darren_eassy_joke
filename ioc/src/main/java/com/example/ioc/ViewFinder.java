package com.example.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by hjcai on 2021/2/5.
 *
 * 主要是调用findViewById
 */
class ViewFinder {
    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public ViewFinder(View view) {
        this.mView = view;
    }

    public View findViewById(int viewId){
        return mActivity == null ? mView.findViewById(viewId) : mActivity.findViewById(viewId);
    }
}
