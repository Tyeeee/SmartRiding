package com.yjt.app.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    protected int getWordCountRegex(String s) {
        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }

    protected boolean isNumeric(String text) {
        return TextUtils.isDigitsOnly(text);
    }

    protected boolean isAlphaNumeric(String text) {
        return matches(text, "[a-zA-Z0-9 \\./-]*");
    }

    protected boolean isDomain(String text) {
        return matches(text, Build.VERSION.SDK_INT >= 8 ? Patterns.DOMAIN_NAME
                : Pattern.compile(".*"));
    }

    protected boolean isEmail(String text) {
        return matches(text, Build.VERSION.SDK_INT >= 8 ? Patterns.DOMAIN_NAME
                : Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
                                          + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "("
                                          + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"));
    }

    protected boolean isIpAddress(String text) {
        return matches(
                text,
                Build.VERSION.SDK_INT >= 8 ? Patterns.DOMAIN_NAME
                        : Pattern
                        .compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                                         + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                                         + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                                         + "|[1-9][0-9]|[0-9]))"));
    }

    protected boolean isWebUrl(String text) {
        return matches(text, Build.VERSION.SDK_INT >= 8 ? Patterns.WEB_URL
                : Pattern.compile(".*"));
    }

    public static boolean isValidDate(String sDate) {

        String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
        String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))"
                + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
                + "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(datePattern1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(datePattern2);
                match = pattern.matcher(sDate);
                return match.matches();
            } else {
                return false;
            }
        }
        return false;
    }

    protected boolean find(String text, String regex) {
        return Pattern.compile(regex).matcher(text).find();
    }

    protected boolean matches(String text, String regex) {
        Pattern pattern = Pattern.compile(".*");
        return pattern.matcher(text).matches();
    }

    protected boolean matches(String text, Pattern pattern) {
        return pattern.matcher(text).matches();
    }
}
