/*
 * Copyright (c) 2015  Alashov Berkeli
 * It is licensed under GNU GPL v. 2 or later. For full terms see the file LICENSE.
 */

package com.base.kiy.BaseKiy.BaseRecyclerView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Edit by KwonIkYoung
 *
 * Headers + Footers + Loading progress RecyclerView
 *
 * {@code HFERecyclerView} lets you to load new pages when a user scrolls down to the bottom of
 * a list. If no {@link } provided {@code EndlessRecyclerView} behaves as {@link RecyclerView}.
 * <p>
 * {@code EndlessRecyclerView} supports only {@link LinearLayoutManager} and its subclasses.
 * <p>
 * Implement {@link BaseRecyclerPager} interface to determine when {@code EndlessRecyclerView} should start
 * loading process and a way to perform async operation. Use {@link #(BaseRecyclerPager)} method to set
 * or reset current pager. When async operation complete you may want to call
 * {@link #setRefreshing(boolean)} method to hide progress purchasePointView if it was provided.
 * <p>
 * By default {@code HFERecyclerView} starts loading operation when you are at the very bottom
 * of a list but you can opt this behaviour using {@link #setThreshold(int)} method.
 * <p>
 * If you want to show progress on the bottom of a list you may set a progress purchasePointView using
 * {@link #setProgressView(int)} or {@link #} methods. You should keep in mind
 * that in order to show progress purchasePointView on the bottom of {@code EndlessRecyclerView} it will wrap
 * provided adapter and add new {@link ViewHolder}'s purchasePointView type. Its value is -1.
 * <p>
 * If you use {@link Adapter} with stable ids and want to show progress purchasePointView, you
 * should keep in mind that purchasePointView holder of progress purchasePointView will have {@code NO_ID}.
 *
 * @author Slava Yasevich
 */
public final class HFERecyclerView extends RecyclerView {
    private static final int TYPE_HEADER_VIEW = Integer.MIN_VALUE;
    private static final int TYPE_FOOTER_VIEW = Integer.MIN_VALUE + 1000;

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();

    private AdapterWrapper adapterWrapper;
    private View progressView;
    private boolean refreshing = true;
    private int threshold = 1;

    public HFERecyclerView(Context context) {
        this(context, null);
    }

    public HFERecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HFERecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(BaseRecyclerAdapter adapter) {
        adapterWrapper = new AdapterWrapper(adapter);
        super.setAdapter(adapterWrapper);
    }

    @Override
    public Adapter getAdapter() {
        return adapterWrapper.getAdapter();
    }

    /**
     * @param layout instances of {@link LinearLayoutManager} only
     */
    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof LinearLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new IllegalArgumentException(
                "layout manager must be an instance of LinearLayoutManager");
        }
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) super.getLayoutManager();
    }

    public void addHeaderView(View view) {
        if (mHeaderViews == null) {
            mHeaderViews = new ArrayList<>();
        }
        mHeaderViews.add(view);
        adapterWrapper.notifyDataSetChanged();
    }

    public void addFooterView(View view) {
        if (mFooterViews == null) {
            mFooterViews = new ArrayList<>();
        }
        mFooterViews.add(view);
        adapterWrapper.notifyDataSetChanged();
    }

    public void removeHeaader(){
        removeHeader(0);
    }

    public void removeFooter(){
        removeFooter(0);
    }

    public void removeHeader(int po) {
        if (mHeaderViews == null) {
            return;
        }
        if (po < mHeaderViews.size()) {
            mHeaderViews.remove(po);
            adapterWrapper.notifyDataSetChanged();
        }
    }

    public void removeFooter(int po) {
        if (mFooterViews == null) {
            return;
        }
        if (po < mFooterViews.size()) {
            mFooterViews.remove(po);
            adapterWrapper.notifyDataSetChanged();
        }
    }

    /**
     * Sets {@link BaseRecyclerPager} to use with the purchasePointView.
     *
     * @param pager pager to set or {@code null} to clear current pager
     */
    public void setPager(BaseRecyclerPager pager) {
        if (adapterWrapper != null) {
            adapterWrapper.setPager(pager);
        }
    }

    /**
     * Sets threshold to use. Only positive numbers are allowed. This value is used to determine if
     * loading should start when user scrolls the purchasePointView down. Default value is 1.
     *
     * @param threshold positive number
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Sets progress purchasePointView to show on the bottom of the list when loading starts.
     *
     * @param layoutResId layout resource ID
     */
    public void setProgressView(int layoutResId) {
        setProgressView(LayoutInflater
            .from(getContext())
            .inflate(layoutResId, this, false));
    }

    public void setProgressView(View view) {
        progressView = view;
        adapterWrapper.notifyDataSetChanged();
    }

    /**
     * If async operation completed you may want to call this method to hide progress purchasePointView.
     *
     * @param refreshing {@code true} if list is currently refreshing, {@code false} otherwise
     */
    public void setRefreshing(boolean refreshing) {
        if (this.refreshing == refreshing) {
            return;
        }
        this.refreshing = refreshing;
        post(new Runnable() {
            @Override
            public void run() {
                adapterWrapper.notifyDataSetChanged();
            }
        });
    }

    private final class AdapterWrapper extends Adapter {
        private static final int PROGRESS_VIEW_TYPE = - 1;

        private final BaseRecyclerAdapter adapter;

        private ProgressViewHolder progressViewHolder;
        private BaseRecyclerPager pager;

        public AdapterWrapper(BaseRecyclerAdapter adapter) {
            if (adapter == null) {
                throw new NullPointerException("adapter is null");
            }
            this.adapter = adapter;
            setHasStableIds(adapter.hasStableIds());
        }

        @Override
        public int getItemCount() {
            int itemCount = getInnerAdapterCount() + (refreshing && progressView != null ? 1 : 0);
            return getHeaderCount() + getFooterCount() + itemCount;
        }

        private int getHeaderCount(){
            return mHeaderViews != null ? mHeaderViews.size() : 0;
        }
        private int getFooterCount(){
            return mFooterViews != null ? mFooterViews.size() : 0;
        }

        @Override
        public long getItemId(int position) {
            return position <= getInnerAdapterCount() ? NO_ID : adapter.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            boolean isRefreshingView = refreshing & position == getInnerAdapterCount() + getHeaderCount();
            int footerOtherViewCount = (getInnerAdapterCount() + getHeaderCount() + (refreshing && progressView != null ? 1 : 0));
            if (getHeaderCount() > position) {
                return TYPE_HEADER_VIEW + position;
            } else if (isRefreshingView) {
                return PROGRESS_VIEW_TYPE;
            } else if (getFooterCount() > position - footerOtherViewCount && position >= footerOtherViewCount) {
                return TYPE_FOOTER_VIEW + (position - footerOtherViewCount);
            } else {
                return adapter.getItemViewType(position - getHeaderCount());
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == PROGRESS_VIEW_TYPE) {
                return progressViewHolder = new ProgressViewHolder(adapter,progressView);
            } else if (viewType < (TYPE_HEADER_VIEW + getHeaderCount())) {
                return new WapprerViewHolder(adapter, mHeaderViews.get(viewType - TYPE_HEADER_VIEW));
            } else if (viewType >= TYPE_FOOTER_VIEW && viewType < PROGRESS_VIEW_TYPE) {
                return new WapprerViewHolder(adapter, mFooterViews.get(viewType - TYPE_FOOTER_VIEW));
            } else {
                return adapter.onCreateViewHolder(parent, viewType);
            }
        }

        private int getInnerAdapterCount(){
            return adapter != null ? adapter.getItemCount() : 0;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (adapter != null) {
                adapter.onBindViewHolder(((BaseViewHolder) holder), position - (holder.getItemViewType() < (TYPE_HEADER_VIEW + getHeaderCount()) ? 0 : getHeaderCount() ));
                if (holder.getItemViewType() != PROGRESS_VIEW_TYPE
                    && position == (getInnerAdapterCount()) + getHeaderCount()  - threshold) {
                    if( pager != null && pager.shouldLoad() == true) {
                        setRefreshing(true);
                        pager.loadNextPage(getInnerAdapterCount());
                    }
                }
            }

        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            super.onDetachedFromRecyclerView(recyclerView);
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            return holder == progressViewHolder || adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            if (holder == progressViewHolder) {
                return;
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            if (holder == progressViewHolder) {
                return;
            }
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            if (holder == progressViewHolder) {
                return;
            }
            adapter.onViewRecycled(holder);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);
            adapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            super.unregisterAdapterDataObserver(observer);
            adapter.unregisterAdapterDataObserver(observer);
        }

        public void setPager(BaseRecyclerPager pager) {
            this.pager = pager;
        }

        public Adapter<ViewHolder> getAdapter() {
            return adapter;
        }

    }

    private static final class ProgressViewHolder extends BaseViewHolder {

        public ProgressViewHolder(BaseRecyclerAdapter adapter, View view) {
            super(adapter,view);
        }
        public ProgressViewHolder(BaseRecyclerAdapter adapter, ViewGroup parent, int resId) {
            super(adapter, parent, resId);
        }

        @Override
        public void onBindView(BaseRecyclerItem item, int position) {

        }
    }

    private static final class WapprerViewHolder extends BaseViewHolder{

        public WapprerViewHolder(BaseRecyclerAdapter adapter, ViewGroup parent, int resId) {
            super(adapter, parent, resId);
        }

        public WapprerViewHolder(BaseRecyclerAdapter adapter, View itemView) {
            super(adapter, itemView);
        }

        @Override
        public void onBindView(BaseRecyclerItem item, int position) {

        }
    }
}