package com.base.kiy.BaseKiy.BaseRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Flitto on 2017. 2. 1..
 */

public abstract class BaseViewHolder<T extends BaseRecyclerItem> extends RecyclerView.ViewHolder {
  private BaseRecyclerAdapter mAdapter;

  public BaseViewHolder(BaseRecyclerAdapter adapter, ViewGroup parent, int resId) {
    this(adapter, LayoutInflater.from(adapter.getContext()).inflate(resId, parent, false));
  }

  public BaseViewHolder(BaseRecyclerAdapter adapter, View itemView) {
    super(itemView);
    mAdapter = adapter;
  }

  public abstract void onBindView(final T item, final int position);

  public BaseRecyclerAdapter getAdapter() {
    return mAdapter;
  }

  public Context getContext() {
    return mAdapter != null ? mAdapter.getContext() : null;
  }
}
