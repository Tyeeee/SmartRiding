package com.yjt.app.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.yjt.app.R;
import com.yjt.app.utils.ToastUtil;

public class BluetoothReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)) {
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
        }
    }
}
