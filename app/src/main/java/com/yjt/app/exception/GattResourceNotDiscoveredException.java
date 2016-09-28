package com.yjt.app.exception;

import com.yjt.app.BuildConfig;

public class GattResourceNotDiscoveredException extends GattException {

    public GattResourceNotDiscoveredException(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return super.toString();
        } else {
            return null;
        }
    }
}
