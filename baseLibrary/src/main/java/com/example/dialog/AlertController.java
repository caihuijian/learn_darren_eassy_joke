package com.example.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by hjcai on 2021/3/2.
 */
public class AlertController {

    //AlertController控制的Dialog
    private final AlertDialog mDialog;
    //Dialog显示的window
    private final Window mWindow;
    //辅助类 通过AlertController控制Dialog的布局显示内容 view的事件监听等
    private DialogViewHelper mViewHelper;

    //构造方法在创建dialog对象时调用
    public AlertController(AlertDialog alertDialog, Window window) {
        this.mDialog = alertDialog;
        this.mWindow = window;
    }

    //设置辅助类 在apply AlertController中的属性时调用
    public void setViewHelper(DialogViewHelper viewHelper) {
        this.mViewHelper = viewHelper;
    }

    //外部通过调用controller的该方法setText
    public void setText(int viewId, CharSequence text) {
        //调用DialogViewHelper操作具体的view setText
        mViewHelper.setText(viewId, text);
    }

    //外部通过调用controller的该方法取得view对象
    public <T extends View> T getView(int viewId) {
        //内部调用DialogViewHelper操作具体的view 得到view对象
        return mViewHelper.getView(viewId);
    }

    //外部通过调用controller的该方法set点击事件
    public void setOnclickListener(int viewId, View.OnClickListener listener) {
        //内部调用DialogViewHelper操作具体的view 给view设置点击事件
        mViewHelper.setOnclickListener(viewId, listener);
    }

    //给静态内部类使用的方法
    private AlertDialog getDialog() {
        return mDialog;
    }

    //给静态内部类使用的方法
    private Window getWindow() {
        return mWindow;
    }

    //静态内部类 可以脱离AlertController存在 用于存储dialog的各种变量
    static class AlertParams {
        public CharSequence mTitle;//存储title
        public int mThemeResId;//存储theme
        public boolean mCancelable = true;//默认可以点击空白收起dialog
        public Context mContext;
        public CharSequence mMessage;//存储Message

        //存储各种listener
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public DialogInterface.OnCancelListener mOnCancelListener;

        //布局和布局id二选一
        public View mView;
        public int mViewLayoutResId;

        //存放int和Object的键值对 比hash map更高效
        //存放各个部分的文字 是id和CharSequence的键值对
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();

        //存储各个控件的点击事件 是viewId和OnClickListener的键值对
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();

        //存储宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        //存储动画的资源id
        public int mAnimations = 0;
        //存储显示位置
        public int mGravity = Gravity.CENTER;
        //存储高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        //将属性直接应用到Dialog或者 通过viewHelper操作Dialog的view
        public void apply(AlertController mAlert) {
            DialogViewHelper viewHelper = null;
            //设置dialog的资源id
            if (mViewLayoutResId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }

            //设置dialog的资源id的另一种方式
            if (mView != null) {
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }

            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局view或布局id");
            }

            //真正设置dialog的地方
            mAlert.getDialog().setContentView(viewHelper.getContentView());
            //感觉没什么用 除非外部想要直接操作AlertController类 此前还要提供getViewHelper方法
            mAlert.setViewHelper(viewHelper);

            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                //调用DialogViewHelper操作具体的view setText
                mAlert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                //调用DialogViewHelper操作具体的view setOnclickListener
                mAlert.setOnclickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }

            Window window = mAlert.getWindow();
            // 设置位置
            window.setGravity(mGravity);

            //设置动画
            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }

            //设置宽高
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }
}
