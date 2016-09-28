package com.yjt.app.exception;

import android.bluetooth.BluetoothGattCharacteristic;

import com.yjt.app.BuildConfig;
import com.yjt.app.constant.Constant;

import java.util.UUID;

public class GattWriteCharacteristicException extends GattException {

    private final BluetoothGattCharacteristic mCharacteristic;
    private final UUID mUuid;
    private final int mState;

    public GattWriteCharacteristicException(BluetoothGattCharacteristic characteristic, String subMessage) {
        this(characteristic, subMessage, Constant.Bluetooth.STATE_UNKNOWN);
    }

    public GattWriteCharacteristicException(BluetoothGattCharacteristic characteristic, String subMessage, int state) {
        super(subMessage);
        this.mCharacteristic = characteristic;
        this.mUuid = characteristic.getUuid();
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
