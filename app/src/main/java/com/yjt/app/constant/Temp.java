package com.yjt.app.constant;

public enum Temp {

    POINT_TYPE("point_type"),
    POINT_CONTENT("point_content");

    private String mContent;

    Temp(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

}
