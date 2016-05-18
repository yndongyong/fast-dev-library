package org.yndongyong.fastandroid.component.refreshlayout;

import android.content.Context;

/**
 * 与数据加载有关
 * Created by Dong on 2016/5/15.
 */
public abstract class DataSource {
    private Context mContext;

    public DataSource(Context context)
    {
        this.mContext = context;
    }

    public boolean isEmpty() {
        return false;
    }

    /**
     * 下拉刷新数据
     */
    public abstract void refreshData();

    /**
     * 上拉加载数据
     * true,先调用加载的方法，true ，有加载数据，false，无加载数据
     * @return
     */
    public abstract boolean loadMore();
}
