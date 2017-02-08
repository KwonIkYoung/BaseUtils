package com.base.kiy.BaseKiy.BaseRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Flitto on 2017. 2. 1..
 */

public abstract class BaseRecyclerAdapter<T extends BaseRecyclerItem> extends RecyclerView.Adapter<BaseViewHolder> {
  private Context mContext;
  private ArrayList<T> mItems;
  protected RecyclerView.Adapter baseAdapter;

  public abstract BaseViewHolder onCreateHolder(ViewGroup parent, int viewType);
  public abstract long getLastId();

  public BaseRecyclerAdapter(Context mContext) {
    this.mContext = mContext;
    setHasStableIds(hasStableIds());
  }

  @Override
  public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return onCreateHolder(parent, viewType);
  }

  @Override
  public void onBindViewHolder(BaseViewHolder holder, int position) {
    if (holder != null && position < getItemCount()) {
      holder.onBindView( getItem(position), position);
    }
  }

  @Override
  public int getItemViewType(int position) {
    return getItem(position).getViewType();
//    return getItem(position) == null ? RecyclerView.NO_POSITION : getItem(position).getViewType();
  }

  @Override
  public int getItemCount() {
    return mItems == null ? 0 : mItems.size();
  }

  @Override
  public long getItemId(int position) {
    return position == getItemCount() ? RecyclerView.NO_ID : getItemId(position);
  }

  public void setBaseAdapter(RecyclerView.Adapter adapter) {
    this.baseAdapter = adapter;
  }

  public Context getContext(){
    return mContext;
  }

  public T getItem(int position) {
    return mItems == null? null : mItems.get(position);
  }

  public ArrayList<T> getItems(){
    return mItems;
  }

  public void addItem(T item) {
    if (mItems == null ) {
      mItems = new ArrayList<>();
    }

    mItems.add(item);
  }

  public void addItem(T item, int poistion) {
    if (mItems == null ) {
      mItems = new ArrayList<>();
    }

    mItems.add(poistion, item);
  }

  public void addItems(ArrayList<T> items){
    if (mItems == null) {
      mItems = new ArrayList<>();
    }

    mItems.addAll(items);
  }


  public void addItems(ArrayList<T> items, int position){
    if (mItems == null) {
      mItems = new ArrayList<>();
    }

    mItems.addAll(position, items);
  }

  public void setItems(ArrayList<T> items) {
    mItems = items;
  }

  public void clear(){
    if (mItems == null) {
      mItems = new ArrayList<>();
    }
    mItems.clear();
  }

}
