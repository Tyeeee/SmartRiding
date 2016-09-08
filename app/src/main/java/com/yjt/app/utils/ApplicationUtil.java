package com.yjt.app.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.yjt.app.base.BaseApplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

/**
 * App工具
 *
 * @author yjt
 */
public class ApplicationUtil {

    private static ApplicationUtil mApplicationUtil;

    private ApplicationUtil() {
        // cannot be instantiated
    }

    public static synchronized ApplicationUtil getInstance() {
        if (mApplicationUtil == null) {
            mApplicationUtil = new ApplicationUtil();
        }
        return mApplicationUtil;
    }

    public static void releaseInstance() {
        if (mApplicationUtil != null) {
            mApplicationUtil = null;
        }
    }

    public String getPackageName() {
        return BaseApplication.getInstance().getPackageName();
    }

    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = BaseApplication.getInstance().getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    public String getVersionName() {
        return getPackageInfo().versionName;
    }

    public Object getMetaData(String key) {
        try {
            return BaseApplication.getInstance().getPackageManager().getApplicationInfo(getPackageName()
                    , PackageManager.GET_META_DATA).metaData.get(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    public boolean getInstallStatus(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        for (PackageInfo info : BaseApplication.getInstance().getPackageManager()
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)) {
            if (info.packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    public boolean isPerceptible() {
        ActivityManager manager = (ActivityManager) BaseApplication.getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if (infos != null && infos.size() > 0) {
            for (RunningAppProcessInfo info : infos) {
                // LogUtil.print("info:" + info.processName + "-" +
                // info.importance);
//                if (info.processName.equals(ctx.getPackageName())
//                        && info.importance == RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
//                    return true;
//                }
                if (info.processName.equals(BaseApplication.getInstance().getPackageName())) {
                    if (info.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE || info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String getProcessName() {
        ActivityManager manager = (ActivityManager) BaseApplication.getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo appProcess : manager.getRunningAppProcesses()) {
            if (appProcess.pid == android.os.Process.myPid()) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public String getSha1() {
        try {
            PackageInfo info = BaseApplication.getInstance().getPackageManager().getPackageInfo(
                    BaseApplication.getInstance().getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            return hexString.toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
