package com.yjt.app.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.services.route.DriveRouteResult;
import com.yjt.app.R;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.ToastUtil;


public class RouteDetailActivity extends BaseActivity {

    private DriveRouteResult mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        if (IntentDataUtil.getInstance().hasIntentExtraValue(this, Temp.ROUTE_INFO.getContent())) {
            mResult = (DriveRouteResult) IntentDataUtil.getInstance().getParcelableData(this, Temp.ROUTE_INFO.getContent());
        } else {
            ToastUtil.getInstance().showToast(this, getString(R.string.route_prompt3), Toast.LENGTH_SHORT);
        }
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
}
