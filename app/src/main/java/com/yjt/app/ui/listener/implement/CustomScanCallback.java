package com.yjt.app.ui.listener.implement;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;

import com.yjt.app.constant.Constant;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CustomScanCallback extends ScanCallback {

    private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();
    private Handler mHandler;

    public CustomScanCallback(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        LogUtil.print("---->onScanResult:");
        BluetoothDevice device = result.getDevice();
        LogUtil.print("---->device:" + device.getAddress());
        LogUtil.print("---->device:" + device.getName());
        LogUtil.print("---->device:" + device.getType());
        LogUtil.print("---->device:" + device.getBondState());
        if (!mDevices.contains(device)) {
            mDevices.add(device);
            mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.GET_DEVICE_LIST_SUCCESS, mDevices));
            //TODO Adapter
        }
    }

    @Override
    public void onScanFailed(int errorCode) {
        super.onScanFailed(errorCode);
        mHandler.sendMessage(MessageUtil.getMessage(Constant.Bluetooth.GET_DEVICE_LIST_FAILED));
    }

    @Override
    public void onBatchScanResults(List<ScanResult> results) {
        super.onBatchScanResults(results);
    }
}
