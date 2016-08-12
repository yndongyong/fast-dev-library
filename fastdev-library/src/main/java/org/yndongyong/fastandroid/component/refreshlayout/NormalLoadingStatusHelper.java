package org.yndongyong.fastandroid.component.refreshlayout;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.view.progress.CircularProgressBar;

/**
 * Created by Dong on 2016/8/12.
 */
public class NormalLoadingStatusHelper extends LoadingStatusHelper {

    private CircularProgressBar progressBar;
    private ImageView iv_icon_placeholder;
    private TextView tv_msg_tips;
    
    private String loadingStr = "请稍后";
    private String networkErrorStr = "无可用的网络连接";
    private String interfaceErrorStr = "访问失败";
    private String dataSetEmptyStr = "暂无数据请稍后重试";

    private int networkErrorImageResId = R.mipmap.ic_error;
    private int interfaceImageResId = R.mipmap.ic_retry;
    private int dataSetEmptyImageResId = R.mipmap.ic_empty;
    
    public NormalLoadingStatusHelper(Context _context) {
        super(_context);
    }

    public void setLoadingStr(String loadingStr) {
        this.loadingStr = loadingStr;
    }

    public void setNetworkErrorStr(String networkErrorStr) {
        this.networkErrorStr = networkErrorStr;
    }

    public void setInterfaceErrorStr(String interfaceErrorStr) {
        this.interfaceErrorStr = interfaceErrorStr;
    }

    public void setDataSetEmptyStr(String dataSetEmptyStr) {
        this.dataSetEmptyStr = dataSetEmptyStr;
    }

    public void setNetworkErrorImageResId(int networkErrorImageResId) {
        this.networkErrorImageResId = networkErrorImageResId;
    }

    public void setInterfaceImageResId(int interfaceImageResId) {
        this.interfaceImageResId = interfaceImageResId;
    }

    public void setDataSetEmptyImageResId(int dataSetEmptyImageResId) {
        this.dataSetEmptyImageResId = dataSetEmptyImageResId;
    }

    @Override
    public View getRefreshStatusView() {
        if (mContainerView == null) {
            mContainerView = View.inflate(mContext,R.layout.view_empty, null);
            progressBar = (CircularProgressBar) mContainerView.findViewById(R.id.loading_progress);
            iv_icon_placeholder = (ImageView) mContainerView.findViewById(R.id.iv_icon_placeholder);
            tv_msg_tips = (TextView) mContainerView.findViewById(R.id.tv_msg_tips);
        }
        return mContainerView;
    }

    @Override
    public void changeToLoading() {
        iv_icon_placeholder.setVisibility(View.GONE);
        tv_msg_tips.setText(loadingStr);
    }

    @Override
    public void changeToDataSetEmpty() {
        progressBar.setVisibility(View.GONE);
        iv_icon_placeholder.setImageResource(dataSetEmptyImageResId);
        iv_icon_placeholder.setVisibility(View.VISIBLE);
        tv_msg_tips.setText(dataSetEmptyStr);
    }

    @Override
    public void changeToInterfaceError() {
        progressBar.setVisibility(View.GONE);
        iv_icon_placeholder.setImageResource(interfaceImageResId);
        iv_icon_placeholder.setVisibility(View.VISIBLE);
        tv_msg_tips.setText(interfaceErrorStr);
    }

    @Override
    public void changeToNetWorkError() {
        progressBar.setVisibility(View.GONE);
        iv_icon_placeholder.setImageResource(networkErrorImageResId);
        iv_icon_placeholder.setVisibility(View.VISIBLE);
        tv_msg_tips.setText(networkErrorStr);
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        iv_icon_placeholder.setOnClickListener(listener);
    }


}
