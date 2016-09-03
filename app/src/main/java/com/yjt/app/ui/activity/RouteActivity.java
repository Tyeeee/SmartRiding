package com.yjt.app.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.DrivePath;
import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.model.RecommendPosition;
import com.yjt.app.ui.adapter.RecommendPositionAdapter;
import com.yjt.app.ui.adapter.binder.RecommendPositionBinder;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.ui.sticky.FixedStickyViewAdapter;
import com.yjt.app.ui.widget.LinearLayoutDividerItemDecoration;
import com.yjt.app.utils.InputUtil;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MapUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RouteActivity extends BaseActivity implements View.OnClickListener, TextWatcher, AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener, Inputtips.InputtipsListener, FixedStickyViewAdapter.OnItemClickListener {

    private ImageView ivBack;
    private EditText  etSearch;
    private ImageView ivDelete;
    private ImageView ivVoice;
    private TextView  tvEnter;

    private TextView tvLocation;
    private TextView tvCollection;

    private RecyclerView rvRecommendPosition;

    private AMapLocationClient       mClient;
    private AMapLocationClientOption mOption;

    private int           mPointType;
    private AMapLocation  mLocation;
    private GeocodeSearch mSearch;
    private String        mCityCode;

    private ProgressDialog mDialog;

    private LinearLayoutManager    mLayoutManager;
    private FixedStickyViewAdapter mAdapter;
    private DrivePath              mPath;
    private RouteHandler           mHandler;

    private Inputtips mTips;

    private static class RouteHandler extends Handler {

        private WeakReference<RouteActivity> mActivitys;

        public RouteHandler(RouteActivity Activity) {
            mActivitys = new WeakReference<>(Activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RouteActivity Activity = mActivitys.get();
            if (Activity != null) {
                switch (msg.what) {
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
        setViewListener();
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
        rvRecommendPosition = ViewUtil.getInstance().findView(this, R.id.rvRecommendPosition);
    }

    @Override
    protected void setViewListener() {
        etSearch.addTextChangedListener(this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        if (IntentDataUtil.getInstance().hasIntentExtraValue(this, Temp.POINT_TYPE.getContent())) {
            mPointType = IntentDataUtil.getInstance().getIntData(this, Temp.POINT_TYPE.getContent());
        }
        if (IntentDataUtil.getInstance().hasIntentExtraValue(this, Temp.POINT_CONTENT.getContent())) {
            String content = IntentDataUtil.getInstance().getStringData(this, Temp.POINT_CONTENT.getContent());
            if (TextUtils.isEmpty(content)) {
                etSearch.setText(getString(R.string.my_location));
            } else {
                etSearch.setText(content);
            }
        }

        mClient = new AMapLocationClient(this);
        mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mOption.setOnceLocation(false);
        mOption.setNeedAddress(true);
        mOption.setLocationCacheEnable(false);
        mOption.setHttpTimeOut(Constant.Map.LOCATION_TIME_OUT);
        mOption.setInterval(Constant.Map.LOCATION_MINIMUM_TIME_INTERVAL);
        mClient.setLocationOption(mOption);
        mClient.startLocation();
        mDialog = ViewUtil.getInstance().showProgressDialog(this, null, getString(R.string.location_prompt), null, false);

        mSearch = new GeocodeSearch(this);

        mTips = new Inputtips(this, this);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecommendPosition.setHasFixedSize(true);
        rvRecommendPosition.setLayoutManager(mLayoutManager);
        rvRecommendPosition.addItemDecoration(new LinearLayoutDividerItemDecoration(getResources().getColor(android.R.color.black), 1, LinearLayoutManager.VERTICAL));
        mAdapter = new RecommendPositionAdapter(this, new RecommendPositionBinder(this, rvRecommendPosition), false);
    }

    @Override
    protected void setListener() {
        mClient.setLocationListener(this);
        mSearch.setOnGeocodeSearchListener(this);
        mAdapter.setOnItemClickListener(this);
        mTips.setInputtipsListener(this);
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
        mClient = null;
        mOption = null;
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
                        && TextUtils.equals(etSearch.getText(), getString(R.string.my_location))) {
                    if (mLocation != null) {
                        Intent intent = new Intent();
                        intent.putExtra(Temp.POINT_TYPE.getContent(), mPointType);
                        intent.putExtra(Temp.POINT_CONTENT.getContent(), etSearch.getText().toString());
                        intent.putExtra(Temp.LOCATION_LATITUDE.getContent(), mLocation.getLatitude());
                        intent.putExtra(Temp.LOCATION_LONGITUDE.getContent(), mLocation.getLongitude());
                        setResult(Constant.Common.RESULT_CODE, intent);
                        onFinish("LOCATION_SUCCESS");
                    } else {
                        SnackBarUtil.getInstance().showSnackBar(view, getString(R.string.search_failed), Snackbar.LENGTH_SHORT, Color.WHITE);
                    }
                } else if (TextUtils.isEmpty(mCityCode)) {
                    SnackBarUtil.getInstance().showSnackBar(view, getString(R.string.location_prompt2), Snackbar.LENGTH_SHORT, Color.WHITE);
                } else if (!TextUtils.isEmpty(etSearch.getText())
                        && !TextUtils.equals(etSearch.getText(), getString(R.string.start_point))
                        && !TextUtils.equals(etSearch.getText(), getString(R.string.end_point))) {
                    mDialog = ViewUtil.getInstance().showProgressDialog(this, null, getString(R.string.location_prompt), null, false);
                    mSearch.getFromLocationNameAsyn(new GeocodeQuery(etSearch.getText().toString(), mCityCode));
                } else {
                    switch (mPointType) {
                        case Constant.PointType.START:
                            SnackBarUtil.getInstance().showSnackBar(view, getString(R.string.start_point), Snackbar.LENGTH_SHORT, Color.WHITE);
                            break;
                        case Constant.PointType.PASS:
                            SnackBarUtil.getInstance().showSnackBar(view, getString(R.string.pass_point), Snackbar.LENGTH_SHORT, Color.WHITE);
                            break;
                        case Constant.PointType.END:
                            SnackBarUtil.getInstance().showSnackBar(view, getString(R.string.end_point), Snackbar.LENGTH_SHORT, Color.WHITE);
                            break;
                    }
                }
                break;
            case R.id.tvLocation:
                if (mLocation != null) {
                    Intent intent = new Intent();
                    intent.putExtra(Temp.POINT_TYPE.getContent(), mPointType);
                    intent.putExtra(Temp.POINT_CONTENT.getContent(), getString(R.string.my_location));
                    intent.putExtra(Temp.LOCATION_LATITUDE.getContent(), mLocation.getLatitude());
                    intent.putExtra(Temp.LOCATION_LONGITUDE.getContent(), mLocation.getLongitude());
                    setResult(Constant.Common.RESULT_CODE, intent);
                    onFinish("LOCATION_SUCCESS");
                } else {
                    SnackBarUtil.getInstance().showSnackBar(view, getString(R.string.location_prompt1), Snackbar.LENGTH_SHORT, Color.WHITE);
                }
                break;
            case R.id.tvCollection:
                SnackBarUtil.getInstance().showSnackBar(view, "tvCollection", Snackbar.LENGTH_SHORT);
                //TODO 服务器返回收藏列表
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(int pos) {
        RecommendPosition position = (RecommendPosition) mAdapter.getItem(pos);
        mDialog = ViewUtil.getInstance().showProgressDialog(this, null, getString(R.string.location_prompt), null, false);
        etSearch.setText(position.getAddress());
        mSearch.getFromLocationNameAsyn(new GeocodeQuery(position.getAddress(), mCityCode));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(etSearch.getText())) {
            ViewUtil.getInstance().setViewGone(tvEnter);
            ViewUtil.getInstance().setViewGone(ivDelete);
            ViewUtil.getInstance().setViewVisible(ivVoice);
        } else {
            ViewUtil.getInstance().setViewVisible(ivDelete);
            ViewUtil.getInstance().setViewGone(ivVoice);
            ViewUtil.getInstance().setViewVisible(tvEnter);
        }

        if (mTips != null) {
            mTips.setQuery(new InputtipsQuery(etSearch.getText().toString(), mCityCode));
            mTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        LogUtil.print("---->onLocationChanged");
        if (location != null) {
            ViewUtil.getInstance().hideDialog(mDialog, this);
            this.mLocation = location;
            LogUtil.print("---->定位类型: " + location.getLocationType());
            LogUtil.print("---->经度: " + location.getLongitude());
            LogUtil.print("---->纬度: " + location.getLatitude());
            LogUtil.print("---->精度: " + location.getAccuracy());
            LogUtil.print("---->提供者: " + location.getProvider());
            LogUtil.print("---->国家: " + location.getCountry());
            LogUtil.print("---->省: " + location.getProvince());
            LogUtil.print("---->市: " + location.getCity());
            LogUtil.print("---->城市编码:" + location.getCityCode());
            LogUtil.print("---->区:" + location.getDistrict());
            LogUtil.print("---->区域码:" + location.getAdCode());
            LogUtil.print("---->地址:" + location.getAddress());
            mClient.stopLocation();
            mCityCode = location.getCityCode();
        } else {
            setResult(Constant.Common.RESULT_CODE);
            onFinish("LOCATION_FAILED");
        }
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int resultCode) {
        LogUtil.print("---->onRegeocodeSearched");
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int resultCode) {
        LogUtil.print("---->onGeocodeSearched");
        ViewUtil.getInstance().hideDialog(mDialog, this);
        if (resultCode == Constant.Map.GEOCODE_SEARCH_SUCCESS) {
            if (geocodeResult != null
                    && geocodeResult.getGeocodeAddressList() != null
                    && geocodeResult.getGeocodeAddressList().size() > 0) {
                LatLonPoint coordinate = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                Intent      intent     = new Intent();
                intent.putExtra(Temp.POINT_TYPE.getContent(), mPointType);
                intent.putExtra(Temp.POINT_CONTENT.getContent(), etSearch.getText().toString());
                intent.putExtra(Temp.LOCATION_LATITUDE.getContent(), coordinate.getLatitude());
                intent.putExtra(Temp.LOCATION_LONGITUDE.getContent(), coordinate.getLongitude());
                setResult(Constant.Common.RESULT_CODE, intent);
                onFinish("tvEnter");
            } else {
                SnackBarUtil.getInstance().showSnackBar(tvEnter, getString(R.string.route_prompt2), Snackbar.LENGTH_SHORT, Color.WHITE);
                setResult(Constant.Common.RESULT_CODE);
                onFinish("LOCATION_FAILED");
            }
        } else {
            MapUtil.getInstance().showMapException(this, resultCode);
        }
    }


    @Override
    public void onGetInputtips(List<Tip> tips, int resultCode) {
        LogUtil.print("---->onGetInputtips");
        LogUtil.print("---->resultCode:" + resultCode);
        if (resultCode == Constant.Map.GEOCODE_SEARCH_SUCCESS && tips != null) {
            List<RecommendPosition> positions = new ArrayList<>();
            for (Tip tip : tips) {
                LogUtil.print("---->city:" + tip.getAdcode());
                LogUtil.print("---->Address:" + tip.getAddress());
                RecommendPosition position = new RecommendPosition();
                position.setAddress(tip.getAddress());
                position.setCity(tip.getAdcode());
                positions.add(position);
            }
            mAdapter.setData(positions);
            rvRecommendPosition.setAdapter(mAdapter);
        }
    }
}
