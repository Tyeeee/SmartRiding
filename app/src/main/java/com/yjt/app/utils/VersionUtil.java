package com.yjt.app.utils;

import android.content.Context;
import android.os.Build;

import com.yjt.app.constant.Regex;

import java.io.File;
import java.io.IOException;

/**
 * 版本升级工具
 *
 * @author yjt
 */
public class VersionUtil {

    private static VersionUtil mVersionUtil;

    private VersionUtil() {
        // cannot be instantiated
    }

    public static synchronized VersionUtil getInstance() {
        if (mVersionUtil == null) {
            mVersionUtil = new VersionUtil();
        }
        return mVersionUtil;
    }

    public static void releaseInstance() {
        if (mVersionUtil != null) {
            mVersionUtil = null;
        }
    }

    /**
     * 动态下载apk
     *
     * @param context  上下文
     * @param url      下载地址
     * @param filename 文件名称
     *
     * @return
     */
    public boolean retrieveApkFromNet(Context context,
                                      String url, String filename) {
        return NetworkUtil.getInstance().download(url, new File(filename));
    }

    /**
     * 获取权限
     *
     * @param permission 权限
     * @param path       路径
     */
    public boolean chmod(String permission, String path) {
        try {
            String command = (new StringBuilder(Regex.CHMOD.getRegext())).append(permission)
                    .append(Regex.SPACE.getRegext()).append(path).toString();
            Runtime runtime = Runtime.getRuntime();
            Process pr = runtime.exec(command);
            pr.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public boolean isKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
