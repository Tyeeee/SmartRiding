package com.yjt.app.exception.handler;

import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.utils.ApplicationUtil;
import com.yjt.app.utils.DateUtil;
import com.yjt.app.utils.DeviceInfoUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.ToastUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 全局异常处理
 *
 * @author yjt
 */
public class CrashHandler implements UncaughtExceptionHandler {

    private static CrashHandler mCrashHandler;
    private UncaughtExceptionHandler mUncaughtExceptionHandler;

    private CrashHandler() {
        // cannot be instantiated
    }

    public static synchronized CrashHandler getInstance() {
        if (mCrashHandler == null) {
            mCrashHandler = new CrashHandler();
        }
        return mCrashHandler;
    }

    public static void releaseInstance() {
        if (mCrashHandler != null) {
            mCrashHandler = null;
        }
    }

    public void initialize() {
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleExcetion(ex) && mUncaughtExceptionHandler != null) {
            mUncaughtExceptionHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LogUtil.print(e.getMessage());
            }
            Process.killProcess(Process.myPid());
        }
    }

    private boolean handleExcetion(Throwable ex) {
        if (ex != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    ToastUtil.getInstance().showToast(BaseApplication.getInstance(), "程序出错啦...", Toast.LENGTH_LONG);
                    Looper.loop();
                }
            }).start();
            LogUtil.print(formatCrashMessage(ex));
//            FileUtil.getInstance().saveFile(formatCrashMessage(ex), true);
            return true;
        }
        return false;
    }

    private String formatCrashMessage(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        StringBuilder builder = new StringBuilder();
        builder.append("------start------");
        builder.append(System.getProperty("line.separator"));
        builder.append(DateUtil.getCurrentTime());
        builder.append(System.getProperty("line.separator"));
        builder.append("app_version:" + ApplicationUtil.getInstance().getVersionName());
        builder.append(System.getProperty("line.separator"));
        builder.append("app_code:" + ApplicationUtil.getInstance().getVersionCode());
        builder.append(System.getProperty("line.separator"));
        builder.append("system_version:" + Build.VERSION.RELEASE);
        builder.append(System.getProperty("line.separator"));
        builder.append("manu_facturer:" + Build.MANUFACTURER);
        builder.append(System.getProperty("line.separator"));
        builder.append("device_model:" + Build.MODEL);
        builder.append(System.getProperty("line.separator"));
        builder.append("device_mid:" + DeviceInfoUtil.getDeviceId(false));
        builder.append(System.getProperty("line.separator"));
        builder.append("exception_message:" + ex.toString());
        builder.append(System.getProperty("line.separator"));
        builder.append("dump_message:" + writer.toString());
        builder.append("------end------");
        builder.append(System.getProperty("line.separator"));
        printWriter.close();
        return builder.toString();
    }
}
