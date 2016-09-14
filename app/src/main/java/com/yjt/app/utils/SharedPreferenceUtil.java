package com.yjt.app.utils;

import android.content.SharedPreferences;
import android.util.Base64;

import com.yjt.app.base.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public class SharedPreferenceUtil {

    private static SharedPreferenceUtil mSharedPreferenceUtil;
    private SharedPreferences mPreferences;

    private SharedPreferenceUtil() {
        // cannot be instantiated
    }

    public static synchronized SharedPreferenceUtil getInstance() {
        if (mSharedPreferenceUtil == null) {
            mSharedPreferenceUtil = new SharedPreferenceUtil();
        }
        return mSharedPreferenceUtil;
    }

    public static void releaseInstance() {
        if (mSharedPreferenceUtil != null) {
            mSharedPreferenceUtil = null;
        }
    }

    public void putLong(String fileName, int mode, String key, long value) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
        mPreferences.edit().putLong(key, value).apply();
    }

    public void putInt(String fileName, int mode, String key, int value) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
        mPreferences.edit().putInt(key, value).apply();
    }

    public void putString(String fileName, int mode, String key, String value) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
        mPreferences.edit().putString(key, value).apply();
    }

    public void putBoolean(String fileName, int mode, String key, boolean value) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
        mPreferences.edit().putBoolean(key, value).apply();
    }

    public long getLong(String fileName, int mode, String key, long defValue) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
        return mPreferences.getLong(key, defValue);
    }

    public int getInt(String fileName, int mode, String key, int defValue) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
        return mPreferences.getInt(key, defValue);
    }

    public String getString(String fileName, int mode, String key, String defValue) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
        return mPreferences.getString(key, defValue);
    }

    public boolean getBoolean(String fileName, int mode, String key, boolean defValue) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
        return mPreferences.getBoolean(key, defValue);
    }

    public void remove(String fileName, int mode, String key) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
        mPreferences.edit().remove(key).apply();
    }

    public void clear(String fileName, int mode) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
        mPreferences.edit().clear().apply();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getObject(String fileName, int mode, String key, String defValue) {
        if (mPreferences == null) {
            mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
        }
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

    public void putObject(String fileName, int mode, String key, List<?> value) {
        try {
            if (mPreferences == null) {
                mPreferences = BaseApplication.getInstance().getSharedPreferences(fileName, mode);
            }
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
