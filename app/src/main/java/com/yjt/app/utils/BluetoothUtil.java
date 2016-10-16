package com.yjt.app.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.common.collect.Maps;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.ui.listener.bluetooth.implement.CustomLeScanCallback;
import com.yjt.app.ui.listener.bluetooth.implement.CustomScanCallback;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class BluetoothUtil {

    private static BluetoothUtil mBluetoothUtil;
    private final static Map<String, String>            SERVICES        = Maps.newHashMap();
    private final static Map<String, String>            CHARACTERISTICS = Maps.newHashMap();
    private final static SparseArray<String>            VALUE_FORMATS   = new SparseArray<>();
    private final static LinkedHashMap<Integer, String> PROPERTIES      = Maps.newLinkedHashMap();

    static {
        SERVICES.put("00001811-0000-1000-8000-00805F9B34FB", "Alert Notification Service");
        SERVICES.put("0000180F-0000-1000-8000-00805F9B34FB", "Battery Service");
        SERVICES.put("00001810-0000-1000-8000-00805F9B34FB", "Blood Pressure");
        SERVICES.put("00001805-0000-1000-8000-00805F9B34FB", "Current Time Service");
        SERVICES.put("00001818-0000-1000-8000-00805F9B34FB", "Cycling Power");
        SERVICES.put("00001816-0000-1000-8000-00805F9B34FB", "Cycling Speed and Cadence");
        SERVICES.put("0000180A-0000-1000-8000-00805F9B34FB", "Device Information");
        SERVICES.put("00001800-0000-1000-8000-00805F9B34FB", "Generic Access");
        SERVICES.put("00001801-0000-1000-8000-00805F9B34FB", "Generic Attribute");
        SERVICES.put("00001808-0000-1000-8000-00805F9B34FB", "Glucose");
        SERVICES.put("00001809-0000-1000-8000-00805F9B34FB", "Health Thermometer");
        SERVICES.put("0000180D-0000-1000-8000-00805F9B34FB", "Heart Rate");
        SERVICES.put("00001812-0000-1000-8000-00805F9B34FB", "Human Interface Device");
        SERVICES.put("00001802-0000-1000-8000-00805F9B34FB", "Immediate Alert");
        SERVICES.put("00001803-0000-1000-8000-00805F9B34FB", "Link Loss");
        SERVICES.put("00001819-0000-1000-8000-00805F9B34FB", "Location and Navigation");
        SERVICES.put("00001807-0000-1000-8000-00805F9B34FB", "Next DST Change Service");
        SERVICES.put("0000180E-0000-1000-8000-00805F9B34FB", "Phone Alert Status Service");
        SERVICES.put("00001806-0000-1000-8000-00805F9B34FB", "Reference Time Update Service");
        SERVICES.put("00001814-0000-1000-8000-00805F9B34FB", "Running Speed and Cadence");
        SERVICES.put("00001813-0000-1000-8000-00805F9B34FB", "Scan Parameters");
        SERVICES.put("00001804-0000-1000-8000-00805F9B34FB", "Tx Power");

        CHARACTERISTICS.put("00002A43-0000-1000-8000-00805F9B34FB", "Alert Category ID");
        CHARACTERISTICS.put("00002A42-0000-1000-8000-00805F9B34FB", "Alert Category ID Bit Mask");
        CHARACTERISTICS.put("00002A06-0000-1000-8000-00805F9B34FB", "Alert Level");
        CHARACTERISTICS.put("00002A44-0000-1000-8000-00805F9B34FB", "Alert Notification Control Point");
        CHARACTERISTICS.put("00002A3F-0000-1000-8000-00805F9B34FB", "Alert Status");
        CHARACTERISTICS.put("00002A01-0000-1000-8000-00805F9B34FB", "Appearance");
        CHARACTERISTICS.put("00002A19-0000-1000-8000-00805F9B34FB", "Battery Level");
        CHARACTERISTICS.put("00002A49-0000-1000-8000-00805F9B34FB", "Blood Pressure Feature");
        CHARACTERISTICS.put("00002A35-0000-1000-8000-00805F9B34FB", "Blood Pressure Measurement");
        CHARACTERISTICS.put("00002A38-0000-1000-8000-00805F9B34FB", "Body Sensor Location");
        CHARACTERISTICS.put("00002A22-0000-1000-8000-00805F9B34FB", "Boot Keyboard Input Report");
        CHARACTERISTICS.put("00002A32-0000-1000-8000-00805F9B34FB", "Boot Keyboard Output Report");
        CHARACTERISTICS.put("00002A33-0000-1000-8000-00805F9B34FB", "Boot Mouse Input Report");
        CHARACTERISTICS.put("00002A5C-0000-1000-8000-00805F9B34FB", "CSC Feature");
        CHARACTERISTICS.put("00002A5B-0000-1000-8000-00805F9B34FB", "CSC Measurement");
        CHARACTERISTICS.put("00002A2B-0000-1000-8000-00805F9B34FB", "Current Time");
        CHARACTERISTICS.put("00002A66-0000-1000-8000-00805F9B34FB", "Cycling Power Control Point");
        CHARACTERISTICS.put("00002A65-0000-1000-8000-00805F9B34FB", "Cycling Power Feature");
        CHARACTERISTICS.put("00002A63-0000-1000-8000-00805F9B34FB", "Cycling Power Measurement");
        CHARACTERISTICS.put("00002A64-0000-1000-8000-00805F9B34FB", "Cycling Power Vector");
        CHARACTERISTICS.put("00002A08-0000-1000-8000-00805F9B34FB", "Date Time");
        CHARACTERISTICS.put("00002A0A-0000-1000-8000-00805F9B34FB", "Day Date Time");
        CHARACTERISTICS.put("00002A09-0000-1000-8000-00805F9B34FB", "Day of Week");
        CHARACTERISTICS.put("00002A00-0000-1000-8000-00805F9B34FB", "Device Name");
        CHARACTERISTICS.put("00002A0D-0000-1000-8000-00805F9B34FB", "DST Offset");
        CHARACTERISTICS.put("00002A0C-0000-1000-8000-00805F9B34FB", "Exact Time 256");
        CHARACTERISTICS.put("00002A26-0000-1000-8000-00805F9B34FB", "Firmware Revision String");
        CHARACTERISTICS.put("00002A51-0000-1000-8000-00805F9B34FB", "Glucose Feature");
        CHARACTERISTICS.put("00002A18-0000-1000-8000-00805F9B34FB", "Glucose Measurement");
        CHARACTERISTICS.put("00002A34-0000-1000-8000-00805F9B34FB", "Glucose Measurement Context");
        CHARACTERISTICS.put("00002A27-0000-1000-8000-00805F9B34FB", "Hardware Revision String");
        CHARACTERISTICS.put("00002A39-0000-1000-8000-00805F9B34FB", "Heart Rate Control Point");
        CHARACTERISTICS.put("00002A37-0000-1000-8000-00805F9B34FB", "Heart Rate Measurement");
        CHARACTERISTICS.put("00002A4C-0000-1000-8000-00805F9B34FB", "HID Control Point");
        CHARACTERISTICS.put("00002A4A-0000-1000-8000-00805F9B34FB", "HID Information");
        CHARACTERISTICS.put("00002A2A-0000-1000-8000-00805F9B34FB", "IEEE 11073-20601 Regulatory Certification Data List");
        CHARACTERISTICS.put("00002A36-0000-1000-8000-00805F9B34FB", "Intermediate Cuff Pressure");
        CHARACTERISTICS.put("00002A1E-0000-1000-8000-00805F9B34FB", "Intermediate Temperature");
        CHARACTERISTICS.put("00002A6B-0000-1000-8000-00805F9B34FB", "LN Control Point");
        CHARACTERISTICS.put("00002A6A-0000-1000-8000-00805F9B34FB", "LN Feature");
        CHARACTERISTICS.put("00002A0F-0000-1000-8000-00805F9B34FB", "Local Time Information");
        CHARACTERISTICS.put("00002A67-0000-1000-8000-00805F9B34FB", "Location and Speed");
        CHARACTERISTICS.put("00002A29-0000-1000-8000-00805F9B34FB", "Manufacturer Name String");
        CHARACTERISTICS.put("00002A21-0000-1000-8000-00805F9B34FB", "Measurement Interval");
        CHARACTERISTICS.put("00002A24-0000-1000-8000-00805F9B34FB", "Model Number String");
        CHARACTERISTICS.put("00002A68-0000-1000-8000-00805F9B34FB", "Navigation");
        CHARACTERISTICS.put("00002A46-0000-1000-8000-00805F9B34FB", "New Alert");
        CHARACTERISTICS.put("00002A04-0000-1000-8000-00805F9B34FB", "Peripheral Preferred Connection Parameters");
        CHARACTERISTICS.put("00002A02-0000-1000-8000-00805F9B34FB", "Peripheral Privacy Flag");
        CHARACTERISTICS.put("00002A50-0000-1000-8000-00805F9B34FB", "PnP ID");
        CHARACTERISTICS.put("00002A69-0000-1000-8000-00805F9B34FB", "Position Quality");
        CHARACTERISTICS.put("00002A4E-0000-1000-8000-00805F9B34FB", "Protocol Mode");
        CHARACTERISTICS.put("00002A03-0000-1000-8000-00805F9B34FB", "Reconnection Address");
        CHARACTERISTICS.put("00002A52-0000-1000-8000-00805F9B34FB", "Record Access Control Point");
        CHARACTERISTICS.put("00002A14-0000-1000-8000-00805F9B34FB", "Reference Time Information");
        CHARACTERISTICS.put("00002A4D-0000-1000-8000-00805F9B34FB", "Report");
        CHARACTERISTICS.put("00002A4B-0000-1000-8000-00805F9B34FB", "Report Map");
        CHARACTERISTICS.put("00002A40-0000-1000-8000-00805F9B34FB", "Ringer Control Point");
        CHARACTERISTICS.put("00002A41-0000-1000-8000-00805F9B34FB", "Ringer Setting");
        CHARACTERISTICS.put("00002A54-0000-1000-8000-00805F9B34FB", "RSC Feature");
        CHARACTERISTICS.put("00002A53-0000-1000-8000-00805F9B34FB", "RSC Measurement");
        CHARACTERISTICS.put("00002A55-0000-1000-8000-00805F9B34FB", "SC Control Point");
        CHARACTERISTICS.put("00002A4F-0000-1000-8000-00805F9B34FB", "Scan Interval Window");
        CHARACTERISTICS.put("00002A31-0000-1000-8000-00805F9B34FB", "Scan Refresh");
        CHARACTERISTICS.put("00002A5D-0000-1000-8000-00805F9B34FB", "Sensor Location");
        CHARACTERISTICS.put("00002A25-0000-1000-8000-00805F9B34FB", "Serial Number String");
        CHARACTERISTICS.put("00002A05-0000-1000-8000-00805F9B34FB", "Service Changed");
        CHARACTERISTICS.put("00002A28-0000-1000-8000-00805F9B34FB", "Software Revision String");
        CHARACTERISTICS.put("00002A47-0000-1000-8000-00805F9B34FB", "Supported New Alert Category");
        CHARACTERISTICS.put("00002A48-0000-1000-8000-00805F9B34FB", "Supported Unread Alert Category");
        CHARACTERISTICS.put("00002A23-0000-1000-8000-00805F9B34FB", "System ID");
        CHARACTERISTICS.put("00002A1C-0000-1000-8000-00805F9B34FB", "Temperature Measurement");
        CHARACTERISTICS.put("00002A1D-0000-1000-8000-00805F9B34FB", "Temperature Type");
        CHARACTERISTICS.put("00002A12-0000-1000-8000-00805F9B34FB", "Time Accuracy");
        CHARACTERISTICS.put("00002A13-0000-1000-8000-00805F9B34FB", "Time Source");
        CHARACTERISTICS.put("00002A16-0000-1000-8000-00805F9B34FB", "Time Update Control Point");
        CHARACTERISTICS.put("00002A17-0000-1000-8000-00805F9B34FB", "Time Update State");
        CHARACTERISTICS.put("00002A11-0000-1000-8000-00805F9B34FB", "Time with DST");
        CHARACTERISTICS.put("00002A0E-0000-1000-8000-00805F9B34FB", "Time Zone");
        CHARACTERISTICS.put("00002A07-0000-1000-8000-00805F9B34FB", "Tx Power Level");
        CHARACTERISTICS.put("00002A45-0000-1000-8000-00805F9B34FB", "Unread Alert Status");

        VALUE_FORMATS.put(BluetoothGattCharacteristic.FORMAT_FLOAT, "32bit float");
        VALUE_FORMATS.put(BluetoothGattCharacteristic.FORMAT_SFLOAT, "16bit float");
        VALUE_FORMATS.put(BluetoothGattCharacteristic.FORMAT_SINT16, "16bit signed int");
        VALUE_FORMATS.put(BluetoothGattCharacteristic.FORMAT_SINT32, "32bit signed int");
        VALUE_FORMATS.put(BluetoothGattCharacteristic.FORMAT_SINT8, "8bit signed int");
        VALUE_FORMATS.put(BluetoothGattCharacteristic.FORMAT_UINT16, "16bit unsigned int");
        VALUE_FORMATS.put(BluetoothGattCharacteristic.FORMAT_UINT32, "32bit unsigned int");
        VALUE_FORMATS.put(BluetoothGattCharacteristic.FORMAT_UINT8, "8bit unsigned int");

        PROPERTIES.put(BluetoothGattCharacteristic.PROPERTY_BROADCAST, "BROADCAST \b");
        PROPERTIES.put(BluetoothGattCharacteristic.PROPERTY_READ, "READ \b");
        PROPERTIES.put(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE, "WRITE NO RESPONSE \b");
        PROPERTIES.put(BluetoothGattCharacteristic.PROPERTY_WRITE, "WRITE \b");
        PROPERTIES.put(BluetoothGattCharacteristic.PROPERTY_NOTIFY, "NOTIFY \b");
        PROPERTIES.put(BluetoothGattCharacteristic.PROPERTY_INDICATE, "INDICATE \b");
        PROPERTIES.put(BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE, "SIGNED WRITE \b");
        PROPERTIES.put(BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS, "EXTENDED PROPS \b");
    }

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

    public boolean refreshCache(BluetoothGatt gatt) {
        try {
            Method refresh = gatt.getClass().getMethod("refresh");
            if (refresh != null) {
                boolean result = (Boolean) refresh.invoke(gatt);
                LogUtil.print("refreshing result: " + result);
                return result;
            }
        } catch (Exception e) {
            LogUtil.print("An exception occured while refreshing device", e);
        }
        return false;
    }

    public void getGattInfo(BluetoothGatt gatt) {
        if (gatt != null) {
            for (BluetoothGattService service : gatt.getServices()) {
                LogUtil.print("---->service type:" + resolveServiceType(service.getType()));
                LogUtil.print("---->service info:" + resolveServiceName(service.getUuid().toString()));
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    LogUtil.print("------>characteristic name:" + resolveCharacteristicName(characteristic.getUuid().toString()));
                    LogUtil.print("------>characteristic property:" + getAvailableProperties(characteristic.getProperties()));
                    LogUtil.print("------>characteristic value type:" + resolveValueTypeDescription(characteristic.getProperties()));
                    LogUtil.print("------>characteristic value:" + Arrays.toString(characteristic.getValue()));
                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                        LogUtil.print("-------->descriptor uuid:" + descriptor.getUuid());
                        LogUtil.print("-------->descriptor value:" + Arrays.toString(descriptor.getValue()));
                    }
                }
            }
        }
    }

    public BluetoothGattCharacteristic findCharacteristic(BluetoothGatt gatt, UUID uuid) {
        if (gatt != null && uuid != null) {
            for (BluetoothGattService service : gatt.getServices()) {
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    if (characteristic.getUuid().equals(uuid)) {
                        LogUtil.print("---->characteristic name:" + resolveCharacteristicName(characteristic.getUuid().toString()));
                        LogUtil.print("---->characteristic property:" + getAvailableProperties(characteristic.getProperties()));
                        LogUtil.print("---->characteristic value type:" + resolveValueTypeDescription(characteristic.getProperties()));
                        LogUtil.print("---->characteristic value:" + Arrays.toString(characteristic.getValue()));
                        return characteristic;
                    }
                }
            }
        }
        return null;
    }

    public BluetoothGattDescriptor findDescriptor(BluetoothGattCharacteristic characteristic, UUID uuid) {
        if (characteristic != null && uuid != null) {
            for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                if (descriptor.getUuid().equals(uuid)) {
                    LogUtil.print("---->descriptor uuid:" + descriptor.getUuid());
                    LogUtil.print("---->descriptor value:" + Arrays.toString(descriptor.getValue()));
                    return descriptor;
                }
            }
        }
        return null;
    }

    public String resolveServiceName(String uuid) {
        if (!TextUtils.isEmpty(uuid)) {
            String res = SERVICES.get(uuid.toUpperCase(Locale.getDefault()));
            return TextUtils.isEmpty(res) ? uuid : res;
        } else {
            return null;
        }
    }

    public String resolveServiceType(int type) {
        switch (type) {
            case BluetoothGattService.SERVICE_TYPE_PRIMARY:
                return Constant.Bluetooth.SERVICE_TYPE_PRIMARY;
            case BluetoothGattService.SERVICE_TYPE_SECONDARY:
                return Constant.Bluetooth.SERVICE_TYPE_SECONDARY;
            default:
                return null;
        }
    }

    public String resolveCharacteristicName(String uuid) {
        if (!TextUtils.isEmpty(uuid)) {
            String res = CHARACTERISTICS.get(uuid.toUpperCase(Locale.getDefault()));
            return TextUtils.isEmpty(res) ? uuid : res;
        } else {
            return null;
        }
    }

    public String resolveValueTypeDescription(int properties) {
        for (int i = 0; i < VALUE_FORMATS.size(); i++) {
            int format = VALUE_FORMATS.keyAt(i);
            if ((format & properties) != 0) {
                return VALUE_FORMATS.get(format, Constant.Bluetooth.UNKNOWN);
            }
        }
        return Constant.Bluetooth.UNKNOWN;
    }

    public String getAvailableProperties(int properties) {
        StringBuilder builder = new StringBuilder();
//        builder.append(properties);
        for (int props : PROPERTIES.keySet()) {
            if (isPropertyAvailable(properties, props)) {
                builder.append(PROPERTIES.get(props));
            }
        }
        return builder.toString();
    }

    public boolean isPropertyAvailable(BluetoothGattCharacteristic characteristic, int prop) {
        return (characteristic.getProperties() & prop) != 0;
    }

    public boolean isPropertiesAvailable(BluetoothGattCharacteristic characteristic, int... props) {
        boolean isPropertiesAvailable = false;
        for (int prop : props) {
            isPropertiesAvailable = (characteristic.getProperties() & prop) != 0;
            if (isPropertiesAvailable) {
                return isPropertiesAvailable;
            }
        }
        return isPropertiesAvailable;
    }


    public boolean isPropertyAvailable(int properties, int prop) {
        return (properties & prop) != 0;
    }
}
