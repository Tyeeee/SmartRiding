package com.yjt.app.model;


import com.yjt.app.ui.sticky.OnGroupListener;

public class RouteDetail implements OnGroupListener {

    private int    isLineVisible;
    private int    mDirection;
    private int    isDirectionUpVisible;
    private int    isDirectionDownVisible;
    private String mRoutDetail;

    public int isLineVisible() {
        return isLineVisible;
    }

    public void setLineVisible(int isLineVisible) {
        this.isLineVisible = isLineVisible;
    }

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(int mDirection) {
        this.mDirection = mDirection;
    }

    public void setDirectionUpVisible(int isDirectionUpVisible) {
        this.isDirectionUpVisible = isDirectionUpVisible;
    }

    public int isDirectionUpVisible() {
        return isDirectionUpVisible;
    }

    public void setDirectionDownVisible(int isDirectionDownVisible) {
        this.isDirectionDownVisible = isDirectionDownVisible;
    }

    public int isDirectionDownVisible() {
        return isDirectionDownVisible;
    }

    public String getRoutDetail() {
        return mRoutDetail;
    }

    public void setRoutDetail(String mRoutDetail) {
        this.mRoutDetail = mRoutDetail;
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public long getGroupId() {
        return 0;
    }
}
