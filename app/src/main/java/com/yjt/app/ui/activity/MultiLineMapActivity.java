package com.yjt.app.ui.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviException;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviStaticInfo;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.ui.dialog.ProgressDialog;
import com.yjt.app.ui.widget.fab.FloatingActionButton;
import com.yjt.app.ui.widget.fab.FloatingActionMenu;
import com.yjt.app.utils.BundleUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MapUtil;
import com.yjt.app.utils.ToastUtil;
import com.yjt.app.utils.ViewUtil;

import java.lang.ref.WeakReference;


public class MultiLineMapActivity extends BaseActivity implements View.OnClickListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, AMap.OnMapLoadedListener, AMapNaviListener {

    private MapView mvMap;
    private FloatingActionMenu fabMenu;
    private FloatingActionButton fabSelection;
    private FloatingActionButton fabNavigation;

    //    private List<NaviLatLng> mStartLatLngs = new ArrayList<>();
//    private List<NaviLatLng> mPassLatLngs = new ArrayList<>();
//    private List<NaviLatLng> mEndLatLngs = new ArrayList<>();
    private NaviLatLng mStartLatLng;
    private NaviLatLng mPassLatLng;
    private NaviLatLng mEndLatLng;
    private SparseArray<RouteOverLay> mRouteOverLays = new SparseArray<>();
    private AMap mAmap;
    private int mRouteIndex;
    private int mZindex;
    private DialogFragment mDialog;
    private MapHandler mHandler;

    protected static class MapHandler extends Handler {

        private WeakReference<MultiLineMapActivity> mActivitys;

        public MapHandler(MultiLineMapActivity activity) {
            mActivitys = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final MultiLineMapActivity activity = mActivitys.get();
            if (activity != null) {
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
        setContentView(R.layout.activity_map_multi_line);
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
        AMapNavi.getInstance(BaseApplication.getInstance()).removeAMapNaviListener(this);
        AMapNavi.getInstance(BaseApplication.getInstance()).destroy();
//        mStartLatLngs.clear();
//        mPassLatLngs.clear();
//        mEndLatLngs.clear();
        mRouteOverLays.clear();
    }

    @Override
    protected void findViewById() {
        mvMap = ViewUtil.getInstance().findView(this, R.id.mvMap);
        fabMenu = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.fabMenu, this);
        fabSelection = new FloatingActionButton(this);
        fabSelection.setId(R.id.fabSelection);
        fabSelection.setSize(FloatingActionButton.SIZE_MINI);
        fabSelection.setColorNormalResId(android.R.color.white);
        fabSelection.setColorPressedResId(R.color.gray_979797);
        fabSelection.setIcon(R.mipmap.icon_car);
        fabSelection.setTitle("选路");
        fabNavigation = new FloatingActionButton(this);
        fabNavigation.setId(R.id.fabNavigation);
        fabNavigation.setSize(FloatingActionButton.SIZE_MINI);
        fabNavigation.setColorNormalResId(android.R.color.white);
        fabNavigation.setColorPressedResId(R.color.gray_979797);
        fabNavigation.setIcon(R.mipmap.icon_end);
    }

