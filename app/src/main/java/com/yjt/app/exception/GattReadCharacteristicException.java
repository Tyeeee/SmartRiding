package com.yjt.app.exception;

import android.bluetooth.BluetoothGattCharacteristic;

import com.yjt.app.BuildConfig;

import java.util.UUID;

public class GattReadCharacteristicException extends GattException {

    private final BluetoothGattCharacteristic mCharacteristic;
    private final UUID mUuid;
    private final int mState;

    public GattReadCharacteristicException(BluetoothGattCharacteristic Characteristic, String subMessage, int state) {
        super(subMessage);
        this.mCharacteristic = Characteristic;
        this.mUuid = Characteristic.getUuid();
        this.mState = state;
    }

    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "---->uuid" + mUuid.toString()
                    + ",characteristic：" + mCharacteristic.getValue()
                    + ",message：" + getMessage()
                    + ",state：" + mState;
        } else {
            return null;
        }
    }
}
