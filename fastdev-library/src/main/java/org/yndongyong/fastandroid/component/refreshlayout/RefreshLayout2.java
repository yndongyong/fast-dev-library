package org.yndongyong.fastandroid.component.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.utils.AbLogUtil;
import org.yndongyong.fastandroid.utils.AbStrUtil;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * 上拉刷新，下拉加载的布局，可以包含任意子view ，使用LoadingStatusHelper类
 * Created by Dong on 2016/5/15.
 */
public class RefreshLayout2 extends BGARefreshLayout implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final String TAG = RefreshLayout2.class.getSimpleName();

    private Context mContext;
    private DataSource mDataSource;
    private View mContentView;
    private View mStatusView;
    public boolean isRefreshing = false;

    //是否 显示 statusView ,为了防止在网络工具类中控制了 加载状态
    protected boolean isShowStatusLoading = true;

    private LoadingStatusHelper mLoadingStatusHelper;


    public RefreshLayout2(Context context) {
        this(context, null);
    }

    public RefreshLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContentView = this.getChildAt(1);
        this.mLoadingStatusHelper = new NormalLoadingStatusHelper(mContext);
        this.setDelegate(this);
    }

    public void setDataSource(DataSource dataSource) {
        this.mDataSource = dataSource;
    }

    public void setShowStatusLoading(boolean showStatusLoading) {
        isShowStatusLoading = showStatusLoading;
    }

    /**
     * 定义下拉刷新 ，上拉加载相关的view风格
     *
     * @param viewHolder
     */
    public void setRefreshViewHolder(BGARefreshViewHolder viewHolder) {
        super.setRefreshViewHolder(viewHolder);
    }

    /**
     * define the loading style, error ,data empty style
     *
     * @param statusHelper
     */
    public void setLoadingStatusHelper(LoadingStatusHelper statusHelper) {
        this.mLoadingStatusHelper = statusHelper;
    }

    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        this.isRefreshing = true;
        this.mDataSource.refreshData();
    }

    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        return this.mDataSource.loadMore();
    }

    /**
     * show container view
     */
    public void showContentView() {
        AbLogUtil.d(TAG, "showContentView()");
        this.mContentView.setVisibility(View.VISIBLE);
        if (this.mStatusView != null) {
            this.mStatusView.setVisibility(View.GONE);
        }
    }

    /**
     * Show empty view when the dataSet is empty
     */
    public void showEmptyView() {
        AbLogUtil.d(TAG, "showEmptyView()");
        if (this.mStatusView == null) {
            this.mStatusView = this.mLoadingStatusHelper.getRefreshStatusView();
            this.addView(this.mStatusView);
            this.mLoadingStatusHelper.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RefreshLayout2.this.showLoadingView();
                    RefreshLayout2.this.mDataSource.refreshData();
                }
            });
        }
        if (isShowStatusLoading) {
            this.mLoadingStatusHelper.changeToDataSetEmpty();
            this.mStatusView.setVisibility(View.VISIBLE);
        }
        this.mContentView.setVisibility(View.GONE);

    }

    /**
     * Show empty view when had dataSet failed;
     */
    public void showErrorView() {
        AbLogUtil.d(TAG, "showErrorView()");
        if (this.mStatusView == null) {
            this.mStatusView = this.mLoadingStatusHelper.getRefreshStatusView();
            this.addView(this.mStatusView);
            this.mLoadingStatusHelper.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RefreshLayout2.this.showLoadingView();
                    RefreshLayout2.this.mDataSource.refreshData();
                }
            });
        }
        if (isShowStatusLoading) {
            this.mLoadingStatusHelper.changeToInterfaceError();
            this.mStatusView.setVisibility(View.VISIBLE);
        }
        this.mContentView.setVisibility(View.GONE);
    }

    /**
     * Show loading view when acquiring dataSet
     */
    public void showLoadingView() {
        AbLogUtil.d(TAG, "showLoadingView()");
        if (this.mStatusView == null) {
            this.mStatusView = this.mLoadingStatusHelper.getRefreshStatusView();
            this.addView(this.mStatusView);
            this.mLoadingStatusHelper.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RefreshLayout2.this.showLoadingView();
                    RefreshLayout2.this.mDataSource.refreshData();
                }
            });
        }
        if (isShowStatusLoading) {
            this.mLoadingStatusHelper.changeToLoading();
            this.mStatusView.setVisibility(View.VISIBLE);
        }
        this.mContentView.setVisibility(View.GONE);
    }

    /**
     * End the refreshing status;
     */
    public void endRefreshing() {
        AbLogUtil.d(TAG, "endRefreshing()");
        this.isRefreshing = false;
        super.endRefreshing();
    }

    /**
     * End the loading status;
     */
    public void endLoadingMore() {
        AbLogUtil.d(TAG, "endRefreshing()");
        super.endLoadingMore();
    }
}
