package com.yjt.app.validation;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.yjt.app.R;

import java.util.ArrayList;

public class EditTextValidator {

    private ArrayList<Validation> mValidationModels;
    private View mButton;
    private Context mContext;

    public EditTextValidator(Context context) {
        init(context, null);
    }

    public EditTextValidator(Context context, View button) {
        init(context, button);
    }

    private void init(Context context, View button) {
        this.mContext = context;
        this.mButton = button;
        mValidationModels = new ArrayList<>();
    }

    public EditTextValidator setButton(View button) {
        this.mButton = button;
        return this;
    }

    public View getButton() {
        return mButton;
    }

    public EditTextValidator add(Validation validationModel) {
        mValidationModels.add(validationModel);
        return this;
    }

    public EditTextValidator execute() {
        for (final Validation validation : mValidationModels) {

            if (validation.getEditText() == null) {
                return this;
            }

            validation.getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    setEnabled();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    validation.isTextEmpty();
                }
            });
        }
        return this;
    }

    private void setEnabled() {
        for (final Validation validationModel : mValidationModels) {
            if (mButton != null) {
                if (validationModel.isTextEmpty()) {
                    changeButtonStatus((Button) mButton,
                                       R.drawable.frame_fillet_grey_unable,
                                       R.color.gray_979797, false);
                    return;
                } else {
                    if (!mButton.isEnabled()) {
                        changeButtonStatus((Button) mButton,
                                           R.drawable.selector_button1, android.R.color.white,
                                           true);
                    }
                }
            }
        }
    }

    public boolean validate() {
        for (Validation validationModel : mValidationModels) {
            if (validationModel.getValidationExecutor() == null
                    || validationModel.getEditText() == null) {
                return true;
            }
            if (!validationModel.getValidationExecutor().doValidate(mContext,
                                                                    validationModel.getEditText().getText().toString())) {
                return false;
            }
        }
        return true;
    }

    private void changeButtonStatus(Button btn, int backgroundId,
                                    int textColorId, boolean enable) {
        btn.setBackgroundDrawable(mContext.getResources().getDrawable(
                backgroundId));
        btn.setTextColor(mContext.getResources().getColor(textColorId));
        btn.setEnabled(enable);
    }
}
