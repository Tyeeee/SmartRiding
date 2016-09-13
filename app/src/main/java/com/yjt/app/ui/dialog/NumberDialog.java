package com.yjt.app.ui.dialog;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.NumberPicker;

import com.yjt.app.R;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseDialogFragment;
import com.yjt.app.ui.dialog.builder.NumberDialogBuilder;
import com.yjt.app.ui.listener.OnNumberDialogListener;
import com.yjt.app.utils.IntentDataUtil;


public class NumberDialog extends BaseDialogFragment {

    private NumberPicker npNumber;

    @Override
    protected Builder build(Builder builder) {
        CharSequence title = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_TITLE.getContent());
        CharSequence positive = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_POSITIVE.getContent());
        CharSequence negative = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEGATIVE.getContent());
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(positive)) {
            builder.setPositiveButton(positive, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OnNumberDialogListener listener : getDialogListeners(OnNumberDialogListener.class)) {
                        listener.onPositiveButtonClicked(mRequestCode, npNumber.getValue());
                    }
                    dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(negative)) {
            builder.setNegativeButton(negative, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OnNumberDialogListener listener : getDialogListeners(OnNumberDialogListener.class)) {
                        listener.onNegativeButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        npNumber = (NumberPicker) builder.getLayoutInflater().inflate(R.layout.view_number, null);
        builder.setView(npNumber);
        npNumber.setMinValue(IntentDataUtil.getInstance().getIntData(getArguments(), Temp.MINIMUM_NUMBER.getContent()));
        npNumber.setMaxValue(IntentDataUtil.getInstance().getIntData(getArguments(), Temp.MAXIMUM_NUMBER.getContent()));
        return builder;
    }

    public static NumberDialogBuilder createBuilder(FragmentManager manager) {
        return new NumberDialogBuilder(manager, NumberDialog.class);
    }
}
