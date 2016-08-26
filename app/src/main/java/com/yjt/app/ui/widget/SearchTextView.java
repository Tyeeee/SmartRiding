package com.yjt.app.ui.widget;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ViewUtil;

public class SearchTextView extends RelativeLayout implements View.OnClickListener {

    private ImageView ivLogin;
    private TextView tvSearch;
    private ImageView ivVoice;

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
        ivLogin = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ivLogin, this);
        tvSearch = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.tvSearch, this);
        ivVoice = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ivVoice, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLogin:
                SnackBarUtil.getInstance().showSnackBar(this, "ivLogin", Snackbar.LENGTH_SHORT);
                break;
            case R.id.tvSearch:
                SnackBarUtil.getInstance().showSnackBar(this, "tvSearch", Snackbar.LENGTH_SHORT);
                break;
            case R.id.ivVoice:
                SnackBarUtil.getInstance().showSnackBar(this, "ivVoice", Snackbar.LENGTH_SHORT);
                break;
            default:
                break;
        }
    }

    public void setText(String text) {
        tvSearch.setText(text);
    }
}
