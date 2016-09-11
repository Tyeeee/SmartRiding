package com.yjt.app.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;

import com.yjt.app.R;
import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseDialogBuilder;
import com.yjt.app.ui.base.BaseDialogFragment;
import com.yjt.app.ui.listener.OnDateDialogListener;
import com.yjt.app.utils.TypefaceUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DatePickerDialog extends BaseDialogFragment {

    private DatePicker dpDate;
    private Calendar   mCalendar;

    @Override
    protected Builder build(Builder builder) {
        CharSequence title    = getArguments().getCharSequence(Temp.DIALOG_TITLE.getContent());
        CharSequence positive = getArguments().getCharSequence(Temp.DIALOG_BUTTON_POSITIVE.getContent());
        CharSequence negative = getArguments().getCharSequence(Temp.DIALOG_BUTTON_NEGATIVE.getContent());
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(positive)) {
            builder.setPositiveButton(positive, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OnDateDialogListener listener : getDialogListeners(OnDateDialogListener.class)) {
                        listener.onPositiveButtonClicked(mRequestCode, getDate());
                    }
                    dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(negative)) {
            builder.setNegativeButton(positive, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OnDateDialogListener listener : getDialogListeners(OnDateDialogListener.class)) {
                        listener.onNegativeButtonClicked(mRequestCode, getDate());
                    }
                    dismiss();
                }
            });
        }
        dpDate = (DatePicker) builder.getLayoutInflater().inflate(R.layout.view_date, null);
        builder.setView(dpDate);
        mCalendar = Calendar.getInstance(TimeZone.getTimeZone(getArguments().getString(Temp.TIME_ZONE.getContent())));
        mCalendar.setTimeInMillis(getArguments().getLong(Temp.DATE.getContent(), System.currentTimeMillis()));
        dpDate.updateDate(mCalendar.get(Calendar.YEAR)
                , mCalendar.get(Calendar.MONTH)
                , mCalendar.get(Calendar.DAY_OF_MONTH));
        return builder;
    }

    public Date getDate() {
        mCalendar.set(Calendar.YEAR, dpDate.getYear());
        mCalendar.set(Calendar.MONTH, dpDate.getMonth());
        mCalendar.set(Calendar.DAY_OF_MONTH, dpDate.getDayOfMonth());
        return mCalendar.getTime();
    }

    public static class DateDialogBuilder extends BaseDialogBuilder<DateDialogBuilder> {

        Date   mDate     = new Date();
        String mTimeZone = null;

        private CharSequence mTitle;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;

        private boolean mShowDefaultButton = true;
        private boolean is24Hours;

        protected DateDialogBuilder(FragmentManager fragmentManager, Class<? extends DatePickerDialog> clazz) {
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

    public static DateDialogBuilder createBuilder(FragmentManager manager) {
        return new DateDialogBuilder(manager, DatePickerDialog.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TypefaceUtil.releaseInstance();
    }
}
