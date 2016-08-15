package com.yjt.app.utils;

import android.os.Build;
import android.os.StrictMode;

import com.yjt.app.BuildConfig;

/**
 * 严苛模式
 *
 * @author yjt
 */
public class StrictModeUtil {

    private static StrictModeUtil mStrictModeUtil;

    private StrictModeUtil() {
        // cannot be instantiated
    }

    public static synchronized StrictModeUtil getInstance() {
        if (mStrictModeUtil == null) {
            mStrictModeUtil = new StrictModeUtil();
        }
        return mStrictModeUtil;
    }

    public static void releaseInstance() {
        if (mStrictModeUtil != null) {
            mStrictModeUtil = null;
        }
    }

    public void initialize() {
        if (BuildConfig.DEBUG) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            builder.detectAll();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                builder.detectLeakedClosableObjects();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    builder.detectLeakedRegistrationObjects();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        builder.detectFileUriExposure();
                    }
                }
            }
            builder.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().penaltyDropBox();
            StrictMode.setVmPolicy(builder.build());
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                               .detectNetwork()
                                               .penaltyLog()
                                               .penaltyDropBox()
                                               .penaltyDialog()
                                               .build());
        }
    }
}
