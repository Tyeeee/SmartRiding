package com.yjt.app.ui.listener.implement;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;

import com.yjt.app.constant.Constant;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.MessageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CustomScanCallback extends ScanCallback {

    private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();
    private Handler mHandler;
    private boolean isScan;

    public CustomScanCallback(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        BluetoothDevice device = result.getDevice();
        LogUtil.print("---->callbackType:" + callbackType);
        LogUtil.print("---->rssi:" + result.getRssi());
        LogUtil.print("---->nanos:" + result.getTimestampNanos());
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
