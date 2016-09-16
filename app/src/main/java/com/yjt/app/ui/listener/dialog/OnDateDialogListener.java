package com.yjt.app.ui.listener.dialog;

import java.util.Date;

public interface OnDateDialogListener extends OnDialogNegativeListener {

    void onPositiveButtonClicked(int requestCode, Date date);

}
