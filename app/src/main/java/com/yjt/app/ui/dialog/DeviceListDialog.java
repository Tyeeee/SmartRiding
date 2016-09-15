package com.yjt.app.ui.dialog;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.constant.Temp;
import com.yjt.app.entity.ParcelableSparseBooleanArray;
import com.yjt.app.ui.dialog.builder.DeviceListDialogBuilder;
import com.yjt.app.ui.listener.OnDialogCancelListener;
import com.yjt.app.ui.listener.OnListDialogListener;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class DeviceListDialog extends ListDialog {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException(
                    "use ListDialogBuilder to construct this dialog");
        }
    }

    @Override
    protected Builder build(Builder builder) {
        CharSequence                     title    = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_TITLE.getContent());
        CharSequence                     positive = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_POSITIVE.getContent());
        CharSequence                     negative = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEGATIVE.getContent());
        final ArrayList<BluetoothDevice> items    = IntentDataUtil.getInstance().getParcelableArrayListData(getArguments(), Temp.DIALOG_ITEMS.getContent());
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(negative)) {
            builder.setNegativeButton(negative, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (OnDialogCancelListener listener : getDialogListeners(OnDialogCancelListener.class)) {
                        listener.onCanceled(mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        if (!TextUtils.isEmpty(positive)) {
            builder.setPositiveButton(positive, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int         selectedPosition = -1;
                    final int[] checkedPositions = asIntArray(getCheckedItems());
                    for (int i : checkedPositions) {
                        if (i >= 0 && i < items.size()) {
                            selectedPosition = i;
                            break;
                        }
                    }
                    if (selectedPosition != -1) {
                        for (OnListDialogListener listener : getDialogListeners(OnListDialogListener.class)) {
                            listener.onListItemSelected(items.get(selectedPosition), selectedPosition, mRequestCode);
                        }
                    } else {
                        for (OnDialogCancelListener listener : getDialogListeners(OnDialogCancelListener.class)) {
                            listener.onCanceled(mRequestCode);
                        }
                    }
                    dismiss();
                }
            });
        }

        if (items != null && items.size() > 0) {
            builder.setItems(new ArrayAdapter<BluetoothDevice>(getActivity(), R.layout.item_dialog_list, R.id.tvItem, items) {

                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_list, parent, false);
                    }
                    TextView textView = ViewUtil.getInstance().findView(convertView, R.id.tvItem);
                    if (textView != null) {
                        textView.setText(getItem(position).getName());
                    }
                    return convertView;
                }
            }, -1, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (OnListDialogListener listener : getDialogListeners(OnListDialogListener.class)) {
                        listener.onListItemSelected(items.get(position), position, mRequestCode);
                    }
                    dismiss();
                }
            });
        }
        return builder;
    }

    private static int[] asIntArray(SparseBooleanArray checkedItems) {
        int checked = 0;
        for (int i = 0; i < checkedItems.size(); i++) {
            int key = checkedItems.keyAt(i);
            if (checkedItems.get(key)) {
                ++checked;
            }
        }
        int[] array = new int[checked];
        for (int i = 0, j = 0; i < checkedItems.size(); i++) {
            int key = checkedItems.keyAt(i);
            if (checkedItems.get(key)) {
                array[j++] = key;
            }
        }
        Arrays.sort(array);
        return array;
    }

    @NonNull
    private ParcelableSparseBooleanArray getCheckedItems() {
        ParcelableSparseBooleanArray items = IntentDataUtil.getInstance().getParcelableData(getArguments(), Temp.DIALOG_CHOICE_ITEM.getContent());
        if (items == null) {
            items = new ParcelableSparseBooleanArray();
        }
        return items;
    }

    public static DeviceListDialogBuilder createBuilder(FragmentManager manager) {
        return new DeviceListDialogBuilder(manager, DeviceListDialog.class);
    }
}
