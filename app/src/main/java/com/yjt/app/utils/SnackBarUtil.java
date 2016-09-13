package com.yjt.app.utils;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;

/**
 * 消息提示工具
 * <p/>
 * //TODO 暂时未完善,故先使用Toast
 *
 * @author yjt
 */
public class SnackBarUtil {

    private Snackbar mSnackbar;

    private static SnackBarUtil mSnackbarUtil;

    private SnackBarUtil() {
        // cannot be instantiated
    }

    public static synchronized SnackBarUtil getInstance() {
        if (mSnackbarUtil == null) {
            mSnackbarUtil = new SnackBarUtil();
        }
        return mSnackbarUtil;
    }

    public static void releaseInstance() {
        if (mSnackbarUtil != null) {
            mSnackbarUtil = null;
        }
    }

    public void showSnackBar(View view, CharSequence message, int length) {
        showSnackBar(view, message, length, null, null, Constant.View.SIZE_DEFAULT, Constant.View.COLOR_DEFAULT);
    }

    public void showSnackBar(View view, CharSequence message, int length, float textSize) {
        showSnackBar(view, message, length, null, null, textSize, Constant.View.COLOR_DEFAULT);
    }

    public void showSnackBar(View view, CharSequence message, int length, int color) {
        showSnackBar(view, message, length, null, null, Constant.View.SIZE_DEFAULT, color);
    }

    public void showSnackBar(View view, CharSequence message, int length, float textSize, int color) {
        showSnackBar(view, message, length, null, null, textSize, color);
    }

    /**
     * Snackbar 样式集中处理
     * <p/>
     * //TODO 后续可添加动画、button颜色、显示位置等
     *
     * @param view
     * @param message1
     * @param length
     * @param message2
     * @param listener
     * @param textSize
     * @param color
     */
    public void showSnackBar(View view, CharSequence message1, int length, CharSequence message2, View.OnClickListener listener, float textSize, int color) {
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(view, message1, length);
        } else {
            mSnackbar.setText(message1);
        }

        if (listener != null) {
            if (!TextUtils.isEmpty(message2)) {
                mSnackbar.setAction(message2, listener);
            }
        }

        if (textSize != Constant.View.SIZE_DEFAULT) {
            ((TextView) ViewUtil.getInstance().findView(mSnackbar.getView(), R.id.snackbar_text)).setTextSize(textSize);
        }
        if (color != Constant.View.COLOR_DEFAULT) {
            ((TextView) ViewUtil.getInstance().findView(mSnackbar.getView(), R.id.snackbar_text)).setTextColor(color);
        }
        mSnackbar.show();
    }

    public void hideSnackBar() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }
}
