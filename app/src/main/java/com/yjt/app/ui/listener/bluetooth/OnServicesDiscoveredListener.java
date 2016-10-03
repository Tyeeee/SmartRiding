package com.yjt.app.ui.listener.bluetooth;


import android.bluetooth.BluetoothGatt;

public interface OnServicesDiscoveredListener {
    
    void onServicesDiscovered(BluetoothGatt gatt);
}
