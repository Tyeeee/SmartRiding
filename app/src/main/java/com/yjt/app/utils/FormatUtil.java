package com.yjt.app.utils;

import com.amap.api.navi.model.NaviLatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期操作工具类
 *
 * @author yjt
 */
public class FormatUtil {

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                                                       Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    public static NaviLatLng parseCoordinate(String text) {
        try {
            double latD = Double.parseDouble(text.split(",")[0]);
            double lonD = Double.parseDouble(text.split(",")[1]);
            return new NaviLatLng(latD, lonD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
