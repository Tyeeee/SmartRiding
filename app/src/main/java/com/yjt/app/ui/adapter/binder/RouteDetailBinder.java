package com.yjt.app.ui.adapter.binder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.services.route.DriveStep;
import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.model.Menu;
import com.yjt.app.model.RouteDetail;
import com.yjt.app.ui.adapter.holder.RouteDetailHolder;
import com.yjt.app.ui.base.BaseViewBinder;
import com.yjt.app.utils.DensityUtil;
import com.yjt.app.utils.MapUtil;
import com.yjt.app.utils.ViewUtil;

public class RouteDetailBinder extends BaseViewBinder {

    private RecyclerView mView;
    private Context      mContext;

    public RouteDetailBinder(Context context, RecyclerView parent) {
        this.mContext = context;
        this.mView = parent;
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, Object o, int position) {
        RouteDetailHolder holder = (RouteDetailHolder) viewHolder;
        RouteDetail       detail = (RouteDetail) o;
        if (detail.isLineVisible() == View.VISIBLE) {
            ViewUtil.getInstance().setViewVisible(holder.vline);
        } else {
            ViewUtil.getInstance().setViewGone(holder.vline);
        }
        if (detail.isDirectionUpVisible() == View.VISIBLE) {
            ViewUtil.getInstance().setViewVisible(holder.ivDirectionUp);
        } else {
            ViewUtil.getInstance().setViewGone(holder.ivDirectionUp);
        }
        if (detail.isDirectionDownVisible() == View.VISIBLE) {
            ViewUtil.getInstance().setViewVisible(holder.ivDirectionDown);
        } else {
            ViewUtil.getInstance().setViewGone(holder.ivDirectionDown);
        }
        holder.ivDirection.setImageResource(detail.getDirection());
        holder.tvRouteDetail.setText(detail.getRoutDetail());
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder() {
        return new RouteDetailHolder(LayoutInflater.from(mContext).inflate(R.layout.item_route_detail, mView, false));
    }
}
