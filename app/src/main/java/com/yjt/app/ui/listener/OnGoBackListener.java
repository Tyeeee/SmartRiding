package com.yjt.app.ui.listener;

import android.webkit.WebView;

import com.yjt.app.ui.base.BaseFragment;

/**
 * 返回监听
 *
 * @author yjt
 */
public interface OnGoBackListener {

    void setOnGoBackListener(boolean isWebView, WebView view,
                             String msg);

    void setSelectedFragment(BaseFragment fragment);

}
