package org.yndongyong.fastandroid.component.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.utils.AbLogUtil;
import org.yndongyong.fastandroid.utils.AbStrUtil;

import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * 上拉刷新，下拉加载的布局，可以饱和任意子view
 * Created by Dong on 2016/5/15.
 */
public class RefreshLayout extends BGARefreshLayout implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final String TAG = RefreshLayout.class.getSimpleName();
    private Context mContext;
    private DataSource mDataSource;
    private RefreshLayoutHelper refreshLayoutHelper;
    private View mContentView;
    private View mEmptyView;
    private boolean isLoadMore = true;
    public boolean isRefreshing = false;
    private int emptyImage;
    private int errorImage;

    public int getErrorImage() {
        return this.errorImage;
    }

    public void setErrorImage(int errorImage) {
        this.errorImage = errorImage;
    }

    public int getEmptyImage() {
        return this.emptyImage;
    }

    public void setEmptyImage(int emptyImage) {
        this.emptyImage = emptyImage;
    }

    public RefreshLayout(Context context) {
        this(context,null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        this.emptyImage = R.mipmap.ic_empty;
//        this.errorImage = R.mipmap.ic_error;
        this.mContext = context;
    }

    public void closeLoadMore() {
        this.isLoadMore = false;
    }
    
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContentView = this.getChildAt(1);
        this.refreshLayoutHelper = new RefreshLayoutHelper(this.mContext);
        this.setDelegate(this);
    }

    public DataSource getDataSource() {
        return this.mDataSource;
    }

    public void setDataSource(DataSource dataSource) {
        // TODO: 2016/5/15 更改bga的风格 
        this.setRefreshViewHolder(new BGANormalRefreshViewHolder(this.mContext, this.isLoadMore));
//        this.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(this.mContext, this.isLoadMore));
//        this.setRefreshViewHolder(new BGAStickinessRefreshViewHolder(this.mContext, this.isLoadMore));
//        this.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(this.mContext, this.isLoadMore));
        
        this.mDataSource = dataSource;
    }

    public RefreshLayoutHelper getRefreshLayoutHelper() {
        return this.refreshLayoutHelper;
    }

    public void setRefreshLayoutHelper(RefreshLayoutHelper refreshLayoutHelper) {
        this.refreshLayoutHelper = refreshLayoutHelper;
    }

    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        this.isRefreshing = true;
        this.mDataSource.refreshData();
    }

    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        return this.mDataSource.loadMore();
    }

    public void showContentView() {
        AbLogUtil.d(TAG,"showContentView()");
        this.mContentView.setVisibility(View.VISIBLE);
        if (this.mEmptyView != null) {
            this.mEmptyView.setVisibility(View.GONE);
        }

    }

    public void showEmptyView() {
        AbLogUtil.d(TAG,"showEmptyView()");
        if (this.mEmptyView == null) {
            this.mEmptyView = this.getRefreshLayoutHelper().getEmptyView();
            this.addView(this.mEmptyView);
            this.refreshLayoutHelper.getImageView().setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RefreshLayout.this.showLoadingView();
                    RefreshLayout.this.mDataSource.refreshData();
                }
            });
        }

        this.refreshLayoutHelper.getCircularProgressBar().setVisibility(View.GONE);
        this.refreshLayoutHelper.getImageView().setVisibility(View.VISIBLE);
        this.refreshLayoutHelper.getImageView().setImageResource(this.emptyImage);
        this.refreshLayoutHelper.getTvMsg().setVisibility(View.VISIBLE);
        this.refreshLayoutHelper.getTvMsg().setText(this.refreshLayoutHelper.getEmptyInfoStr());
        this.mEmptyView.setVisibility(View.VISIBLE);
        this.mContentView.setVisibility(View.GONE);
    }

    public void showErrorView(String errorMsg) {
        AbLogUtil.d(TAG,"showErrorView()");
        if (this.mEmptyView == null) {
            this.mEmptyView = this.getRefreshLayoutHelper().getEmptyView();
            this.addView(this.mEmptyView);
            this.refreshLayoutHelper.getImageView().setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RefreshLayout.this.showLoadingView();
                    RefreshLayout.this.mDataSource.refreshData();
                }
            });
        }

        this.refreshLayoutHelper.getCircularProgressBar().setVisibility(View.GONE);
        this.refreshLayoutHelper.getImageView().setVisibility(View.VISIBLE);
        this.refreshLayoutHelper.getImageView().setImageResource(this.errorImage);
        this.refreshLayoutHelper.getTvMsg().setVisibility(View.VISIBLE);
        this.refreshLayoutHelper.getTvMsg().setText(AbStrUtil.isEmpty(errorMsg) ? this.refreshLayoutHelper.getErrorMsg() : errorMsg);
        this.mEmptyView.setVisibility(View.VISIBLE);
        this.mContentView.setVisibility(View.GONE);
    }

    public void showLoadingView() {
        AbLogUtil.d(TAG,"showLoadingView()");
        if (this.mEmptyView == null) {
            this.mEmptyView = this.getRefreshLayoutHelper().getEmptyView();
            this.addView(this.mEmptyView);
            this.refreshLayoutHelper.getImageView().setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    RefreshLayout.this.showLoadingView();
                    RefreshLayout.this.mDataSource.refreshData();
                }
            });
        }

        this.refreshLayoutHelper.getCircularProgressBar().setVisibility(View.VISIBLE);
        this.refreshLayoutHelper.getImageView().setVisibility(View.GONE);
        this.refreshLayoutHelper.getTvMsg().setVisibility(View.VISIBLE);
        this.refreshLayoutHelper.getTvMsg().setText(this.refreshLayoutHelper.getLoadingInfoStr());
        this.mEmptyView.setVisibility(View.VISIBLE);
        this.mContentView.setVisibility(View.GONE);
    }

    public void endRefreshing() {
        AbLogUtil.d(TAG,"endRefreshing()");
        this.isRefreshing = false;
        super.endRefreshing();
    }

    public int getContentViewVisibility() {
        return this.mContentView.getVisibility();
    }
}
