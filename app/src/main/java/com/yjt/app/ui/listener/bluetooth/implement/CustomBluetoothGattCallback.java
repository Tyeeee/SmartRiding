package com.yjt.app.ui.listener.bluetooth.implement;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.listener.bluetooth.OnConnectedListener;
import com.yjt.app.ui.listener.bluetooth.OnConnectingListener;
import com.yjt.app.ui.listener.bluetooth.OnDataListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectedListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectingListener;
import com.yjt.app.ui.listener.bluetooth.OnMtuChangedListener;
import com.yjt.app.ui.listener.bluetooth.OnReadRemoteRssiListener;
import com.yjt.app.ui.listener.bluetooth.OnServicesDiscoveredListener;
import com.yjt.app.utils.BluetoothUtil;
import com.yjt.app.utils.LogUtil;

public class CustomBluetoothGattCallback extends BluetoothGattCallback {

    private OnConnectingListener         mConnectingListener;
    private OnConnectedListener          mConnectedListener;
    private OnDisconnectingListener      mDisconnectingListener;
    private OnDisconnectedListener       mDisconnectedListener;
    private OnServicesDiscoveredListener mDiscoverListener;
    private OnReadRemoteRssiListener     mReadRemoteRssiListener;
    private OnDataListener               mDataListener;
    private OnMtuChangedListener         mMtuChangeListener;

    public CustomBluetoothGattCallback(OnConnectingListener connectingListener
            , OnConnectedListener connectedListener
            , OnDisconnectingListener disconnectingListener
            , OnDisconnectedListener disconnectedListener
            , OnServicesDiscoveredListener discoverListener
            , OnReadRemoteRssiListener readRemoteRssiListener
            , OnDataListener dataListener
            , OnMtuChangedListener mtuChangeListener) {
        this.mConnectingListener = connectingListener;
        this.mConnectedListener = connectedListener;
        this.mDisconnectingListener = disconnectingListener;
        this.mDisconnectedListener = disconnectedListener;
        this.mDiscoverListener = discoverListener;
        this.mReadRemoteRssiListener = readRemoteRssiListener;
        this.mDataListener = dataListener;
        this.mMtuChangeListener = mtuChangeListener;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                        int newState) {
        super.onConnectionStateChange(gatt, status, newState);
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
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                LogUtil.print("---->Disconnected from GATT server.");
                BaseApplication.getInstance().sendBroadcast(
                        new Intent(Constant.Bluetooth.ACTION_CONNECT)
                                .putExtra(Temp.CONNECTION_STATUS.getContent(), BluetoothProfile.STATE_DISCONNECTED));
                if (mDisconnectedListener != null) {
                    mDisconnectedListener.onDisconnected(gatt);
                }
//                gatt.close();
//                BluetoothUtil.getInstance().refreshCache(gatt);
                break;
            default:
                break;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        LogUtil.print("---->onServicesDiscovered");
        if (status == BluetoothGatt.GATT_SUCCESS && mDiscoverListener != null) {
            mDiscoverListener.onServicesDiscovered(gatt);
        } else {
            LogUtil.print("---->onServicesDiscovered received: " + status);
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        LogUtil.print("---->onCharacteristicRead");
        if (status == BluetoothGatt.GATT_SUCCESS && mDataListener != null) {
            mDataListener.onCharacteristicRead(gatt, characteristic, status);
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        if (mDataListener != null) {
            mDataListener.onCharacteristicWrite(gatt, characteristic);
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        LogUtil.print("---->onCharacteristicChanged");
        if (mDataListener != null) {
            mDataListener.onCharacteristicChanged(gatt, characteristic);
        }
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
        LogUtil.print("---->onDescriptorRead");
        if (mDataListener != null) {
            mDataListener.onDescriptorRead(gatt, descriptor, status);
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        LogUtil.print("---->onDescriptorWrite");
        if (mDataListener != null) {
            mDataListener.onDescriptorWrite(gatt, descriptor, status);
        }
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);
        LogUtil.print("---->onReliableWriteCompleted");
        if (mDataListener != null) {
            mDataListener.onReliableWriteCompleted(gatt, status);
        }
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
        LogUtil.print("---->onReadRemoteRssi: " + rssi + "," + status);
        if (mReadRemoteRssiListener != null) {
            mReadRemoteRssiListener.onReadRemoteRssi(gatt, rssi, status);
        }
        BaseApplication.getInstance().sendBroadcast(new Intent(Constant.Bluetooth.ACTION_RSSI).putExtra(Temp.RSSI_STATUS.getContent(), rssi));
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        super.onMtuChanged(gatt, mtu, status);
        LogUtil.print("---->onMtuChanged");
        if (mMtuChangeListener != null) {
            mMtuChangeListener.onMtuChanged(gatt, mtu, status);
        }
    }
}
