package com.yjt.app.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.utils.BundleUtil;
import com.yjt.app.utils.ToastUtil;

public class BluetoothReceiver extends BroadcastReceiver {

    private static BluetoothReceiver mReceiver;

    private BluetoothReceiver() {
        // cannot be instantiated
    }

    public static synchronized BluetoothReceiver getInstance() {
        if (mReceiver == null) {
            mReceiver = new BluetoothReceiver();
        }
        return mReceiver;
    }

    public void registerReceiver(IntentFilter filter) {
        BaseApplication.getInstance().registerReceiver(this, filter);
    }

    public void unRegisterReceiver() {
        BaseApplication.getInstance().unregisterReceiver(this);
    }

    public static void releaseInstance() {
        if (mReceiver != null) {
            mReceiver = null;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Constant.Bluetooth.ACTION_CONNECT:
                switch (BundleUtil.getInstance().getIntData(intent, Temp.CONNECTION_STATUS.getContent())) {
                    case BluetoothProfile.STATE_CONNECTING:
                        ToastUtil.getInstance().showToast(context, R.string.bluetooth_status4_1, Toast.LENGTH_SHORT);
                        break;
                    case BluetoothProfile.STATE_CONNECTED:
                        ToastUtil.getInstance().showToast(context, R.string.bluetooth_status4, Toast.LENGTH_SHORT);
                        break;
                    case BluetoothProfile.STATE_DISCONNECTING:
                        ToastUtil.getInstance().showToast(context, R.string.bluetooth_status3_1, Toast.LENGTH_SHORT);
                        break;
                    case BluetoothProfile.STATE_DISCONNECTED:
                        ToastUtil.getInstance().showToast(context, R.string.bluetooth_status3, Toast.LENGTH_SHORT);
                        break;
                    default:
                        break;
                }
                break;
            case Constant.Bluetooth.ACTION_RSSI:
                ToastUtil.getInstance().showToast(context, BundleUtil.getInstance().getIntData(intent, Temp.RSSI_STATUS.getContent()), Toast.LENGTH_SHORT);
                break;
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                switch (BundleUtil.getInstance().getIntData(intent, BluetoothAdapter.EXTRA_STATE)) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        ToastUtil.getInstance().showToast(context, R.string.bluetooth_status2_1, Toast.LENGTH_SHORT);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        ToastUtil.getInstance().showToast(context, R.string.bluetooth_status2, Toast.LENGTH_SHORT);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        ToastUtil.getInstance().showToast(context, R.string.bluetooth_status1_1, Toast.LENGTH_SHORT);
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        ToastUtil.getInstance().showToast(context, R.string.bluetooth_status1, Toast.LENGTH_SHORT);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
