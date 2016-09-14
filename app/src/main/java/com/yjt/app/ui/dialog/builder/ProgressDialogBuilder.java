package com.yjt.app.ui.dialog.builder;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannedString;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseDialogBuilder;
import com.yjt.app.ui.dialog.ProgressDialog;


public class ProgressDialogBuilder extends BaseDialogBuilder<ProgressDialogBuilder> {

    private CharSequence mTitle;
    private CharSequence mPrompt;

    public ProgressDialogBuilder(FragmentManager fragmentManager, Class<? extends ProgressDialog> clazz) {
        super(fragmentManager, clazz);
    }

    public ProgressDialogBuilder setTitle(int titleResourceId) {
        mTitle = BaseApplication.getInstance().getString(titleResourceId);
        return this;
    }


    public ProgressDialogBuilder setTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    public ProgressDialogBuilder setPrompt(int messageResourceId) {
        mPrompt = BaseApplication.getInstance().getText(messageResourceId);
        return this;
    }

    public ProgressDialogBuilder setPrompt(int resourceId, Object... formatArgs) {
        mPrompt = Html.fromHtml(String.format(Html.toHtml(new SpannedString(BaseApplication.getInstance().getText(resourceId))), formatArgs));
        return this;
    }

    public ProgressDialogBuilder setPrompt(CharSequence message) {
        mPrompt = message;
        return this;
    }

    @Override
    protected Bundle prepareArguments() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(Temp.DIALOG_TITLE.getContent(), mTitle);
        bundle.putCharSequence(Temp.DIALOG_PROMPT.getContent(), mPrompt);
        return bundle;
    }

    @Override
    protected ProgressDialogBuilder self() {
        return this;
    }
}
