package com.yjt.app.ui.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.ui.base.BaseHolder;
import com.yjt.app.utils.ViewUtil;

public class RouteDetailHolder extends BaseHolder {

    public View      vline;
    public ImageView ivDirection;
    public ImageView ivDirectionUp;
    public ImageView ivDirectionDown;
    public TextView  tvRouteDetail;

    public RouteDetailHolder(View itemView) {
        super(itemView);
        vline = ViewUtil.getInstance().findView(itemView, R.id.vline);
        ivDirection = ViewUtil.getInstance().findView(itemView, R.id.ivDirection);
        ivDirectionUp = ViewUtil.getInstance().findView(itemView, R.id.ivDirectionUp);
        ivDirectionDown = ViewUtil.getInstance().findView(itemView, R.id.ivDirectionDown);
        tvRouteDetail = ViewUtil.getInstance().findView(itemView, R.id.tvRouteDetail);
    }
}