    @Override
    protected void setViewListener() {
        fabSelection.setOnClickListener(this);
        fabNavigation.setOnClickListener(this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        mvMap.onCreate(savedInstanceState);
        mDialog = ProgressDialog.createBuilder(getSupportFragmentManager())
                .setPrompt(getString(R.string.location_prompt))
                .setCancelableOnTouchOutside(false)
                .setCancelable(false)
                .setRequestCode(Constant.RequestCode.DIALOG_PROGRESS_LOCATION_INFO)
                .show();
        if (BundleUtil.getInstance().hasBundleExtraValue(this, Temp.START_LOCATION_LONGITUDE.getContent()) && BundleUtil.getInstance().hasBundleExtraValue(this, Temp.START_LOCATION_LATITUDE.getContent())) {
            double latitude = BundleUtil.getInstance().getDoubleData(this, Temp.START_LOCATION_LATITUDE.getContent());
            double longitude = BundleUtil.getInstance().getDoubleData(this, Temp.START_LOCATION_LONGITUDE.getContent());
            LogUtil.print("---->显示 StartLongitude:" + longitude);
            LogUtil.print("---->显示 StartLatitude:" + latitude);
//            mStartLatLngs.add(new NaviLatLng(latitude, longitude));
            mStartLatLng = new NaviLatLng(latitude, longitude);
        }
        if (BundleUtil.getInstance().hasBundleExtraValue(this, Temp.PASS_LOCATION_LONGITUDE.getContent()) && BundleUtil.getInstance().hasBundleExtraValue(this, Temp.PASS_LOCATION_LATITUDE.getContent())) {
            double latitude = BundleUtil.getInstance().getDoubleData(this, Temp.PASS_LOCATION_LATITUDE.getContent());
            double longitude = BundleUtil.getInstance().getDoubleData(this, Temp.PASS_LOCATION_LONGITUDE.getContent());
            LogUtil.print("---->显示 PassLongitude:" + longitude);
            LogUtil.print("---->显示 PassLatitude:" + latitude);
//            mPassLatLngs.add(new NaviLatLng(latitude, longitude));
            mPassLatLng = new NaviLatLng(latitude, longitude);
        }
        if (BundleUtil.getInstance().hasBundleExtraValue(this, Temp.END_LOCATION_LONGITUDE.getContent()) && BundleUtil.getInstance().hasBundleExtraValue(this, Temp.END_LOCATION_LATITUDE.getContent())) {
            double latitude = BundleUtil.getInstance().getDoubleData(this, Temp.END_LOCATION_LATITUDE.getContent());
            double longitude = BundleUtil.getInstance().getDoubleData(this, Temp.END_LOCATION_LONGITUDE.getContent());
            LogUtil.print("---->显示 EndLongitude:" + longitude);
            LogUtil.print("---->显示 EndLatitude:" + latitude);
//            mEndLatLngs.add(new NaviLatLng(latitude, longitude));
            mEndLatLng = new NaviLatLng(latitude, longitude);
        }

        if (mAmap == null) {
            mAmap = mvMap.getMap();
            mAmap.getUiSettings().setZoomControlsEnabled(false);
        }
    }

    @Override
    protected void setListener() {
        AMapNavi.getInstance(BaseApplication.getInstance()).addAMapNaviListener(this);
        mAmap.setOnMapLoadedListener(this);
        mAmap.setOnMarkerClickListener(this);
        mAmap.setOnInfoWindowClickListener(this);
        mAmap.setInfoWindowAdapter(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabSelection:
                if (mRouteIndex >= mRouteOverLays.size()) {
                    mRouteIndex = 0;
                }
                int index = mRouteOverLays.keyAt(mRouteIndex);
                for (int i = 0; i < mRouteOverLays.size(); i++) {
                    mRouteOverLays.get(mRouteOverLays.keyAt(i)).setTransparency(Constant.Map.TRANSPARENCY_SELECTED);
                }
                mRouteOverLays.get(index).setTransparency(Constant.Map.TRANSPARENCY_UNSELECT);
                mRouteOverLays.get(index).setZindex(++mZindex);
                AMapNavi.getInstance(this).selectRouteId(index);
                mRouteIndex++;
                break;
            case R.id.fabDetail:
                startActivity(MultiRouteDetailActivity.class);
                break;
            case R.id.fabNavigation:
                startActivity(MultiLineNavigationActivity.class);
                break;
        }
    }

    @Override
    public void onMapLoaded() {
        LogUtil.print("---->onMapLoaded");
        mAmap.showBuildings(true);
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
    public void onInitNaviFailure() {
        LogUtil.print("---->onInitNaviFailure");
        ToastUtil.getInstance().showToast(this, getString(R.string.search_failed), Toast.LENGTH_SHORT);
        onFinish("onInitNaviFailure");
    }

    @Override
    public void onInitNaviSuccess() {
        LogUtil.print("---->onInitNaviSuccess");
//        try {
//            AMapNavi.getInstance(BaseApplication.getInstance()).calculateDriveRoute(mStartLatLngs
//                    , mEndLatLngs
//                    , mPassLatLngs
//                    , AMapNavi.getInstance(BaseApplication.getInstance()).strategyConvert(true, true, true, false, true));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        AMapNavi.getInstance(BaseApplication.getInstance()).calculateRideRoute(mStartLatLng, mEndLatLng);
    }

    @Override
    public void onStartNavi(int i) {
        LogUtil.print("---->onStartNavi");
    }

    @Override
    public void onTrafficStatusUpdate() {
        LogUtil.print("---->onTrafficStatusUpdate");
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        LogUtil.print("---->onLocationChange");
    }

    @Override
    public void onGetNavigationText(int i, String result) {
        LogUtil.print("---->onGetNavigationText");
    }

    @Override
    public void onEndEmulatorNavi() {
        LogUtil.print("---->onEndEmulatorNavi");
    }

    @Override
    public void onArriveDestination() {
        LogUtil.print("---->onArriveDestination");
    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {
        LogUtil.print("---->onArriveDestination");
    }

    @Override
    public void onArriveDestination(AMapNaviStaticInfo aMapNaviStaticInfo) {
        LogUtil.print("---->onArriveDestination");
    }

    @Override
    public void onCalculateRouteSuccess() {
        LogUtil.print("---->onCalculateRouteSuccess");
        ViewUtil.getInstance().hideDialog(mDialog);
        mRouteOverLays.clear();
        try {
            mAmap.moveCamera(CameraUpdateFactory.changeTilt(Constant.Map.ZOOM_ANGLE));
            RouteOverLay overLay = new RouteOverLay(mAmap, AMapNavi.getInstance(this).getNaviPath(), this);
            overLay.setTrafficLine(true);
            overLay.setWidth(getResources().getDimension(R.dimen.dp_15));
            overLay.removeFromMap();
            overLay.setStartPointBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_start));
            overLay.setEndPointBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_end));
            overLay.zoomToSpan(Constant.Map.ZOOM_VALUE);
            overLay.addToMap();
            mRouteOverLays.put(Constant.Map.DEFAULT_VALUE, overLay);
        } catch (AMapNaviException e) {
            e.printStackTrace();
        }
        fabMenu.addButton(fabNavigation);
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] routeIds) {
        LogUtil.print("---->onCalculateMultipleRoutesSuccess");
        ViewUtil.getInstance().hideDialog(mDialog);
        mRouteOverLays.clear();
        try {
            for (int routeId : routeIds) {
                LogUtil.print("---->routeId:" + routeId);
                mAmap.moveCamera(CameraUpdateFactory.changeTilt(Constant.Map.ZOOM_ANGLE));
                RouteOverLay overLay = new RouteOverLay(mAmap, AMapNavi.getInstance(this).getNaviPaths().get(routeId), this);
                overLay.setTrafficLine(true);
                overLay.setWidth(getResources().getDimension(R.dimen.dp_15));
                overLay.removeFromMap();
                overLay.setStartPointBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_start));
                overLay.setEndPointBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_end));
                overLay.zoomToSpan(Constant.Map.ZOOM_VALUE);
                overLay.addToMap();
                mRouteOverLays.put(routeId, overLay);
            }
        } catch (AMapNaviException e) {
            e.printStackTrace();
        }

        if (mRouteOverLays.size() > 1) {
            fabMenu.addButton(fabSelection);
        }
        fabMenu.addButton(fabNavigation);
    }

    @Override
    public void onCalculateRouteFailure(int resultCode) {
        ViewUtil.getInstance().hideDialog(mDialog);
        MapUtil.getInstance().showMapError(this, resultCode);
        LogUtil.print("---->onCalculateRouteFailure:");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        LogUtil.print("---->onReCalculateRouteForYaw");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        LogUtil.print("---->onReCalculateRouteForTrafficJam");
    }

    @Override
    public void onArrivedWayPoint(int i) {
        LogUtil.print("---->onArrivedWayPoint");
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
        LogUtil.print("---->onGpsOpenStatus");
        if (!enabled) {
            ToastUtil.getInstance().showToast(this, getString(R.string.gps_prompt), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        LogUtil.print("---->onNaviInfoUpdated");
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        LogUtil.print("---->onNaviInfoUpdate");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        LogUtil.print("---->OnUpdateTrafficFacility");
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        LogUtil.print("---->OnUpdateTrafficFacility");
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        LogUtil.print("---->showCross");
    }

    @Override
    public void hideCross() {
        LogUtil.print("---->hideCross");
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
        LogUtil.print("---->showLaneInfo");
    }

    @Override
    public void hideLaneInfo() {
        LogUtil.print("---->hideLaneInfo");
    }

    @Override
    public void notifyParallelRoad(int i) {
        LogUtil.print("---->notifyParallelRoad");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        LogUtil.print("---->OnUpdateTrafficFacility");
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        LogUtil.print("---->updateAimlessModeStatistics");
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        LogUtil.print("---->updateAimlessModeCongestionInfo");
    }
}
