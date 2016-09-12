package com.yjt.app.utils;

import com.yjt.app.constant.Regex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String getCurrentTime() {
        return getCurrentTime(new Date(System.currentTimeMillis()), Regex.DATE_FORMAT_ALL.getRegext());
    }

    public static String getCurrentTime(Date date, String regex) {
        return new SimpleDateFormat(regex, Locale.getDefault()).format(date);
    }
}
