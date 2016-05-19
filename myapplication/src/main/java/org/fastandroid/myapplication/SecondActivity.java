package org.fastandroid.myapplication;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

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
        setTitle("第二页");
    }


}
