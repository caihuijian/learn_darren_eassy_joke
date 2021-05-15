package com.example.testrecyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by hjcai on 2021/5/14.
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private int mDividerHeightOrWidth;
    private int mOrientation;

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
                c.drawRect(rect, mPaint);
            }
        }
        // 直接利用Canvas去绘制一个矩形 在留出来的地方

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
