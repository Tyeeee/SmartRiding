package com.yjt.app.utils;


import android.app.Activity;
import android.os.Parcelable;

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

    public boolean hasIntentExtraValue(Activity activity, String extraKey) {
        return activity.getIntent() != null && activity.getIntent().hasExtra(extraKey);
    }

    public boolean hasBundleExtraValue(Activity activity, String extraKey) {
        return activity.getIntent().getExtras() != null && activity.getIntent().hasExtra(extraKey);
    }

    public int getIntData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            return activity.getIntent().getExtras().getInt(key);
        }
        return 0;
    }

    public double getDoubleData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            return activity.getIntent().getExtras().getDouble(key);
        }
        return 0.0;
    }

    public String getStringData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            return activity.getIntent().getExtras().getString(key);
        }
        return null;
    }

    public Parcelable getParcelableData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            return activity.getIntent().getExtras().getParcelable(key);
        }
        return null;
    }
}
