package com.yjt.app.ui.listener;

public interface OnNumberDialogListener extends OnDialogNegativeListener {

    void onPositiveButtonClicked(int requestCode, int number);
}
