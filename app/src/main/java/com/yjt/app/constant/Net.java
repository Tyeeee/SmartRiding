package com.yjt.app.constant;

/**
 * 网络状态枚举
 *
 * @author yjt
 */
public enum Net {

    // wifi
    WIFI("wifi"),
    // 2g
    GG("2G"),
    // 3g
    GGG("3G"),
    // 4g
    GGGG("4G"),
    // TD-SCDMA
    TD_SCDMA("TD-SCDMA"),
    // WCDMA
    WCDMA("WCDMA"),
    // CDMA2000
    CDMA2000("CDMA2000");

    private String mContent;

    Net(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

}
