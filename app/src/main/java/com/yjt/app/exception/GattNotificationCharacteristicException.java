package com.yjt.app.exception;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.yjt.app.BuildConfig;
import com.yjt.app.constant.Constant;

import java.util.UUID;

public class GattNotificationCharacteristicException extends GattException {

    private final UUID mUuid;
    private final BluetoothGattCharacteristic characteristic;
    private final BluetoothGattDescriptor mDescriptor;
    private final int mStatus;

    public GattNotificationCharacteristicException(BluetoothGattDescriptor descriptor, String subMessage) {
        this(descriptor, subMessage, Constant.Bluetooth.STATE_UNKNOWN);
    }

    public GattNotificationCharacteristicException(BluetoothGattDescriptor descriptor, String subMessage, int status) {
        super(subMessage);
        this.mDescriptor = descriptor;
        this.characteristic = descriptor.getCharacteristic();
        this.mUuid = characteristic.getUuid();
        this.mStatus = status;
    }


    @Override
    public String toString() {
        if (BuildConfig.DEBUG) {
            return "---->uuid" + mUuid.toString()
                    + ",descriptor：" + mDescriptor
                    + ",message：" + getMessage()
                    + ",status：" + mStatus;
        } else {
            return null;
        }
    }
}