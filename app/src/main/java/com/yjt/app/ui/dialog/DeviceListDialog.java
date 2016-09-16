package com.yjt.app.ui.dialog;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.dialog.builder.DeviceListDialogBuilder;
import com.yjt.app.ui.listener.dialog.OnDialogCancelListener;
import com.yjt.app.ui.listener.dialog.OnListDialogListener;
import com.yjt.app.utils.BundleUtil;
import com.yjt.app.utils.ViewUtil;

import java.util.ArrayList;

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
        CharSequence                     title    = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_TITLE.getContent());
        CharSequence                     negative = BundleUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEGATIVE.getContent());
        final ArrayList<BluetoothDevice> items    = BundleUtil.getInstance().getParcelableArrayListData(getArguments(), Temp.DIALOG_ITEMS.getContent());
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
            }, Constant.View.RESOURCE_DEFAULT, new AdapterView.OnItemClickListener() {
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

    public static DeviceListDialogBuilder createBuilder(FragmentManager manager) {
        return new DeviceListDialogBuilder(manager, DeviceListDialog.class);
    }
}
