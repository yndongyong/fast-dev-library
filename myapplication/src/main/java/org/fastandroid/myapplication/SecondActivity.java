package org.fastandroid.myapplication;

import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.yndongyong.fastandroid.base.FaBaseSwipeBackActivity;
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
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存原始大小和裁剪过后的图片
                .centerCrop()//进行裁剪到目标大小，然后居中
//                .fitCenter()//整个图都缩小显示
                .into(head);

       
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        d("onDestroy() ");
    }
}
