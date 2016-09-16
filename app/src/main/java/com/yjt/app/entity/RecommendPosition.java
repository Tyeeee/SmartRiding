package com.yjt.app.entity;

import com.yjt.app.ui.listener.sticky.OnGroupListener;

public class RecommendPosition implements Comparable<RecommendPosition>, OnGroupListener {


    private double mLongitude;
    private double mLatitue;
    private String mAddress;
    private String mCity;

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getLatitue() {
        return mLatitue;
    }

    public void setLatitue(double mLatitue) {
        this.mLatitue = mLatitue;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public long getGroupId() {
        return 0;
    }

    @Override
    public int compareTo(RecommendPosition o) {
        return 0;
    }
}
