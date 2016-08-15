package com.yjt.app.utils;

import android.app.Activity;
import android.os.Process;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity任务栈管理工具
 *
 * @author yjt
 */
public class ActivityUtil {

    private ActivityUtil() {
        // cannot be instantiated
    }

    private static List<WeakReference<Activity>> allActList = new ArrayList<>();

    public static void add(Activity activity) {
        if (!allActList.contains(activity)) {
            allActList.add(new WeakReference<>(activity));
        }
    }

    public static void removeActivity() {
        for (int i = 0; i < allActList.size(); i++) {
            if (allActList.get(i).get() != null) {
                allActList.get(i).get().finish();
            }
        }
    }

    public static void remove() {
        for (int i = 0; i < allActList.size(); i++) {
            if (allActList.get(i).get() != null) {
                allActList.get(i).get().finish();
            }
        }
        allActList.clear();
        Process.killProcess(Process.myPid());
    }
}
