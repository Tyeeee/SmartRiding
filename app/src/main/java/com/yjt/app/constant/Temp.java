package com.yjt.app.constant;

public enum Temp {

    POINT_TYPE("point_type"),
    POINT_CONTENT("point_content"),
    START_POINT("start_point"),
    PASS_POINT("pass_point"),
    END_POINT("end_point"),
    LOCATION_LONGITUDE("location_longitude"),
    LOCATION_LATITUDE("location_latitude"),
    START_LOCATION_LONGITUDE("start_location_longitude"),
    START_LOCATION_LATITUDE("start_location_latitude"),
    PASS_LOCATION_LONGITUDE("pass_location_longitude"),
    PASS_LOCATION_LATITUDE("pass_location_latitude"),
    END_LOCATION_LONGITUDE("end_location_longitude"),
    END_LOCATION_LATITUDE("end_location_latitude"),
    ROUTE_INFO("route_info");

    private String mContent;

    Temp(String content) {
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }

}
