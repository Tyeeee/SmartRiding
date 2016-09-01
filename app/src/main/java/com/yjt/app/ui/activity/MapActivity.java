package com.yjt.app.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.ui.widget.DriveRouteColorfulOverLay;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MapUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends BaseActivity implements View.OnClickListener, AMapNaviListener, AMapNaviViewListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMapClickListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener, AMap.OnMapLoadedListener {

    private MapView mvMap;
    private FloatingActionButton fabNavigation;

    private LatLonPoint mStartPoint;
    private LatLonPoint mPassPoint;
    private LatLonPoint mEndPoint;

    private AMap mAmap;
    private RouteSearch mSearch;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findViewById();
        initialize(savedInstanceState);
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
        fabNavigation = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.fabNavigation, this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        mvMap.onCreate(savedInstanceState);
        mDialog = ViewUtil.getInstance().showProgressDialog(this, null, getString(R.string.location_prompt), null, false);
        if (IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.START_LOCATION_LONGITUDE.getContent()) && IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.START_LOCATION_LATITUDE.getContent())) {
            LogUtil.print("--->显示 StartLongitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.START_LOCATION_LONGITUDE.getContent()));
            LogUtil.print("--->显示 StartLatitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.START_LOCATION_LATITUDE.getContent()));
            mStartPoint = new LatLonPoint(IntentDataUtil.getInstance().getDoubleData(this, Temp.START_LOCATION_LATITUDE.getContent()), IntentDataUtil.getInstance().getDoubleData(this, Temp.START_LOCATION_LONGITUDE.getContent()));
        }
        if (IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.PASS_LOCATION_LONGITUDE.getContent()) && IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.PASS_LOCATION_LATITUDE.getContent())) {
            LogUtil.print("--->显示 PassLongitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.PASS_LOCATION_LONGITUDE.getContent()));
            LogUtil.print("--->显示 PassLatitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.PASS_LOCATION_LATITUDE.getContent()));
            mPassPoint = new LatLonPoint(IntentDataUtil.getInstance().getDoubleData(this, Temp.PASS_LOCATION_LATITUDE.getContent()), IntentDataUtil.getInstance().getDoubleData(this, Temp.PASS_LOCATION_LONGITUDE.getContent()));
        }
        if (IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.END_LOCATION_LONGITUDE.getContent()) && IntentDataUtil.getInstance().hasBundleExtraValue(this, Temp.END_LOCATION_LATITUDE.getContent())) {
            LogUtil.print("--->显示 EndLongitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.END_LOCATION_LONGITUDE.getContent()));
            LogUtil.print("--->显示 EndLatitude:" + IntentDataUtil.getInstance().getDoubleData(this, Temp.END_LOCATION_LATITUDE.getContent()));
            mEndPoint = new LatLonPoint(IntentDataUtil.getInstance().getDoubleData(this, Temp.END_LOCATION_LATITUDE.getContent()), IntentDataUtil.getInstance().getDoubleData(this, Temp.END_LOCATION_LONGITUDE.getContent()));
        }

        if (mAmap == null) {
            mAmap = mvMap.getMap();
            mAmap.getUiSettings().setZoomControlsEnabled(false);
        }
        mSearch = new RouteSearch(this);
        setListener();
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
            case R.id.fabNavigation:
                SnackBarUtil.getInstance().showSnackBar(view, "fabNavigation", Snackbar.LENGTH_SHORT);
                break;
        }
    }

    @Override
    public void onInitNaviFailure() {
        LogUtil.print("------onInitNaviFailure");
    }

    @Override
    public void onInitNaviSuccess() {
        LogUtil.print("------onInitNaviSuccess");
    }

    @Override
    public void onStartNavi(int i) {
        LogUtil.print("------onStartNavi");
    }

    @Override
    public void onTrafficStatusUpdate() {
        LogUtil.print("------onTrafficStatusUpdate");
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        LogUtil.print("------onLocationChange");
    }

    @Override
    public void onGetNavigationText(int i, String s) {
        LogUtil.print("------onGetNavigationText");
    }

    @Override
    public void onEndEmulatorNavi() {
        LogUtil.print("------onEndEmulatorNavi");
    }

    @Override
    public void onArriveDestination() {
        LogUtil.print("------onArriveDestination");
    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {
        LogUtil.print("------onArriveDestination");
    }

    @Override
    public void onCalculateRouteSuccess() {
        LogUtil.print("------onCalculateRouteSuccess");
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        LogUtil.print("------onCalculateRouteFailure");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        LogUtil.print("------onReCalculateRouteForYaw");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        LogUtil.print("------onReCalculateRouteForTrafficJam");
    }

    @Override
    public void onArrivedWayPoint(int i) {
        LogUtil.print("------onArrivedWayPoint");
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
        LogUtil.print("------onGpsOpenStatus");
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        LogUtil.print("------onNaviInfoUpdated");
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        LogUtil.print("------onNaviInfoUpdate");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        LogUtil.print("------OnUpdateTrafficFacility");
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        LogUtil.print("------OnUpdateTrafficFacility");
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        LogUtil.print("------showCross");
    }

    @Override
    public void hideCross() {
        LogUtil.print("------hideCross");
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
        LogUtil.print("------showLaneInfo");
    }

    @Override
    public void hideLaneInfo() {
        LogUtil.print("------hideLaneInfo");
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] routeIds) {
        LogUtil.print("------onCalculateMultipleRoutesSuccess");
    }

    @Override
    public void notifyParallelRoad(int i) {
        LogUtil.print("------notifyParallelRoad");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        LogUtil.print("------OnUpdateTrafficFacility");
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        LogUtil.print("------updateAimlessModeStatistics");
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        LogUtil.print("------updateAimlessModeCongestionInfo");
    }

    @Override
    public void onNaviSetting() {
        LogUtil.print("------onNaviSetting");
    }

    @Override
    public void onNaviCancel() {
        LogUtil.print("------onNaviCancel");
    }

    @Override
    public boolean onNaviBackClick() {
        LogUtil.print("------onNaviBackClick");
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {
        LogUtil.print("------onNaviMapMode");
    }

    @Override
    public void onNaviTurnClick() {
        LogUtil.print("------onNaviTurnClick");
    }

    @Override
    public void onNextRoadClick() {
        LogUtil.print("------onNextRoadClick");
    }

    @Override
    public void onScanViewButtonClick() {
        LogUtil.print("------onScanViewButtonClick");
    }

    @Override
    public void onLockMap(boolean b) {
        LogUtil.print("------onLockMap");
    }

    @Override
    public void onNaviViewLoaded() {
        LogUtil.print("------onNaviViewLoaded");
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        LogUtil.print("------onRegeocodeSearched");
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        LogUtil.print("------onGeocodeSearched");
    }

    @Override
    public void onMapLoaded() {
        LogUtil.print("------onMapLoaded");
        mAmap.showBuildings(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LogUtil.print("------onMapClick");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LogUtil.print("------onMarkerClick");
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        LogUtil.print("------onInfoWindowClick");
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LogUtil.print("------getInfoWindow");
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LogUtil.print("------getInfoContents");
        return null;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int resultCode) {
        LogUtil.print("------onBusRouteSearched");
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int resultCode) {
        LogUtil.print("------onDriveRouteSearched");
        ViewUtil.getInstance().hideDialog(mDialog, this);
        mAmap.clear();
        if (resultCode == Constant.Map.GEOCODE_SEARCH_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null && driveRouteResult.getPaths().size() > 0) {
                DrivePath path = driveRouteResult.getPaths().get(0);
                DriveRouteColorfulOverLay overLay = new DriveRouteColorfulOverLay(mAmap, path, driveRouteResult.getStartPos(), driveRouteResult.getTargetPos(), null);
                overLay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                overLay.removeFromMap();
                overLay.addToMap();
                overLay.zoomToSpan();
            } else {
                SnackBarUtil.getInstance().showSnackBar(mvMap, getString(R.string.route_prompt2), Snackbar.LENGTH_SHORT);
            }
        } else {
            MapUtil.getInstance().showMapException(this, resultCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int resultCode) {
        LogUtil.print("------onWalkRouteSearched");
        ViewUtil.getInstance().hideDialog(mDialog, this);
        mAmap.clear();
        if (resultCode == Constant.Map.GEOCODE_SEARCH_SUCCESS) {
            if (walkRouteResult != null && walkRouteResult.getPaths() != null && walkRouteResult.getPaths().size() > 0) {
                WalkPath path = walkRouteResult.getPaths().get(0);
                WalkRouteOverlay overlay = new WalkRouteOverlay(this, mAmap, path, walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
                overlay.removeFromMap();
                overlay.addToMap();
                overlay.zoomToSpan();
            } else {
                SnackBarUtil.getInstance().showSnackBar(mvMap, getString(R.string.route_prompt2), Snackbar.LENGTH_SHORT);
            }
        } else {
            MapUtil.getInstance().showMapException(this, resultCode);
        }
    }
}
