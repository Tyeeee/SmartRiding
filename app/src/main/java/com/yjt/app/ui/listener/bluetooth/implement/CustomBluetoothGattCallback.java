package com.yjt.app.ui.listener.bluetooth.implement;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.listener.bluetooth.OnConnectListener;
import com.yjt.app.ui.listener.bluetooth.OnDataAvailableListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectListener;
import com.yjt.app.ui.listener.bluetooth.OnServiceDiscoverListener;
import com.yjt.app.utils.LogUtil;

public class CustomBluetoothGattCallback extends BluetoothGattCallback {

    private OnConnectListener         mConnectListener;
    private OnDisconnectListener      mDisconnectListener;
    private OnServiceDiscoverListener mDiscoverListener;
    private OnDataAvailableListener   mDataListener;

    public CustomBluetoothGattCallback(OnConnectListener connectListener
            , OnDisconnectListener disconnectListener
            , OnServiceDiscoverListener discoverListener
            , OnDataAvailableListener dataListener) {
        this.mConnectListener = connectListener;
        this.mDisconnectListener = disconnectListener;
        this.mDiscoverListener = discoverListener;
        this.mDataListener = dataListener;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                        int newState) {
        switch (newState) {
            case BluetoothProfile.STATE_CONNECTED:
                LogUtil.print("---->Connected to GATT server.");
                if (gatt != null) {
                    LogUtil.print("---->Attempting to start service discovery:" + gatt.discoverServices());
//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            gatt.readRemoteRssi();
//                        }
//                    }, Constant.Bluetooth.RSSI_DELAY, Constant.Bluetooth.RSSI_PERIOD);
                }
                BaseApplication.getInstance().sendBroadcast(
                        new Intent(Constant.Bluetooth.ACTION_CONNECT)
                                .putExtra(Temp.CONNECTION_STATUS.getContent(), Constant.Bluetooth.DEVICE_CONNECTED));
                if (mConnectListener != null) {
                    mConnectListener.onConnect(gatt);
                }
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                LogUtil.print("---->Disconnected from GATT server.");
                BaseApplication.getInstance().sendBroadcast(
                        new Intent(Constant.Bluetooth.ACTION_CONNECT)
                                .putExtra(Temp.CONNECTION_STATUS.getContent(), Constant.Bluetooth.DEVICE_DISCONNECTED));
                if (mDisconnectListener != null) {
                    mDisconnectListener.onDisconnect(gatt);
                }
                gatt.close();
                break;
            default:
                break;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS
                && mDiscoverListener != null) {
            mDiscoverListener.onServiceDiscover(gatt);
        } else {
            LogUtil.print("---->onServicesDiscovered received: " + status);
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic, int status) {
        if (mDataListener != null) {
            mDataListener.onCharacteristicRead(gatt, characteristic, status);
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        if (mDataListener != null) {
            mDataListener.onCharacteristicWrite(gatt, characteristic);
        }
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        LogUtil.print("---->rssi: " + rssi);
        LogUtil.print("---->status: " + status);
        BaseApplication.getInstance().sendBroadcast(new Intent(Constant.Bluetooth.ACTION_RSSI).putExtra(Temp.RSSI_STATUS.getContent(), rssi));
    }
}
