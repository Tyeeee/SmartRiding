package com.yjt.app.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Regex;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.utils.InputUtil;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MessageUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ViewUtil;

import java.lang.ref.WeakReference;

public class RouteActivity extends BaseActivity implements View.OnClickListener, TextWatcher, AMapLocationListener {

    private ImageView ivBack;
    private EditText etSearch;
    private ImageView ivDelete;
    private ImageView ivVoice;
    private TextView tvEnter;

    private TextView tvLocation;
    private TextView tvCollection;

    private AMapLocationClient mClient;
    private AMapLocationClientOption mOption;

    private int mPointType;

    private RouteHandler mHandler;
    private ProgressDialog mDialog;

    protected static class RouteHandler extends Handler {

        private WeakReference<RouteActivity> mActivitys;

        public RouteHandler(RouteActivity activity) {
            mActivitys = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RouteActivity activity = mActivitys.get();
            if (activity != null) {
                switch (msg.what) {
                    case Constant.Message.LOCATION_SUCCESS:
                        ViewUtil.getInstance().hideDialog(activity.mDialog, activity);
                        AMapLocation location = (AMapLocation) msg.obj;
                        LogUtil.print("--->定位类型: " + location.getLocationType());
                        LogUtil.print("--->经度: " + location.getLongitude());
                        LogUtil.print("--->纬度: " + location.getLatitude());
                        LogUtil.print("--->精度: " + location.getAccuracy());
                        LogUtil.print("--->提供者: " + location.getProvider());
                        LogUtil.print("--->国家: " + location.getCountry());
                        LogUtil.print("--->省: " + location.getProvince());
                        LogUtil.print("--->市: " + location.getCity());
                        LogUtil.print("--->城市编码:" + location.getCityCode());
                        LogUtil.print("--->区:" + location.getDistrict());
                        LogUtil.print("--->区域码:" + location.getAdCode());
                        LogUtil.print("--->地址:" + location.getAddress());
                        activity.etSearch.setText(activity.getString(R.string.my_location));
                        SnackBarUtil.getInstance().showSnackBar(activity.tvLocation, activity.getString(R.string.location_success), Snackbar.LENGTH_SHORT);
                        activity.mClient.stopLocation();
                        break;
                    case Constant.Message.LOCATION_FAILED:
                        SnackBarUtil.getInstance().showSnackBar(activity.tvLocation, activity.getString(R.string.location_failed), Snackbar.LENGTH_SHORT);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        setTheme(android.R.style.Theme_Material_Light_NoActionBar);
        findViewById();
        initialize(savedInstanceState);
        setListener();
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
    protected void initialize(Bundle savedInstanceState) {
        mHandler = new RouteHandler(this);

        if (IntentDataUtil.getInstance().hasExtraValue(this, Temp.POINT_TYPE.getContent())) {
            mPointType = IntentDataUtil.getInstance().getIntData(this, Temp.POINT_TYPE.getContent());
            switch (mPointType) {
                case Constant.PointType.START:
                    etSearch.setText(Regex.NONE.getRegext());
                    break;
                case Constant.PointType.PASS:
                    etSearch.setText(Regex.NONE.getRegext());
                    break;
                case Constant.PointType.END:
                    etSearch.setText(Regex.NONE.getRegext());
                    break;
            }
        }

        mClient = new AMapLocationClient(this);
        mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mOption.setOnceLocation(false);
        mOption.setNeedAddress(true);
        mOption.setLocationCacheEnable(false);
        mOption.setGpsFirst(true);
        mOption.setHttpTimeOut(Constant.Map.LOCATION_TIME_OUT);
        mOption.setInterval(Constant.Map.LOCATION_MINIMUM_TIME_INTERVAL);
        mClient.setLocationOption(mOption);
    }

    @Override
    protected void setListener() {
        etSearch.addTextChangedListener(this);
        mClient.setLocationListener(this);
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
    public void onDestroy() {
        super.onDestroy();
        mClient.onDestroy();
    }

    @Override
    public void onClick(View view) {
        InputUtil.getInstance().hideKeyBoard(this, view);
        switch (view.getId()) {
            case R.id.ivBack:
                onFinish("ivBack");
                break;
            case R.id.ivDelete:
                etSearch.setText(null);
                break;
            case R.id.ivVoice:

                break;
            case R.id.tvEnter:
                if (!TextUtils.isEmpty(etSearch.getText())
                        && !TextUtils.equals(etSearch.getText(), getString(R.string.start_point))
                        && !TextUtils.equals(etSearch.getText(), getString(R.string.end_point))) {
                    Intent intent = new Intent();
                    intent.putExtra(Temp.POINT_TYPE.getContent(), mPointType);
                    intent.putExtra(Temp.POINT_CONTENT.getContent(), etSearch.getText().toString());
                    setResult(Constant.Common.RESULT_CODE, intent);
                    onFinish("tvEnter");
                } else {
                    switch (mPointType) {
                        case Constant.PointType.START:
                            SnackBarUtil.getInstance().showSnackBar(view, getString(R.string.start_point), Snackbar.LENGTH_SHORT);
                            break;
                        case Constant.PointType.PASS:
                            SnackBarUtil.getInstance().showSnackBar(view, getString(R.string.pass_point), Snackbar.LENGTH_SHORT);
                            break;
                        case Constant.PointType.END:
                            SnackBarUtil.getInstance().showSnackBar(view, getString(R.string.end_point), Snackbar.LENGTH_SHORT);
                            break;
                    }
                }
                break;
            case R.id.tvLocation:
                mClient.startLocation();
                mDialog = ViewUtil.getInstance().showProgressDialog(this, null, getString(R.string.location_prompt), null);
                break;
            case R.id.tvCollection:
                //TODO 服务器返回收藏列表
                break;
            default:
                break;
        }
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

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            mHandler.sendMessage(MessageUtil.getMessage(Constant.Message.LOCATION_SUCCESS, location));
        } else {
            mHandler.sendMessage(MessageUtil.getMessage(Constant.Message.LOCATION_FAILED, location));
        }
    }
}
