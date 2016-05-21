package org.yndongyong.fastandroid.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.yndongyong.fastandroid.swipeback.SwipeBackActivityBase;
import org.yndongyong.fastandroid.swipeback.SwipeBackActivityHelper;
import org.yndongyong.fastandroid.swipeback.SwipeBackLayout;
import org.yndongyong.fastandroid.swipeback.Utils;
import org.yndongyong.fastandroid.utils.FaActivityUtil;

/**
 * Created by Dong on 2016/5/12.
 */
public abstract class FaBaseSwipeBackActivity extends FaBaseActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        if (mToolbar != null) {
            if (mToolbar.getNavigationIcon() != null) {
                //TODO 有这个的一定是二级页面，且能够滑动返回
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        FaActivityUtil.removeActivity((AppCompatActivity)mContext);
                    }
                });
                
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
