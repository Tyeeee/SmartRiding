package com.yjt.app.ui.listener;

import android.webkit.WebView;

import com.yjt.app.ui.base.BaseFragment;

public interface OnGoBackListener {

    void onGoBackListener(boolean isWebView, WebView view,
                             String msg);

    void onSelectedFragment(BaseFragment fragment);

}
