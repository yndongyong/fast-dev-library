package org.fastandroid.myapplication;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.afollestad.materialdialogs.internal.MDAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.yndongyong.fastandroid.base.FaBaseActivity;
import org.yndongyong.fastandroid.component.refreshlayout.DataSource;
import org.yndongyong.fastandroid.component.refreshlayout.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

@EActivity
public class StaggeredActivity extends FaBaseActivity {

    @ViewById(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @ViewById(R.id.rv_photos)
    RecyclerView mRecyclerView;

    PhotoAdapter mAdapter;

    List<PhotoEntity> lists = new ArrayList<>();


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_staggered;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTransitionMode = TransitionMode.RIGHT;
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews() {
        mRefreshLayout.setEmptyImage(R.mipmap.ico_empty);
        mRefreshLayout.setErrorImage(R.mipmap.ico_empty);
        BGARefreshViewHolder viewholder = new BGANormalRefreshViewHolder(this, true);
        viewholder.setLoadingMoreText("正在卖力加载...");

        mRefreshLayout.setRefreshViewHolder(viewholder, true);
        mRefreshLayout.setDataSource(new DataSource(this) {
            @Override
            public void refreshData() {
                
            }

            @Override
            public boolean loadMore() {
                return false;
            }
        });

        mAdapter = new PhotoAdapter(mRecyclerView);


        lists.add(new PhotoEntity("http://ww1.sinaimg.cn/mw690/718878b5jw1f4rcmu8tl1j21jk111e6s.jpg", "分类一", 20160708));
        lists.add(new PhotoEntity("http://ww1.sinaimg.cn/mw690/718878b5jw1f4rcmu8tl1j21jk111e6s.jpg", "分类一", 20160708));
        lists.add(new PhotoEntity("http://ww1.sinaimg.cn/mw690/718878b5jw1f4rcmu8tl1j21jk111e6s.jpg", "分类一", 20160708));
        lists.add(new PhotoEntity("http://ww1.sinaimg.cn/mw690/718878b5jw1f4rcmu8tl1j21jk111e6s.jpg", "分类一", 20160708));
        lists.add(new PhotoEntity("http://ww1.sinaimg.cn/mw690/718878b5jw1f4rcmu8tl1j21jk111e6s.jpg", "分类二", 20160709));
        lists.add(new PhotoEntity("http://ww1.sinaimg.cn/mw690/718878b5jw1f4rcmu8tl1j21jk111e6s.jpg", "分类二", 20160709));
        lists.add(new PhotoEntity("http://ww1.sinaimg.cn/mw690/718878b5jw1f4rcmu8tl1j21jk111e6s.jpg", "分类二", 20160709));
        lists.add(new PhotoEntity("", "分类二", 20160709));
        lists.add(new PhotoEntity("", "分类二", 20160709));
        lists.add(new PhotoEntity("", "分类二", 20160709));
        lists.add(new PhotoEntity("", "分类二", 20160709));
        lists.add(new PhotoEntity("", "分类二", 20160709));


        mAdapter.setDatas(lists);

        mRecyclerView.setAdapter(mAdapter);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        
        mRecyclerView.setItemAnimator(new FadeInLeftAnimator());
    }
}
