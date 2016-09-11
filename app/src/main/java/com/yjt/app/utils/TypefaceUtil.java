package com.yjt.app.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.SimpleArrayMap;

import com.yjt.app.constant.Regex;

public class TypefaceUtil {

    private static TypefaceUtil mTypefaceUtil;
    private static SimpleArrayMap<String, Typeface> mCache = new SimpleArrayMap<>();

    private TypefaceUtil() {
        // cannot be instantiated
    }

    public static synchronized TypefaceUtil getInstance() {
        if (mTypefaceUtil == null) {
            mTypefaceUtil = new TypefaceUtil();
        }
        return mTypefaceUtil;
    }

    public static void releaseInstance() {
        if (mTypefaceUtil != null) {
            mTypefaceUtil = null;
        }
        if (mCache != null) {
            mCache.clear();
            mCache = null;
        }
    }


    public synchronized Typeface get(Context ctx, String name) {
        if (!mCache.containsKey(name)) {
            Typeface typeface = Typeface.createFromAsset(
                    ctx.getAssets(), String.format(Regex.FILE_TTF.getRegext(), name));
            mCache.put(name, typeface);
            return typeface;
        }
        return mCache.get(name);
    }
}
