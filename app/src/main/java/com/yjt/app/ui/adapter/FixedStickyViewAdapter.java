package com.yjt.app.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.yjt.app.constant.Constant;
import com.yjt.app.ui.base.BaseViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器基类
 *
 * @author yjt
 */
public abstract class FixedStickyViewAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    protected ArrayList<FixedStickyView> mHeaderViews = new ArrayList<>();
    protected ArrayList<FixedStickyView> mFooterViews = new ArrayList<>();

    private SparseArray<FixedViewHoldGenerator> mGenerators = new SparseArray<>();
    private BaseViewBinder mViewBinder;

    protected List<T> mItems = new ArrayList<>();
    protected OnItemClickListener mItemClickListener;
    protected OnErrorClickListener mErrorClickListener;

    public FixedStickyViewAdapter() { }

    public FixedStickyViewAdapter(BaseViewBinder binder) {
        this.mViewBinder = binder;
    }

    public List<T> getItems() {
        return mItems;
    }

    public FixedStickyViewAdapter setData(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            mItems.clear();
            mItems.addAll(datas);
            notifyDataSetChanged();
        }
        return this;
    }

    public void addItems(List<T> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        addItem(item, mItems.size());
    }


    public void addItem(T item, int position) {
        if (position <= mItems.size()) {
            mItems.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void addItemToHead(T item) {
        mItems.add(0, item);
        notifyDataSetChanged();
    }

    public void addItemsToHead(List<T> items) {
        mItems.addAll(0, items);
        notifyDataSetChanged();
    }


    public void removeItem(T item) {
        mItems.remove(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position < mItems.size()) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mItems.clear();
        mHeaderViews.clear();
        mFooterViews.clear();
        mGenerators.clear();
        notifyDataSetChanged();
    }

    public void bindDataToHeaderOrFooter(int id, Object object, int viewType) {
        List<FixedStickyView> views = null;
        if (viewType == Constant.ItemType.HEADER_VIEW) {
            views = mHeaderViews;
        } else if (viewType == Constant.ItemType.FOOTER_VIEW) {
            views = mFooterViews;
        }
        if (views != null && views.size() > 0) {
            for (int i = 0; i < views.size(); i++) {
                FixedStickyView view = views.get(i);
                if (view.mId == id) {
                    view.mObject = object;
                    notifyDataSetChanged();
                    break;
                }
            }
        }

    }

    public void addHeaderView(int id, int layoutId, int viewType, int fixedStickyViewType, FixedViewHoldGenerator generator) {
        FixedStickyView view = new FixedStickyView();
        view.mId = id;
        view.mObject = null;
        view.mLayoutId = layoutId;
        view.mViewType = viewType;
        view.mFixedStickyViewType = fixedStickyViewType;
        mHeaderViews.add(view);
        if (mGenerators.get(fixedStickyViewType) == null) {
            mGenerators.append(fixedStickyViewType, generator);
        }
        notifyDataSetChanged();
    }

    public boolean hasHeaderView(int id) {
        for (int i = 0; i < mHeaderViews.size(); i++) {
            if (mHeaderViews.get(i).mId == id) {
                return true;
            }
        }
        return false;
    }

    public void removeHeaderView(int id) {
        if (mHeaderViews.size() > 0) {
            removeFixedViewInfo(id, mHeaderViews);
            notifyDataSetChanged();
        }
    }

    private void removeFixedViewInfo(int id, ArrayList<FixedStickyView> where) {
        for (int i = 0; i < where.size(); ++i) {
            if (where.get(i).mId == id) {
                where.remove(i);
                break;
            }
        }
    }

    public void addFooterView(int id, int layoutId, int viewType, int fixedStickyViewType, FixedViewHoldGenerator generator) {
        final FixedStickyView info = new FixedStickyView();
        info.mId = id;
        info.mObject = null;
        info.mLayoutId = layoutId;
        info.mViewType = viewType;
        info.mFixedStickyViewType = fixedStickyViewType;
        if (mGenerators.get(fixedStickyViewType) == null) {
            mGenerators.append(fixedStickyViewType, generator);
        }
        mFooterViews.add(info);
        notifyDataSetChanged();
    }

    public boolean hasFooterView(int id) {
        for (int i = 0; i < mFooterViews.size(); i++) {
            if (mFooterViews.get(i).mId == id) {
                return true;
            }
        }
        return false;
    }

    public void removeFooterView(int id) {
        if (mFooterViews.size() > 0) {
            removeFixedStickyView(id, mFooterViews);
            notifyDataSetChanged();
        }
    }

    private void removeFixedStickyView(int id, ArrayList<FixedStickyView> where) {
        for (int i = 0; i < where.size(); ++i) {
            if (where.get(i).mId == id) {
                where.remove(i);
                break;
            }
        }
    }

//    public T getItem(int position) {
//        return position < mItems.size() ? mItems.get(position) : null;
//    }

    public Object getItem(int position) {
        if (position < mHeaderViews.size()) {
            return mHeaderViews.get(position);
        }
        if (position >= mHeaderViews.size() && position < mHeaderViews.size() + mItems.size()) {
            return mItems.get(position - mHeaderViews.size());
        }
        return mFooterViews.get(position - mHeaderViews.size() - mItems.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constant.ItemType.CONTENT_VIEW:
                return mViewBinder.getViewHolder();
            default:
                return mGenerators.get(viewType).generate();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object obj = getItem(position);
        if (obj instanceof FixedStickyView) {
            onBindHeaderOrFooter(holder, obj);
        } else {
            mViewBinder.bind(holder, obj, position);
        }
        final int itemPosition = position;
        if (holder.getItemViewType() == Constant.ItemType.CONTENT_VIEW) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(itemPosition);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        FixedStickyView view;
        if (position < mHeaderViews.size()) {
            view = mHeaderViews.get(position);
            return view.mFixedStickyViewType;
        } else if ((position >= mHeaderViews.size() && position < mHeaderViews.size() + mItems.size())) {
            return Constant.ItemType.CONTENT_VIEW;
        } else {
            view = mFooterViews.get(position - mHeaderViews.size() - mItems.size());
            return view.mFixedStickyViewType;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size() + mHeaderViews.size() + mFooterViews.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnErrorClickListener {
        void onErrorClick();
    }

    public void setOnErrorClickListener(OnErrorClickListener listener) {
        this.mErrorClickListener = listener;
    }

    protected abstract void onBindHeaderOrFooter(RecyclerView.ViewHolder holder, Object object);

    public static class FixedStickyView {
        public int mId;
        public int mViewType;
        public int mFixedStickyViewType;
        public int mLayoutId;
        public Object mObject;
    }

    public static class FixedViewHoldGenerator {
        public RecyclerView.ViewHolder generate() {
            return null;
        }
    }

    /*****************************************************************************/

    public void addHeaderView(FixedStickyView header) {
        if (header == null) {
            throw new RuntimeException("header is null");
        }
        mHeaderViews.add(header);
        notifyDataSetChanged();
    }

    public void addFooterView(FixedStickyView footer) {
        if (footer == null) {
            throw new RuntimeException("footer is null");
        }
        mFooterViews.add(footer);
        notifyDataSetChanged();
    }

    public FixedStickyView getFooterView() {
        return getFooterViewsCount() > 0 ? mFooterViews.get(0) : null;
    }

    public FixedStickyView getHeaderView() {
        return getHeaderViewsCount() > 0 ? mHeaderViews.get(0) : null;
    }

    public void removeHeaderView(View view) {
        mHeaderViews.remove(view);
        notifyDataSetChanged();
    }

    public void removeFooterView(View view) {
        mFooterViews.remove(view);
        notifyDataSetChanged();
    }

    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    public boolean isHeader(int position) {
        return getHeaderViewsCount() > 0 && position == 0;
    }

    public boolean isFooter(int position) {
        int lastPosition = getItemCount() - 1;
        return getFooterViewsCount() > 0 && position == lastPosition;
    }
}
