package com.yjt.app.constant;

public enum Temp {

    POINT_TYPE("point_type"),
    POINT_CONTENT("point_content"),
    START_POINT("start_point"),
    PASS_POINT("pass_point"),
    END_POINT("end_point"),
    LOCATION_LONGITUDE("location_longitude"),
    LOCATION_LATITUDE("location_latitude");

    private String mContent;

    Temp(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

}
