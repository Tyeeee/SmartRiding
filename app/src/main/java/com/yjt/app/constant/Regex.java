package com.yjt.app.constant;

public enum Regex {

    NONE(""),
    CHINESE_AREA_CODE("+86"),
    PLUS("+"),
    MINUS("-"),
    SPACE(" "),
    COMMA(","),
    POINT("."),
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),
    SLASH("/"),
    SUPERSCRIPT("#"),
    ENTER("[\r\n]+|[\n]+|[\r]+"),
    DATE_FORMAT_ALL("yyyy-MM-dd HH:mm:ss"),
    DATE_FORMAT1("yyyy-MM-dd"),
    CHMOD("chmod "),
    PERMISSION("777"),
    FILE_NAME("/DownloadFile.apk"),
    FILE_HEAD("file://"),
    FILE_TTF("fonts/%s.ttf"),
    FILE_TYPE("application/vnd.android.package-archive"),
    IMAGE_JPG(".jpg");

    private String mRegext;

    Regex(String regex) {
        this.mRegext = regex;
    }

    public String getRegext() {
        return mRegext;
    }

}
