package com.yjt.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import com.yjt.app.constant.Constant;

public class InputUtil {

    private static InputMethodManager imm;
    private static long               mLastClickTime;

    private static InputUtil mInputUtil;

    private InputUtil() {
        // cannot be instantiated
    }

    public static synchronized InputUtil getInstance() {
        if (mInputUtil == null) {
            mInputUtil = new InputUtil();
        }
        return mInputUtil;
    }

    public static void releaseInstance() {
        if (mInputUtil != null) {
            mInputUtil = null;
        }
    }


    public void hideKeyBoard(MotionEvent event, Activity activity) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (imm == null)
                imm = (InputMethodManager) activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                if (activity.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                                                        .getWindowToken(),
                                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    public void hideKeyBoard(final Context context, ScrollView view) {
        view.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imm = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                View focusView = (((Activity) context).getCurrentFocus());
                if (focusView != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(),
                                                InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
    }

    public void closeKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {  //一直是true
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void hideKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean isDoubleClick() {
        long timeS = System.currentTimeMillis();
        long timeE = mLastClickTime - timeS;
        if (timeE > Constant.View.CLICK_PERIOD) {
            return true;
        }
        mLastClickTime = timeS;
        return false;
    }
}
