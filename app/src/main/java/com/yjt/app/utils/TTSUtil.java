package com.yjt.app.utils;

import android.os.Bundle;

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
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;
import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;

public class TTSUtil implements SynthesizerListener, AMapNaviListener {

    public static TTSUtil mTTSUtil;
    boolean isfinish = true;
    private SpeechSynthesizer mSpeechSynthesizer;

    private SpeechListener mListener = new SpeechListener() {

        @Override
        public void onData(byte[] arg0) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error != null) {

            }
        }

        @Override
        public void onEvent(int arg0, Bundle arg1) {
        }
    };

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
        SpeechUser.getUser().login(BaseApplication.getInstance(), null, null,
                                   Constant.IFLY_APP_ID, mListener);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(BaseApplication.getInstance());
        initializeSpeechSynthesizer();
    }

    public void play(String playText) {
        if (!isfinish) {
            return;
        }
        if (null == mSpeechSynthesizer) {
            mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(BaseApplication.getInstance());
            initializeSpeechSynthesizer();
        }
        mSpeechSynthesizer.startSpeaking(playText, this);

    }

    public void stopSpeaking() {
        if (mSpeechSynthesizer != null)
            mSpeechSynthesizer.stopSpeaking();
    }

    public void startSpeaking() {
        isfinish = true;
    }

    private void initializeSpeechSynthesizer() {
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, BaseApplication.getInstance().getString(R.string.tts_role_default));
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, BaseApplication.getInstance().getString(R.string.tts_speed_key));
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, BaseApplication.getInstance().getString(R.string.tts_volume_key));
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, BaseApplication.getInstance().getString(R.string.tts_pitch_key));
    }

    @Override
    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCompleted(SpeechError arg0) {
        isfinish = true;
    }

    @Override
    public void onSpeakBegin() {
        isfinish = false;

    }

    @Override
    public void onSpeakPaused() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpeakProgress(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpeakResumed() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onArriveDestination() {
        play(Constant.Map.MOVE_STATUS16);
    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {
        play(Constant.Map.MOVE_STATUS16);
    }

    @Override
    public void onArrivedWayPoint(int arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onCalculateRouteFailure(int arg0) {
        play(Constant.Map.MOVE_STATUS18);
    }

    @Override
    public void onCalculateRouteSuccess() {
        play(Constant.Map.MOVE_STATUS15);
    }

    @Override
    public void onEndEmulatorNavi() {
        play(Constant.Map.MOVE_STATUS17);

    }

    @Override
    public void onGetNavigationText(int arg0, String arg1) {
        play(arg1);
    }

    @Override
    public void onInitNaviFailure() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitNaviSuccess() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        play(Constant.Map.MOVE_STATUS19);
    }

    @Override
    public void onReCalculateRouteForYaw() {
        play(Constant.Map.MOVE_STATUS20);
    }

    @Override
    public void onStartNavi(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTrafficStatusUpdate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsOpenStatus(boolean arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {

        // TODO Auto-generated method stub

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }
}
