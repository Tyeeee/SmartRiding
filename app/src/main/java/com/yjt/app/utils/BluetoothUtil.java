package com.yjt.app.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.pm.PackageManager;
import android.os.Build;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Regex;
import com.yjt.app.service.BluetoothService;
import com.yjt.app.ui.listener.bluetooth.implement.CustomLeScanCallback;
import com.yjt.app.ui.listener.bluetooth.implement.CustomScanCallback;

import java.util.ArrayList;
import java.util.List;

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

    public void stopScanner(BluetoothAdapter adapter, BluetoothLeScanner scanner, CustomScanCallback scanCallback, CustomLeScanCallback leScanCallback, boolean isCancel) {
        if (adapter != null && adapter.isEnabled()) {
            if (scanner != null && scanCallback != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    scanner.stopScan(scanCallback);
                    if (isCancel) {
                        scanCallback.cancelCallback();
                    }
                }
            } else {
                if (leScanCallback != null) {
                    adapter.stopLeScan(leScanCallback);
                    if (isCancel) {
                        leScanCallback.cancelCallback();
                    }
                }
            }
        }
    }

    public String getServiceType(int type) {
        switch (type) {
            case BluetoothGattService.SERVICE_TYPE_PRIMARY:
                return "SERVICE_TYPE_PRIMARY";
            case BluetoothGattService.SERVICE_TYPE_SECONDARY:
                return "SERVICE_TYPE_SECONDARY";
            default:
                return null;
        }
    }

    private String getGattCharacteristicPermission(int permission) {
        switch (permission) {
            case BluetoothGattDescriptor.PERMISSION_READ:
                return "READ";
            case BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED:
                return "READ_ENCRYPTED";
            case BluetoothGattDescriptor.PERMISSION_READ_ENCRYPTED_MITM:
                return "READ_ENCRYPTED_MITM";
            case BluetoothGattDescriptor.PERMISSION_WRITE:
                return "WRITE";
            case BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED:
                return "WRITE_ENCRYPTED";
            case BluetoothGattDescriptor.PERMISSION_WRITE_ENCRYPTED_MITM:
                return "WRITE_ENCRYPTED_MITM";
            case BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED:
                return "WRITE_SIGNED";
            case BluetoothGattDescriptor.PERMISSION_WRITE_SIGNED_MITM:
                return "WRITE_SIGNED_MITM";
            default:
                return "UNKNOWN";
        }

    }

    private String getGattCharacteristicProperty(int property) {
        switch (property) {
            case BluetoothGattCharacteristic.PROPERTY_BROADCAST:
                return "BROADCAST";
            case BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS:
                return "EXTENDED_PROPS";
            case BluetoothGattCharacteristic.PROPERTY_INDICATE:
                return "INDICATE";
            case BluetoothGattCharacteristic.PROPERTY_NOTIFY:
                return "NOTIFY";
            case BluetoothGattCharacteristic.PROPERTY_READ:
                return "READ";
            case BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE:
                return "WRITE_ENCRYPTED_MITM";
            case BluetoothGattCharacteristic.PROPERTY_WRITE:
                return "WRITE";
            case BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE:
                return "WRITE_NO_RESPONSE";
            default:
                return "UNKNOWN";
        }

    }

    public String getGattCharacteristicInfo(int value, boolean flag) {
        String result = Regex.NONE.getRegext();
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            int b = 1 << i;
            if ((value & b) > 0) {
                values.add(b);
            }
        }
        for (int i = 0; i < values.size(); i++) {
            if (flag) {
                result += getGattCharacteristicPermission(values.get(i)) + Regex.VERTICAL.getRegext();
            } else {
                result += getGattCharacteristicProperty(values.get(i)) + Regex.VERTICAL.getRegext();
            }
        }
        return result;
    }

    public void lightOperation(int state, BluetoothGattCharacteristic characteristic, BluetoothService service) {
        switch (state) {
            case Constant.Bluetooth.LIGHT_LEFT:
                characteristic.setValue(Constant.Bluetooth.DATA_LIGHT_LEFT);
                break;
            case Constant.Bluetooth.LIGHT_RIGHT:
                characteristic.setValue(Constant.Bluetooth.DATA_LIGHT_RIGHT);
                break;
            case Constant.Bluetooth.LIGHT_OPEN:
                characteristic.setValue(Constant.Bluetooth.DATA_LIGHT_OPEN);
                break;
            case Constant.Bluetooth.LIGHT_CLOSE:
                characteristic.setValue(Constant.Bluetooth.DATA_LIGHT_CLOSE);
                break;
            default:
                characteristic.setValue(Constant.Bluetooth.DATA_LIGHT_CLOSE);
                break;
        }
        service.writeCharacteristic(characteristic);
    }
}
