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

    private TextView tvSearch;
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
        tvSearch = ViewUtil.getInstance().findView(this, R.id.tvSearch);
        vLine = ViewUtil.getInstance().findView(this, R.id.vLine);
    }

    public void setText(int resId) {
        tvSearch.setText(getResources().getString(resId));
    }

    public void setText(String text) {
        tvSearch.setText(text);
    }

    public String getText() {
        return tvSearch.getText().toString();
    }

    public void setTextColor(int resId) {
        tvSearch.setTextColor(resId);
    }

    public void setLineVisible(int visible) {
        vLine.setVisibility(visible);
    }
}
