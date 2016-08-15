package com.yjt.app.utils;

import android.os.Bundle;
import android.os.Message;

/**
 * Message工具
 *
 * @author yjt
 */
public class MessageUtil {

    private MessageUtil() {
        // cannot be instantiated
    }

    public static Message getMessage(int state) {
        Message msg = Message.obtain();
        msg.what = state;
        return msg;
    }

    public static Message getMessage(int state, Bundle bundle) {
        Message msg = Message.obtain();
        msg.setData(bundle);
        msg.what = state;
        return msg;
    }

    public static Message getMessage(int state, Object obj) {
        Message msg = Message.obtain();
        msg.obj = obj;
        msg.what = state;
        return msg;
    }

    public static Message getMessage(int state, String param) {
        Message msg = Message.obtain();
        msg.what = state;
        msg.obj = param;
        return msg;
    }


    public static Message getReturnInfo(int state, Class<?> cls) {
        Message msg = Message.obtain();
        msg.what = state;
        msg.obj = cls;
        return msg;
    }

    public static Message getErrorMessage(int state, Exception e, String error) {
        Message msg = Message.obtain();
        msg.what = state;
//        if (e.getMessage() != null) {
//            msg.obj = e.getMessage();
//        } else {
        msg.obj = error;
//        }
        return msg;
    }
}
