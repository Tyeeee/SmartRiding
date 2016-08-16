package com.yjt.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

/**
 * 文件持久化工具
 *
 * @author yjt
 */
public class SharedPreferenceUtil {

    private static SharedPreferences mPreferences;

    private SharedPreferenceUtil() {
        // cannot be instantiated
    }

    public static void init(Context context, String fileName, int mode) {
        if (mPreferences == null) {
            mPreferences = context.getSharedPreferences(fileName, mode);
        }
    }

    public static void releaseInstance() {
        if (mPreferences != null) {
            mPreferences = null;
        }
    }

    /**
     * 存储数据(Long)
     */
    public static void putLong(String key, long value) {
        mPreferences.edit().putLong(key, value).apply();
    }

    /**
     * 存储数据(Int)
     */
    public static void putInt(String key, int value) {
        mPreferences.edit().putInt(key, value).apply();
    }

    /**
     * 存储数据(String)
     */
    public static void putString(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    /**
     * 存储数据(boolean)
     */
    public static void putBoolean(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     * 取出数据(Long)
     */
    public static long getLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    /**
     * 取出数据(int)
     */
    public static int getInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    /**
     * 取出数据(String)
     */
    public static String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    /**
     * 取出数据(boolean)
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    /**
     * 移除指定数据
     */
    public static void remove(String key) {
        mPreferences.edit().remove(key).apply();
    }

    /**
     * 清空�?有数�?
     */
    public static void clear() {
        mPreferences.edit().clear().apply();
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, String>> getObject(String key,
                                                      String defValue) {
        byte[] data = Base64.decode(mPreferences.getString(key, defValue),
                                    Base64.DEFAULT);
        List<Map<String, String>> value = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            value = (List<Map<String, String>>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void putObject(String key, List<?> value) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            String base64Str = new String(Base64.encode(
                    byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            mPreferences.edit().putString(key, base64Str).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
