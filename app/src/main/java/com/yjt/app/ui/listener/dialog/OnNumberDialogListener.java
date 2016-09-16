package com.yjt.app.ui.listener.dialog;

import com.yjt.app.ui.listener.dialog.OnDialogNegativeListener;

public interface OnNumberDialogListener extends OnDialogNegativeListener {

    void onPositiveButtonClicked(int requestCode, int number);
}
