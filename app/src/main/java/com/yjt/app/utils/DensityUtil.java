package com.yjt.app.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

/**
 * 页面显示单位转换工具
 *
 * @author yjt
 */
public class DensityUtil {

    private static DensityUtil mDensityUtil;
    private Context mContext;

    private DensityUtil(Context context) {
        // cannot be instantiated
        this.mContext = context;
    }

    public static synchronized DensityUtil getInstance(Context context) {
        if (mDensityUtil == null) {
            mDensityUtil = new DensityUtil(context);
        }
        return mDensityUtil;
    }

    public static void releaseInstance() {
        if (mDensityUtil != null) {
            mDensityUtil = null;
        }
    }

    public int getScreenWidth() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }

    public float getDensity() {
        return mContext.getResources().getDisplayMetrics().density;
    }

    public int getDensityDpi() {
        return mContext.getResources().getDisplayMetrics().densityDpi;
    }

    public int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dp(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public int getNavigationBarStatus() {
        boolean hasMenuKey = ViewConfiguration.get(mContext).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            return mContext.getResources().getDimensionPixelSize(mContext.getResources().getIdentifier("navigation_bar_height", "dimen", "android"));
        } else {
            return 0;
        }
    }
}