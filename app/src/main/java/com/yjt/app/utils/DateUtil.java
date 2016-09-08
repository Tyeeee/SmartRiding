package com.yjt.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                                                       Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }
}
