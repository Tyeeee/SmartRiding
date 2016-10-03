package com.yjt.app.ui.listener.bluetooth;

import android.bluetooth.BluetoothGatt;

public interface OnMtuChangedListener {

    void onMtuChanged(BluetoothGatt gatt, int mtu, int status);
}
