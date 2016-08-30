package com.yjt.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.ViewUtil;

public class RouteActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private ImageView ivBack;
    private EditText etSearch;
    private ImageView ivDelete;
    private ImageView ivVoice;
    private TextView tvEnter;

    private TextView tvLocation;
    private TextView tvCollection;

    private int mPointType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        setTheme(android.R.style.Theme_Material_Light_NoActionBar);
        findViewById();
        setListener();
        initialize(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onFinish("ivBack");
                break;
            case R.id.ivDelete:
                etSearch.setText(null);
                break;
            case R.id.ivVoice:

                break;
            case R.id.tvEnter:
                Intent intent = new Intent();
                intent.putExtra(Temp.POINT_TYPE.getContent(), mPointType);
                intent.putExtra(Temp.POINT_CONTENT.getContent(), etSearch.getText().toString());
                setResult(Constant.Common.RESULT_CODE, intent);
                onFinish("tvEnter");
                break;
            case R.id.tvLocation:

                break;
            case R.id.tvCollection:

                break;
            default:
                break;
        }
    }


    @Override
    protected void findViewById() {
        ivBack = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ivBack, this);
        etSearch = ViewUtil.getInstance().findView(this, R.id.etSearch);
        ivDelete = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ivDelete, this);
        ivVoice = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.ivVoice, this);
        tvEnter = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.tvEnter, this);
        tvLocation = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.tvLocation, this);
        tvCollection = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.tvCollection, this);
    }

    @Override
    protected void setListener() {
        etSearch.addTextChangedListener(this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        if (IntentDataUtil.getInstance().hasExtraValue(this, Temp.POINT_TYPE.getContent())) {
            mPointType = IntentDataUtil.getInstance().getIntData(this, Temp.POINT_TYPE.getContent());
            switch (mPointType) {
                case Constant.PointType.START:
                    etSearch.setText(getString(R.string.start_point));
                    break;
                case Constant.PointType.PASS:
                    etSearch.setText(getString(R.string.pass_point));
                    break;
                case Constant.PointType.END:
                    etSearch.setText(getString(R.string.end_point));
                    break;
            }
        }
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(etSearch.getText())) {
            ViewUtil.getInstance().setViewGone(tvEnter);
            ViewUtil.getInstance().setViewVisible(ivVoice);
        } else {
            ViewUtil.getInstance().setViewVisible(ivDelete);
            ViewUtil.getInstance().setViewGone(ivVoice);
            ViewUtil.getInstance().setViewVisible(tvEnter);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
