package com.yjt.app.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.pm.PackageManager;
import android.os.Build;

import com.yjt.app.base.BaseApplication;

public class BluetoothUtil {

    private static BluetoothUtil mBluetoothUtil;

    private BluetoothUtil() {
        // cannot be instantiated
    }

    public static synchronized BluetoothUtil getInstance() {
        if (mBluetoothUtil == null) {
            mBluetoothUtil = new BluetoothUtil();
        }
        return mBluetoothUtil;
    }

    public static void releaseInstance() {
        if (mBluetoothUtil != null) {
            mBluetoothUtil = null;
        }
    }

    public boolean isBluetoothSupported() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    public boolean isBluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled()
                && BaseApplication.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public boolean turnOnBluetooth() {
        return BluetoothAdapter.getDefaultAdapter().enable();
    }

    public boolean turnOffBluetooth() {
        return BluetoothAdapter.getDefaultAdapter().disable();
    }

    public void stopScanner(BluetoothAdapter adapter, BluetoothLeScanner scanner, ScanCallback scanCallback, BluetoothAdapter.LeScanCallback leScanCallback) {
        if (scanner != null && scanCallback != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                scanner.stopScan(scanCallback);
            }
        } else {
            if (adapter != null && leScanCallback != null) {
                adapter.stopLeScan(leScanCallback);
            }
        }
    }
}
