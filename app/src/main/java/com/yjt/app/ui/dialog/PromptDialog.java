package com.yjt.app.ui.dialog;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;

import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseDialogFragment;
import com.yjt.app.ui.dialog.builder.PromptDialogBuilder;
import com.yjt.app.ui.listener.OnDialogNegativeListener;
import com.yjt.app.ui.listener.OnDialogNeutralListener;
import com.yjt.app.ui.listener.OnDialogPositiveListener;
import com.yjt.app.utils.IntentDataUtil;

public class PromptDialog extends BaseDialogFragment {

    @Override
    protected Builder build(Builder builder) {
        CharSequence title = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_TITLE.getContent());
        CharSequence prompt = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_PROMPT.getContent());
        CharSequence positive = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_POSITIVE.getContent());
        CharSequence negative = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEGATIVE.getContent());
        CharSequence neutral = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEUTRAL.getContent());
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(prompt)) {
            builder.setMessage(prompt);
        }
        if (!TextUtils.isEmpty(positive)) {
            builder.setPositiveButton(positive, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OnDialogPositiveListener listener : getDialogListeners(OnDialogPositiveListener.class)) {
                        listener.onPositiveButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(negative)) {
            builder.setNegativeButton(negative, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (OnDialogNegativeListener listener : getDialogListeners(OnDialogNegativeListener.class)) {
                        listener.onNegativeButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(neutral)) {
            builder.setNeutralButton(neutral, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (OnDialogNeutralListener listener : getDialogListeners(OnDialogNeutralListener.class)) {
                        listener.onNeutralButtonClicked(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        return builder;
    }

    public static PromptDialogBuilder createBuilder(FragmentManager fragmentManager) {
        return new PromptDialogBuilder(fragmentManager, PromptDialog.class);
    }
}
