package com.yjt.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.HashSet;
import java.util.Set;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    private static final int[] STATE_CHECKED = {android.R.attr.state_checked};
    private Set<Checkable> mCheckableItems = new HashSet<>();
    private boolean isChecked;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); ++i) {
            View view = getChildAt(i);
            if (view instanceof Checkable) {
                mCheckableItems.add((Checkable) view);
            }
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked == this.isChecked) {
            return;
        }
        this.isChecked = checked;
        for (Checkable checkable : mCheckableItems) {
            checkable.setChecked(checked);
        }
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, STATE_CHECKED);
        }
        return drawableState;
    }
}
