package com.yjt.app.ui.dialog.builder;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.yjt.app.base.BaseApplication;
import com.yjt.app.constant.Temp;
import com.yjt.app.entity.ParcelableSparseBooleanArray;
import com.yjt.app.ui.dialog.ListDialog;

import java.util.ArrayList;


public class DeviceListDialogBuilder extends ListDialogBuilder {

    private   CharSequence               mTitle;
    private   CharSequence               mPositiveButtonText;
    private   CharSequence               mNegativeButtonText;
    protected ArrayList<BluetoothDevice> mItems;
    private   int[]                      mChoiceItems;

    public DeviceListDialogBuilder(FragmentManager fragmentManager, Class<? extends ListDialog> clazz) {
        super(fragmentManager, clazz);
    }

    public DeviceListDialogBuilder setTitle(CharSequence title) {
        this.mTitle = title;
        return this;
    }

    public DeviceListDialogBuilder setTitle(int titleResID) {
        this.mTitle = BaseApplication.getInstance().getString(titleResID);
        return this;
    }

    public DeviceListDialogBuilder setCheckedItems(int... positions) {
        this.mChoiceItems = positions;
        return this;
    }

    public DeviceListDialogBuilder setSelectedItem(int position) {
        this.mChoiceItems = new int[]{position};
        return this;
    }


    public DeviceListDialogBuilder setItems(ArrayList<BluetoothDevice> devices) {
        this.mItems = devices;
        return this;
    }

    public DeviceListDialogBuilder setPositiveButtonText(int textResourceId) {
        mPositiveButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public DeviceListDialogBuilder setPositiveButtonText(CharSequence text) {
        mPositiveButtonText = text;
        return this;
    }

    public DeviceListDialogBuilder setNegativeButtonText(int textResourceId) {
        mNegativeButtonText = BaseApplication.getInstance().getString(textResourceId);
        return this;
    }

    public DeviceListDialogBuilder setNegativeButtonText(CharSequence text) {
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
        bundle.putParcelableArrayList(Temp.DIALOG_CHOICE_ITEMS.getContent(), mItems);
        ParcelableSparseBooleanArray array = new ParcelableSparseBooleanArray();
        for (int index = 0; mChoiceItems != null && index < mChoiceItems.length; index++) {
            array.put(mChoiceItems[index], true);
        }
        bundle.putParcelable(Temp.DIALOG_CHOICE_ITEM.getContent(), array);
        return bundle;
    }

    @Override
    protected DeviceListDialogBuilder self() {
        return this;
    }
}
