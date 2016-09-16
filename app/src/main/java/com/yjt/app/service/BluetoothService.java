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
import com.yjt.app.ui.listener.bluetooth.OnConnectListener;
import com.yjt.app.ui.listener.bluetooth.OnDataAvailableListener;
import com.yjt.app.ui.listener.bluetooth.OnDisconnectListener;
import com.yjt.app.ui.listener.bluetooth.OnReadRemoteRssiListener;
import com.yjt.app.ui.listener.bluetooth.OnServiceDiscoverListener;
import com.yjt.app.ui.listener.bluetooth.implement.CustomBluetoothGattCallback;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.ToastUtil;

import java.util.List;


public class BluetoothService extends Service {

    private BluetoothAdapter mAdapter;
    private BluetoothGatt    mGatt;
    //    private String           mAddress;

    private OnConnectListener         mConnectListener;
    private OnDisconnectListener      mDisconnectListener;
    private OnReadRemoteRssiListener  onRssiListener;
    private OnServiceDiscoverListener mDiscoverListener;
    private OnDataAvailableListener   mDataListener;

    public void setAdapter(BluetoothAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void setOnConnectListener(OnConnectListener listener) {
        this.mConnectListener = listener;
    }

    public void setOnDisconnectListener(OnDisconnectListener listener) {
        this.mDisconnectListener = listener;
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
                    , new CustomBluetoothGattCallback(mConnectListener
                            , mDisconnectListener
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

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
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
        if (enabled) {
            LogUtil.print("---->Enable Notification");
            mGatt.setCharacteristicNotification(characteristic, true);
        } else {
            LogUtil.print("---->Disable Notification");
            mGatt.setCharacteristicNotification(characteristic, false);
        }
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(Constant.Bluetooth.CLIENT_UUID);
        descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        mGatt.writeDescriptor(descriptor);
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        if (mGatt != null) {
            return mGatt.getServices();
        }
        return null;
    }
}
