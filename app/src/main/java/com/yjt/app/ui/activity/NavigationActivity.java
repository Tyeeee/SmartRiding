package com.yjt.app.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.IconType;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.route.RouteResult;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MapUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.TTSUtil;
import com.yjt.app.utils.ToastUtil;
import com.yjt.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;


public class NavigationActivity extends Activity implements AMapNaviListener, AMapNaviViewListener {

    private AMapNaviView nvMap;
    private RouteResult  mResult;
    private List<NaviLatLng> mStartLatLngs = new ArrayList<>();
    private List<NaviLatLng> mPassLatLngs  = new ArrayList<>();
    private List<NaviLatLng> mEndLatLngs   = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        findViewById();
        initialize(savedInstanceState);
        setViewListener();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nvMap.onResume();
        setMapOptions();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        nvMap.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nvMap.onPause();
        TTSUtil.getInstance().stopSpeaking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nvMap.onDestroy();
        AMapNavi.getInstance(BaseApplication.getInstance()).stopNavi();
        AMapNavi.getInstance(BaseApplication.getInstance()).destroy();
        TTSUtil.getInstance().stopSpeaking();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ViewUtil.getInstance().showAlertDialog(this, getString(R.string.prompt_title), getString(R.string.exit_navigation_prompt_content), getString(R.string.enter), getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, null);
    }

    protected void findViewById() {
        nvMap = ViewUtil.getInstance().findView(this, R.id.nvMap);
    }

    protected void setViewListener() {
        nvMap.setAMapNaviViewListener(this);
    }

    protected void initialize(Bundle savedInstanceState) {
        nvMap.onCreate(savedInstanceState);
        setMapOptions();
        if (IntentDataUtil.getInstance().hasIntentExtraValue(this, Temp.ROUTE_INFO.getContent())) {
            mResult = (RouteResult) IntentDataUtil.getInstance().getParcelableData(this, Temp.ROUTE_INFO.getContent());
            mStartLatLngs.add(MapUtil.getInstance().parseCoordinate(mResult.getStartPos().toString()));
            mEndLatLngs.add(MapUtil.getInstance().parseCoordinate(mResult.getTargetPos().toString()));
        } else {
            SnackBarUtil.getInstance().showSnackBar(nvMap, getString(R.string.route_prompt3), Snackbar.LENGTH_SHORT);
        }
        AMapNavi.getInstance(BaseApplication.getInstance()).setEmulatorNaviSpeed(Constant.Map.SIMULATED_NAVIGATION_SPEED);
        TTSUtil.getInstance().initialize();
    }

    protected void setListener() {
        AMapNavi.getInstance(BaseApplication.getInstance()).addAMapNaviListener(this);
        AMapNavi.getInstance(BaseApplication.getInstance()).addAMapNaviListener(TTSUtil.getInstance());
    }

    private void setMapOptions() {
        if (nvMap != null) {
            AMapNaviViewOptions options = nvMap.getViewOptions();
            options.setSettingMenuEnabled(false);
            options.setNaviNight(false);
            options.setReCalculateRouteForYaw(true);
            options.setTrafficLine(true);
            options.setReCalculateRouteForTrafficJam(false);
            options.setLeaderLineEnabled(Color.GRAY);
            options.setTrafficInfoUpdateEnabled(false);
            options.setCameraInfoUpdateEnabled(false);
            options.setMonitorCameraEnabled(false);
            options.setScreenAlwaysBright(true);
            options.setAutoDrawRoute(true);
            options.setCrossDisplayEnabled(true);
            options.setCrossDisplayShow(true);
            options.setAutoChangeZoom(true);
            options.setLayoutVisible(true);
            nvMap.setViewOptions(options);
        }
    }

    @Override
    public void onInitNaviFailure() {
        LogUtil.print("---->onInitNaviFailure");
    }

    @Override
    public void onInitNaviSuccess() {
        LogUtil.print("---->onInitNaviSuccess");
//        AMapNavi.getInstance(BaseApplication.getInstance()).calculateWalkRoute(MapUtil.getInstance().parseCoordinate(mResult.getStartPos().toString()), MapUtil.getInstance().parseCoordinate(mResult.getTargetPos().toString()));
        int strategy = 0;
        try {
            strategy = AMapNavi.getInstance(BaseApplication.getInstance()).strategyConvert(true, true, true, false, false);
        } catch (Exception e) {
        }
        AMapNavi.getInstance(BaseApplication.getInstance()).calculateDriveRoute(mStartLatLngs, mEndLatLngs, mPassLatLngs, strategy);
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
        LogUtil.print("-----------------1:" + result);
        if (!result.contains(Constant.Map.MOVE_STATUS15)
                && !result.contains(Constant.Map.MOVE_STATUS16)
                && !result.contains(Constant.Map.MOVE_STATUS17)) {
            TTSUtil.getInstance().startSpeaking(result);
        }
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
    public void onCalculateRouteSuccess() {
        LogUtil.print("---->onCalculateRouteSuccess");
        AMapNavi.getInstance(BaseApplication.getInstance()).startNavi(NaviType.EMULATOR);
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        LogUtil.print("---->onCalculateRouteFailure");
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
    public void onGpsOpenStatus(boolean b) {
        LogUtil.print("---->onGpsOpenStatus");
        if (!b) {
            SnackBarUtil.getInstance().showSnackBar(nvMap, getString(R.string.gps_prompt), Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        LogUtil.print("---->onNaviInfoUpdated");
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        LogUtil.print("---->onNaviInfoUpdate");
        switch (naviInfo.getIconType()) {
            case IconType.LEFT:
            case IconType.LEFT_BACK:
            case IconType.LEFT_FRONT:
            case IconType.LEFT_TURN_AROUND:
                if (naviInfo.getCurStepRetainDistance() < Constant.Map.STEP_DISTANCE) {
                    ToastUtil.getInstance().showToast(this, "左转弯", Toast.LENGTH_SHORT);
                }
                break;
            case IconType.RIGHT:
            case IconType.RIGHT_BACK:
            case IconType.RIGHT_FRONT:
                if (naviInfo.getCurStepRetainDistance() < Constant.Map.STEP_DISTANCE) {
                    ToastUtil.getInstance().showToast(this, "右转弯", Toast.LENGTH_SHORT);
                }
                break;
        }

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
    public void onCalculateMultipleRoutesSuccess(int[] routeIds) {
        LogUtil.print("---->onCalculateMultipleRoutesSuccess");
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

    @Override
    public void onNaviSetting() {
        LogUtil.print("---->onNaviSetting");
    }

    @Override
    public void onNaviCancel() {
        LogUtil.print("---->onNaviCancel");
    }

    @Override
    public boolean onNaviBackClick() {
        LogUtil.print("---->onNaviBackClick");
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {
        LogUtil.print("---->onNaviMapMode");
    }

    @Override
    public void onNaviTurnClick() {
        LogUtil.print("---->onNaviTurnClick");
    }

    @Override
    public void onNextRoadClick() {
        LogUtil.print("---->onNextRoadClick");
    }

    @Override
    public void onScanViewButtonClick() {
        LogUtil.print("---->onScanViewButtonClick");
    }

    @Override
    public void onLockMap(boolean b) {
        LogUtil.print("---->onLockMap");
    }

    @Override
    public void onNaviViewLoaded() {
        LogUtil.print("---->onNaviViewLoaded");
    }

}
