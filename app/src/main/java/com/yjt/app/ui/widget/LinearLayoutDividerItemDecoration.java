package com.yjt.app.ui.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 列表分割线
 *
 * @author yjt
 */
public class LinearLayoutDividerItemDecoration extends RecyclerView.ItemDecoration {

    private int mOrientation;
    private int mSize;
    private Paint mPaint;

    public LinearLayoutDividerItemDecoration(int color, int size, int orientation) {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请传入正确的参数");
        }
        this.mSize = size;
        this.mOrientation = orientation;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        switch (mOrientation) {
            case LinearLayoutManager.HORIZONTAL:
                drawHorizontal(c, parent);
                break;
            case LinearLayoutManager.VERTICAL:
                drawVertical(c, parent);
                break;
            default:
                drawVertical(c, parent);
                break;
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mSize;
            canvas.drawRect(parent.getPaddingLeft(), top, parent.getMeasuredWidth() - parent.getPaddingRight(), bottom, mPaint);
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mSize;
            canvas.drawRect(left, parent.getPaddingTop(), right, parent.getMeasuredHeight() - parent.getPaddingBottom(), mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        switch (mOrientation) {
            case LinearLayoutManager.HORIZONTAL:
                outRect.set(0, 0, 0, mSize);
                break;
            case LinearLayoutManager.VERTICAL:
                outRect.set(0, 0, mSize, 0);
                break;
            default:
                outRect.set(0, 0, mSize, 0);
                break;
        }
    }
}
