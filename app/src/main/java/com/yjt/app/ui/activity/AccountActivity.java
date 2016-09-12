package com.yjt.app.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Regex;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.ui.dialog.DatePickerDialog;
import com.yjt.app.ui.listener.OnDateDialogListener;
import com.yjt.app.ui.widget.CircleImageView;
import com.yjt.app.utils.DateUtil;
import com.yjt.app.utils.ViewUtil;

import java.util.Date;

public class AccountActivity extends BaseActivity implements View.OnClickListener, OnDateDialogListener {

    private RelativeLayout rlHeadPortrait;
    private CircleImageView civHeadPortrait;

    private RelativeLayout rlNickname;
    private TextView tvNickname;

    private RelativeLayout rlPhoneNumber;
    private TextView tvPhoneNumber;

    private RelativeLayout rlGender;
    private TextView tvGender;

    private RelativeLayout rlHeight;
    private TextView tvHeight;

    private RelativeLayout rlWeight;
    private TextView tvWeight;

    private RelativeLayout rlBirthday;
    private TextView tvBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        findViewById();
        setViewListener();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        ViewUtil.getInstance().setToolBar(this, R.id.tbTitle, true);
        rlHeadPortrait = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlHeadPortrait, this);
        civHeadPortrait = ViewUtil.getInstance().findView(this, R.id.civHeadPortrait);
        rlNickname = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlNickname, this);
        tvNickname = ViewUtil.getInstance().findView(this, R.id.tvNickname);
        rlPhoneNumber = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlPhoneNumber, this);
        tvPhoneNumber = ViewUtil.getInstance().findView(this, R.id.tvPhoneNumber);
        rlGender = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlGender, this);
        tvGender = ViewUtil.getInstance().findView(this, R.id.tvGender);
        rlHeight = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlHeight, this);
        tvHeight = ViewUtil.getInstance().findView(this, R.id.tvHeight);
        rlWeight = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlWeight, this);
        tvWeight = ViewUtil.getInstance().findView(this, R.id.tvWeight);
        rlBirthday = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlBirthday, this);
        tvBirthday = ViewUtil.getInstance().findView(this, R.id.tvBirthday);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialize(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void permissionRequestIntent() {

    }

    @Override
    protected void permissionRequestResult() {

    }

    @Override
    protected void endOperation() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlHeadPortrait:
                break;
            case R.id.rlNickname:
                break;
            case R.id.rlPhoneNumber:
                break;
            case R.id.rlGender:
                break;
            case R.id.rlHeight:
                break;
            case R.id.rlWeight:
                break;
            case R.id.rlBirthday:
                DatePickerDialog.createBuilder(getSupportFragmentManager())
                        .setDate(new Date())
                        .setPositiveButtonText(R.string.enter)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_DATE)
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPositiveButtonClicked(int requestCode, Date date) {
        tvBirthday.setText(DateUtil.getCurrentTime(date, Regex.DATE_FORMAT1.getRegext()));
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }
}
