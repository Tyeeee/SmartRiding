package com.yjt.app.exception;

import com.yjt.app.BuildConfig;
import com.yjt.app.constant.Constant;

public class GattException extends Throwable {

    public static final String STATUS_RESULT_FAIL = "Check Gatt Service Available or Connection!";
    public static final String NONE_BT = "GATT / BLE Power or Permission is not available or disabled";

    public GattException() {
        super(Constant.Bluetooth.UNKNOWN);
    }

    public GattException(String detailMessage) {
        super(detailMessage);
    }


    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "---->message：" + getMessage();
        } else {
            return null;
        }
    }
}