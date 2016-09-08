package com.yjt.app.ui.adapter.binder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.model.Menu;
import com.yjt.app.ui.adapter.holder.MenuHolder;
import com.yjt.app.ui.base.BaseViewBinder;
import com.yjt.app.utils.DensityUtil;
import com.yjt.app.utils.ViewUtil;

public class MenuBinder extends BaseViewBinder {

    private RecyclerView mView;
    private Context mContext;

    public MenuBinder(Context context, RecyclerView parent) {
        this.mContext = context;
        this.mView = parent;
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, Object o, int position) {
        MenuHolder holder = (MenuHolder) viewHolder;
        Menu menu = (Menu) o;
        ViewUtil.getInstance().setText(holder.tvMenu, menu.getTitle(), menu.getIcon(), DensityUtil.getInstance().dp2px(24), DensityUtil.getInstance().dp2px(24), Constant.View.DRAWABLE_LEFT, true);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder() {
        return new MenuHolder(LayoutInflater.from(mContext).inflate(R.layout.item_menu, mView, false));
    }
}
