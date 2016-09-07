package com.yjt.app.ui.adapter.binder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.yjt.app.R;
import com.yjt.app.model.RecommendPosition;
import com.yjt.app.ui.adapter.holder.MenuHolder;
import com.yjt.app.ui.adapter.holder.RecommendPositionHolder;
import com.yjt.app.ui.base.BaseViewBinder;

public class RecommendPositionBinder extends BaseViewBinder {

    private RecyclerView mView;
    private Context mContext;

    public RecommendPositionBinder(Context context, RecyclerView parent) {
        this.mContext = context;
        this.mView = parent;
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, Object o, int pos) {
        RecommendPositionHolder holder = (RecommendPositionHolder) viewHolder;
        RecommendPosition position = (RecommendPosition) o;
        holder.tvPosition.setText(position.getAddress());
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder() {
        return new RecommendPositionHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recommend_position, mView, false));
    }
}
