package com.yjt.app.utils;


public class DataUtil {
    
    private static DataUtil mDataUtil;

    private DataUtil() {
        // cannot be instantiated
    }

    public static synchronized DataUtil getInstance() {
        if (mDataUtil == null) {
            mDataUtil = new DataUtil();
        }
        return mDataUtil;
    }

    public static void releaseInstance() {
        if (mDataUtil != null) {
            mDataUtil = null;
        }
    }

    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int    v  = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
