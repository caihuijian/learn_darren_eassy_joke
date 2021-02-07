package com.example.ioc;

import android.app.Activity;
import android.view.View;

/**
 * Created by hjcai on 2021/2/5.
 * <p>
 * 主要是调用findViewById
 */
class ViewFinder {
    private Activity mActivity;

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public View findViewById(int viewId) {
        return mActivity.findViewById(viewId);
    }
}
