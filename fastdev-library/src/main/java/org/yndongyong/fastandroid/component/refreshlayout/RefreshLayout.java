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
 * 上拉刷新，下拉加载的布局，可以包含任意子view
 * Created by Dong on 2016/5/15.
 */
public class RefreshLayout extends BGARefreshLayout implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final String TAG = RefreshLayout.class.getSimpleName();
    private Context mContext;
    private DataSource mDataSource;
    private RefreshLayoutLoadingHelper mLoadingHelper;
    private View mContentView;
    private View mStatusView;
    private boolean isLoadMore = true;
    public boolean isRefreshing = false;
    private int emptyImage;
    private int errorImage;

    //设置加载错误时显示的图片
    public void setErrorImage(int errorImage) {
        this.errorImage = errorImage;
    }
    //设置加载数据我空时显示的图片
    public void setEmptyImage(int emptyImage) {
        this.emptyImage = emptyImage;
    }

    public RefreshLayout(Context context) {
        this(context,null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.emptyImage = R.mipmap.ic_empty;
        this.errorImage = R.mipmap.ic_error;
        this.mContext = context;
    }

    public void closeLoadMore() {
        this.isLoadMore = false;
    }
    
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContentView = this.getChildAt(1);
        this.mLoadingHelper = new RefreshLayoutLoadingHelper(this.mContext);
        this.setDelegate(this);
    }

    public void setDataSource(DataSource dataSource) {
        this.mDataSource = dataSource;
    }
    /**
     * 定义下拉刷新 ，上拉加载相关的view风格
     * @param viewHolder
     */
    public void setRefreshViewHolder(BGARefreshViewHolder viewHolder, boolean isLoadMore) {
        this.setRefreshViewHolder(viewHolder);
        this.isLoadMore = isLoadMore;
    }

    public RefreshLayoutLoadingHelper getmLoadingHelper() {
        return this.mLoadingHelper;
    }

    /**
     * define the loading style, error ,data empty style 
     * @param refreshLayoutHelper
     */
    public void setRefreshLayoutLoadingHelper(RefreshLayoutLoadingHelper refreshLayoutHelper) {
        this.mLoadingHelper = refreshLayoutHelper;
    }

    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        this.isRefreshing = true;
        this.mDataSource.refreshData();
    }

    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        this.isLoadMore = true;
        return this.mDataSource.loadMore();
    }

    /**
     * show container view
     */
    public void showContentView() {
        AbLogUtil.d(TAG,"showContentView()");
        this.mContentView.setVisibility(View.VISIBLE);
        if (this.mStatusView != null) {
            this.mStatusView.setVisibility(View.GONE);
        }
    }

    /**
     * Show empty view when the dataSet is empty  
     */
    public void showEmptyView() {
        AbLogUtil.d(TAG,"showEmptyView()");
        if (this.mStatusView == null) {
            this.mStatusView = this.getmLoadingHelper().getStatusView();
            this.addView(this.mStatusView);
            this.mLoadingHelper.getImageView().setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RefreshLayout.this.showLoadingView();
                    RefreshLayout.this.mDataSource.refreshData();
                }
            });
        }

        this.mLoadingHelper.getCircularProgressBar().setVisibility(View.GONE);
        this.mLoadingHelper.getImageView().setVisibility(View.VISIBLE);
        this.mLoadingHelper.getImageView().setImageResource(this.emptyImage);
        this.mLoadingHelper.getTvMsg().setVisibility(View.VISIBLE);
        this.mLoadingHelper.getTvMsg().setText(this.mLoadingHelper.getEmptyInfoStr());
        this.mStatusView.setVisibility(View.VISIBLE);
        this.mContentView.setVisibility(View.GONE);

    }

    /**
     * Show empty view when had dataSet failed;
     */
    public void showErrorView(String errorMsg) {
        AbLogUtil.d(TAG,"showErrorView()");
        if (this.mStatusView == null) {
            this.mStatusView = this.getmLoadingHelper().getStatusView();
            this.addView(this.mStatusView);
            this.mLoadingHelper.getImageView().setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RefreshLayout.this.showLoadingView();
                    RefreshLayout.this.mDataSource.refreshData();
                }
            });
        }

        this.mLoadingHelper.getCircularProgressBar().setVisibility(View.GONE);
        this.mLoadingHelper.getImageView().setVisibility(View.VISIBLE);
        this.mLoadingHelper.getImageView().setImageResource(this.errorImage);
        this.mLoadingHelper.getTvMsg().setVisibility(View.VISIBLE);
        this.mLoadingHelper.getTvMsg().setText(AbStrUtil.isEmpty(errorMsg) ? this.mLoadingHelper.getErrorMsg() : errorMsg);
        this.mStatusView.setVisibility(View.VISIBLE);
        this.mContentView.setVisibility(View.GONE);
    }

    /**
     * Show loading view when acquiring dataSet  
     */
    public void showLoadingView() {
        AbLogUtil.d(TAG,"showLoadingView()");
        if (this.mStatusView == null) {
            this.mStatusView = this.getmLoadingHelper().getStatusView();
            this.addView(this.mStatusView);
            this.mLoadingHelper.getImageView().setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RefreshLayout.this.showLoadingView();
                    RefreshLayout.this.mDataSource.refreshData();
                }
            });
        }

        this.mLoadingHelper.getCircularProgressBar().setVisibility(View.VISIBLE);
        this.mLoadingHelper.getImageView().setVisibility(View.GONE);
        this.mLoadingHelper.getTvMsg().setVisibility(View.VISIBLE);
        this.mLoadingHelper.getTvMsg().setText(this.mLoadingHelper.getLoadingInfoStr());
        this.mStatusView.setVisibility(View.VISIBLE);
        this.mContentView.setVisibility(View.GONE);
    }

    /**
     * End the refreshing status;
     */
    public void endRefreshing() {
        AbLogUtil.d(TAG,"endRefreshing()");
        this.isRefreshing = false;
        super.endRefreshing();
        
       
    }
    /**
     * End the loading status;
     */
    public void endLoadingMore(){
        AbLogUtil.d(TAG,"endRefreshing()");
        this.isLoadMore = false;
        super.endLoadingMore();
        
    }
}
