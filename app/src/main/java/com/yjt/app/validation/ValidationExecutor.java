package com.yjt.app.validation;

import android.content.Context;

import com.yjt.app.utils.ValidationUtil;

public abstract class ValidationExecutor extends ValidationUtil {

    public abstract boolean doValidate(Context context, String text);

}
