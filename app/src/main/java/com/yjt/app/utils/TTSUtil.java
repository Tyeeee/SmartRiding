package com.yjt.app.utils;

import android.app.Activity;
import android.os.Bundle;
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
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.ui.listener.OnDictationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class TTSUtil implements SynthesizerListener, AMapNaviListener, InitListener, RecognizerListener, RecognizerDialogListener {

    public static TTSUtil mTTSUtil;
    private SpeechSynthesizer mSpeechSynthesizer;
    private SpeechRecognizer mSpeechRecognizer;
    private RecognizerDialog mDialog;
    private OnDictationListener mListener;

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

    public void initializeSpeechSynthesizer() {
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(BaseApplication.getInstance(), this);
        setSpeechSynthesizerParameter();
    }

    public void initializeSpeechRecognizer() {
        mSpeechRecognizer = SpeechRecognizer.createRecognizer(BaseApplication.getInstance(), this);
        setSpeechRecognizerParameter();
    }

    public void startPlaying(String content) {
        if (mSpeechSynthesizer == null) {
            initializeSpeechSynthesizer();
        }
        mSpeechSynthesizer.startSpeaking(content, this);
    }

    public void stopPlaying() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
        }
    }

    public void startListening(Activity activity) {
        if (mSpeechRecognizer == null) {
            initializeSpeechRecognizer();
        }
        if (mDialog == null) {
            mDialog = new RecognizerDialog(activity, this);
        }
        mDialog.setListener(this);
        mDialog.show();
//        mSpeechRecognizer.startListening(this);

    }

    public void stopListening() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.stopListening();
        }
    }

    public void destroy() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
            mSpeechSynthesizer.destroy();
        }
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
        }
        releaseInstance();
    }

    private void setSpeechSynthesizerParameter() {
        mSpeechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC);
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, Constant.Map.TTS_ROLE);
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, Constant.Map.TTS_SPEED);
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, Constant.Map.TTS_VOLUME);
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, Constant.Map.TTS_PITCH);
        mSpeechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, Constant.Map.STREAM_TYPE);
        mSpeechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, Constant.Map.KEY_REQUEST_FOCUS);
        mSpeechSynthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, Constant.Map.AUDIO_FORMAT);
//        mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + Constant.Map.TTS_AUDIO_PATH);
    }

    private void setSpeechRecognizerParameter() {
        mSpeechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, Constant.Map.JSON);
        mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC);
        mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, Constant.Map.LANGUAGE);
        mSpeechRecognizer.setParameter(SpeechConstant.ACCENT, Constant.Map.LANGUAGE_TYPE);
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS, Constant.Map.START_TIME_LINE);
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS, Constant.Map.END_TIME_LINE);
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT, Constant.Map.PUNCTUATION);
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, Constant.Map.AUDIO_FORMAT);
//        mSpeechRecognizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + Constant.Map.TTS_AUDIO_PATH);
    }


    public void setListener(OnDictationListener mListener) {
        this.mListener = mListener;
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
        LogUtil.print("-----------------------:" + result);
        if (!result.contains(Constant.Map.MOVE_STATUS15)
                && !result.contains(Constant.Map.MOVE_STATUS16)
                && !result.contains(Constant.Map.MOVE_STATUS17)) {
            TTSUtil.getInstance().startPlaying(result);
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
            startPlaying(null);
        }
    }

    @Override
    public void onVolumeChanged(int i, byte[] bytes) {
        LogUtil.print("-------->onVolumeChanged");
    }

    @Override
    public void onBeginOfSpeech() {
        LogUtil.print("-------->onBeginOfSpeech");
    }

    @Override
    public void onEndOfSpeech() {
        LogUtil.print("-------->onEndOfSpeech");
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean islast) {
        LogUtil.print("-------->onResult");
        try {
            StringBuilder builder = new StringBuilder();
            JSONObject object1 = new JSONObject(new JSONTokener(recognizerResult.getResultString()));
            JSONArray array1 = object1.getJSONArray(Constant.Map.WS);
            for (int i = 0; i < array1.length(); i++) {
                builder.append(array1.getJSONObject(i).getJSONArray(Constant.Map.CW).getJSONObject(0).getString(Constant.Map.W));
            }
            mListener.setOnDictationListener(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(SpeechError speechError) {
        LogUtil.print("-------->onError");
        ToastUtil.getInstance().showToast(BaseApplication.getInstance(), speechError.getPlainDescription(true), Toast.LENGTH_SHORT);
        mDialog.dismiss();
    }
}
