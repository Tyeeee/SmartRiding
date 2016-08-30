package com.yjt.app.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 页面切换处理
 *
 * @author yjt
 */
public class PageSwitchUtil {

    public final static String INTENT_EXTRA_FRAGMENT_TYPE = "fragment_type";
    public final static String INTENT_EXTRA_FRAGMENT_ARGUMENT = "args";
    public final static String BUNDLE_FRAGMENT_CACHE = "cache";
    public final static String BUNDLE_FRAGMENT_ANIMATION = "anim";

    private static PageSwitchUtil mPageSwitchUtil;

    private PageSwitchUtil() {
        // cannot be instantiated
    }

    public static synchronized PageSwitchUtil getInstance() {
        if (mPageSwitchUtil == null) {
            mPageSwitchUtil = new PageSwitchUtil();
        }
        return mPageSwitchUtil;
    }

    public static void releaseInstantce() {
        if (mPageSwitchUtil != null) {
            mPageSwitchUtil = null;
        }
    }

    public static void switchToPage(Context context, int fragmentType, Bundle bundle, int intentFlag) {
        Intent intent = new Intent(context, null);//TODO 缺少fragmentactivity
        if (intentFlag != 0) {
            intent.setFlags(intentFlag);
        }
        intent.putExtra(INTENT_EXTRA_FRAGMENT_TYPE, fragmentType);
        if (bundle != null) {
            intent.putExtra(INTENT_EXTRA_FRAGMENT_ARGUMENT, bundle);
        }
        context.startActivity(intent);
    }

    public static void switchToPage(Context context, int fragmentType, Bundle bundle) {
        switchToPage(context, fragmentType, bundle, 0);
    }

    public static void switchToPage(Context context, int fragmentType) {
        switchToPage(context, fragmentType, null, 0);
    }

}
