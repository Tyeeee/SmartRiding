package com.yjt.app.utils;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.amap.api.navi.AMapNaviListener;
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
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;

public class TTSUtil implements SynthesizerListener, AMapNaviListener, InitListener {

    public static TTSUtil           mTTSUtil;
    private       SpeechSynthesizer mSpeechSynthesizer;


    private TTSUtil() {
        // cannot be instantiated
    }

    public static synchronized TTSUtil getInstance() {
        if (mTTSUtil == null) {
            mTTSUtil = new TTSUtil();
        }
        return mTTSUtil;
    }

    public static void releaseInstance() {
        if (mTTSUtil != null) {
            mTTSUtil = null;
        }
    }

    public void initialize() {
        SpeechUtility.createUtility(BaseApplication.getInstance(), Constant.IFLY_APP_ID);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(BaseApplication.getInstance(), this);
        initializeSpeechSynthesizer();
    }

    public void startSpeaking(String playText) {
        if (mSpeechSynthesizer == null) {
            mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(BaseApplication.getInstance(), this);
            initializeSpeechSynthesizer();
        }
        mSpeechSynthesizer.startSpeaking(playText, this);

    }

    public void stopSpeaking() {
        if (mSpeechSynthesizer != null)
            mSpeechSynthesizer.stopSpeaking();
    }

    private void initializeSpeechSynthesizer() {
        mSpeechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, Constant.Map.TTS_ROLE);
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, Constant.Map.TTS_SPEED);
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, Constant.Map.TTS_VOLUME);
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, Constant.Map.TTS_PITCH);
        mSpeechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, Constant.Map.STREAM_TYPE);
        mSpeechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, Constant.Map.KEY_REQUEST_FOCUS);
        mSpeechSynthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, Constant.Map.AUDIO_FORMAT);
        mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + Constant.Map.TTS_AUDIO_PATH);
    }

    @Override
    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
        LogUtil.print("-------->onBufferProgress");

    }


    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
        LogUtil.print("-------->onEvent");
    }

    @Override
    public void onSpeakBegin() {
        LogUtil.print("-------->onSpeakBegin");

    }

    @Override
    public void onSpeakPaused() {
        LogUtil.print("-------->onSpeakPaused");

    }

    @Override
    public void onSpeakProgress(int arg0, int arg1, int arg2) {
        LogUtil.print("-------->onSpeakProgress");

    }

    @Override
    public void onCompleted(SpeechError speechError) {
        LogUtil.print("-------->onCompleted");
    }

    @Override
    public void onSpeakResumed() {
        LogUtil.print("-------->onSpeakResumed");

    }

    @Override
    public void onArriveDestination() {
        LogUtil.print("-------->onArriveDestination");
    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {
        LogUtil.print("-------->onArriveDestination");
    }

    @Override
    public void onArrivedWayPoint(int arg0) {
        LogUtil.print("-------->onArrivedWayPoint");
    }

    @Override
    public void onCalculateRouteFailure(int arg0) {
        LogUtil.print("-------->onCalculateRouteFailure");
    }

    @Override
    public void onCalculateRouteSuccess() {
        LogUtil.print("-------->onCalculateRouteSuccess");
    }

    @Override
    public void onEndEmulatorNavi() {
        LogUtil.print("-------->onEndEmulatorNavi");

    }

    @Override
    public void onGetNavigationText(int arg0, String result) {
        LogUtil.print("-------->onGetNavigationText");
        if (!result.contains(Constant.Map.MOVE_STATUS15)) {
            startSpeaking(result);
        }
    }

    @Override
    public void onInitNaviFailure() {
        LogUtil.print("-------->onInitNaviFailure");

    }

    @Override
    public void onInitNaviSuccess() {
        LogUtil.print("-------->onInitNaviSuccess");

    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {
        LogUtil.print("-------->onLocationChange");

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        LogUtil.print("-------->onReCalculateRouteForTrafficJam");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        LogUtil.print("-------->onReCalculateRouteForYaw");
    }

    @Override
    public void onStartNavi(int arg0) {
        LogUtil.print("-------->onStartNavi");

    }

    @Override
    public void onTrafficStatusUpdate() {
        LogUtil.print("-------->onTrafficStatusUpdate");

    }

    @Override
    public void onGpsOpenStatus(boolean arg0) {
        LogUtil.print("-------->onGpsOpenStatus");

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {
        LogUtil.print("-------->onNaviInfoUpdated");

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {
        LogUtil.print("-------->onNaviInfoUpdate");

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        LogUtil.print("-------->OnUpdateTrafficFacility");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        LogUtil.print("-------->OnUpdateTrafficFacility");
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        LogUtil.print("-------->showCross");
    }

    @Override
    public void hideCross() {
        LogUtil.print("-------->hideCross");
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
        LogUtil.print("-------->showLaneInfo");
    }

    @Override
    public void hideLaneInfo() {
        LogUtil.print("-------->hideLaneInfo");
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        LogUtil.print("-------->onCalculateMultipleRoutesSuccess");
    }

    @Override
    public void notifyParallelRoad(int i) {
        LogUtil.print("-------->notifyParallelRoad");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        LogUtil.print("-------->OnUpdateTrafficFacility");
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        LogUtil.print("-------->updateAimlessModeStatistics");
    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        LogUtil.print("-------->updateAimlessModeCongestionInfo");
    }

    @Override
    public void onInit(int resultCode) {
        LogUtil.print("-------->onInit");
        if (resultCode != ErrorCode.SUCCESS) {
            ToastUtil.getInstance().showToast(BaseApplication.getInstance(), BaseApplication.getInstance().getString(R.string.tts_prompt), Toast.LENGTH_SHORT);
        } else {
            // 初始化成功，之后可以调用startSpeaking方法
            // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
            // 正确的做法是将onCreate中的startSpeaking调用移至这里
            startSpeaking(null);
        }
    }
}
