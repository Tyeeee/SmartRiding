package com.yjt.app.ui.listener.bluetooth;


import android.bluetooth.BluetoothGatt;

public interface OnReadRemoteRssiListener {

    void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status);
}
