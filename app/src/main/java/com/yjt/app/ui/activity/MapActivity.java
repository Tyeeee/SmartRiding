package com.yjt.app.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.ui.widget.CustomOverlay;
import com.yjt.app.ui.widget.fab.FloatingActionButton;
import com.yjt.app.ui.widget.fab.FloatingActionMenu;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MapUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends BaseActivity implements View.OnClickListener, AMap.OnMapClickListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener, AMap.OnMapLoadedListener {

    private MapView mvMap;
    private FloatingActionMenu fabMenu;
    private FloatingActionButton fabDetail;
    private FloatingActionButton fabNavigation;

    private LatLonPoint mStartPoint;
    private LatLonPoint mPassPoint;
    private LatLonPoint mEndPoint;

    private AMap mAmap;
    private RouteSearch mSearch;
    private RouteResult mResult;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findViewById();
        setViewListener();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvMap.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvMap.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvMap.onDestroy();
    }

    @Override
    protected void findViewById() {
        mvMap = ViewUtil.getInstance().findView(this, R.id.mvMap);
        fabMenu = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.fabMenu, this);
        fabDetail = new FloatingActionButton(this);
        fabDetail.setId(R.id.fabDetail);
        fabDetail.setSize(FloatingActionButton.SIZE_MINI);
        fabDetail.setColorNormalResId(android.R.color.white);
        fabDetail.setColorPressedResId(R.color.gray_979797);
        fabDetail.setIcon(R.mipmap.icon_start);
        fabNavigation = new FloatingActionButton(this);
        fabNavigation.setId(R.id.fabNavigation);
        fabNavigation.setSize(FloatingActionButton.SIZE_MINI);
        fabNavigation.setColorNormalResId(android.R.color.white);
        fabNavigation.setColorPressedResId(R.color.gray_979797);
        fabNavigation.setIcon(R.mipmap.icon_end);
    }

    @Override
    protected void setViewListener() {
        fabDetail.setOnClickListener(this);
        fabNavigation.setOnClickListener(this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        mvMap.onCreate(savedInstanceState);
        mDialog = ViewUtil.getInstance().showProgressDialog(this, null, getString(R.string.location_prompt), null, false);
        if (IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.START_LOCATION_LONGITUDE.getContent()) && IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.START_LOCATION_LATITUDE.getContent())) {
            LogUtil.print("---->显示 StartLongitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.START_LOCATION_LONGITUDE.getContent()));
            LogUtil.print("---->显示 StartLatitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.START_LOCATION_LATITUDE.getContent()));
            mStartPoint = new LatLonPoint(IntentDataUtil.getInstance().getDoubleData(this, Temp.START_LOCATION_LATITUDE.getContent()), IntentDataUtil.getInstance().getDoubleData(this, Temp.START_LOCATION_LONGITUDE.getContent()));
        }
        if (IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.PASS_LOCATION_LONGITUDE.getContent()) && IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.PASS_LOCATION_LATITUDE.getContent())) {
            LogUtil.print("---->显示 PassLongitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.PASS_LOCATION_LONGITUDE.getContent()));
            LogUtil.print("---->显示 PassLatitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.PASS_LOCATION_LATITUDE.getContent()));
            mPassPoint = new LatLonPoint(IntentDataUtil.getInstance().getDoubleData(this, Temp.PASS_LOCATION_LATITUDE.getContent()), IntentDataUtil.getInstance().getDoubleData(this, Temp.PASS_LOCATION_LONGITUDE.getContent()));
        }
        if (IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.END_LOCATION_LONGITUDE.getContent()) && IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.END_LOCATION_LATITUDE.getContent())) {
            LogUtil.print("---->显示 EndLongitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.END_LOCATION_LONGITUDE.getContent()));
            LogUtil.print("---->显示 EndLatitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.END_LOCATION_LATITUDE.getContent()));
            mEndPoint = new LatLonPoint(IntentDataUtil.getInstance().getDoubleData(this, Temp.END_LOCATION_LATITUDE.getContent()), IntentDataUtil.getInstance().getDoubleData(this, Temp.END_LOCATION_LONGITUDE.getContent()));
        }

        if (mAmap == null) {
            mAmap = mvMap.getMap();
            mAmap.getUiSettings().setZoomControlsEnabled(false);
        }
        mSearch = new RouteSearch(this);
        if (mStartPoint != null && mEndPoint != null) {
            List<LatLonPoint> points = new ArrayList<>();
            mAmap.addMarker(new MarkerOptions().position(MapUtil.getInstance().convertToLatLng(mStartPoint)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_start)));
            if (mPassPoint != null) {
                points = new ArrayList<>();
                points.add(mPassPoint);
                mAmap.addMarker(new MarkerOptions().position(MapUtil.getInstance().convertToLatLng(mPassPoint)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
            }
            mAmap.addMarker(new MarkerOptions().position(MapUtil.getInstance().convertToLatLng(mEndPoint)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_end)));
            mSearch.calculateDriveRouteAsyn(new RouteSearch.DriveRouteQuery(new RouteSearch.FromAndTo(mStartPoint, mEndPoint), RouteSearch.DrivingNoHighAvoidCongestionSaveMoney, points, null, null));
//            mSearch.calculateWalkRouteAsyn(new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(mStartPoint, mEndPoint), RouteSearch.WalkMultipath));
        } else {
            SnackBarUtil.getInstance().showSnackBar(mvMap, getString(R.string.route_prompt1), Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    protected void setListener() {
        mAmap.setOnMapLoadedListener(this);
        mAmap.setOnMapClickListener(this);
        mAmap.setOnMarkerClickListener(this);
        mAmap.setOnInfoWindowClickListener(this);
        mAmap.setInfoWindowAdapter(this);
        mSearch.setRouteSearchListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabDetail:
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable(Temp.ROUTE_INFO.getContent(), mResult);
                startActivity(RouteDetailActivity.class, bundle1);
                break;
            case R.id.fabNavigation:
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable(Temp.ROUTE_INFO.getContent(), mResult);
                startActivity(NavigationActivity.class, bundle2);
                break;
        }
    }


    @Override
    public void onMapLoaded() {
        LogUtil.print("---->onMapLoaded");
        mAmap.showBuildings(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LogUtil.print("---->onMapClick");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LogUtil.print("---->onMarkerClick");
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        LogUtil.print("---->onInfoWindowClick");
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LogUtil.print("---->getInfoWindow");
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LogUtil.print("---->getInfoContents");
        return null;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int resultCode) {
        LogUtil.print("---->onBusRouteSearched");
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int resultCode) {
        LogUtil.print("---->onDriveRouteSearched");
        this.mResult = driveRouteResult;
        ViewUtil.getInstance().hideDialog(mDialog, this);
        mAmap.clear();
        if (resultCode == Constant.Map.GEOCODE_SEARCH_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null && driveRouteResult.getPaths().size() > 0) {
                CustomOverlay overLay = new CustomOverlay(mAmap, driveRouteResult.getPaths().get(0), driveRouteResult.getStartPos(), driveRouteResult.getTargetPos(), null);
                overLay.setRouteWidth(getResources().getDimension(R.dimen.dp_10));
                overLay.setColor(true);
                overLay.setNodeIconVisible(true);
                overLay.setPassMarkerVisible(true);
                overLay.removeMarkerAndLine();
                overLay.addRouteToMap();
                overLay.zoomToSpan();
            } else {
                SnackBarUtil.getInstance().showSnackBar(mvMap, getString(R.string.route_prompt2), Snackbar.LENGTH_SHORT);
            }
        } else {
            MapUtil.getInstance().showMapException(this, resultCode);
        }

        fabMenu.addButton(fabDetail);
        fabMenu.addButton(fabNavigation);
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int resultCode) {
        LogUtil.print("---->onWalkRouteSearched");
        this.mResult = walkRouteResult;
        ViewUtil.getInstance().hideDialog(mDialog, this);
        mAmap.clear();
        if (resultCode == Constant.Map.GEOCODE_SEARCH_SUCCESS) {
            if (walkRouteResult != null && walkRouteResult.getPaths() != null && walkRouteResult.getPaths().size() > 0) {
                WalkRouteOverlay overlay = new WalkRouteOverlay(this, mAmap, walkRouteResult.getPaths().get(0), walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
                overlay.removeFromMap();
                overlay.setNodeIconVisibility(false);
                overlay.addToMap();
                overlay.zoomToSpan();
            } else {
                SnackBarUtil.getInstance().showSnackBar(mvMap, getString(R.string.route_prompt2), Snackbar.LENGTH_SHORT);
            }
        } else {
            MapUtil.getInstance().showMapException(this, resultCode);
        }

        fabMenu.addButton(fabDetail);
        fabMenu.addButton(fabNavigation);
    }
}
