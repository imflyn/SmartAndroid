package com.flyn.smartandroid.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.SuperViewHolder> {

    protected static final int TYPE_HEADER = -0x100;
    protected static final int TYPE_FOOTER = -0x101;

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    protected RecyclerView mRecyclerView;
    private View mHeader;
    private View mFooter;

    private List<T> mList = new CopyOnWriteArrayList<>();

    public BaseAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public List<T> getList() {
        return mList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int size = mList.size();
        if (hasHeaderView())
            size++;
        if (hasFooterView())
            size++;
        return size;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (isHeaderView(position)) {
            viewType = TYPE_HEADER;
        } else if (isFooterView(position)) {
            viewType = TYPE_FOOTER;
        } else {
            if (getItemViewCount() > 1) {
                if (hasHeaderView()) {
                    position--;
                }
                return getMultiItemViewType(position, mList.get(position));
            }
            return 0;
        }
        return viewType;
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final SuperViewHolder holder = onCreate(null, parent, viewType);
        if (!(holder.itemView instanceof AdapterView)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, viewType, holder.getLayoutPosition());
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(v, viewType, holder.getLayoutPosition());
                        return true;
                    }
                    return false;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (hasHeaderView())
            position--;
        if (viewType != TYPE_HEADER && viewType != TYPE_FOOTER) {
            onBind(holder, viewType, position, mList.get(position));
        }
    }

    public SuperViewHolder onCreate(View convertView, ViewGroup parent, int viewType) {
        final SuperViewHolder holder;
        if (viewType == TYPE_HEADER && hasHeaderView()) {
            return new SuperViewHolder(getHeaderView());
        } else if (viewType == TYPE_FOOTER && hasFooterView()) {
            return new SuperViewHolder(getFooterView());
        }

        if (getItemViewCount() > 1) {
            holder = SuperViewHolder.get(convertView, mLayoutInflater.inflate(getMultiLayoutId(viewType), parent, false));
        } else {
            holder = SuperViewHolder.get(convertView, mLayoutInflater.inflate(getLayoutId(), parent, false));
        }

        return holder;
    }

    public abstract void onBind(SuperViewHolder holder, int viewType, int position, T item);

    public int getItemViewCount() {
        return 1;
    }

    public int getMultiItemViewType(int position, T t) {
        return -1;
    }

    public int getMultiLayoutId(int viewType) {
        return -1;
    }

    public int getLayoutId() {
        return -1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (mRecyclerView != null && mRecyclerView != recyclerView)
            Log.i("BaseSuperAdapter", "Does not support multiple RecyclerViews now.");
        mRecyclerView = recyclerView;
        // Ensure a situation that add header or footer before setAdapter().
        ifGridLayoutManager();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = null;
    }

    @Override
    public void onViewAttachedToWindow(SuperViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            // add header or footer to StaggeredGridLayoutManager
            if (isHeaderView(holder.getLayoutPosition()) || isFooterView(holder.getLayoutPosition())) {
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
            }
        }
    }

    public boolean hasLayoutManager() {
        return mRecyclerView != null && mRecyclerView.getLayoutManager() != null;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return hasLayoutManager() ? mRecyclerView.getLayoutManager() : null;
    }

    public View getHeaderView() {
        return mHeader;
    }

    public View getFooterView() {
        return mFooter;
    }

    public void addHeaderView(View header) {
        if (hasHeaderView())
            throw new IllegalStateException("You have already added a header view.");
        mHeader = header;
        ifGridLayoutManager();
        notifyItemInserted(0);
    }

    public void addFooterView(View footer) {
        if (hasFooterView())
            throw new IllegalStateException("You have already added a footer view.");
        mFooter = footer;
        ifGridLayoutManager();
        notifyItemInserted(getItemCount() - 1);
    }

    public boolean removeHeaderView() {
        if (hasHeaderView()) {
            notifyItemRemoved(0);
            mHeader = null;
            return true;
        }
        return false;
    }

    public boolean removeFooterView() {
        if (hasFooterView()) {
            notifyItemRemoved(getItemCount() - 1);
            mFooter = null;
            return true;
        }
        return false;
    }

    public boolean hasHeaderView() {
        return getHeaderView() != null;
    }

    public boolean hasFooterView() {
        return getFooterView() != null;
    }

    public boolean isHeaderView(int position) {
        return hasHeaderView() && position == 0;
    }

    public boolean isFooterView(int position) {
        return hasFooterView() && position == getItemCount() - 1;
    }

    private void ifGridLayoutManager() {
        final RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager.SpanSizeLookup originalSpanSizeLookup =
                    ((GridLayoutManager) layoutManager).getSpanSizeLookup();
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() :
                            originalSpanSizeLookup.getSpanSize(position);
                }
            });
        }
    }

    public final void add(T item) {
        mList.add(item);
        int index = mList.size() - 1;
        if (hasHeaderView())
            index++;
        notifyItemInserted(index);
    }

    public final void insert(int index, T item) {
        mList.add(index, item);
        if (hasHeaderView())
            index++;
        notifyItemInserted(index);
    }

    public final void addAll(List<T> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        int start = mList.size();
        mList.addAll(items);
        if (hasHeaderView())
            start++;
        notifyItemRangeInserted(start, items.size());
    }

    public final void remove(T item) {
        if (contains(item)) {
            int index = mList.indexOf(item);
            remove(index);
        }
    }

    public final void remove(int index) {
        mList.remove(index);
        if (hasHeaderView())
            index++;
        notifyItemRemoved(index);
    }

    public final void set(T oldItem, T newItem) {
        set(mList.indexOf(oldItem), newItem);
    }

    public final void set(int index, T item) {
        mList.set(index, item);
        if (hasHeaderView())
            index++;
        notifyItemChanged(index);
    }

    public final void replaceAll(List<T> items) {
        clear();
        addAll(items);
    }

    public final boolean contains(T item) {
        return mList.contains(item);
    }

    public final void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    protected static class SuperViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> childViews = new SparseArray<>();

        public SuperViewHolder(View itemView) {
            super(itemView);
        }

        public static SuperViewHolder get(View convertView, View itemView) {
            SuperViewHolder holder;
            if (convertView == null) {
                holder = new SuperViewHolder(itemView);
                convertView = itemView;
                convertView.setTag(holder);
            } else {
                holder = (SuperViewHolder) convertView.getTag();
            }
            return holder;
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int id) {
            View childView = childViews.get(id);
            if (childView == null) {
                childView = itemView.findViewById(id);
                if (childView != null)
                    childViews.put(id, childView);
            }
            return (T) childView;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int viewType, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, int viewType, int position);
    }

}
