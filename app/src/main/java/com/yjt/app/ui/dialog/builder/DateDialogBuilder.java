package com.yjt.app.ui.dialog.builder;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseDialogBuilder;
import com.yjt.app.ui.dialog.DateDialog;

import java.util.Date;
import java.util.TimeZone;


public class DateDialogBuilder extends BaseDialogBuilder<DateDialogBuilder> {

    private Date mDate = new Date();
    private String mTimeZone;

    private CharSequence mTitle;
    private CharSequence mPositiveButtonText;
    private CharSequence mNegativeButtonText;

    private boolean is24Hours;

    public DateDialogBuilder(FragmentManager fragmentManager, Class<? extends DateDialog> clazz) {
        super(fragmentManager, clazz);
        is24Hours = DateFormat.is24HourFormat(BaseApplication.getInstance());
    }

    public DateDialogBuilder setTitle(int titleResourceId) {
        mTitle = BaseApplication.getInstance().getString(titleResourceId);
        return this;
    }


    public DateDialogBuilder setTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    public DateDialogBuilder setPositiveButtonText(int textResourceId) {
        mPositiveButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public DateDialogBuilder setPositiveButtonText(CharSequence text) {
        mPositiveButtonText = text;
        return this;
    }

    public DateDialogBuilder setNegativeButtonText(int textResourceId) {
        mNegativeButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public DateDialogBuilder setNegativeButtonText(CharSequence text) {
        mNegativeButtonText = text;
        return this;
    }

    public DateDialogBuilder setDate(Date date) {
        mDate = date;
        return this;
    }

    public DateDialogBuilder setTimeZone(String zone) {
        mTimeZone = zone;
        return this;
    }

    public DateDialogBuilder set24hour(boolean state) {
        is24Hours = state;
        return this;
    }

    @Override
    protected Bundle prepareArguments() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(Temp.DIALOG_TITLE.getContent(), mTitle);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_POSITIVE.getContent(), mPositiveButtonText);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_NEGATIVE.getContent(), mNegativeButtonText);
        bundle.putLong(Temp.DATE.getContent(), mDate.getTime());
        bundle.putBoolean(Temp.TIME_FORMAT.getContent(), is24Hours);
        if (mTimeZone != null) {
            bundle.putString(Temp.TIME_ZONE.getContent(), mTimeZone);
        } else {
            bundle.putString(Temp.TIME_ZONE.getContent(), TimeZone.getDefault().getID());
        }
        return bundle;
    }

    @Override
    protected DateDialogBuilder self() {
        return this;
    }
}
