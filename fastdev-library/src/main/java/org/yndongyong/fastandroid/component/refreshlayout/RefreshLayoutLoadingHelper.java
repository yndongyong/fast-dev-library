package org.yndongyong.fastandroid.component.refreshlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.view.progress.CircularProgressBar;

/**
 * Created by Dong on 2016/5/15.
 */
public class RefreshLayoutLoadingHelper {
    
    private Context mContext;
    private String loadingInfoStr;
    private String emptyInfoStr;
    private String errorMsg;
    
    private View statusView;
    private int resEmptyView;
   
    private ImageView imageView;
    private TextView tvMsg;
    private CircularProgressBar circularProgressBar;
    

   

    public RefreshLayoutLoadingHelper(Context context, int resEmptyView) {
        this.resEmptyView = R.layout.view_empty;
        this.mContext = context;
        this.resEmptyView = resEmptyView;
    }

    public TextView getTvMsg() {
        return this.tvMsg;
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public CircularProgressBar getCircularProgressBar() {
        return this.circularProgressBar;
    }

    public RefreshLayoutLoadingHelper(Context context) {
        this.resEmptyView = R.layout.view_empty;
        this.mContext = context;
    }

    public View getStatusView() {
        if (this.statusView == null) {
            this.statusView = LayoutInflater.
                    from(this.mContext).inflate(this.resEmptyView, (ViewGroup) null);
            this.circularProgressBar = 
                    (CircularProgressBar) this.statusView.findViewById(R.id
                    .loading_progress);
            this.imageView = (ImageView) this.statusView.findViewById(R.id.iv_icon_placeholder);
            this.tvMsg = (TextView) this.statusView.findViewById(R.id.tv_msg_tips);
            this.loadingInfoStr = this.mContext.getString(R.string.common_loading_message);
            this.emptyInfoStr = this.mContext.getString(R.string.common_empty_msg);
            this.errorMsg = this.mContext.getString(R.string.common_error_msg);
        }

        return this.statusView;
    }

    public String getLoadingInfoStr() {
        return this.loadingInfoStr;
    }

    public void setLoadingInfoStr(String loadingInfoStr) {
        this.loadingInfoStr = loadingInfoStr;
    }

    public String getEmptyInfoStr() {
        return this.emptyInfoStr;
    }

    public void setEmptyInfoStr(String emptyInfoStr) {
        this.emptyInfoStr = emptyInfoStr;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
