package com.yjt.app.utils;


import android.app.Activity;

public class IntentDataUtil {

    private static IntentDataUtil mIntentDataUtil;

    private IntentDataUtil() {
        // cannot be instantiated
    }

    public static synchronized IntentDataUtil getInstance() {
        if (mIntentDataUtil == null) {
            mIntentDataUtil = new IntentDataUtil();
        }
        return mIntentDataUtil;
    }

    public static void releaseInstance() {
        if (mIntentDataUtil != null) {
            mIntentDataUtil = null;
        }
    }

    public boolean hasExtraValue(Activity activity, String extraKey) {
        return activity.getIntent() != null && activity.getIntent().hasExtra(extraKey);
    }

    public String hasExtraObject(Activity activity, String extraKey) {
        if (activity.getIntent() != null) {
            return activity.getIntent().getExtras().getString(extraKey);
        }
        return null;
    }

    public int getIntData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            return activity.getIntent().getExtras().getInt(key);
        }
        return 0;
    }
}
