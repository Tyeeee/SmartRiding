package com.yjt.app.ui.listener.bluetooth;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

public interface OnDataListener {

    void onCharacteristicRead(BluetoothGatt gatt,
                              BluetoothGattCharacteristic characteristic, int status);

    void onCharacteristicWrite(BluetoothGatt gatt,
                               BluetoothGattCharacteristic characteristic);

    void onCharacteristicChanged(BluetoothGatt gatt,
                                 BluetoothGattCharacteristic characteristic);

    void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status);

    void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status);

    void onReliableWriteCompleted(BluetoothGatt gatt, int status);
}
