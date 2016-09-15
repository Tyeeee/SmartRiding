package com.yjt.app.utils;


import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

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

    public int getIntData(Bundle bundle, String key) {
        if (bundle != null) {
            return bundle.getInt(key);
        }
        return 0;
    }

    public double getDoubleData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            return activity.getIntent().getExtras().getDouble(key);
        }
        return 0.0;
    }

    public CharSequence getCharSequenceData(Bundle bundle, String key) {
        if (bundle != null) {
            return bundle.getCharSequence(key);
        }
        return null;
    }

    public CharSequence[] getCharSequenceArrayData(Bundle bundle, String key) {
        if (bundle != null) {
            return bundle.getCharSequenceArray(key);
        }
        return null;
    }

    public String getStringData(Bundle bundle, String key) {
        if (bundle != null) {
            return bundle.getString(key);
        }
        return null;
    }


    public String getStringData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            return activity.getIntent().getExtras().getString(key);
        }
        return null;
    }

    public long getLongData(Bundle bundle, String key, long defaultValue) {
        if (bundle != null) {
            return bundle.getLong(key, defaultValue);
        }
        return 0;
    }

    public boolean getBooleanData(Bundle bundle, String key) {
        if (bundle != null) {
            return bundle.getBoolean(key);
        }
        return false;
    }

    public <T extends Parcelable> T getParcelableData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            return (T) activity.getIntent().getExtras().getParcelable(key);
        }
        return null;
    }

    public <T extends Parcelable> T getParcelableData(Bundle bundle, String key) {
        if (bundle != null) {
            return (T) bundle.getParcelable(key);
        }
        return null;
    }

    public <T extends Collection<? extends Parcelable>> T getParcelableArrayListData(Bundle bundle, String key) {
        if (bundle != null) {
            return (T) bundle.getParcelableArrayList(key);
        }
        return null;
    }
}
