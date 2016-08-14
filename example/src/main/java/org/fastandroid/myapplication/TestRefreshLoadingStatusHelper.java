package org.fastandroid.myapplication;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.yndongyong.fastandroid.base.FaBaseActivity;
import org.yndongyong.fastandroid.component.refreshlayout.DataSource;
import org.yndongyong.fastandroid.component.refreshlayout.NormalLoadingStatusHelper;
import org.yndongyong.fastandroid.component.refreshlayout.RefreshLayout2;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

@EActivity(R.layout.activity_loading_status_helper)
public class TestRefreshLoadingStatusHelper extends FaBaseActivity {

    @ViewById(R.id.refreshLayout)
    RefreshLayout2 mRefreshLayout;

    @ViewById(R.id.rv_main_userinfo)
    RecyclerView mRecyclerView;
    
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_loading_status_helper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTransitionMode = TransitionMode.RIGHT;
        super.onCreate(savedInstanceState);
        setTitle("测试statushelper");
    }

    @AfterViews
    public void afterViews() {
        BGARefreshViewHolder viewholder = new BGANormalRefreshViewHolder(this, true);
        viewholder.setLoadingMoreText("正在卖力加载...");

        mRefreshLayout.setRefreshViewHolder(viewholder);

        NormalLoadingStatusHelper statusHelper = new NormalLoadingStatusHelper(this);
        statusHelper.setDataSetEmptyStr("获取的数据集为空");
        mRefreshLayout.setLoadingStatusHelper(statusHelper);
        
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        mRecyclerView.setItemAnimator(new FadeInLeftAnimator());

        mRefreshLayout.setDataSource(new DataSource(mContext) {
            @Override
            public void refreshData() {
                refresh();
            }

            @Override
            public boolean loadMore() {
                return false;
            }
        });
        
    }

    private void refresh() {
        //调用框架的loading动画，或者自己写一个       
        mRefreshLayout.showLoadingView();
        //TODO 实现网络请求

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //case1.失败
//                mRefreshLayout.showErrorView();

                mRefreshLayout.showNetWorkErrorView();

                //case2.无数据
//                mRefreshLayout.showEmptyView();

                //case3.成功
//        mRefreshLayout.showContentView();
            }
        },3000);

    }
}
