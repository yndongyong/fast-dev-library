package org.fastandroid.myapplication;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.yndongyong.fastandroid.base.FaBaseActivity;
import org.yndongyong.fastandroid.component.refreshlayout.DataSource;
import org.yndongyong.fastandroid.component.refreshlayout.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

@EActivity
public class MainActivity extends FaBaseActivity {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       
        mTransitionMode = TransitionMode.DEFAULT;
        super.onCreate(savedInstanceState);
        d("onCreate()");
//        mToolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setTitle("沉浸式状态栏");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setDisplayHomeAsUpDisEnabled();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        d("onRestart()");
    }

    @Override
    protected void onResume() {
        d("onResume()");
        super.onResume();
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        d("onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        d("onStop()");
        //使用返回框架，当前层的parentactivity一直处于onPause状态，
        //
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        d("onDestroy()");
    }

    MenuItem menuItem;

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main_menu, menu);
        this.menuItem = menu.getItem(0);
        return true;
    }

    @ViewById(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    @ViewById(R.id.rv_main_userinfo)
    RecyclerView mRecyclerView;

    UserInfoAdapter mUserInfoAdapter;

    List<UserEntity> userEntities = new ArrayList<>();

    @AfterViews
    public void afterViews() {
        d("afterViews()");
        mRefreshLayout.setEmptyImage(R.mipmap.ico_empty);
        mRefreshLayout.setErrorImage(R.mipmap.ico_empty);

        mUserInfoAdapter = new UserInfoAdapter(mRecyclerView);
        mUserInfoAdapter.setDatas(userEntities);

        mRecyclerView.setAdapter(mUserInfoAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        mRecyclerView.setItemAnimator(new FadeInLeftAnimator());

        mUserInfoAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup viewGroup, View view, int position) {
                UserEntity entity = mUserInfoAdapter.getItem(position);
                showToast(entity.getUsername()+":"+entity.getAge());
                readyGo(SecondActivity.class);
            }
        });
        mUserInfoAdapter.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup viewGroup, View childView, int position) {
                TextView tvAge = (TextView) childView;
                showToast("child:"+tvAge.getText());
            }
        });
        
        mRefreshLayout.setDataSource(new DataSource(mContext) {
            @Override
            public void refreshData() {
//                调用加载的方法;
                refresh();
            }

            @Override
            public boolean loadMore() {
                return load();
            }
        });

        
        refresh();
    }

    /**
     * 刷新
     */
    private void refresh() {
        mRefreshLayout.showLoadingView();
        mUserInfoAdapter.getDatas().clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<UserEntity> list = new ArrayList<UserEntity>();
                            list.add(new UserEntity("dong", 23));
                            list.add(new UserEntity("zhi", 24));
                            list.add(new UserEntity("yong", 25));
                            mRefreshLayout.showContentView();
//                            mRefreshLayout.showEmptyView();
//                            mRefreshLayout.showErrorView("无网络连接");
                            mUserInfoAdapter.clear();
                            mUserInfoAdapter.addNewDatas(list);//添加原有内容的最上面
                            mRefreshLayout.endRefreshing();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    
                }
            }
        }).start();
    }

    /**
     * 上拉加载
     * @return
     */
    private boolean load() {
        if (mUserInfoAdapter.getDatas().size() > 6) {
//            showSnackBar(findViewById(android.R.id.content),"数据已经全部加载完毕");
//            showSnackBar(getFloatTargetView(),"数据已经全部加载完毕");
            showToast("数据加载完毕");
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<UserEntity> list = new ArrayList<UserEntity>();
                            list.add(new UserEntity("cai", 23));
                            list.add(new UserEntity("10", 24));
                            list.add(new UserEntity("yoiu", 25));
                            mUserInfoAdapter.addMoreDatas(list);
//                            使用第三方的动画框架，不能使用这个
//                            mUserInfoAdapter.notifyDataSetChanged();
                            mRefreshLayout.endLoadingMore();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
        }).start();
        
        return true;
    }


}
