package com.yjt.app.utils;

import android.Manifest.permission;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.exception.DeviceInfoException;

import java.util.UUID;

/**
 * 设备信息工具类
 *
 * @author yjt
 */
public class DeviceInfoUtil {

    private static volatile String uuid;

    private enum Ids {
        TELEPHONY_ID {
            @Override
            String getId(Context ctx) {
                // TODO : add a SIM based mechanism ? tm.getSimSerialNumber();
                final TelephonyManager tm = (TelephonyManager) ctx
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (tm == null) {
                    LogUtil.print("Telephony Manager not available");
                    return null;
                }
                assertPermission(ctx, permission.READ_PHONE_STATE);
                return tm.getDeviceId();
            }
        },
        ANDROID_ID {
            @Override
            String getId(Context ctx) throws DeviceInfoException {
                // no permission needed !
                final String androidId = Secure.getString(
                        ctx.getContentResolver(),
                        Secure.ANDROID_ID);
                if (BUGGY_ANDROID_ID.equals(androidId)) {
                    LogUtil.print("The device suffers from "
                                          + "the Android ID bug - its ID is the emulator ID : "
                                          + Ids.BUGGY_ANDROID_ID);
                    throw new DeviceInfoException();
                }
                return androidId;
            }
        },
        WIFI_MAC {
            @Override
            String getId(Context ctx) {
                WifiManager wm = (WifiManager) ctx
                        .getSystemService(Context.WIFI_SERVICE);
                if (wm == null) {
                    LogUtil.print("Wifi Manager not available");
                    return null;
                }
                assertPermission(ctx, permission.ACCESS_WIFI_STATE); // I guess
                // getMacAddress() has no java doc !!!
                return wm.getConnectionInfo().getMacAddress();
            }
        },
        BLUETOOTH_MAC {
            @Override
            String getId(Context ctx) {
                BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
                if (ba == null) {
                    LogUtil.print("Bluetooth Adapter not available");
                    return null;
                }
                assertPermission(ctx, permission.BLUETOOTH);
                return ba.getAddress();
            }
        };

        static final String BUGGY_ANDROID_ID = "9774d56d682e549c";

        abstract String getId(Context ctx) throws DeviceInfoException;
    }

    private DeviceInfoUtil() {
        // cannot be instantiated
    }

    private static void assertPermission(Context ctx, String perm) {
        final int checkPermission = ctx.getPackageManager().checkPermission(
                perm, ctx.getPackageName());
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Permission " + perm + " is required");
        }
    }

    // need lazy initialization to get a context
    public static String getDeviceIdentifier(boolean ignoreBuggyAndroidID) throws DeviceInfoException {
        String result = uuid;
        if (result == null) {
            synchronized (DeviceInfoUtil.class){
                result = uuid;
                if (result == null) {
                    for (Ids id : Ids.values()) {
                        try {
                            result = uuid = id.getId(BaseApplication.getInstance());
                        } catch (DeviceInfoException e) {
                            if (!ignoreBuggyAndroidID)
                                throw new DeviceInfoException(e);
                        }
                        if (result != null)
                            return result;
                    }
                    throw new DeviceInfoException();
                }
            }
        }
        return result;
    }

    public static String getDeviceId(boolean encrypt) {
        try {
            TelephonyManager manager = (TelephonyManager) BaseApplication.getInstance()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId  = manager.getDeviceId();
            String androidId = Settings.System.getString(BaseApplication.getInstance().getContentResolver(), Secure.ANDROID_ID);
            // String serialNum = (String) Class.forName("android.os.SystemProperties")
            // .getMethod("get", String.class)
            // .invoke(context, "ro.serialno");
            if (!TextUtils.isEmpty(deviceId)) {
                if (encrypt) {
                    return new UUID(androidId.hashCode(), deviceId.hashCode()).toString();
                } else {
                    return deviceId;
                }
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
