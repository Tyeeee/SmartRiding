package com.yjt.app.utils;

import android.util.SparseArray;

import com.yjt.app.constant.Constant;
import com.yjt.app.ui.base.BaseFragment;
import com.yjt.app.ui.fragment.DeviceFragment;
import com.yjt.app.ui.fragment.HomeFragment;
import com.yjt.app.ui.fragment.MessageFragment;
import com.yjt.app.ui.fragment.SettingFragment;

/**
 * fragment处理
 *
 * @author yjt
 */
public class FragmentUtil {

    private SparseArray<BaseFragment> mFragmentCache = new SparseArray<BaseFragment>();

    private static FragmentUtil mFragmentUtil;

    private FragmentUtil() {
    }

    public synchronized static FragmentUtil getInstance() {
        if (mFragmentUtil == null) {
            mFragmentUtil = new FragmentUtil();
        }
        return mFragmentUtil;
    }

    public static void releaseInstance() {
        if (mFragmentUtil != null) {
            mFragmentUtil.clearCache();
            mFragmentUtil = null;
        }
    }

    public BaseFragment getFragmentCache(int type, boolean useCache) {
        BaseFragment fragment = null;
        if (useCache && (fragment = mFragmentCache.get(type)) != null) {
            return fragment;
        }
        switch (type) {
            case Constant.Page.HOME:
                fragment = new HomeFragment();
                break;
            case Constant.Page.DEVICE:
                fragment = new DeviceFragment();
                break;
            case Constant.Page.MESSAGE:
                fragment = new MessageFragment();
                break;
            case Constant.Page.SETTING:
                fragment = new SettingFragment();
                break;
            default:
                fragment = new HomeFragment();
                break;
        }
        if (useCache) {
            mFragmentCache.put(type, fragment);
        }
        return fragment;
    }

    public void removeFragment(int type) {
        mFragmentCache.remove(type);
    }

    public void getFragment(int type) {
        mFragmentCache.get(type);
    }

    public void clearCache() {
        mFragmentCache.clear();
    }

}
