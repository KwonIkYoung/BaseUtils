package com.base.kiy.BaseKiy.BaseRecyclerView;

/**
 * Created by Flitto on 2017. 2. 1..
 */

public abstract class BaseRecyclerItem {
  protected int viewType;

  public BaseRecyclerItem() {
  }

  public BaseRecyclerItem(int viewType) {
    this.viewType = viewType;
  }

  public void setViewType(int viewType) {
    this.viewType = viewType;
  }

  protected abstract int getViewType();
}
