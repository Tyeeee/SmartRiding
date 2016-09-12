package com.yjt.app.ui.dialog.builder;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.ChoiceMode;
import com.yjt.app.constant.Temp;
import com.yjt.app.model.ParcelableSparseBooleanArray;
import com.yjt.app.ui.base.BaseDialogBuilder;
import com.yjt.app.ui.dialog.ListDialog;


public class ListDialogBuilder extends BaseDialogBuilder<ListDialogBuilder> {

    private CharSequence mTitle;
    private CharSequence mPositiveButtonText;
    private CharSequence mNegativeButtonText;
    private CharSequence[] mItems;
    private int[] mChoiceItems;
    @ChoiceMode
    private int mChoiceMode;

    public ListDialogBuilder(FragmentManager fragmentManager, Class<? extends ListDialog> clazz) {
        super(fragmentManager, clazz);
    }

    public ListDialogBuilder setTitle(CharSequence title) {
        this.mTitle = title;
        return this;
    }

    public ListDialogBuilder setTitle(int titleResID) {
        this.mTitle = BaseApplication.getInstance().getString(titleResID);
        return this;
    }

    public ListDialogBuilder setCheckedItems(int... positions) {
        this.mChoiceItems = positions;
        return this;
    }

    public ListDialogBuilder setSelectedItem(int position) {
        this.mChoiceItems = new int[]{position};
        return this;
    }

    public ListDialogBuilder setChoiceMode(@ChoiceMode int choiceMode) {
        this.mChoiceMode = choiceMode;
        return this;
    }

    public ListDialogBuilder setItems(CharSequence... items) {
        this.mItems = items;
        return this;
    }

    public ListDialogBuilder setItems(int itemsArrayResID) {
        this.mItems = BaseApplication.getInstance().getResources().getStringArray(itemsArrayResID);
        return this;
    }

    public ListDialogBuilder setPositiveButtonText(int textResourceId) {
        mPositiveButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public ListDialogBuilder setPositiveButtonText(CharSequence text) {
        mPositiveButtonText = text;
        return this;
    }

    public ListDialogBuilder setNegativeButtonText(int textResourceId) {
        mNegativeButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public ListDialogBuilder setNegativeButtonText(CharSequence text) {
        mNegativeButtonText = text;
        return this;
    }

    @Override
    public ListDialog show() {
        return (ListDialog) super.show();
    }

    @Override
    protected Bundle prepareArguments() {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(Temp.DIALOG_TITLE.getContent(), mTitle);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_POSITIVE.getContent(), mPositiveButtonText);
        bundle.putCharSequence(Temp.DIALOG_BUTTON_NEGATIVE.getContent(), mNegativeButtonText);
        bundle.putCharSequenceArray(Temp.DIALOG_CHOICE_ITEMS.getContent(), mItems);
        ParcelableSparseBooleanArray array = new ParcelableSparseBooleanArray();
        for (int index = 0; mChoiceItems != null && index < mChoiceItems.length; index++) {
            array.put(mChoiceItems[index], true);
        }
        bundle.putParcelable(Temp.DIALOG_CHOICE_ITEM.getContent(), array);
        bundle.putInt(Temp.DIALOG_CHOICE_MODE.getContent(), mChoiceMode);
        return bundle;
    }

    @Override
    protected ListDialogBuilder self() {
        return this;
    }
}
