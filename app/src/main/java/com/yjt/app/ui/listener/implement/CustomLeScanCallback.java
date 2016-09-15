package com.yjt.app.ui.listener.implement;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.text.TextUtils;

import com.yjt.app.constant.Constant;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MessageUtil;

import java.util.ArrayList;

public class CustomLeScanCallback implements BluetoothAdapter.LeScanCallback {

    private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();
    private Handler mHandler;
    private boolean isScan;

    public CustomLeScanCallback(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        LogUtil.print("---->rssi:" + rssi);
        LogUtil.print("---->address:" + device.getAddress());
        LogUtil.print("---->name:" + device.getName());
        LogUtil.print("---->type:" + device.getType());
        LogUtil.print("---->state:" + device.getBondState());
        if (!mDevices.contains(device) && !TextUtils.isEmpty(device.getName())) {
            mDevices.add(device);
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isScan) {
                    isScan = !isScan;
                    mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.GET_DEVICE_LIST_SUCCESS, mDevices));
                }
            }
        }, Constant.Bluetooth.SCAN_PERIOD);
    }
}


