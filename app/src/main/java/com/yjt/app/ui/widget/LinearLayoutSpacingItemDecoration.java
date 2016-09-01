package com.yjt.app.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 列表间距
 *
 * @author yjt
 */
public class LinearLayoutSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;
    private int mMaxSize;
    private int mOrientation;

    public LinearLayoutSpacingItemDecoration(int space, int maxSize, int orientation) {
        this.mSpace = space;
        this.mMaxSize = maxSize;
        this.mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        switch (mOrientation) {
            case LinearLayoutManager.HORIZONTAL:
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.left = mSpace;
                }
                if (parent.getChildAdapterPosition(view) != mMaxSize) {
                    outRect.right = mSpace;
                }
//                outRect.bottom = mSpace;
//                outRect.top = mSpace;
                break;
            case LinearLayoutManager.VERTICAL:
//                outRect.left = mSpace;
//                outRect.right = mSpace;
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = mSpace;
                }
                if (parent.getChildAdapterPosition(view) != mMaxSize) {
                    outRect.bottom = mSpace;
                }
                break;
            default:
//                outRect.left = mSpace;
//                outRect.right = mSpace;
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = mSpace;
                }
                if (parent.getChildAdapterPosition(view) != mMaxSize) {
                    outRect.bottom = mSpace;
                }
                break;
        }
    }
}
