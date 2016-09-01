package com.yjt.app.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yjt.app.R;
import com.yjt.app.utils.ViewUtil;

public class SearchEditText extends RelativeLayout implements View.OnClickListener {

    private EditText etSearch;
    private ImageView ivDelete;

    public SearchEditText(Context context) {
        super(context);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        findViewById();
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        findViewById();
    }

    public void findViewById() {
        LayoutInflater.from(getContext()).inflate(R.layout.textview_search, this);
        etSearch = ViewUtil.getInstance().findView(this, R.id.etSearch);
        ivDelete = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ivDelete, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDelete:
                etSearch.setText(null);
                break;
            default:
                break;
        }
    }

    public void setText(int resId) {
        etSearch.setText(getResources().getString(resId));
    }

    public void setText(String text) {
        etSearch.setText(text);
    }

    public void setTextColor(int resId) {
        etSearch.setTextColor(resId);
    }
}
