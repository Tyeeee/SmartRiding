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
import com.yjt.app.ui.listener.bluetooth.OnDataListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectedListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectingListener;
import com.yjt.app.ui.listener.bluetooth.OnMtuChangedListener;
import com.yjt.app.ui.listener.bluetooth.OnReadRemoteRssiListener;
import com.yjt.app.ui.listener.bluetooth.OnServicesDiscoveredListener;
import com.yjt.app.ui.listener.bluetooth.implement.CustomBluetoothGattCallback;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.ToastUtil;

import java.util.UUID;


public class BluetoothService extends Service {

    private BluetoothAdapter mAdapter;
    private BluetoothGatt    mGatt;

    private OnConnectingListener         mConnectingListener;
    private OnConnectedListener          mConnectedListener;
    private OnDisconnectingListener      mDisconnectingListener;
    private OnDisconnectedListener       mDisconnectedListener;
    private OnServicesDiscoveredListener mServicesDiscoveredListener;
    private OnReadRemoteRssiListener     mReadRemoteRssiListener;
    private OnDataListener               mDataListener;
    private OnMtuChangedListener         mMtuChangedListener;

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
        this.mReadRemoteRssiListener = listener;
    }

    public void setServicesDiscoveredListener(OnServicesDiscoveredListener listener) {
        this.mServicesDiscoveredListener = listener;
    }

    public void setOnDataListener(OnDataListener listener) {
        this.mDataListener = listener;
    }

    public void setOnMtuChangedListener(OnMtuChangedListener mtuChangedListener) {
        this.mMtuChangedListener = mtuChangedListener;
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
        BluetoothDevice device = mAdapter.getRemoteDevice(address);
        if (device != null) {
            mGatt = device.connectGatt(BaseApplication.getInstance()
                    , false
                    , new CustomBluetoothGattCallback(mConnectingListener
                            , mConnectedListener
                            , mDisconnectingListener
                            , mDisconnectedListener
                            , mServicesDiscoveredListener
                            , mReadRemoteRssiListener
                            , mDataListener
                            , mMtuChangedListener));
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

    private boolean isCharacteristicReadable(BluetoothGattCharacteristic characteristic) {
        return mAdapter != null && mGatt != null && characteristic != null && (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0;
    }

    private boolean isCharacteristicWritable(BluetoothGattCharacteristic characteristic) {
        return mAdapter != null && mGatt != null && characteristic != null && (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) != 0;
    }

    private boolean isCharacteristicNoResponseWritable(BluetoothGattCharacteristic characteristic) {
        return mAdapter != null && mGatt != null && characteristic != null && (characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) != 0;
    }

    private boolean isCharacteristicNotifyable(BluetoothGattCharacteristic characteristic) {
        return mAdapter != null && mGatt != null && characteristic != null && (characteristic.getProperties()
                & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }

    public void readDeviceName() {
        readCharacteristic(mGatt.getService(UUID.fromString(Constant.Bluetooth.GENERIC_ACCESS_SERVICE_UUID))
                                   .getCharacteristic(UUID.fromString(Constant.Bluetooth.DEVICE_NAME_CHARACTERISTIC_UUID)));
    }

    public void readDumpEnergy() {
        readCharacteristic(mGatt.getService(UUID.fromString(Constant.Bluetooth.BATTERY_SERVICE_UUID))
                                   .getCharacteristic(UUID.fromString(Constant.Bluetooth.BATTERY_CHARACTERISTIC_UUID)));
    }

    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) {
        return isCharacteristicReadable(characteristic) ? mGatt.readCharacteristic(characteristic) : false;
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] value) {
        if (isCharacteristicWritable(characteristic)) {
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            characteristic.setValue(value);
            return mGatt.writeCharacteristic(characteristic);
        } else {
            return false;
        }
    }

    public boolean writeCharacteristicWithNoRsp(BluetoothGattCharacteristic characteristic, byte[] value) {
        if (isCharacteristicNoResponseWritable(characteristic)) {
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            characteristic.setValue(value);
            return mGatt.writeCharacteristic(characteristic);
        } else {
            return false;
        }
    }

    public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enable) {
        if (!isCharacteristicNotifyable(characteristic) && !mGatt.setCharacteristicNotification(characteristic, enable)) {
            return false;
        }
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(Constant.Bluetooth.CLIENT_UUID));
        byte[]                  value      = (enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);

        if (descriptor == null || !descriptor.setValue(value)) {
            return false;
        }
        return mGatt.writeDescriptor(descriptor);
    }

    public BluetoothGattService getService(BluetoothGatt gatt, String serviceUUID) {
        return gatt != null ? gatt.getService(UUID.fromString(serviceUUID)) : null;
    }

    public BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String charactUUID) {
        return service != null ? service.getCharacteristic(UUID.fromString(charactUUID)) : null;
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
