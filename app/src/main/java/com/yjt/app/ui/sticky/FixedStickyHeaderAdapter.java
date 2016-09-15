package com.yjt.app.ui.sticky;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yjt.app.R;
import com.yjt.app.ui.base.BaseHolder;
import com.yjt.app.ui.base.BaseViewBinder;
import com.yjt.app.utils.ViewUtil;


public class FixedStickyHeaderAdapter<T extends OnGroupListener, V extends RecyclerView.ViewHolder>
        extends FixedStickyViewAdapter<T, V> implements StickyRecyclerHeadersAdapter {

    private Context mContext;
    private boolean groupable;

    private static final int GROUP_ID_UNAVAILABLE = -1;

    public FixedStickyHeaderAdapter(Context ctx, BaseViewBinder binder, boolean groupable) {
        super(binder);
        this.mContext = ctx;
        this.groupable = groupable;
    }

    @Override
    protected void onBindHeaderOrFooter(RecyclerView.ViewHolder holder, Object object) {

    }

    @Override
    public long getHeaderId(int position) {
        if (!groupable) {
            return GROUP_ID_UNAVAILABLE;
        }
        Object obj = getItem(position);
        if (obj != null) {
            int numHeaders = mHeaderViews.size();
            int numItems   = mItems.size();
            if (position < numHeaders ||
                    position >= numHeaders + numItems) {
                return GROUP_ID_UNAVAILABLE;
            }
            return ((OnGroupListener) obj).getGroupId();
        }
        return GROUP_ID_UNAVAILABLE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new BaseHolder(LayoutInflater.from(mContext).inflate(R.layout.item_group_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (groupable) {
            TextView tvHeader = ViewUtil.getInstance().findView(holder.itemView, R.id.tvDate);
            if (getHeaderId(position) != GROUP_ID_UNAVAILABLE) {
                tvHeader.setText(((OnGroupListener) getItem(position)).getGroupName());
            }
        }
    }
}
