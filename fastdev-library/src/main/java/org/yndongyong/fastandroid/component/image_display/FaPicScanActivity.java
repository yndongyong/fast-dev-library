package org.yndongyong.fastandroid.component.image_display;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.base.FaBaseSwipeBackActivity;
import org.yndongyong.fastandroid.component.slider.SliderLayout;
import org.yndongyong.fastandroid.component.slider.SliderTypes.PhotoViewSliderView;
import org.yndongyong.fastandroid.component.slider.Tricks.InfiniteViewPager;
import org.yndongyong.fastandroid.viewmodel.SerializableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/6/2.
 */
public class FaPicScanActivity extends FaBaseSwipeBackActivity implements InfiniteViewPager.OnPageChangeListener {
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_picscan;
    }

    public static final String EXTRA_IMAGES = "EXTRA_IMAGES";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_CURRENT_INDEX = "EXTRA_CURRENT_INDEX";
    SliderLayout slBanner;
    private List<PicScanModel> mLisPics = new ArrayList();
    private String mTitle;
    private int mCurrentIndex = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
        this.slBanner = ((SliderLayout) findViewById(R.id.sl_banner));
        this.mToolbar.getBackground().setAlpha(0);


        this.mLisPics = ((SerializableList) getIntent().getSerializableExtra("EXTRA_IMAGES")).getLis();
        this.mTitle = getIntent().getStringExtra("EXTRA_TITLE");
        this.mCurrentIndex = getIntent().getIntExtra("EXTRA_CURRENT_INDEX", this.mCurrentIndex);
//        setTitle(this.mTitle);
//        setTitle(mCurrentIndex + "/" + mLisPics.size());
        for (int i = 0; i < this.mLisPics.size(); i++) {
            PhotoViewSliderView sliderView = new PhotoViewSliderView(this.mContext);
            sliderView.error(R.drawable.default_error);

            if (!TextUtils.isEmpty(((PicScanModel) this.mLisPics.get(i)).getUrl()))
                sliderView.image(((PicScanModel) this.mLisPics.get(i)).getUrl());
            else if (((PicScanModel) this.mLisPics.get(i)).getRes() != 0)
                sliderView.image(((PicScanModel) this.mLisPics.get(i)).getRes());
            else {
                sliderView.empty(R.drawable.default_error);
            }

            sliderView.description(((PicScanModel) this.mLisPics.get(i)).getRemark());

            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            sliderView.bundle(bundle);
            this.slBanner.addSlider(sliderView);
        }
        this.slBanner.addOnPageChangeListener(this);
        this.slBanner.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        this.slBanner.setCurrentPosition(this.mCurrentIndex);

    }

    @Override
    protected void onDestroy() {
        this.slBanner.stopAutoCycle();
        super.onDestroy();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position++;
//        setTitle(position + "/" + mLisPics.size());
//        showToolBar();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        hideToolBar();
    }

    void showToolBar() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mToolbar, "translationY", -mToolbar.getHeight(),
                0);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    void hideToolBar() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mToolbar, "translationY", 0,
                -mToolbar.getHeight());
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
