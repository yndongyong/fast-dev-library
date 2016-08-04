package org.yndongyong.fastandroid.component.refreshlayout;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用户控制Refreshlayout 加载中，加载为空，加载失败等的情况的view
 * Created by Dong on 2016/8/4.
 */
public abstract class RefreshLoadingStatusHelper {
    
    protected Context mContext;
    //String of loading
    protected String mLoadingMsg;
    //String of dataSet is empty
    protected String mEmptyMsg;
    //String of acquiring dataSet failed
    protected String mErrorMsg;
    //container view    
    protected View mContainerView;
    //container view resource id
    protected int mContainerViewResourceId;

    protected ImageView mImageView;
    protected TextView mTvStatusMsg;

    public RefreshLoadingStatusHelper(Context _context, @LayoutRes int resId) {
        this.mContext = _context;
        this.mContainerView = LayoutInflater.from(mContext).inflate(mContainerViewResourceId, null);
    }
    
    protected View getStatusView() {
        if (mContainerView == null) {
            this.mContainerView = LayoutInflater.from(mContext).inflate(mContainerViewResourceId, null);
        }
        return mContainerView;
    }

    abstract View getImageView(@IntegerRes int resId);
    
}
