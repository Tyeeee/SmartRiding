package com.yjt.app.ui.base;

import android.support.v7.widget.RecyclerView;

import com.yjt.app.ui.listener.OnViewBinderListener;


public abstract class BaseViewBinder implements OnViewBinderListener {

    @Override
    public abstract void bind(RecyclerView.ViewHolder viewHolder, Object o, int position);

    @Override
    public abstract RecyclerView.ViewHolder getViewHolder();
}
