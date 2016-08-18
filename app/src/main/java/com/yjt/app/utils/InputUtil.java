package com.yjt.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

/**
 * 界面输入工具
 *
 * @author yjt
 */
public class InputUtil {

    private static InputMethodManager imm;
    private static long mLastClickTime;

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


    /**
     * 针对普通视图下键盘隐藏的操作
     *
     * @param event
     * @param act
     */
    public void onTouchCommonView(MotionEvent event, Activity act) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (imm == null)
                imm = (InputMethodManager) act
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (act.getCurrentFocus() != null) {
                if (act.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(act.getCurrentFocus()
                                                        .getWindowToken(),
                                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * 针对ScrollView为父视图的情况下的键盘隐藏操作
     *
     * @param context
     * @param view
     */
    public void onTouchScrollView(final Context context, ScrollView view) {
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

    /**
     * 对点击事件进行频率控制
     *
     * @return
     */
    public boolean isDoubleClick() {
        long timeS = System.currentTimeMillis();
        long timeE = timeS - mLastClickTime;
        if (0 < timeE && timeE < 50) {
            return true;
        }
        mLastClickTime = timeS;
        return false;
    }

}
