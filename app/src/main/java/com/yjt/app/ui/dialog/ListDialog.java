package com.yjt.app.ui.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.constant.ChoiceMode;
import com.yjt.app.constant.Temp;
import com.yjt.app.model.ParcelableSparseBooleanArray;
import com.yjt.app.ui.base.BaseDialogFragment;
import com.yjt.app.ui.dialog.builder.ListDialogBuilder;
import com.yjt.app.ui.listener.OnDialogCancelListener;
import com.yjt.app.ui.listener.OnListDialogListener;
import com.yjt.app.ui.listener.OnMultiChoiceListDialogListener;
import com.yjt.app.utils.IntentDataUtil;
import com.yjt.app.utils.ViewUtil;

import java.util.Arrays;

public class ListDialog extends BaseDialogFragment {

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
        CharSequence title = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_TITLE.getContent());
        CharSequence positive = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_POSITIVE.getContent());
        CharSequence negative = IntentDataUtil.getInstance().getCharSequenceData(getArguments(), Temp.DIALOG_BUTTON_NEGATIVE.getContent());
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

        if (getChoiceMode() != AbsListView.CHOICE_MODE_NONE) {
            View.OnClickListener listener = null;
            switch (getChoiceMode()) {
                case AbsListView.CHOICE_MODE_MULTIPLE:
                    listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int[] checkedPositions = asIntArray(getCheckedItems());
                            final CharSequence[] items = getItems();
                            final CharSequence[] checkedItems = new CharSequence[checkedPositions.length];
                            int i = 0;
                            for (int checkedPosition : checkedPositions) {
                                if (checkedPosition >= 0 && checkedPosition < items.length) {
                                    checkedItems[i++] = items[checkedPosition];
                                }
                            }
                            for (OnMultiChoiceListDialogListener listener : getDialogListeners(OnMultiChoiceListDialogListener.class)) {
                                listener.onMultiChoiceListItemsSelected(checkedItems, checkedPositions, mRequestCode);
                            }
                            dismiss();
                        }
                    };
                    break;
                case AbsListView.CHOICE_MODE_SINGLE:
                    listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int selectedPosition = -1;
                            final int[] checkedPositions = asIntArray(getCheckedItems());
                            final CharSequence[] items = getItems();
                            for (int i : checkedPositions) {
                                if (i >= 0 && i < items.length) {
                                    selectedPosition = i;
                                    break;
                                }
                            }
                            if (selectedPosition != -1) {
                                for (OnListDialogListener listener : getDialogListeners(OnListDialogListener.class)) {
                                    listener.onListItemSelected(items[selectedPosition], selectedPosition, mRequestCode);
                                }
                            } else {
                                for (OnDialogCancelListener listener : getDialogListeners(OnDialogCancelListener.class)) {
                                    listener.onCanceled(mRequestCode);
                                }
                            }
                            dismiss();
                        }
                    };
                    break;
                case AbsListView.CHOICE_MODE_NONE:
                    break;
                default:
                    break;
            }
            if (!TextUtils.isEmpty(positive)) {
                builder.setPositiveButton(positive, listener);
            }
        }

        final CharSequence[] items = getItems();
        if (items != null && items.length > 0) {
            switch (getChoiceMode()) {
                case AbsListView.CHOICE_MODE_MULTIPLE:
                    buildMultiChoice(builder);
                    break;
                case AbsListView.CHOICE_MODE_SINGLE:
                    buildSingleChoice(builder);
                    break;
                case AbsListView.CHOICE_MODE_NONE:
                    buildNormalChoice(builder);
                    break;
                default:
                    break;
            }
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

    private ListAdapter prepareAdapter(final int itemLayoutId) {
        return new ArrayAdapter<Object>(getActivity(), itemLayoutId, R.id.tvItem, getItems()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
                }
                TextView textView = ViewUtil.getInstance().findView(convertView, R.id.tvItem);
                if (textView != null) {
                    textView.setText((CharSequence) getItem(position));
                }
                return convertView;
            }
        };
    }

    private void buildMultiChoice(Builder builder) {
        builder.setItems(
                prepareAdapter(R.layout.item_dialog_list_check),
                asIntArray(getCheckedItems()), AbsListView.CHOICE_MODE_MULTIPLE,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setCheckedItems(new ParcelableSparseBooleanArray(((ListView) parent).getCheckedItemPositions()));
                    }
                });
    }

    private void buildSingleChoice(Builder builder) {
        builder.setItems(
                prepareAdapter(R.layout.item_dialog_list_radio),
                asIntArray(getCheckedItems()),
                AbsListView.CHOICE_MODE_SINGLE, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setCheckedItems(new ParcelableSparseBooleanArray(((ListView) parent).getCheckedItemPositions()));
                    }
                });
    }

    private void buildNormalChoice(Builder builder) {
        builder.setItems(
                prepareAdapter(R.layout.item_dialog_list), -1,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (OnListDialogListener listener : getDialogListeners(OnListDialogListener.class)) {
                            listener.onListItemSelected(getItems()[position], position, mRequestCode);
                        }
                        dismiss();
                    }
                });
    }

    @SuppressWarnings("ResourceType")
    @ChoiceMode
    private int getChoiceMode() {
        return IntentDataUtil.getInstance().getIntData(getArguments(), Temp.DIALOG_CHOICE_MODE.getContent());
    }

    private CharSequence[] getItems() {
        return IntentDataUtil.getInstance().getCharSequenceArrayData(getArguments(), Temp.DIALOG_CHOICE_ITEMS.getContent());
    }

    @NonNull
    private ParcelableSparseBooleanArray getCheckedItems() {
        ParcelableSparseBooleanArray items = IntentDataUtil.getInstance().getParcelableData(getArguments(), Temp.DIALOG_CHOICE_ITEM.getContent());
        if (items == null) {
            items = new ParcelableSparseBooleanArray();
        }
        return items;
    }

    private void setCheckedItems(ParcelableSparseBooleanArray checkedItems) {
        getArguments().putParcelable(Temp.DIALOG_CHOICE_ITEM.getContent(), checkedItems);
    }

    public static ListDialogBuilder createBuilder(FragmentManager manager) {
        return new ListDialogBuilder(manager, ListDialog.class);
    }
}
