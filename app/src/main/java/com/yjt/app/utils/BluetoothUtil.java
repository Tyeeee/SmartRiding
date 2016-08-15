package com.yjt.app.utils;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by yjt on 2016/6/28.
 */

public class BluetoothUtil {

    private static BluetoothUtil mBluetoothUtil;

    private BluetoothUtil() {
        // cannot be instantiated
    }

    public static synchronized BluetoothUtil getInstance() {
        if (mBluetoothUtil == null) {
            mBluetoothUtil = new BluetoothUtil();
        }
        return mBluetoothUtil;
    }

    public void releaseInstance() {
        if (mBluetoothUtil != null) {
            mBluetoothUtil = null;
        }
    }

    public boolean isBluetoothSupported() {
        return BluetoothAdapter.getDefaultAdapter() != null ? true : false;
    }

    public boolean isBluetoothEnabled() {
        return BluetoothAdapter
                .getDefaultAdapter().isEnabled();
    }

    public boolean turnOnBluetooth() {
        return BluetoothAdapter
                .getDefaultAdapter().enable();
    }

    public boolean turnOffBluetooth() {
        return BluetoothAdapter
                .getDefaultAdapter().disable();
    }
}
