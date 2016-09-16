package com.yjt.app.ui.listener.bluetooth;

import android.bluetooth.BluetoothGatt;

public interface OnConnectingListener {

    void onConnecting(BluetoothGatt gatt);
}
