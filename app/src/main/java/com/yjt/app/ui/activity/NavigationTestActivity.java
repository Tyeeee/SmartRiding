package com.yjt.app.ui.activity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
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
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.File;
import com.yjt.app.service.BluetoothService;
import com.yjt.app.ui.dialog.PromptDialog;
import com.yjt.app.ui.listener.dialog.OnPromptDialogListener;
import com.yjt.app.utils.BluetoothUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MapUtil;
import com.yjt.app.utils.MessageUtil;
import com.yjt.app.utils.SharedPreferenceUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.TTSUtil;
import com.yjt.app.utils.ToastUtil;
import com.yjt.app.utils.ViewUtil;

import java.lang.ref.WeakReference;


public class NavigationTestActivity extends FragmentActivity implements AMapNaviListener, AMapNaviViewListener, OnPromptDialogListener {

    private AMapNaviView nvMap;

    private BluetoothService mService;
    private BluetoothGattCharacteristic mCharacteristic;

    private NavigationHandler mHandler;

    protected static class NavigationHandler extends Handler {

        private WeakReference<NavigationTestActivity> mActivitys;

        public NavigationHandler(NavigationTestActivity activity) {
            mActivitys = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final NavigationTestActivity activity = mActivitys.get();
            if (activity != null) {
                switch (msg.what) {
                    case Constant.Bluetooth.LIGHT_OPEN:
                        if (activity.mService != null && activity.mCharacteristic != null) {
                            LogUtil.print("---->LIGHT_OPEN");
                            BluetoothUtil.getInstance().lightOperation(Constant.Bluetooth.LIGHT_OPEN, activity.mCharacteristic, activity.mService);
                        }
                        break;
                    case Constant.Bluetooth.LIGHT_CLOSE:
                        if (activity.mService != null && activity.mCharacteristic != null) {
                            LogUtil.print("---->LIGHT_CLOSE");
                            BluetoothUtil.getInstance().lightOperation(Constant.Bluetooth.LIGHT_CLOSE, activity.mCharacteristic, activity.mService);
                        }
                        break;
                    case Constant.Bluetooth.LIGHT_LEFT:
                        if (activity.mService != null && activity.mCharacteristic != null) {
                            LogUtil.print("---->LIGHT_LEFT");
                            BluetoothUtil.getInstance().lightOperation(Constant.Bluetooth.LIGHT_LEFT, activity.mCharacteristic, activity.mService);
                        }
                        break;
                    case Constant.Bluetooth.LIGHT_RIGHT:
                        if (activity.mService != null && activity.mCharacteristic != null) {
                            LogUtil.print("---->LIGHT_RIGHT");
                            BluetoothUtil.getInstance().lightOperation(Constant.Bluetooth.LIGHT_RIGHT, activity.mCharacteristic, activity.mService);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (SnackBarUtil.getInstance().isShown()) {
            ViewUtil.getInstance().setSystemUiVisibility(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        findViewById();
        initialize(savedInstanceState);
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
        TTSUtil.getInstance().stopPlaying();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nvMap.onDestroy();
        AMapNavi.getInstance(BaseApplication.getInstance()).stopNavi();
        AMapNavi.getInstance(BaseApplication.getInstance()).destroy();
        TTSUtil.getInstance().destroy();
        AMapNavi.getInstance(BaseApplication.getInstance()).removeAMapNaviListener(this);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        PromptDialog.createBuilder(getSupportFragmentManager())
                .setTitle(getString(R.string.prompt_title))
                .setPrompt(getString(R.string.prompt_exit_navigation))
                .setPositiveButtonText(R.string.enter)
                .setNegativeButtonText(R.string.cancel)
                .setRequestCode(Constant.RequestCode.DIALOG_EXIT)
                .show();
    }

    protected void findViewById() {
        nvMap = ViewUtil.getInstance().findView(this, R.id.nvMap);
    }

    protected void initialize(Bundle savedInstanceState) {
        nvMap.onCreate(savedInstanceState);
        mHandler = new NavigationHandler(this);
        mService = BaseApplication.getInstance().getService();
        mCharacteristic = BaseApplication.getInstance().getCharacteristic();
        LogUtil.print("---->mService:" + mService);
        LogUtil.print("---->mCharacteristic:" + mCharacteristic);
        setMapOptions();
        TTSUtil.getInstance().initializeSpeechSynthesizer();
        AMapNavi.getInstance(BaseApplication.getInstance()).setEmulatorNaviSpeed(Constant.Map.SIMULATED_NAVIGATION_SPEED);
        if (SharedPreferenceUtil.getInstance().getInt(File.FILE_NAME.getContent(), Context.MODE_PRIVATE, File.NAVIGATION_DEMONSTRATION_PATTERN.getContent(), Constant.Common.DEFAULT_VALUE) == Constant.Map.NAVIGATION_GPS) {
            AMapNavi.getInstance(BaseApplication.getInstance()).startNavi(NaviType.GPS);
        } else {
            AMapNavi.getInstance(BaseApplication.getInstance()).startNavi(NaviType.EMULATOR);
        }
    }

    protected void setListener() {
        nvMap.setAMapNaviViewListener(this);
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
            options.setCrossDisplayEnabled(false);
            options.setCrossDisplayShow(false);
            options.setAutoChangeZoom(true);
            options.setLayoutVisible(true);
            nvMap.setViewOptions(options);
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_EXIT:
                LogUtil.print("---->DIALOG_EXIT onNegativeButtonClicked");
                break;
            default:
                break;
        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_EXIT:
                LogUtil.print("---->DIALOG_EXIT onNeutralButtonClicked");
                break;
            default:
                break;
        }
    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_EXIT:
                mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.LIGHT_CLOSE));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onInitNaviFailure() {
        LogUtil.print("---->onInitNaviFailure");
    }

    @Override
    public void onInitNaviSuccess() {
        LogUtil.print("---->onInitNaviSuccess");
        ToastUtil.getInstance().showToast(this, getString(R.string.navigation_failed), Toast.LENGTH_SHORT);
        finish();
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
        mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.LIGHT_CLOSE));
    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {
        LogUtil.print("---->onArriveDestination");
        mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.LIGHT_CLOSE));
    }

