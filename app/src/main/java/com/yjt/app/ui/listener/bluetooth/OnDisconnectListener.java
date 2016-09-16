package com.yjt.app.ui.listener.bluetooth;


import android.bluetooth.BluetoothGatt;

public interface OnDisconnectListener {

    void onDisconnect(BluetoothGatt gatt);
}
