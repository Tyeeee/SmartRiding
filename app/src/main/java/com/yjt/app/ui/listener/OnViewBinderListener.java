package com.yjt.app.ui.listener;


import android.support.v7.widget.RecyclerView;

public interface OnViewBinderListener {

    void bind(RecyclerView.ViewHolder viewHolder, Object t, int position);

    RecyclerView.ViewHolder getViewHolder();
}
