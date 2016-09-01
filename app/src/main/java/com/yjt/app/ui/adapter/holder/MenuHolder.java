package com.yjt.app.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.ui.base.BaseHolder;
import com.yjt.app.utils.ViewUtil;

public class MenuHolder extends BaseHolder {

    public TextView tvMenu;

    public MenuHolder(View itemView) {
        super(itemView);
        tvMenu = ViewUtil.getInstance().findView(itemView, R.id.tvMenu);
    }
}
