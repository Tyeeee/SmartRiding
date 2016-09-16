package com.yjt.app.ui.listener.bluetooth;


import android.bluetooth.BluetoothGatt;

public interface OnServiceDiscoverListener {
    
    void onServiceDiscover(BluetoothGatt gatt);
}
