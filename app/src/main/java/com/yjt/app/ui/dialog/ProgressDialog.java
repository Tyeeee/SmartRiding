package com.yjt.app.ui.dialog;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseDialogFragment;
import com.yjt.app.ui.dialog.builder.ProgressDialogBuilder;
import com.yjt.app.ui.listener.OnDialogCancelListener;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.ViewUtil;

public class ProgressDialog extends BaseDialogFragment {

    @Override
    protected Builder build(Builder builder) {
        CharSequence title    = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_TITLE.getContent());
        CharSequence prompt   = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_PROMPT.getContent());
        View         view     = builder.getLayoutInflater().inflate(R.layout.view_progress, null);
        TextView     tvPrompt = ViewUtil.getInstance().findView(view, R.id.tvPrompt);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(prompt)) {
            tvPrompt.setText(prompt);
        }
        builder.setNegativeButton(null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (OnDialogCancelListener listener : getDialogListeners(OnDialogCancelListener.class)) {
                    listener.onCanceled(mRequestCode);
                }
                dismiss();
            }
        });
        builder.setView(view);
        return builder;
    }

    public static ProgressDialogBuilder createBuilder(FragmentManager fragmentManager) {
        return new ProgressDialogBuilder(fragmentManager, ProgressDialog.class);
    }
}
