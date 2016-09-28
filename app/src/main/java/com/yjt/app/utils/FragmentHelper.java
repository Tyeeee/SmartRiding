package com.yjt.app.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.google.common.collect.Maps;

import java.util.HashMap;

public class FragmentHelper {

    private FragmentManager mFragmentManager;
    private HashMap<String, OperationInfo> mItems = Maps.newHashMap();
    private OperationInfo mInfo;
    private int mResource;
    private int[] mAnimations = new int[2];

    public FragmentHelper(FragmentManager fragmentManager, int mResource) {
        this.mFragmentManager = fragmentManager;
        this.mResource = mResource;
        mItems.clear();
        mAnimations[0] = android.R.anim.fade_in;
        mAnimations[1] = android.R.anim.fade_out;
    }

    public void addItem(OperationInfo info) {
        mItems.put(info.getTag(), info);
    }

    public void getItem(OperationInfo info) {
        mItems.get(info.getTag());
    }

    public boolean isShowing(String tag) {
        return mInfo.mTag.equals(tag);
    }

    public Fragment show(String tag, boolean isAnimate) {
        return show(mItems.get(tag), isAnimate, null, 0);
    }

    public Fragment show(int id, boolean isAnimate) {
        return show(mItems.get(String.valueOf(id)), isAnimate, null, 0);
    }

    public Fragment show(int id, boolean isAnimate, Toolbar toolbar, int resId) {
        return show(mItems.get(String.valueOf(id)), isAnimate, toolbar, resId);
    }

    public Fragment show(String tag, Bundle args, boolean isAnimate) {
        OperationInfo info = mItems.get(tag);
        info.mBundle = args;
        return show(info, isAnimate, null, 0);
    }

    private Fragment show(OperationInfo info, boolean isAnimate, Toolbar toolbar, int resId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction().disallowAddToBackStack();
        if (isAnimate) {
            transaction.setCustomAnimations(mAnimations[0], mAnimations[1]);
        }
        if (mInfo != info) {
            if (mInfo != null && mInfo.mFragment != null) {
                transaction.hide(mInfo.mFragment);
                if (toolbar != null) {
                    toolbar.setTitle(resId);
                }
            }
            mInfo = info;
            if (mInfo != null) {
                if (mInfo.mFragment == null) {
                    mInfo.mFragment = Fragment.instantiate(mInfo.mContext, mInfo.mClass.getName(), mInfo.mBundle);
                    if (info.mBundle != null) {
                        mInfo.mFragment.setArguments(info.mBundle);
                    }
                    transaction.add(mResource, mInfo.mFragment, mInfo.mTag);
                } else {
                    transaction.show(mInfo.mFragment);
                }
                if (toolbar != null) {
                    toolbar.setTitle(resId);
                }
            }
        } else {
            //已经显示
        }
        transaction.commitAllowingStateLoss();
        if (mInfo == null) {
            return null;
        } else {
            return mInfo.mFragment;
        }
    }

    public static class OperationInfo {
        protected Context mContext;
        protected String mTag;
        protected Class<?> mClass;
        protected Bundle mBundle;
        protected Fragment mFragment;

        public OperationInfo(Context context, String tag, Class<?> cls) {
            this(context, tag, cls, null);
        }


        public OperationInfo(Context context, int viewId, Class<?> cls) {
            this(context, viewId, cls, null);
        }

        public OperationInfo(Context context, String tag, Class<?> cls, Bundle args) {
            this.mContext = context;
            this.mTag = tag;
            this.mClass = cls;
            this.mBundle = args;
        }

        public OperationInfo(Context context, int viewId, Class<?> cls, Bundle args) {
            this.mContext = context;
            this.mTag = String.valueOf(viewId);
            this.mClass = cls;
            this.mBundle = args;
        }


        public String getTag() {
            return mTag;
        }
    }
}
