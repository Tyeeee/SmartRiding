package com.yjt.app.exception;

import com.yjt.app.BuildConfig;

public class GattRssiException extends GattException {

    private final int mState;

    public GattRssiException(int state) {
        this.mState = state;
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "---->state" + mState;
        } else {
            return null;
        }
    }
}
