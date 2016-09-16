package com.yjt.app.ui.listener.bluetooth;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

public interface OnDataAvailableListener {

    void onCharacteristicRead(BluetoothGatt gatt,
                              BluetoothGattCharacteristic characteristic, int status);

    void onCharacteristicWrite(BluetoothGatt gatt,
                               BluetoothGattCharacteristic characteristic);
}
