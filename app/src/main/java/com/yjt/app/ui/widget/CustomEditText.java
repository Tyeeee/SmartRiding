package com.yjt.app.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yjt.app.R;
import com.yjt.app.utils.ViewUtil;

public class CustomEditText extends RelativeLayout implements View.OnClickListener, TextWatcher {

    private EditText etContent;
    private ImageView ivDelete;
    private ImageView ivVoice;

    public CustomEditText(Context context) {
        super(context);
        findViewById();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        findViewById();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        findViewById();
    }

    public void findViewById() {
        LayoutInflater.from(getContext()).inflate(R.layout.edittext_custom, this);
        etContent = ViewUtil.getInstance().findView(this, R.id.etContent);
        ivDelete = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ivDelete, this);
        ivVoice = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ivVoice, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDelete:
                etContent.setText(null);
                break;
            default:
                break;
        }
    }

    public void setText(int resId) {
        etContent.setText(getResources().getString(resId));
    }

    public void setText(String text) {
        etContent.setText(text);
    }

    public String getText() {
        return etContent.getText().toString();
    }

    public void setTextColor(int resId) {
        etContent.setTextColor(resId);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(etContent.getText())) {
            ViewUtil.getInstance().setViewGone(ivDelete);
            ViewUtil.getInstance().setViewVisible(ivVoice);
        } else {
            ViewUtil.getInstance().setViewVisible(ivDelete);
            ViewUtil.getInstance().setViewGone(ivVoice);
        }
    }
}
