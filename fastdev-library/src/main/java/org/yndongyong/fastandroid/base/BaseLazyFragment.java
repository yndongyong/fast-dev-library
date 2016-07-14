package org.yndongyong.fastandroid.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * Created by Dong on 2016/7/13.
 */
public abstract class BaseLazyFragment extends Fragment {

    protected static String TAG_LOG = null;
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0F;
    protected Context mContext = null;
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;

    public BaseLazyFragment() {
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG_LOG = this.getClass().getSimpleName();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return this.getContentViewLayoutID() != 0 ? inflater.inflate(this.getContentViewLayoutID(), (ViewGroup) null) : super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.mScreenDensity = displayMetrics.density;
        this.mScreenHeight = displayMetrics.heightPixels;
        this.mScreenWidth = displayMetrics.widthPixels;
        this.initViewsAndEvents();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDetach() {
        super.onDetach();

        try {
            Field e = Fragment.class.getDeclaredField("mChildFragmentManager");
            e.setAccessible(true);
            e.set(this, (Object) null);
        } catch (NoSuchFieldException var2) {
            throw new RuntimeException(var2);
        } catch (IllegalAccessException var3) {
            throw new RuntimeException(var3);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initPrepare();
    }

    public void onResume() {
        super.onResume();
        if (this.isFirstResume) {
            this.isFirstResume = false;
        } else {
            if (this.getUserVisibleHint()) {
                this.onUserVisible();
            }

        }
    }

    public void onPause() {
        super.onPause();
        if (this.getUserVisibleHint()) {
            this.onUserInvisible();
        }

    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (this.isFirstVisible) {
                this.isFirstVisible = false;
                this.initPrepare();
            } else {
                this.onUserVisible();
            }
        } else if (this.isFirstInvisible) {
            this.isFirstInvisible = false;
            this.onFirstUserInvisible();
        } else {
            this.onUserInvisible();
        }

    }

    private synchronized void initPrepare() {
        if (this.isPrepared) {
            this.onFirstUserVisible();
        } else {
            this.isPrepared = true;
        }

    }

    protected abstract int getContentViewLayoutID();

    protected abstract void initViewsAndEvents();

    protected abstract void onFirstUserVisible();

    protected abstract void onUserVisible();

    private void onFirstUserInvisible() {
    }

    protected abstract void onUserInvisible();


    protected FragmentManager getSupportFragmentManager() {
        return this.getActivity().getSupportFragmentManager();
    }

    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this.getActivity(), clazz);
        this.startActivity(intent);
    }

    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this.getActivity(), clazz);
        this.startActivity(intent);
        this.getActivity().finish();
    }

    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this.getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        this.startActivity(intent);
    }

    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this.getActivity(), clazz);
        this.startActivityForResult(intent, requestCode);
    }

    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this.getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        this.startActivityForResult(intent, requestCode);
    }

    protected void showSnackbar(String msg) {
        if (null != msg && !TextUtils.isEmpty(msg)) {
            Snackbar.make(((Activity) this.mContext).getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT)
                    .show();
        }

    }
}
