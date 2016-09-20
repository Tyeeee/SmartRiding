package com.yjt.app.ui.base;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;


public abstract class BaseDialogBuilder<T extends BaseDialogBuilder<T>> {

    private String mTag = Constant.View.CUSTOM_DIALOG;
    private int mRequestCode = Constant.RequestCode.DIALOG;
    protected final FragmentManager mFragmentManager;
    protected final Class<? extends BaseDialogFragment> mClass;
    private Fragment mTargetFragment;
    private boolean isCancelable = true;
    private boolean isCancelableOnTouchOutside = true;
    private boolean isUseDarkTheme = false;
    private boolean isUseLightTheme = false;

    public BaseDialogBuilder(FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
        mFragmentManager = fragmentManager;
        mClass = clazz;
    }

    protected abstract T self();

    protected abstract Bundle prepareArguments();

    public T setCancelable(boolean cancelable) {
        isCancelable = cancelable;
        return self();
    }

    public T setCancelableOnTouchOutside(boolean cancelable) {
        isCancelableOnTouchOutside = cancelable;
        if (cancelable) {
            isCancelable = cancelable;
        }
        return self();
    }

    public T setTargetFragment(Fragment fragment, int requestCode) {
        mTargetFragment = fragment;
        mRequestCode = requestCode;
        return self();
    }

    public T setRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return self();
    }

    public T setTag(String tag) {
        mTag = tag;
        return self();
    }

    public T useDarkTheme() {
        isUseDarkTheme = true;
        return self();
    }

    public T useLightTheme() {
        isUseLightTheme = true;
        return self();
    }

    private BaseDialogFragment create() {
        final Bundle bundle = prepareArguments();
        final BaseDialogFragment fragment = (BaseDialogFragment) Fragment.instantiate(BaseApplication.getInstance(), mClass.getName(), bundle);
        bundle.putBoolean(Temp.CANCELABLE_ON_TOUCH_OUTSIDE.getContent(), isCancelableOnTouchOutside);
        bundle.putBoolean(Temp.USE_DARK_THEME.getContent(), isUseDarkTheme);
        bundle.putBoolean(Temp.USE_LIGHT_THEME.getContent(), isUseLightTheme);
        if (mTargetFragment != null) {
            fragment.setTargetFragment(mTargetFragment, mRequestCode);
        } else {
            bundle.putInt(Temp.REQUEST_CODE.getContent(), mRequestCode);
        }
        fragment.setCancelable(isCancelable);
        return fragment;
    }

    public DialogFragment show() {
        BaseDialogFragment fragment = create();
        fragment.show(mFragmentManager, mTag);
        return fragment;
    }

    public void dismiss() {
        create().dismiss();
    }

    public DialogFragment showAllowingStateLoss() {
        BaseDialogFragment fragment = create();
        fragment.showAllowingStateLoss(mFragmentManager, mTag);
        return fragment;
    }
}
