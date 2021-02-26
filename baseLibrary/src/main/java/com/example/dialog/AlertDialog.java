package com.example.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.ioc.R;

/**
 * Created by hjcai on 2021/3/2.
 */
public class AlertDialog extends Dialog implements DialogInterface {
    private final AlertController mAlert;

    //protected方法 让外部无法直接new对象 只能通过Builder创建对象
    protected AlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mAlert = new AlertController(this, getWindow());
    }

    //通过AlertDialog的引用可以setText 虽然和builder中的setText方法最终调用的都是AlertController的方法
    //但是builder 一般在初次创建的时候才会使用 如果后期需要变化文字 可以通过AlertDialog的引用设置
    public void setText(int viewId, CharSequence text) {
        mAlert.setText(viewId,text);
    }

    //可以通过AlertDialog的引用根据viewId获取指定的view
    public <T extends View> T getView(int viewId) {
        return mAlert.getView(viewId);
    }

    //可以通过AlertDialog的引用设置点击事件
    public void setOnclickListener(int viewId, View.OnClickListener listener) {
        mAlert.setOnclickListener(viewId,listener);
    }


    public static class Builder {
        private final AlertController.AlertParams P;

        //各种set方法 都是直接将参数保存到AlertParams中
        //存储标题
        public AlertDialog.Builder setTitle(CharSequence title) {
            P.mTitle = title;
            return this;
        }

        //存储标题方式2
        public AlertDialog.Builder setTitle(@StringRes int titleId) {
            P.mTitle = P.mContext.getText(titleId);
            return this;
        }

        //存储消息体
        public AlertDialog.Builder setMessage(@StringRes int messageId) {
            P.mMessage = P.mContext.getText(messageId);
            return this;
        }

        //存储dialog的布局
        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        //另一种方法存储dialog的布局
        public Builder setContentView(int layoutId) {
            P.mView = null;
            P.mViewLayoutResId = layoutId;
            return this;
        }

        //和通过AlertDialog的引用setText的最终方式一样
        //这里只是存储变量
        public Builder setText(int viewId,CharSequence text){
            P.mTextArray.put(viewId,text);
            return this;
        }

        //存储dialog取消的listener
        public AlertDialog.Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        //存储dialog的宽度
        public Builder fullWidth(){
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        //存储是否从底部弹出 并且是否使用动画
        public Builder formBottom(boolean isAnimation){
            if(isAnimation){
                P.mAnimations = R.style.dialog_from_bottom_anim;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        //存储dialog的宽高
        public Builder setWidthAndHeight(int width, int height){
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        //存储dialog的默认动画
        public Builder addDefaultAnimation(){
            P.mAnimations = R.style.dialog_scale_anim;
            return this;
        }

        //存储dialog的动画
        public Builder setAnimations(int styleAnimation){
            P.mAnimations = styleAnimation;
            return this;
        }

        //存储dialog点击空白区域是否可以取消
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        //存储dialog消失的监听
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        //存储dialog键值监听
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }
        //end各种set方法

        //构造方法
        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        //构造方法 带有主题
        public Builder(Context context, int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }

        //创建dialog对象
        public AlertDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final AlertDialog dialog = new AlertDialog(P.mContext, P.mThemeResId);
            P.apply(dialog.mAlert);//将AlertParams中的参数应用到dialog中 间接应用 (将参数传递给AlertController 接着还有可能通过DialogViewHelper最终影响dialog)
            dialog.setCancelable(P.mCancelable);//将AlertParams中的参数应用到dialog中 直接应用
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);//将AlertParams中的参数应用到dialog中 直接应用
            }
            dialog.setOnCancelListener(P.mOnCancelListener);//将AlertParams中的参数应用到dialog中 直接应用
            dialog.setOnDismissListener(P.mOnDismissListener);//将AlertParams中的参数应用到dialog中 直接应用
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);//将AlertParams中的参数应用到dialog中 直接应用
            }
            dialog.setTitle(P.mTitle);
            return dialog;
        }

        //显示方法
        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
