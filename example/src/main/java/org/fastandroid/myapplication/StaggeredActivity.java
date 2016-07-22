package org.fastandroid.myapplication;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

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

    int pageNum = 1;


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


        //禁止下拉刷新
//        mRefreshLayout.setPullDownRefreshEnable(false);

        mRefreshLayout.setRefreshViewHolder(viewholder, true);
        mRefreshLayout.setDataSource(new DataSource(this) {
            @Override
            public void refreshData() {
                pageNum=1;
                refresh();
            }

            @Override
            public boolean loadMore() {
                pageNum++;
                return load();
            }
        });

        mAdapter = new PhotoAdapter(this,lists);

        mRecyclerView.setAdapter(mAdapter);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

//        GridLayoutManager layoutManager1 = new GridLayoutManager(this, 3);
//        mRecyclerView.setLayoutManager(layoutManager1);
        
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        refresh();
    }

    private void refresh() {
        mAdapter.clear();
            

//        mRefreshLayout.showLoadingView();
        List<PhotoEntity> list = new ArrayList<>();
        list.add(new PhotoEntity("http://ww1.sinaimg.cn/mw690/718878b5jw1f4rcmu8tl1j21jk111e6s.jpg", "分类一", 20160708));
        list.add(new PhotoEntity("http://pic25.nipic.com/20121112/5955207_224247025000_2.jpg", "分类一", 20160708));

        list.add(new PhotoEntity("http://m2.quanjing.com/2m/fod_liv002/fo-11171537.jpg", "分类一", 20160708));
        list.add(new PhotoEntity("http://pic14.nipic.com/20110615/1347158_233357498344_2.jpg", "分类一", 20160708));

        list.add(new PhotoEntity("http://pic17.nipic.com/20111109/7674259_144501034328_2.jpg", "分类二", 20160709));
        list.add(new PhotoEntity("http://pic14.nipic.com/20110427/2944718_000916112196_2.jpg", "分类二", 20160709));
        list.add(new PhotoEntity("http://pic14.nipic.com/20110529/7011463_085102601343_2.jpg", "分类二", 20160709));
        list.add(new PhotoEntity("http://pic10.nipic.com/20101001/4438138_140843092127_2.jpg", "分类二", 20160709));
        list.add(new PhotoEntity("http://pic41.nipic.com/20140509/18696269_121755386187_2.png", "分类二", 20160709));
        list.add(new PhotoEntity("http://h.hiphotos.baidu.com/image/h%3D200/sign=cd65e7fa13d5ad6eb5f963eab1cb39a3/377adab44aed2e7394aa5a128f01a18b87d6fa49.jpg", "分类二", 20160709));
        list.add(new PhotoEntity("http://pic24.nipic.com/20121003/10754047_140022530392_2.jpg", "分类二", 20160709));
        list.add(new PhotoEntity("http://pic24.nipic.com/20121009/4744012_103328385000_2.jpg", "分类二", 20160709));


        mAdapter = new PhotoAdapter(this,list);
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setData(list);
        mRefreshLayout.showContentView();
    }

    private boolean load() {

        if (pageNum > 2) {
            Toast.makeText(mContext, "load all！", Toast.LENGTH_SHORT).show();
            return false;
        }

        List<PhotoEntity> list = new ArrayList<>();

        list.add(new PhotoEntity("http://ww1.sinaimg.cn/mw690/718878b5jw1f4rcmu8tl1j21jk111e6s" +
                ".jpg", "分类三", 20160710));
        list.add(new PhotoEntity("http://pic25.nipic.com/20121112/5955207_224247025000_2.jpg", 
                "分类三", 20160710));

        list.add(new PhotoEntity("http://m2.quanjing.com/2m/fod_liv002/fo-11171537.jpg", "分类三", 
                20160710));
        list.add(new PhotoEntity("http://pic14.nipic.com/20110615/1347158_233357498344_2.jpg", 
                "分类三", 20160710));

        list.add(new PhotoEntity("http://pic17.nipic.com/20111109/7674259_144501034328_2.jpg", 
                "分类三", 20160710));
        list.add(new PhotoEntity("http://pic14.nipic.com/20110427/2944718_000916112196_2.jpg", 
                "分类三", 20160710));
        list.add(new PhotoEntity("http://pic14.nipic.com/20110529/7011463_085102601343_2.jpg",
                "分类四", 20160711));
        list.add(new PhotoEntity("http://pic10.nipic.com/20101001/4438138_140843092127_2.jpg", 
                "分类四", 20160711));
        list.add(new PhotoEntity("http://pic41.nipic.com/20140509/18696269_121755386187_2.png", 
                "分类四", 20160711));
        list.add(new PhotoEntity("http://h.hiphotos.baidu.com/image/h%3D200/sign=cd65e7fa13d5ad6eb5f963eab1cb39a3/377adab44aed2e7394aa5a128f01a18b87d6fa49.jpg",
                "分类四", 20160711));

        list.add(new PhotoEntity("http://pic24.nipic.com/20121003/10754047_140022530392_2.jpg", 
                "分类四", 20160711));
        list.add(new PhotoEntity("http://pic24.nipic.com/20121009/4744012_103328385000_2.jpg", 
                "分类四", 20160711));


        mAdapter.setData(list);
//                            使用第三方的动画框架，不能使用这个
//                            mUserInfoAdapter.notifyDataSetChanged();
        mRefreshLayout.endLoadingMore();


        return false;
    }
}
