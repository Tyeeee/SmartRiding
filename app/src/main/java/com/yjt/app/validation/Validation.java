package com.yjt.app.validation;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * 校验模型
 *
 * @author yjt
 */
public class Validation {

    private EditText mEditText;
    private View mButton;
    private ValidationExecutor mExecutor;

    public Validation(EditText editText, View button,
                      ValidationExecutor validationExecutor) {
        this.mEditText = editText;
        this.mButton = button;
        this.mExecutor = validationExecutor;
    }

    public EditText getEditText() {
        return mEditText;
    }

    public Validation setEditText(EditText editText) {
        this.mEditText = editText;
        return this;
    }

    public View getButton() {
        return mButton;
    }

    public void setButton(View button) {
        this.mButton = button;
    }

    public ValidationExecutor getValidationExecutor() {
        return mExecutor;
    }

    public Validation setValidationExecutor(
            ValidationExecutor validationExecutor) {
        this.mExecutor = validationExecutor;
        return this;
    }

    public boolean isTextEmpty() {
        if (mEditText == null || TextUtils.isEmpty(mEditText.getText())) {
            mButton.setVisibility(View.GONE);
            return true;
        }
        mButton.setVisibility(View.VISIBLE);
        return false;
    }

}
