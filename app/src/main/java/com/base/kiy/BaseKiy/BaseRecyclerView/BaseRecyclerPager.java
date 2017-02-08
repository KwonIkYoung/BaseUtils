package com.base.kiy.BaseKiy.BaseRecyclerView;

/**
 * Created by Flitto on 2017. 2. 8..
 */

public interface BaseRecyclerPager {
  boolean shouldLoad();
  void loadNextPage(int adapterCount);
}
