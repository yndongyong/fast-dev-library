package org.yndongyong.fastandroid.base;

import android.os.Bundle;
import android.view.View;

import org.yndongyong.fastandroid.swipeback.SwipeBackActivityBase;
import org.yndongyong.fastandroid.swipeback.SwipeBackActivityHelper;
import org.yndongyong.fastandroid.swipeback.SwipeBackLayout;
import org.yndongyong.fastandroid.swipeback.Utils;

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
