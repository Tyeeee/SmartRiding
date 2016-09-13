package com.yjt.app.ui.dialog.builder;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannedString;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.base.BaseDialogBuilder;
import com.yjt.app.ui.dialog.PromptDialog;


public class PromptDialogBuilder extends BaseDialogBuilder<PromptDialogBuilder> {

    private CharSequence mTitle;
    private CharSequence mPrompt;
    private CharSequence mPositiveButtonText;
    private CharSequence mNegativeButtonText;
    private CharSequence mNeutralButtonText;

    public PromptDialogBuilder(FragmentManager fragmentManager, Class<? extends PromptDialog> clazz) {
        super(fragmentManager, clazz);
    }

    public PromptDialogBuilder setTitle(int titleResourceId) {
        mTitle = BaseApplication.getInstance().getString(titleResourceId);
        return this;
    }


    public PromptDialogBuilder setTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    public PromptDialogBuilder setPrompt(int messageResourceId) {
        mPrompt = BaseApplication.getInstance().getText(messageResourceId);
        return this;
    }

    public PromptDialogBuilder setPrompt(int resourceId, Object... formatArgs) {
        mPrompt = Html.fromHtml(String.format(Html.toHtml(new SpannedString(BaseApplication.getInstance().getText(resourceId))), formatArgs));
        return this;
    }

    public PromptDialogBuilder setPrompt(CharSequence message) {
        mPrompt = message;
        return this;
    }

    public PromptDialogBuilder setPositiveButtonText(int textResourceId) {
        mPositiveButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public PromptDialogBuilder setPositiveButtonText(CharSequence text) {
        mPositiveButtonText = text;
        return this;
    }

    public PromptDialogBuilder setNegativeButtonText(int textResourceId) {
        mNegativeButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public PromptDialogBuilder setNegativeButtonText(CharSequence text) {
        mNegativeButtonText = text;
        return this;
    }

    public PromptDialogBuilder setNeutralButtonText(int textResourceId) {
        mNeutralButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public PromptDialogBuilder setNeutralButtonText(CharSequence text) {
        mNeutralButtonText = text;
        return this;
    }

    @Override
    protected Bundle prepareArguments() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(Temp.DIALOG_TITLE.getContent(), mTitle);
        bundle.putCharSequence(Temp.DIALOG_PROMPT.getContent(), mPrompt);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_POSITIVE.getContent(), mPositiveButtonText);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_NEGATIVE.getContent(), mNegativeButtonText);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_NEUTRAL.getContent(), mNeutralButtonText);
        return bundle;
    }

    @Override
    protected PromptDialogBuilder self() {
        return this;
    }
}
