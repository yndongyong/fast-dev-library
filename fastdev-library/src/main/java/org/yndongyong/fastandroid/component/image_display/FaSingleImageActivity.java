package org.yndongyong.fastandroid.component.image_display;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.base.FaBaseActivity;
import org.yndongyong.fastandroid.view.SmoothImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 提供类是微信，微博，点击图片，查看大图的效果
 * Created by Dong on 2016/5/25.
 */
public class FaSingleImageActivity extends FaBaseActivity {


    public static final String INTENT_IMAGE_URL_TAG = "INTENT_IMAGE_URL_TAG";
    public static final String INTENT_IMAGE_BITMAP_TAG = "INTENT_IMAGE_BITMAP_TAG";
    public static final String INTENT_IMAGE_X_TAG = "INTENT_IMAGE_X_TAG";
    public static final String INTENT_IMAGE_Y_TAG = "INTENT_IMAGE_Y_TAG";
    public static final String INTENT_IMAGE_W_TAG = "INTENT_IMAGE_W_TAG";
    public static final String INTENT_IMAGE_H_TAG = "INTENT_IMAGE_H_TAG";
    private String mImageUrl;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap;

    private SmoothImageView mSmoothImageView;

    /**
     * 
     * @param act Context
     * @param loadUrl 要加载的图片 的url
     * @param loctionX 图片的原始位置 x
     * @param locationY 图片的原始位置 y
     * @param width 图片的原始宽
     * @param height 图片的原始高
     * @param bitmap 要加载的图片 和url 只能二选一
     */
    public static void actionStart(Context act, String loadUrl, int loctionX, int locationY,
                                   int width,int height,Bitmap bitmap) {
        Intent intent = new Intent(act, FaSingleImageActivity.class);
        intent.putExtra(INTENT_IMAGE_URL_TAG, loadUrl);
        intent.putExtra(INTENT_IMAGE_X_TAG, loctionX);
        intent.putExtra(INTENT_IMAGE_Y_TAG, locationY);
        intent.putExtra(INTENT_IMAGE_W_TAG, width);
        intent.putExtra(INTENT_IMAGE_H_TAG, height);
        intent.putExtra(INTENT_IMAGE_BITMAP_TAG,bitmap);
        act.startActivity(intent);
    }

    /**
     * 从屏幕中心开始缩放
     * @param act  Context
     * @param loadUrl  要加载的图片 的url
     * @param bitmap
     */
    public static void actionStart(Context act, String loadUrl,Bitmap bitmap) {
        Intent intent = new Intent(act, FaSingleImageActivity.class);
        intent.putExtra(INTENT_IMAGE_URL_TAG, loadUrl);
        intent.putExtra(INTENT_IMAGE_BITMAP_TAG,bitmap);
        act.startActivity(intent);
    }
    
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_images_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mTransitionMode = TransitionMode.FADE;
        super.onCreate(savedInstanceState);
        this.mSmoothImageView = (SmoothImageView) findViewById(R.id.images_detail_smooth_image);
        getbundelExtras(getIntent().getExtras());
        initViewAndEvent();
    }
    

    private void getbundelExtras(Bundle bundle) {
        this.mImageUrl = bundle.getString(INTENT_IMAGE_URL_TAG, "");
        this.mLocationX = bundle.getInt(INTENT_IMAGE_X_TAG, mScreenWidth / 2);
        this.mLocationY = bundle.getInt(INTENT_IMAGE_Y_TAG, mScreenHeight / 2);
        this.mWidth = bundle.getInt(INTENT_IMAGE_W_TAG, 0);
        this.mHeight = bundle.getInt(INTENT_IMAGE_H_TAG, 0);
//        this.mBitmap = bundle.getParcelable(INTENT_IMAGE_BITMAP_TAG);

    }

    private void initViewAndEvent() {
        this.mSmoothImageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        this.mSmoothImageView.transformIn();
        if (mBitmap != null) {
            this.mSmoothImageView.setImageBitmap(mBitmap);
        } else {
            Glide.with(mContext)//
                    .load(mImageUrl)//
                    .error(R.mipmap.ic_error)//
                    .priority(Priority.IMMEDIATE)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//
                    .fitCenter().into(mSmoothImageView);
        }
        this.mSmoothImageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == SmoothImageView.STATE_TRANSFORM_OUT) {
                    FaSingleImageActivity.this.finish();
                    FaSingleImageActivity.this.overridePendingTransition(0,0);
                }
            }
        });
        this.mSmoothImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                mSmoothImageView.transformOut();
            }

            @Override
            public void onOutsidePhotoTap() {
                mSmoothImageView.transformOut();
            }
        });
        
    }

    @Override
    public void onBackPressed() {
        mSmoothImageView.transformOut();
    }
}
