package org.fastandroid.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.yndongyong.fastandroid.base.FaBaseActivity;
import org.yndongyong.fastandroid.component.image_display.FaPicScanActivity;
import org.yndongyong.fastandroid.component.image_display.PicScanModel;
import org.yndongyong.fastandroid.component.refreshlayout.DataSource;
import org.yndongyong.fastandroid.component.refreshlayout.RefreshLayout;
import org.yndongyong.fastandroid.okhttp.OkHttpUtils;
import org.yndongyong.fastandroid.okhttp.callback.Callback;
import org.yndongyong.fastandroid.view.dialog.ActionSheetDialog;
import org.yndongyong.fastandroid.view.dialog.IosDialog;
import org.yndongyong.fastandroid.view.wheel.city.AddressData;
import org.yndongyong.fastandroid.view.wheel.city.OnWheelChangedListener;
import org.yndongyong.fastandroid.view.wheel.city.WheelView;
import org.yndongyong.fastandroid.view.wheel.city.adapters.AbstractWheelTextAdapter;
import org.yndongyong.fastandroid.view.wheel.city.adapters.ArrayWheelAdapter;
import org.yndongyong.fastandroid.view.wheel.time.TimepickerDialog;
import org.yndongyong.fastandroid.viewmodel.SerializableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

@EActivity
public class MainActivity extends FaBaseActivity {

    private String cityTxt;

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
        
