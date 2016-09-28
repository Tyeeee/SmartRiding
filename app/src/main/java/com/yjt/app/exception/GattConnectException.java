package com.yjt.app.exception;

import com.yjt.app.BuildConfig;
import com.yjt.app.constant.Constant;

/**
 * Created by Kang Young Won on 2016-05-12.
 */
public class GattConnectException extends GattException {

    private final String mAddress;

    public GattConnectException(String address, String subMessage) {
        super(subMessage);
        this.mAddress = address;
    }

    public GattConnectException(String subMessage) {
        this(Constant.Bluetooth.UNKNOWN, subMessage);
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "---->address" + mAddress + ",message：" + getMessage();
        } else {
            return null;
        }
    }
}