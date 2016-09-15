package com.yjt.app.ui.listener.implement;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.yjt.app.utils.LogUtil;

import java.util.ArrayList;

public class CustomLeScanCallback implements BluetoothAdapter.LeScanCallback {

    private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        LogUtil.print("---->onLeScan:");
        LogUtil.print("---->device:" + device.getAddress());
        LogUtil.print("---->device:" + device.getName());
        LogUtil.print("---->device:" + device.getType());
        LogUtil.print("---->device:" + device.getBondState());
        if (!mDevices.contains(device)) {
            mDevices.add(device);
            //TODO Adapter
        }
    }
}


