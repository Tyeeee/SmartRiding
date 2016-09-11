package com.yjt.app.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Temp;
import com.yjt.app.ui.listener.OnDialogCancelListener;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.TypefaceUtil;
import com.yjt.app.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class BaseDialogFragment extends DialogFragment implements DialogInterface.OnShowListener {

    protected int mRequestCode;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onCreateDialog() invoked!!");
        Dialog dialog = new Dialog(getActivity(), resolveTheme());
        Bundle bundle = getArguments();
        if (bundle != null) {
            dialog.setCanceledOnTouchOutside(
                    bundle.getBoolean(Temp.CANCELABLE_ON_TOUCH_OUTSIDE.getContent()));
        }
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onCreateView() invoked!!");
        return build(new Builder(getActivity(), inflater, container)).create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onActivityCreated() invoked!!");
        final Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            mRequestCode = getTargetRequestCode();
        } else {
            Bundle args = getArguments();
            if (args != null) {
                mRequestCode = args.getInt(Temp.REQUEST_CODE.getContent(), 0);
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onCancel() invoked!!");
        for (OnDialogCancelListener listener : getCancelListeners()) {
            listener.onCancelled(mRequestCode);
        }
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
        LogUtil.d(getClass().getName(), this.getClass().getSimpleName()
                + " onDestroyView() invoked!!");
    }


    @StyleRes
    private int resolveTheme() {
        int theme = getTheme();
        if (theme != 0) {
            return theme;
        }
        boolean isLightTheme = isActivityThemeLight();
        Bundle  bundle       = getArguments();
        if (bundle != null) {
            if (bundle.getBoolean(Temp.USE_DARK_THEME.getContent())) {
                isLightTheme = false;
            } else if (bundle.getBoolean(Temp.USE_LIGHT_THEME.getContent())) {
                isLightTheme = true;
            }
        }
        return isLightTheme ? R.style.yjt_Light_Dialog_Appearance : R.style.yjt_Dark_Dialog_Appearance;
    }

    private boolean isActivityThemeLight() {
        try {
            TypedValue val = new TypedValue();
            getActivity().getTheme().resolveAttribute(R.attr.isLightTheme, val, true);
            TypedArray styledAttributes =
                    getActivity().obtainStyledAttributes(val.data, new int[]{R.attr.isLightTheme});
            boolean lightTheme = styledAttributes.getBoolean(0, false);
            styledAttributes.recycle();
            return lightTheme;
        } catch (RuntimeException e) {
            return true;
        }
    }

    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (getView() != null) {
            ScrollView  svDialogContent = ViewUtil.getInstance().findView(getView(), R.id.svDialogContent);
            FrameLayout flDialogCustom  = ViewUtil.getInstance().findView(getView(), R.id.flDialogCustom);
            ListView    lvDialogContent = ViewUtil.getInstance().findView(getView(), R.id.lvDialogContent);
            boolean     scrollable      = false;
            if (flDialogCustom.getChildCount() > 0) {
                View firstChild = flDialogCustom.getChildAt(0);
                if (firstChild instanceof ViewGroup) {
                    scrollable = ViewUtil.getInstance().isScrollable((ViewGroup) firstChild);
                }
            }
            modifyButtonsBasedOnScrollableContent(ViewUtil.getInstance().isScrollable(lvDialogContent)
                                                          || ViewUtil.getInstance().isScrollable(svDialogContent)
                                                          || scrollable);
        }
    }

    private void modifyButtonsBasedOnScrollableContent(boolean scrollable) {
        if (getView() == null) {
            return;
        }
        View           vDialogDivider               = ViewUtil.getInstance().findView(getView(), R.id.vDialogDivider);
        View           vDialogButtonsBottomSpace    = ViewUtil.getInstance().findView(getView(), R.id.vDialogButtonsBottomSpace);
        RelativeLayout rlDialogLayoutButtons        = ViewUtil.getInstance().findView(getView(), R.id.rlDialogLayoutButtons);
        LinearLayout   llDialogLayoutButtonsStacked = ViewUtil.getInstance().findView(getView(), R.id.llDialogLayoutButtonsStacked);
        if (ViewUtil.getInstance().isGone(rlDialogLayoutButtons)
                && ViewUtil.getInstance().isGone(llDialogLayoutButtonsStacked)) {
            ViewUtil.getInstance().setViewGone(vDialogDivider);
            ViewUtil.getInstance().setViewGone(vDialogButtonsBottomSpace);
        } else if (scrollable) {
            ViewUtil.getInstance().setViewVisible(vDialogDivider);
            ViewUtil.getInstance().setViewGone(vDialogButtonsBottomSpace);
        } else {
            ViewUtil.getInstance().setViewGone(vDialogDivider);
            ViewUtil.getInstance().setViewVisible(vDialogButtonsBottomSpace);
        }
    }

    protected <T> List<T> getDialogListeners(Class<T> listenerInterface) {
        final Fragment targetFragment = getTargetFragment();
        List<T>        listeners      = new ArrayList<T>(2);
        if (targetFragment != null && listenerInterface.isAssignableFrom(targetFragment.getClass())) {
            listeners.add((T) targetFragment);
        }
        if (getActivity() != null && listenerInterface.isAssignableFrom(getActivity().getClass())) {
            listeners.add((T) getActivity());
        }
        return Collections.unmodifiableList(listeners);
    }

    protected List<OnDialogCancelListener> getCancelListeners() {
        return getDialogListeners(OnDialogCancelListener.class);
    }

    protected static class Builder {

        private final Context                         mContext;
        private final ViewGroup                       mContainer;
        private final LayoutInflater                  mInflater;
        private       CharSequence                    mTitle;
        private       CharSequence                    mPositiveButtonText;
        private       View.OnClickListener            mPositiveButtonListener;
        private       CharSequence                    mNegativeButtonText;
        private       View.OnClickListener            mNegativeButtonListener;
        private       CharSequence                    mNeutralButtonText;
        private       View.OnClickListener            mNeutralButtonListener;
        private       CharSequence                    mMessage;
        private       View                            mCustomView;
        private       ListAdapter                     mListAdapter;
        private       int                             mListCheckedItemIdx;
        private       int                             mChoiceMode;
        private       int[]                           mListCheckedItemMultipleIds;
        private       AdapterView.OnItemClickListener mOnItemClickListener;

        public Builder(Context context, LayoutInflater inflater, ViewGroup container) {
            this.mContext = context;
            this.mContainer = container;
            this.mInflater = inflater;
        }

        public LayoutInflater getLayoutInflater() {
            return mInflater;
        }

        public Builder setTitle(int titleId) {
            this.mTitle = mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.mTitle = title;
            return this;
        }

        public Builder setPositiveButton(int textId, final View.OnClickListener listener) {
            mPositiveButtonText = mContext.getText(textId);
            mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, final View.OnClickListener listener) {
            mNegativeButtonText = mContext.getText(textId);
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
            mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(int textId, final View.OnClickListener listener) {
            mNeutralButtonText = mContext.getText(textId);
            mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, final View.OnClickListener listener) {
            mNeutralButtonText = text;
            mNeutralButtonListener = listener;
            return this;
        }

        public Builder setMessage(int messageId) {
            mMessage = mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }

        public Builder setItems(ListAdapter listAdapter, int[] checkedItemIds, int choiceMode, final AdapterView.OnItemClickListener listener) {
            mListAdapter = listAdapter;
            mListCheckedItemMultipleIds = checkedItemIds;
            mOnItemClickListener = listener;
            mChoiceMode = choiceMode;
            mListCheckedItemIdx = -1;
            return this;
        }

        public Builder setItems(ListAdapter listAdapter, int checkedItemIdx,
                                final AdapterView.OnItemClickListener listener) {
            mListAdapter = listAdapter;
            mOnItemClickListener = listener;
            mListCheckedItemIdx = checkedItemIdx;
            mChoiceMode = AbsListView.CHOICE_MODE_NONE;
            return this;
        }

        public Builder setView(View view) {
            mCustomView = view;
            return this;
        }

        public View create() {
            LinearLayout   content                        = (LinearLayout) mInflater.inflate(R.layout.dialog_custom, mContainer, false);
            TextView       tvDialogTitle                  = ViewUtil.getInstance().findView(content, R.id.tvDialogTitle);
            TextView       tvDialogMessage                = ViewUtil.getInstance().findView(content, R.id.tvDialogMessage);
            FrameLayout    flDialogCustom                 = ViewUtil.getInstance().findView(content, R.id.flDialogCustom);
            RelativeLayout rlDialogLayoutButtons          = ViewUtil.getInstance().findView(content, R.id.rlDialogLayoutButtons);
            Button         btnDialogNeutral               = ViewUtil.getInstance().findView(content, R.id.btnDialogNeutral);
            Button         btnDialogNegative              = ViewUtil.getInstance().findView(content, R.id.btnDialogNegative);
            Button         btnDialogPositive              = ViewUtil.getInstance().findView(content, R.id.btnDialogPositive);
            LinearLayout   llDialogLayoutButtonsStacked   = ViewUtil.getInstance().findView(content, R.id.llDialogLayoutButtonsStacked);
            Button         btnDialogButtonPositiveStacked = ViewUtil.getInstance().findView(content, R.id.btnDialogButtonPositiveStacked);
            Button         btnDialogButtonNegativestacked = ViewUtil.getInstance().findView(content, R.id.btnDialogButtonNegativestacked);
            Button         btnDialogButtonNeutralStacked  = ViewUtil.getInstance().findView(content, R.id.btnDialogButtonNeutralStacked);
            ListView       lvDialogContent                = ViewUtil.getInstance().findView(content, R.id.lvDialogContent);

            Typeface regularFont = TypefaceUtil.getInstance().get(mContext, Constant.View.ROBOTO_REGULAR);
            Typeface mediumFont  = TypefaceUtil.getInstance().get(mContext, Constant.View.ROBOTO_MEDIUM);

            setText(tvDialogTitle, mTitle, regularFont);
            setText(tvDialogMessage, mMessage, mediumFont);
            setPaddingOfTitleAndMessage(tvDialogTitle, tvDialogMessage);

            if (mCustomView != null) {
                flDialogCustom.addView(mCustomView);
            }
            if (mListAdapter != null) {
                lvDialogContent.setAdapter(mListAdapter);
                lvDialogContent.setOnItemClickListener(mOnItemClickListener);
                if (mListCheckedItemIdx != -1) {
                    lvDialogContent.setSelection(mListCheckedItemIdx);
                }
                if (mListCheckedItemMultipleIds != null) {
                    lvDialogContent.setChoiceMode(mChoiceMode);
                    for (int i : mListCheckedItemMultipleIds) {
                        lvDialogContent.setItemChecked(i, true);
                    }
                }
            }

            if (shouldStackButtons()) {
                setText(btnDialogButtonPositiveStacked, mPositiveButtonText, mediumFont, mPositiveButtonListener);
                setText(btnDialogButtonNegativestacked, mNegativeButtonText, mediumFont, mNegativeButtonListener);
                setText(btnDialogButtonNeutralStacked, mNeutralButtonText, mediumFont, mNeutralButtonListener);
                ViewUtil.getInstance().setViewGone(rlDialogLayoutButtons);
                ViewUtil.getInstance().setViewVisible(rlDialogLayoutButtons);
                ViewUtil.getInstance().setViewVisible(llDialogLayoutButtonsStacked);
            } else {
                setText(btnDialogPositive, mPositiveButtonText, mediumFont, mPositiveButtonListener);
                setText(btnDialogNegative, mNegativeButtonText, mediumFont, mNegativeButtonListener);
                setText(btnDialogNeutral, mNeutralButtonText, mediumFont, mNeutralButtonListener);
                ViewUtil.getInstance().setViewVisible(rlDialogLayoutButtons);
                ViewUtil.getInstance().setViewGone(llDialogLayoutButtonsStacked);
            }
            if (TextUtils.isEmpty(mPositiveButtonText) && TextUtils.isEmpty(mNegativeButtonText) && TextUtils.isEmpty
                    (mNeutralButtonText)) {
                ViewUtil.getInstance().setViewGone(rlDialogLayoutButtons);
            }

            return content;
        }

        private void setPaddingOfTitleAndMessage(TextView vTitle, TextView vMessage) {
            int dp6 = mContext.getResources().getDimensionPixelSize(R.dimen.dp_6);
            int dp4 = mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
            if (!TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mMessage)) {
                vTitle.setPadding(dp6, dp6, dp6, dp4);
                vMessage.setPadding(dp6, 0, dp6, dp4);
            } else if (TextUtils.isEmpty(mTitle)) {
                vMessage.setPadding(dp6, dp4, dp6, dp4);
            } else if (TextUtils.isEmpty(mMessage)) {
                vTitle.setPadding(dp6, dp6, dp6, dp4);
            }
        }

        private boolean shouldStackButtons() {
            return shouldStackButton(mPositiveButtonText) || shouldStackButton(mNegativeButtonText)
                    || shouldStackButton(mNeutralButtonText);
        }

        private boolean shouldStackButton(CharSequence text) {
            return text != null && text.length() > 12;
        }

        private void setText(Button button, CharSequence text, Typeface font, View.OnClickListener listener) {
            setText(button, text, font);
            if (listener != null) {
                button.setOnClickListener(listener);
            }
        }

        private void setText(TextView textView, CharSequence text, Typeface font) {
            if (text != null) {
                textView.setText(text);
                textView.setTypeface(font);
            } else {
                ViewUtil.getInstance().setViewGone(textView);
            }
        }
    }

    protected abstract Builder build(Builder initialBuilder);
}
