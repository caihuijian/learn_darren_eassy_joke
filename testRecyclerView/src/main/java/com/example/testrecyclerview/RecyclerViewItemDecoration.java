package com.example.testrecyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by hjcai on 2021/5/14.
 * 注意：该分割线不算在recycler view item总高度中
 * 如 假设Recycler View为横向
 * 我们设置每个item宽度100dp 如果ItemDecoration的宽度2dp
 * 那么Recycler View item总宽度其实为102dp
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint mPaint;
    private final int mDividerHeightOrWidth;
    private final int mOrientation;
    private Drawable mDividerDrawable;

    public RecyclerViewItemDecoration(Drawable dividerDrawable, int orientation) {
        mOrientation = orientation;
        mDividerDrawable = dividerDrawable;
        mDividerHeightOrWidth = mOrientation == RecyclerView.VERTICAL ? dividerDrawable.getIntrinsicHeight() : dividerDrawable.getIntrinsicWidth();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public RecyclerViewItemDecoration(int color, int dividerHeightOrWidth, int orientation) {
        mOrientation = orientation;
        mDividerHeightOrWidth = dividerHeightOrWidth;
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mDividerDrawable != null) {
            drawDividerByDrawable(c, parent);
        } else {
            drawDividerByColor(c, parent);
        }
    }

    private void drawDividerByColor(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        // 获取需要绘制的区域
        Rect rect = new Rect();
        if (mOrientation == RecyclerView.VERTICAL) {
            // left right取自recycler view
            rect.left = parent.getPaddingLeft();
            rect.right = parent.getMeasuredWidth() - parent.getPaddingRight();
            for (int i = 0; i < childCount; i++) {
                View childView = parent.getChildAt(i);
                rect.top = childView.getBottom();
                rect.bottom = rect.top + mDividerHeightOrWidth;
                // 直接利用Canvas去绘制一个矩形 在留出来的地方
                c.drawRect(rect, mPaint);
            }
        } else {
            // top bottom取自recycler view
            rect.top = parent.getPaddingTop();
            rect.bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
            for (int i = 0; i < childCount; i++) {
                View childView = parent.getChildAt(i);
                rect.left = childView.getRight();
                rect.right = rect.left + mDividerHeightOrWidth;
                // 直接利用Canvas去绘制一个矩形 在留出来的地方
                c.drawRect(rect, mPaint);
            }
        }
    }

    private void drawDividerByDrawable(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        // 获取需要绘制的区域
        Rect rect = new Rect();
        if (mOrientation == RecyclerView.VERTICAL) {
            // left right取自recycler view
            rect.left = parent.getPaddingLeft();
            rect.right = parent.getMeasuredWidth() - parent.getPaddingRight();
            for (int i = 0; i < childCount; i++) {
                View childView = parent.getChildAt(i);
                rect.top = childView.getBottom();
                rect.bottom = rect.top + mDividerHeightOrWidth;
                // 直接利用Canvas去绘制一个矩形 在留出来的地方
                mDividerDrawable.setBounds(rect);
                mDividerDrawable.draw(c);
            }
        } else {
            // top bottom取自recycler view
            rect.top = parent.getPaddingTop();
            rect.bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
            for (int i = 0; i < childCount; i++) {
                View childView = parent.getChildAt(i);
                rect.left = childView.getRight();
                rect.right = rect.left + mDividerHeightOrWidth;
                // 直接利用Canvas去绘制一个矩形 在留出来的地方
                mDividerDrawable.setBounds(rect);
                mDividerDrawable.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == RecyclerView.VERTICAL) {
            outRect.bottom += mDividerHeightOrWidth;
        } else {
            outRect.right += mDividerHeightOrWidth;
        }
    }
}
