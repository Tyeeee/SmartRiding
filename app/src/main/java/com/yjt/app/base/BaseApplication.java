package com.yjt.app.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * 全局变量
 *
 * @author yjt
 */
public class BaseApplication extends MultiDexApplication {

    private static BaseApplication mApplication;

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
        super.onCreate();
        mApplication = this;
//        CrashHandler.getInstance().initialize();
//        CrashReport.initCrashReport(getApplicationContext(), Constant.BUGLY_APP_ID, false);
    }

    public void releaseReference() {
        mApplication = null;
    }
}
