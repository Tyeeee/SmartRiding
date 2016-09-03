package com.yjt.app.ui.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.ui.base.BaseHolder;
import com.yjt.app.utils.ViewUtil;

public class RecommendPositionHolder extends BaseHolder {

    public TextView tvPosition;

    public RecommendPositionHolder(View itemView) {
        super(itemView);
        tvPosition = ViewUtil.getInstance().findView(itemView, R.id.tvPosition);
    }
}
