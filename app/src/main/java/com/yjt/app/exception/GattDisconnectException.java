package com.yjt.app.exception;

import com.yjt.app.BuildConfig;
import com.yjt.app.constant.Regex;

public class GattDisconnectException extends GattException {

    private final String mAddress;

    public GattDisconnectException(String macAddress, String subMessage) {
        super(subMessage);
        this.mAddress = macAddress;
    }

    public GattDisconnectException(String subMessage) {
        super(subMessage);
        this.mAddress = Regex.UNKNOWN.getRegext();
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