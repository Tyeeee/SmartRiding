package com.yjt.app.ui.listener.bluetooth;

import android.bluetooth.BluetoothGatt;

public interface OnConnectedListener {

    void onConnected(BluetoothGatt gatt);
}
