package com.yjt.app.ui.listener.bluetooth;


import android.bluetooth.BluetoothGatt;

public interface OnDisconnectingListener {

    void onDisconnecting(BluetoothGatt gatt);
}
