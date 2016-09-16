package com.yjt.app.ui.listener.bluetooth;


import android.bluetooth.BluetoothGatt;

public interface OnDisconnectedListener {

    void onDisconnected(BluetoothGatt gatt);
}
