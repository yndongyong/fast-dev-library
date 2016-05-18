package org.fastandroid.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.yndongyong.fastandroid.base.FaBaseSwipeBackActivity;

/**
 * Created by Dong on 2016/5/15.
 */
public class SecondActivity extends FaBaseSwipeBackActivity {
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_second;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mTransitionMode = TransitionMode.RIGHT;
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//向上导航,会导致parentactivity销毁，重新创建
    }

    
    
}
