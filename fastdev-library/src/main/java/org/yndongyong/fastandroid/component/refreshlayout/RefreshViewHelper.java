package org.yndongyong.fastandroid.component.refreshlayout;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.yndongyong.fastandroid.view.progress.CircularProgressBar;

/**
 * 刷新过程中，出现的哪些进度，提示，重试之类的视图的操作类
 * 1、正在加载
 * 2、数据为空
 * 3、加载失败，重试
 * 4、网络不用
 * Created by Dong on 2016/5/17.
 */
public class RefreshViewHelper {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private View loadingView;//正在加载
    private View emptyView;//数据为空
    private View errorView;//加载失败，且重试
    private View netView;//网络不用

    public RefreshViewHelper(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    
    public View getLoadingView(@NonNull int resId){
        if (loadingView == null) {
            loadingView = mLayoutInflater.inflate(resId, null);
        }
        return loadingView;
    }

    

    public View getEmptyView(@NonNull int resId){
        if (emptyView == null) {
            emptyView = mLayoutInflater.inflate(resId, null);
        }

        return emptyView;
    }

    public View getErrorView(@NonNull int resId){
        if (errorView == null) {
            errorView = mLayoutInflater.inflate(resId, null);
        }

        return errorView;
    }
}
