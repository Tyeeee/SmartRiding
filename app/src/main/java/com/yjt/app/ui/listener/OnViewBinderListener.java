package com.yjt.app.ui.listener;


import android.support.v7.widget.RecyclerView;

/**
 * view数据绑定
 *
 * @author yjt
 */
public interface OnViewBinderListener {

    void bind(RecyclerView.ViewHolder viewHolder, Object t, int position);

    RecyclerView.ViewHolder getViewHolder();
}
