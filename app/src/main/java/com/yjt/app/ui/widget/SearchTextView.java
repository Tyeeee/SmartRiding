package com.yjt.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.utils.ViewUtil;

public class SearchTextView extends RelativeLayout {

    private TextView tvPoint;
    private View vLine;

    public SearchTextView(Context context) {
        super(context);
    }

    public SearchTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        findViewById();
    }

    public SearchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        findViewById();
    }

    public void findViewById() {
        LayoutInflater.from(getContext()).inflate(R.layout.textview_search, this);
        tvPoint = ViewUtil.getInstance().findView(this, R.id.tvPoint);
        vLine = ViewUtil.getInstance().findView(this, R.id.vLine);
    }

    public void setHint(int resId) {
        tvPoint.setHint(getResources().getString(resId));
    }

    public void setHint(String text) {
        tvPoint.setHint(text);
    }

    public void setText(int resId) {
        tvPoint.setText(getResources().getString(resId));
    }

    public void setText(String text) {
        tvPoint.setText(text);
    }

    public String getText() {
        return tvPoint.getText().toString();
    }

    public void setTextColor(int resId) {
        tvPoint.setTextColor(resId);
    }

    public void setLineVisible(int visible) {
        vLine.setVisibility(visible);
    }
}
