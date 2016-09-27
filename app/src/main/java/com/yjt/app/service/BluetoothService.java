package com.yjt.app.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.ui.listener.bluetooth.OnConnectedListener;
import com.yjt.app.ui.listener.bluetooth.OnConnectingListener;
import com.yjt.app.ui.listener.bluetooth.OnDataAvailableListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectedListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectingListener;
import com.yjt.app.ui.listener.bluetooth.OnReadRemoteRssiListener;
import com.yjt.app.ui.listener.bluetooth.OnServiceDiscoverListener;
import com.yjt.app.ui.listener.bluetooth.implement.CustomBluetoothGattCallback;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.ToastUtil;

import java.util.List;
import java.util.UUID;


public class BluetoothService extends Service {

    private BluetoothAdapter mAdapter;
    private BluetoothGatt mGatt;
    //    private String           mAddress;

    private OnConnectingListener mConnectingListener;
    private OnConnectedListener mConnectedListener;
    private OnDisconnectingListener mDisconnectingListener;
    private OnDisconnectedListener mDisconnectedListener;
    private OnReadRemoteRssiListener onRssiListener;
    private OnServiceDiscoverListener mDiscoverListener;
    private OnDataAvailableListener mDataListener;

    public void setAdapter(BluetoothAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void setOnConnectingListener(OnConnectingListener listener) {
        this.mConnectingListener = listener;
    }

    public void setOnConnectedListener(OnConnectedListener listener) {
        this.mConnectedListener = listener;
    }

    public void setOnDisconnectingListener(OnDisconnectingListener listener) {
        this.mDisconnectingListener = listener;
    }

    public void setOnDisconnectedListener(OnDisconnectedListener listener) {
        this.mDisconnectedListener = listener;
    }

    public void setOnReadRemoteRssiListener(OnReadRemoteRssiListener listener) {
        this.onRssiListener = listener;
    }

    public void setOnServiceDiscoverListener(OnServiceDiscoverListener listener) {
        this.mDiscoverListener = listener;
    }

    public void setOnDataAvailableListener(OnDataAvailableListener listener) {
        this.mDataListener = listener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean connect(String address) {
        if (mAdapter == null || TextUtils.isEmpty(address)) {
            return false;
        }
//        if (!TextUtils.isEmpty(mAddress) && TextUtils.equals(address, mAddress) && mGatt != null) {
//            LogUtil.print("---->Trying to use an existing mBluetoothGatt for connection.");
//            return mGatt.connect();
//        } else {
        BluetoothDevice device = mAdapter.getRemoteDevice(address);
        if (device != null) {
            mGatt = device.connectGatt(BaseApplication.getInstance()
                    , false
                    , new CustomBluetoothGattCallback(mConnectingListener
                            , mConnectedListener
                            , mDisconnectingListener
                            , mDisconnectedListener
                            , mDiscoverListener
                            , mDataListener));
//            mAddress = address;
            return true;
        } else {
            ToastUtil.getInstance().showToast(BaseApplication.getInstance(), BaseApplication.getInstance().getString(R.string.bluetooth_status6), Toast.LENGTH_SHORT);
            return false;
        }
//        }
    }

    public void disconnect() {
        if (mAdapter != null && mGatt != null) {
            mGatt.disconnect();
        } else {
            LogUtil.print("---->BluetoothAdapter not initialized");
        }
    }

    public void readDumpEnergy() {
        readCharacteristic(mGatt.getService(Constant.Bluetooth.BATTERY_SERVICE_UUID).getCharacteristic(Constant.Bluetooth.BATTERY_CHARACTERISTIC_UUID));
    }

    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mAdapter != null && mGatt != null) {
            mGatt.readCharacteristic(characteristic);
        } else {
            LogUtil.print("---->BluetoothAdapter not initialized");
        }
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mAdapter != null && mGatt != null) {
            mGatt.writeCharacteristic(characteristic);
        } else {
            LogUtil.print("---->BluetoothAdapter not initialized");
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mAdapter == null || mGatt == null) {
            LogUtil.print("---->BluetoothAdapter not initialized");
            return;
        }
        mGatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(characteristic.getUuid());
        if (descriptor != null) {
            if (enabled) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            } else {
                descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            }
            mGatt.writeDescriptor(descriptor);
        }
    }

    public BluetoothGattService getService(BluetoothGatt gatt, String serviceUUID) {
        if (gatt != null) {
            return gatt.getService(UUID.fromString(serviceUUID));
        }
        return null;
    }

    public BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String charactUUID) {
        if (service != null) {
            return service.getCharacteristic(UUID.fromString(charactUUID));
        }
        return null;
    }

    public BluetoothGattCharacteristic getCharacteristic(BluetoothGatt gatt, String serviceUUID, String charactUUID) {
        if (gatt != null) {
            BluetoothGattService service = getService(gatt, serviceUUID);
            if (service != null) {
                return getCharacteristic(service, charactUUID);
            }
        }
        return null;
    }
}
