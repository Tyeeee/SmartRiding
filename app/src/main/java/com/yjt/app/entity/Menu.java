package com.yjt.app.entity;

import com.yjt.app.ui.sticky.OnGroupListener;

public class Menu implements Comparable<Menu>, OnGroupListener {

    private int mIcon;
    private String mTitle;

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int mIcon) {
        this.mIcon = mIcon;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    public int compareTo(Menu menu) {
        return 0;
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
