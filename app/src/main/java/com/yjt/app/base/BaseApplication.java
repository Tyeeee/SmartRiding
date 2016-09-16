package com.yjt.app.base;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.iflytek.cloud.SpeechUtility;
import com.yjt.app.constant.Constant;
import com.yjt.app.utils.ActivityUtil;
import com.yjt.app.utils.AnimationUtil;
import com.yjt.app.utils.BluetoothUtil;
import com.yjt.app.utils.CrashHandler;
import com.yjt.app.utils.DensityUtil;
import com.yjt.app.utils.FragmentUtil;
import com.yjt.app.utils.InputUtil;
import com.yjt.app.utils.MapUtil;
import com.yjt.app.utils.NetworkUtil;
import com.yjt.app.utils.SharedPreferenceUtil;
import com.yjt.app.utils.SnackBarUtil;
import com.yjt.app.utils.StrictModeUtil;
import com.yjt.app.utils.TTSUtil;
import com.yjt.app.utils.ToastUtil;
import com.yjt.app.utils.TypefaceUtil;
import com.yjt.app.utils.VersionUtil;
import com.yjt.app.utils.ViewUtil;

public class BaseApplication extends MultiDexApplication {

    private static BaseApplication             mApplication;
    private        BluetoothGattCharacteristic mCharacteristic;

    public static BaseApplication getInstance() {
        return mApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
//        StrictModeUtil.getInstance().initialize();
        SpeechUtility.createUtility(this, Constant.IFLY_APP_ID);
        super.onCreate();
        mApplication = this;
//        CrashHandler.getInstance().initialize();
//        CrashReport.initCrashReport(getApplicationContext(), Constant.BUGLY_APP_ID, false);
    }

    public void releaseReference() {
        AnimationUtil.releaseInstance();
        BluetoothUtil.releaseInstance();
        CrashHandler.releaseInstance();
        DensityUtil.releaseInstance();
        FragmentUtil.releaseInstance();
        InputUtil.releaseInstance();
        MapUtil.releaseInstance();
        NetworkUtil.releaseInstance();
        SharedPreferenceUtil.releaseInstance();
        SnackBarUtil.releaseInstance();
        StrictModeUtil.releaseInstance();
        ToastUtil.releaseInstance();
        TTSUtil.releaseInstance();
        VersionUtil.releaseInstance();
        ViewUtil.releaseInstance();
        FragmentUtil.getInstance().clearCache();
        FragmentUtil.releaseInstance();
        TypefaceUtil.releaseInstance();
        ActivityUtil.removeAll();
        mApplication = null;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.mCharacteristic = characteristic;
    }
}
