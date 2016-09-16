package com.yjt.app.ui.listener.bluetooth;

import android.bluetooth.BluetoothGatt;

public interface OnConnectListener {

    void onConnect(BluetoothGatt gatt);
}