        d("onCreate() toolbar id:"+mToolbar.getId());
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
        // TODO: 2016/6/13 注册广播监听，恢复onPause方法中释放的资源  
    }

    @Override
    protected void onPause() {
        super.onPause();
        d("onPause()");
        // TODO: 2016/6/13 执行一些释放动画资源，注册的广播等，等的轻量级操作 ，
        // 断开系统资源链接，一些用户输入的内容保存到内存中
    }

    @Override
    protected void onStop() {
        d("onStop()");
        super.onStop();
        //使用返回框架，当前层的parentactivity一直处于Pause状态，
        // TODO: 2016/6/13 在生命周期中，执行一些重量级的操作，比如数据库存储，sd卡读写
        //释放一些，连接资源，有时候由于用户的操作，onDestory方法不一定会被调用到
    }

    ////activity需要被重建时，会执行 
    // TODO: 2016/6/13 跳转到其他的activity或者是点击Home都会执行 
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        
    }

    @Override
    protected void onDestroy() {
        d("onDestroy()");
        super.onDestroy();
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
        BGARefreshViewHolder viewholder = new BGANormalRefreshViewHolder(this, true);
        viewholder.setLoadingMoreText("正在卖力加载...");

        mRefreshLayout.setRefreshViewHolder(viewholder, true);

        mUserInfoAdapter = new UserInfoAdapter(mRecyclerView);
        mUserInfoAdapter.setDatas(userEntities);

        mRecyclerView.setAdapter(mUserInfoAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        mRecyclerView.setItemAnimator(new FadeInLeftAnimator());

        mUserInfoAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup viewGroup, View view, int position) {
                UserEntity entity = mUserInfoAdapter.getItem(position);
//                showToast(entity.getUsername()+":"+entity.getAge());
//                readyGo(SecondActivity_.class);
                switch (entity.getUsername()) {
                    case "alertSheet1":
                        new ActionSheetDialog(MainActivity.this)
                                .builder().setCancelable(false)
                                .setCanceledOnTouchOutside(false)
                                .setTitle("清空消息列表后，聊天记录依然保留，确定要清空消息列表？")
                                .addSheetItem("清空消息列表", ActionSheetDialog.SheetItemColor.Blue, new
                                        ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                Toast.makeText(MainActivity.this, "你点了 " + which + " ", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                .show();
                        break;
                    case "alertSheet2":
                        new ActionSheetDialog(MainActivity.this)
                                .builder()
//                                .setCancelable(false)
//                                .setCanceledOnTouchOutside(false)
                                .addSheetItem("item 1", ActionSheetDialog.SheetItemColor.Blue, new
                                        ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                Toast.makeText(MainActivity.this, "你点了 " + which + " ", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                .addSheetItem("item 2", ActionSheetDialog.SheetItemColor.Blue, new
                                        ActionSheetDialog.OnSheetItemClickListener() {
                                            @Override
                                            public void onClick(int which) {
                                                Toast.makeText(MainActivity.this, "你点了 " + which + " ", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                .setNegativeButton("忽  略", ActionSheetDialog.SheetItemColor.Blue)
                                .show();
                        break;
                    case "iosDialog3":
                        final IosDialog dialog = new IosDialog(MainActivity.this)
                                .builder()
                                .setTitle("提示")
                                .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
//                                .setEditText("1111111111111")
//                        .setView(timepickerview1)
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                        dialog.setPositiveButton("保存", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.show();

                        break;
                    case "iosDialog4":
                        final IosDialog dialog2 = new IosDialog(MainActivity.this)
                                .builder()
                                .setTitle("提示")
                                .setMsg("请输入账号")
                                .setEditText(null, "账号")
//                        .setView(timepickerview1)
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                        /*dialog2.setPositiveButton("保存", new View.OnClickListener() {
                            @Override
                            public void onResult(View v) {
                                d(v.toString());
                                Toast.makeText(MainActivity.this, v.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });*/
                        dialog2.setPositiveButton("保存", new IosDialog.OnEditResultListener() {
                            @Override
                            public void onResult(CharSequence charSequence) {
                                Toast.makeText(MainActivity.this, charSequence.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog2.show();
                        break;
                    case "timepicker":
                        TimepickerDialog.createDialog(MainActivity.this, "选择时间", "确定", new
                                TimepickerDialog
                                        .OnTimePickerResultListener() {
                                    @Override
                                    public void onResult(String timeStr) {
                                        Toast.makeText(MainActivity.this, "选择的时间是:" + timeStr, Toast.LENGTH_SHORT).show();
                                    }
                                }).show();

                        break;
                    case "cityPicker":
                        View view2 = dialogm();
                        IosDialog dialog1 = new IosDialog(mContext)
                                .builder()
                                .setTitle("请选择收货地址")
                                .setView(view2)
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });

                        dialog1.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mContext, cityTxt, Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog1.show();
                        break;
                    case "qrcoder1":
//                        readyGoForResult(CaptureActivity.class, 200);
                        break;
                    case "qrcoder2":
//                        readyGoForResult(CaptureSimpleActivity.class, 300);
                        break;
                    case "FaSingleImageActivity":

                        SecondActivity_.intent(MainActivity.this).start();
                        break;
//                    list.add(new UserEntity("DoubanLoading", 25));
//                    list.add(new UserEntity("FaPicScanActivity", 25));
                    case "DoubanLoading":
                        readyGo(ThirdActivity.class);
                        break;
                    case "FaPicScanActivity":
                        SerializableList<PicScanModel> imags = new SerializableList<PicScanModel>();
                        List<PicScanModel> list = new ArrayList<PicScanModel>();
                        PicScanModel picScanModel;

                        picScanModel = new PicScanModel();
                        picScanModel.setRemark("第一章图片");
                        picScanModel.setUrl("http://ww1.sinaimg.cn/mw690/718878b5jw1f4rcmu8tl1j21jk111e6s.jpg");
                        list.add(picScanModel);

                        picScanModel = new PicScanModel();
                        picScanModel.setRemark("第二章图片");
                        picScanModel.setUrl("http://ww4.sinaimg.cn/mw690/006nS8nZgw1f4gpwhzmb9j30u01hcafa.jpg");
                        list.add(picScanModel);

                        picScanModel = new PicScanModel();
                        picScanModel.setRemark("第三章图片");
                        picScanModel.setUrl("http://ww3.sinaimg.cn/mw690/ad673c42gw1f4gxop3jufj20ku112ah9.jpg");
                        list.add(picScanModel);

                        picScanModel = new PicScanModel();
                        picScanModel.setRemark("第四章图片");
                        picScanModel.setUrl("http://ww1.sinaimg.cn/mw690/c4ff9961gw1f4gx7xmtprj20j60y3grz.jpg");
                        list.add(picScanModel);

                        imags.setLis(list);

                        Intent intent = new Intent(mContext, FaPicScanActivity.class);
                        intent.putExtra(FaPicScanActivity.EXTRA_CURRENT_INDEX, 0);
                        intent.putExtra(FaPicScanActivity.EXTRA_IMAGES, imags);
                        readyGo(intent);
                        break;
                }

            }
        });
        mUserInfoAdapter.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup viewGroup, View childView, int position) {
                TextView tvAge = (TextView) childView;
                showToast("child:" + tvAge.getText());
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                String txt = data.getExtras().getString("content");
                Toast.makeText(mContext, txt, Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 300) {
            if (resultCode == RESULT_OK) {
                String txt = data.getExtras().getString("content");
                Toast.makeText(mContext, txt, Toast.LENGTH_SHORT).show();
            }
        }
    }

    Dialog mProgressDialog;

    /**
     * 刷新
     */
    private void refresh() {
//        String url = "http://120.24.160.24/api/history/content/2/1";
        String url = "http://gank.io/api/history/content/2/1";
        OkHttpUtils.getInstance().debug(TAG);

        OkHttpUtils
                .get()
                .url(url)
                .addParams("dong", "dong")
                .build()
                .execute(new Callback<GankResponse>() {
                    @Override
                    public GankResponse parseNetworkResponse(Response response) throws IOException {
                        return new Gson().fromJson(response.body().string(), GankResponse.class);
                    }

                    @Override
                    public void onBefore(Request request) {
//                        mProgressDialog = DYProgressHUD.createLoading(MainActivity.this,
//                                "loading...");
                        mProgressDialog = ProgressDialog.show(MainActivity.this, null, "loading");
                        mProgressDialog.show();
//                        mRefreshLayout.showLoadingView();
                        mUserInfoAdapter.getDatas().clear();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
//                        mRefreshLayout.showErrorView(AbAppException.getError(e));

                        List<UserEntity> list = new ArrayList<UserEntity>();
                        list.add(new UserEntity("alertSheet1", 23));
                        list.add(new UserEntity("alertSheet2", 24));
                        list.add(new UserEntity("iosDialog3", 25));
                        list.add(new UserEntity("iosDialog4", 23));
                        list.add(new UserEntity("timepicker", 24));
                        list.add(new UserEntity("cityPicker", 25));
                        list.add(new UserEntity("qrcoder1", 25));
                        list.add(new UserEntity("qrcoder2", 25));
                        list.add(new UserEntity("FaSingleImageActivity", 25));
                        list.add(new UserEntity("DoubanLoading", 25));
                        list.add(new UserEntity("FaPicScanActivity", 25));

                        mRefreshLayout.showContentView();
//                            mRefreshLayout.showEmptyView();
//                           
                        mUserInfoAdapter.clear();
                        mUserInfoAdapter.addNewDatas(list);//添加原有内容的最上面
                    }

                    @Override
                    public void onResponse(GankResponse response) {
                        Toast.makeText(mContext, "response.Error:" + response.Error, Toast.LENGTH_SHORT).show();
                        List<UserEntity> list = new ArrayList<UserEntity>();
                        list.add(new UserEntity("alertSheet1", 23));
                        list.add(new UserEntity("alertSheet2", 24));
                        list.add(new UserEntity("iosDialog3", 25));
                        list.add(new UserEntity("iosDialog4", 23));
                        list.add(new UserEntity("timepicker", 24));
                        list.add(new UserEntity("cityPicker", 25));
                        list.add(new UserEntity("qrcoder1", 25));
                        list.add(new UserEntity("qrcoder2", 25));
                        list.add(new UserEntity("FaSingleImageActivity", 25));
                        list.add(new UserEntity("DoubanLoading", 25));
                        list.add(new UserEntity("FaPicScanActivity", 25));

                        mRefreshLayout.showContentView();
//                            mRefreshLayout.showEmptyView();
//                           
                        mUserInfoAdapter.clear();
                        mUserInfoAdapter.addNewDatas(list);//添加原有内容的最上面
//                        mRefreshLayout.endRefreshing();
                    }

                    @Override
                    public void onAfter() {
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                });
    }

    /**
     * 上拉加载
     *
     * @return
     */
    private boolean load() {
        if (mUserInfoAdapter.getDatas().size() > 6) {
//            showSnackBar(findViewById(android.R.id.content),"数据已经全部加载完毕");
//            showSnackBar(getFloatTargetView(),"数据已经全部加载完毕");
            showToast("数据加载完毕");
            return false;
        }

        String url = "http://gank.io/api/history/content/2/1";

        OkHttpUtils
                .get()
                .url(url)
                .addParams("dong", "dong")
                .build()
                .execute(new Callback<GankResponse>() {
                    @Override
                    public GankResponse parseNetworkResponse(Response response) throws IOException {
                        return new Gson().fromJson(response.body().string(), GankResponse.class);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(GankResponse response) {
                        Toast.makeText(mContext, "response.Error:" + response.Error, Toast.LENGTH_SHORT).show();
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

        return true;
    }

    private View dialogm() {
        View contentView = LayoutInflater.from(mContext).inflate(
                org.yndongyong.fastandroid.R.layout.wheelcity_cities_layout, null);
        final WheelView country = (WheelView) contentView
                .findViewById(org.yndongyong.fastandroid.R.id.wheelcity_country);
        country.setVisibleItems(3);
        country.setViewAdapter(new CountryAdapter(mContext));

        final String cities[][] = AddressData.CITIES;
        final String ccities[][][] = AddressData.COUNTIES;
        final WheelView city = (WheelView) contentView
                .findViewById(org.yndongyong.fastandroid.R.id.wheelcity_city);
        city.setVisibleItems(0);

        // 地区选择
        final WheelView ccity = (WheelView) contentView
                .findViewById(org.yndongyong.fastandroid.R.id.wheelcity_ccity);
        ccity.setVisibleItems(0);// 不限城市

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCities(city, cities, newValue);
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        city.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updatecCities(ccity, ccities, country.getCurrentItem(),
                        newValue);
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        ccity.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                        + " | "
                        + AddressData.CITIES[country.getCurrentItem()][city
                        .getCurrentItem()]
                        + " | "
                        + AddressData.COUNTIES[country.getCurrentItem()][city
                        .getCurrentItem()][ccity.getCurrentItem()];
            }
        });

        country.setCurrentItem(1);// 设置北京
        city.setCurrentItem(1);
        ccity.setCurrentItem(1);
        return contentView;
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(mContext,
                cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Updates the ccity wheel
     */
    private void updatecCities(WheelView city, String ccities[][][], int index,
                               int index2) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(mContext,
                ccities[index][index2]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] = AddressData.PROVINCES;

        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, org.yndongyong.fastandroid.R.layout.wheelcity_country_layout, NO_RESOURCE);
            setItemTextResource(org.yndongyong.fastandroid.R.id.wheelcity_country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }
}
