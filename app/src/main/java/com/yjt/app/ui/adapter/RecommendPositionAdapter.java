package com.yjt.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.yjt.app.model.Menu;
import com.yjt.app.model.RecommendPosition;
import com.yjt.app.ui.adapter.holder.MenuHolder;
import com.yjt.app.ui.adapter.holder.RecommendPositionHolder;
import com.yjt.app.ui.base.BaseViewBinder;
import com.yjt.app.ui.sticky.FixedStickyHeaderAdapter;

public class RecommendPositionAdapter extends FixedStickyHeaderAdapter<RecommendPosition, RecommendPositionHolder> {

    public RecommendPositionAdapter(Context ctx, BaseViewBinder binder, boolean groupable) {
        super(ctx, binder, groupable);
    }

    @Override
    protected void onBindHeaderOrFooter(RecyclerView.ViewHolder holder, Object object) {
        super.onBindHeaderOrFooter(holder, object);
    }


}
