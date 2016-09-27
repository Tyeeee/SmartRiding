package com.yjt.app.ui.listener.bluetooth.implement;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.listener.bluetooth.OnConnectedListener;
import com.yjt.app.ui.listener.bluetooth.OnConnectingListener;
import com.yjt.app.ui.listener.bluetooth.OnDataAvailableListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectedListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectingListener;
import com.yjt.app.ui.listener.bluetooth.OnServiceDiscoverListener;
import com.yjt.app.utils.BluetoothUtil;
import com.yjt.app.utils.LogUtil;

public class CustomBluetoothGattCallback extends BluetoothGattCallback {

    private OnConnectingListener mConnectingListener;
    private OnConnectedListener mConnectedListener;
    private OnDisconnectingListener mDisconnectingListener;
    private OnDisconnectedListener mDisconnectedListener;
    private OnServiceDiscoverListener mDiscoverListener;
    private OnDataAvailableListener mDataListener;

    public CustomBluetoothGattCallback(OnConnectingListener connectingListener
            , OnConnectedListener connectedListener
            , OnDisconnectingListener disconnectingListener
            , OnDisconnectedListener disconnectedListener
            , OnServiceDiscoverListener discoverListener
            , OnDataAvailableListener dataListener) {
        this.mConnectingListener = connectingListener;
        this.mConnectedListener = connectedListener;
        this.mDisconnectingListener = disconnectingListener;
        this.mDisconnectedListener = disconnectedListener;
        this.mDiscoverListener = discoverListener;
        this.mDataListener = dataListener;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                        int newState) {
        LogUtil.print("---->onConnectionStateChange");
        switch (newState) {
            case BluetoothProfile.STATE_CONNECTING:
                LogUtil.print("---->Connecting to GATT server.");
                BaseApplication.getInstance().sendBroadcast(
                        new Intent(Constant.Bluetooth.ACTION_CONNECT)
                                .putExtra(Temp.CONNECTION_STATUS.getContent(), BluetoothProfile.STATE_CONNECTING));
                if (mConnectingListener != null) {
                    mConnectingListener.onConnecting(gatt);
                }
                break;
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
                                .putExtra(Temp.CONNECTION_STATUS.getContent(), BluetoothProfile.STATE_CONNECTED));
                if (mConnectedListener != null) {
                    mConnectedListener.onConnected(gatt);
                }
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                LogUtil.print("---->Disconnecting from GATT server.");
                BaseApplication.getInstance().sendBroadcast(
                        new Intent(Constant.Bluetooth.ACTION_CONNECT)
                                .putExtra(Temp.CONNECTION_STATUS.getContent(), BluetoothProfile.STATE_DISCONNECTING));
                if (mDisconnectingListener != null) {
                    mDisconnectingListener.onDisconnecting(gatt);
                }
                gatt.close();
                BluetoothUtil.getInstance().refreshCache(gatt);
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                LogUtil.print("---->Disconnected from GATT server.");
                BaseApplication.getInstance().sendBroadcast(
                        new Intent(Constant.Bluetooth.ACTION_CONNECT)
                                .putExtra(Temp.CONNECTION_STATUS.getContent(), BluetoothProfile.STATE_DISCONNECTED));
                if (mDisconnectedListener != null) {
                    mDisconnectedListener.onDisconnected(gatt);
                }
                gatt.close();
                BluetoothUtil.getInstance().refreshCache(gatt);
                break;
            default:
                break;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        LogUtil.print("---->onServicesDiscovered");
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
        LogUtil.print("---->onCharacteristicRead");
        if (mDataListener != null) {
            mDataListener.onCharacteristicRead(gatt, characteristic, status);
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        LogUtil.print("---->onCharacteristicChanged");
        if (mDataListener != null) {
            mDataListener.onCharacteristicWrite(gatt, characteristic);
        }
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        LogUtil.print("---->onReadRemoteRssi: " + rssi + "," + status);
        BaseApplication.getInstance().sendBroadcast(new Intent(Constant.Bluetooth.ACTION_RSSI).putExtra(Temp.RSSI_STATUS.getContent(), rssi));
    }
}
