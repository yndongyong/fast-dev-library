package org.fastandroid.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.yndongyong.fastandroid.base.FaBaseSwipeBackActivity;
import org.yndongyong.fastandroid.component.image_display.FaSingleImageActivity;
import org.yndongyong.fastandroid.view.RecyclerImageView;

/**
 * Created by Dong on 2016/5/15.
 */
@EActivity
public class SecondActivity extends FaBaseSwipeBackActivity {
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_second;
    }

    @ViewById
    RecyclerImageView head;

    @ViewById
    RecyclerImageView head2;

    @ViewById
    RecyclerImageView head3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTransitionMode = TransitionMode.RIGHT;
        super.onCreate(savedInstanceState);
        d("onCreate()");
        setTitle("第二页");
//        head = (ImageView) findViewById(R.id.head);
/*
        Glide
                .with(this)
                .load("http://ww4.sinaimg.cn/mw690/6cb26641gw1f429qm5uhyj20w00hsgnn.jpg")
                .placeholder(R.mipmap.ico_empty)//站位图片
                .error(R.mipmap.ic_empty)
                .listener(new CommonRequestListener())
                .crossFade()
                .into(head);*/



    }
    static class CommonRequestListener implements RequestListener {

        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            Log.e("dong", "onException:" + e.toString());
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.d("dong", "onResourceReady:" + resource.toString());
            return false;
        }
        
    }
    @AfterViews
    void afterViews(){
        d("afterViews()");
        Glide.with(this).load("http://ww4.sinaimg.cn/mw690/6cb26641gw1f429qm5uhyj20w00hsgnn.jpg")
                .placeholder(R.mipmap.ico_empty)//站位图片
                .thumbnail(0.2f)//先显示原图1/5大小的缩略图，在显示原图
                .skipMemoryCache(true)//不使用内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存原始大小的图片
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存裁剪过后的图片
                .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存原始大小和裁剪过后的图片
                .centerCrop()//进行裁剪到目标大小，然后居中
//                .fitCenter()//整个图都缩小显示
                .into(head);

        Glide.with(this).load("http://ww2.sinaimg.cn/mw690/006tnXvegw1f4328siu8hj30qo0zk7e9.jpg")
                .placeholder(R.mipmap.ico_empty)//站位图片
                .thumbnail(0.2f)//先显示原图1/5大小的缩略图，在显示原图
                .skipMemoryCache(true)//不使用内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存原始大小的图片
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存裁剪过后的图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存原始大小和裁剪过后的图片
                .centerCrop()//进行裁剪到目标大小，然后居中
//                .fitCenter()//整个图都缩小显示
                .into(head2);

        Glide.with(this).load("http://ww2.sinaimg.cn/mw690/718878b5jw1f4548jqmtfj20go0b3mzx.jpg")
                .placeholder(R.mipmap.ico_empty)//站位图片
                .thumbnail(0.2f)//先显示原图1/5大小的缩略图，在显示原图
                .skipMemoryCache(true)//不使用内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存原始大小的图片
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存裁剪过后的图片
                .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存原始大小和裁剪过后的图片
                .centerCrop()//进行裁剪到目标大小，然后居中
//                .fitCenter()//整个图都缩小显示
                .into(head3);

       
    }
    @Click(R.id.head)
    public  void onClick(View view) {
        String url = "http://ww4.sinaimg.cn/mw690/6cb26641gw1f429qm5uhyj20w00hsgnn.jpg";
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int locationX = location[0];
        int locationY = location[1];
        int width = view.getWidth();
        int height = view.getWidth();
        
        FaSingleImageActivity.actionStart(this, url,locationX,locationY,width,height, null);
    }
    @Click(R.id.head2)
    public  void onClick2(View view) {
        String url = "http://ww2.sinaimg.cn/mw690/006tnXvegw1f4328siu8hj30qo0zk7e9.jpg";
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int locationX = location[0];
        int locationY = location[1];
        int width = view.getWidth();
        int height = view.getWidth();

        FaSingleImageActivity.actionStart(this, url,locationX,locationY,width,height, null);
    }
    @Click(R.id.head3)
    public  void onClick3(View view) {
        String url = "http://ww2.sinaimg.cn/mw690/718878b5jw1f4548jqmtfj20go0b3mzx.jpg";
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int locationX = location[0];
        int locationY = location[1];
        int width = view.getWidth();
        int height = view.getWidth();
        FaSingleImageActivity.actionStart(this, url,locationX,locationY,width,height, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        d("onDestroy() ");
    }
}