    @Override
    public void onCalculateRouteSuccess() {
        LogUtil.print("---->onCalculateRouteSuccess");
    }

    @Override
    public void onCalculateRouteFailure(int resultCode) {
        MapUtil.getInstance().showMapError(this, resultCode);
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
    public void onGpsOpenStatus(boolean enabled) {
        LogUtil.print("---->onGpsOpenStatus");
        if (!enabled) {
            SnackBarUtil.getInstance().showSnackBar(this, getString(R.string.gps_prompt), Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        LogUtil.print("---->onNaviInfoUpdated");
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        LogUtil.print("---->onNaviInfoUpdate1:" + naviInfo.getCurrentRoadName());
        LogUtil.print("---->onNaviInfoUpdate2:" + naviInfo.getCurStepRetainDistance());
        LogUtil.print("---->onNaviInfoUpdate3:" + naviInfo.getPathRetainDistance());
        LogUtil.print("---->onNaviInfoUpdate4:" + naviInfo.getCurStep());
        LogUtil.print("---->onNaviInfoUpdate5:" + naviInfo.getCurLink());
        LogUtil.print("---->onNaviInfoUpdate6:" + naviInfo.getDirection());
        LogUtil.print("---->onNaviInfoUpdate7:" + naviInfo.getPathRetainTime());
        switch (naviInfo.getIconType()) {
            case IconType.LEFT:
            case IconType.LEFT_BACK:
            case IconType.LEFT_FRONT:
            case IconType.LEFT_TURN_AROUND:
                if (naviInfo.getCurStepRetainDistance() < Constant.Map.STEP_DISTANCE) {
                    ToastUtil.getInstance().showToast(this, "左转弯", Toast.LENGTH_SHORT);
                    mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.LIGHT_LEFT));
                } else {
                    mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.LIGHT_CLOSE));
                }
                break;
            case IconType.RIGHT:
            case IconType.RIGHT_BACK:
            case IconType.RIGHT_FRONT:
                if (naviInfo.getCurStepRetainDistance() < Constant.Map.STEP_DISTANCE) {
                    ToastUtil.getInstance().showToast(this, "右转弯", Toast.LENGTH_SHORT);
                    mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.LIGHT_RIGHT));
                } else {
                    mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.LIGHT_CLOSE));
                }
                break;
            default:
                mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.LIGHT_CLOSE));
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
        return true;
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
