package com.yjt.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.yjt.app.model.RouteDetail;
import com.yjt.app.ui.adapter.holder.RouteDetailHolder;
import com.yjt.app.ui.base.BaseViewBinder;

public class RouteDetailAdapter extends FixedStickyHeaderAdapter<RouteDetail, RouteDetailHolder> {

    public RouteDetailAdapter(Context ctx, BaseViewBinder binder, boolean groupable) {
        super(ctx, binder, groupable);
    }

    @Override
    protected void onBindHeaderOrFooter(RecyclerView.ViewHolder holder, Object object) {
        super.onBindHeaderOrFooter(holder, object);
    }


}
