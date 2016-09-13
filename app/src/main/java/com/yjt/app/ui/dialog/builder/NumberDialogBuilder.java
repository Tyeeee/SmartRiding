package com.yjt.app.ui.dialog.builder;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseDialogBuilder;
import com.yjt.app.ui.dialog.NumberDialog;


public class NumberDialogBuilder extends BaseDialogBuilder<NumberDialogBuilder> {

    private int mMinimumValue;
    private int mMaximumValue;

    private CharSequence mTitle;
    private CharSequence mPositiveButtonText;
    private CharSequence mNegativeButtonText;

    public NumberDialogBuilder(FragmentManager fragmentManager, Class<? extends NumberDialog> clazz) {
        super(fragmentManager, clazz);
    }

    public NumberDialogBuilder setTitle(int titleResourceId) {
        mTitle = BaseApplication.getInstance().getString(titleResourceId);
        return this;
    }


    public NumberDialogBuilder setTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    public NumberDialogBuilder setPositiveButtonText(int textResourceId) {
        mPositiveButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public NumberDialogBuilder setPositiveButtonText(CharSequence text) {
        mPositiveButtonText = text;
        return this;
    }

    public NumberDialogBuilder setNegativeButtonText(int textResourceId) {
        mNegativeButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public NumberDialogBuilder setNegativeButtonText(CharSequence text) {
        mNegativeButtonText = text;
        return this;
    }

    public NumberDialogBuilder setMinimumValue(int number) {
        mMinimumValue = number;
        return this;
    }

    public NumberDialogBuilder setMaximumValue(int number) {
        mMaximumValue = number;
        return this;
    }

    @Override
    protected Bundle prepareArguments() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(Temp.DIALOG_TITLE.getContent(), mTitle);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_POSITIVE.getContent(), mPositiveButtonText);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_NEGATIVE.getContent(), mNegativeButtonText);
        bundle.putInt(Temp.MINIMUM_NUMBER.getContent(), mMinimumValue);
        bundle.putInt(Temp.MAXIMUM_NUMBER.getContent(), mMaximumValue);
        return bundle;
    }

    @Override
    protected NumberDialogBuilder self() {
        return this;
    }
}
