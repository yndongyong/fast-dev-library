package org.yndongyong.fastandroid.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.androidadvance.topsnackbar.TSnackbar;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.utils.AbLogUtil;
import org.yndongyong.fastandroid.utils.AbStrUtil;
import org.yndongyong.fastandroid.utils.AbToastUtil;
import org.yndongyong.fastandroid.utils.AbViewUtil;
import org.yndongyong.fastandroid.utils.FaActivityUtil;

/**
 * 所有Activity的基类
 * Created by Dong on 2016/5/11.
 */
public abstract class FaBaseActivity extends AppCompatActivity {
    private static final int INVALID = -1;
    protected String TAG = null;

    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0F;

    protected float scaleWidth = 0;
    protected float scaleHeight = 0;
    
    protected Context mContext = null;
    protected Toolbar mToolbar;
    protected MaterialDialog mProgressDialog = null;//进度条
    protected boolean isHeaderRefreshing = false;
    private float downY;
    private boolean isStatusBarTranslucency = true;//statusbar 给要透明 ，支持android4.4
    protected TextView tvTitle;
    protected FaBaseActivity.TransitionMode mTransitionMode;//定义Activity的转场动画

    public FaBaseActivity() {
        this.mTransitionMode = FaBaseActivity.TransitionMode.DEFAULT;
    }

    /*
    * 设置布局的资源id
    */
    protected abstract int getContentViewLayoutID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (this.toggleOverridePendingTransition()) {
            switch (TransitionMode.values()[this.getOverridePendingTransitionMode().ordinal()]) {
                case LEFT:
                    this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    this.overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    this.overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    this.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    this.overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
                case DEFAULT:
                    this.overridePendingTransition(0, 0);
            }
        }
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();

        this.mContext = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.mScreenDensity = displayMetrics.density;
        this.mScreenHeight = displayMetrics.heightPixels;
        this.mScreenWidth = displayMetrics.widthPixels;

        scaleWidth = mScreenWidth / mUiWidth;
        scaleHeight = mScreenHeight/ mUiHeight;

        if (this.getContentViewLayoutID() != 0) {
            this.setContentView(this.getContentViewLayoutID());
            this.setTranslucentStatus(this.isApplyStatusBarTranslucency());

            if (this.isApplyKitKatTranslucency()) {
                this.setStatusbarByColorPrimaryDark();
            }

            FaActivityUtil.addActivity(this);
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }


    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        this.mToolbar = (Toolbar) findViewById(R.id.common_toolbar);
        if (null != this.mToolbar) {
            this.tvTitle = (TextView) mToolbar.findViewById(R.id.tv_common_toolbar_title);
            if (this.tvTitle != null) {
                this.mToolbar.setTitle("");
                this.tvTitle.setText(this.getTitle());
            }

            this.setSupportActionBar(this.mToolbar);
            this.getSupportActionBar().setHomeButtonEnabled(true);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            this.onBackPressed();
            //this.finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 设置居中给的标题
     *
     * @param title
     */
    protected void setTitle(String title) {
        if (this.tvTitle != null) {
            this.tvTitle.setText(title);
        } else {
            this.mToolbar.setTitle(title);
        }
    }

    /**
     * 为居中的标题设置文本颜色，字号
     * @param color
     */
    protected void setTitleStyle(int color,int unit,float size) {
        if (this.tvTitle != null) {
            this.tvTitle.setTextColor(color );
            this.tvTitle.setTextSize(unit,size);
        }
    }

    /**
     * 设置 toolbar的NavigationIcon
     *
     * @param resId
     */
    protected void setNavigationIcon(int resId) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(resId);
        }
    }

    /**
     * 设置 toolbar的NavigationIcon的点击回调
     *
     * @param listener
     */
    protected void setNavigationOnClickListener(View.OnClickListener listener) {
        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(listener);
        }
    }

    /**
     * 关闭 DisplayHomeAsUpEnabled
     */
    protected void setDisplayHomeAsUpDisEnabled() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    /**
     * 设置toolbar的背景颜色
     *
     * @param color
     */
    protected void setToolBarBackground(int color) {
        this.mToolbar.setBackgroundColor(color);
    }

    /**
     * 设置toolbar的背景图片
     *
     * @param drawable
     */
    protected void setToolBarBackground(Drawable drawable) {
        this.mToolbar.setBackground(drawable);
    }


    protected void onDestroy() {
        super.onDestroy();
        FaActivityUtil.removeActivity(this);
    }


    protected View getFloatTargetView() {
        return this.getWindow().getDecorView();
    }


    /**
     * statusbar是否是透明
     *
     * @return
     */
    public boolean isApplyStatusBarTranslucency() {
        return this.isStatusBarTranslucency;
    }

    /**
     * 设置 statusbar是否是透明
     *
     * @param isStatusBarTranslucency
     */
    public void setApplyStatusBarTranslucency(boolean isStatusBarTranslucency) {
        this.isStatusBarTranslucency = isStatusBarTranslucency;
    }

    /**
     * todo 默认是false的
     * 在android 4.4及以下 statusbar是否是透明
     *
     * @return
     */
    protected boolean isApplyKitKatTranslucency() {
        return false;
    }

    /**
     * 设置 沉浸式状态栏
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
       /* if (this.mToolbar != null)
            this.mToolbar.setPadding(0, (int)AbViewUtil.dip2px(this, 22.0F), 0, 0);*/

    }

    /**
     * 设置 4.4 沉浸式状态栏
     */
    protected void setStatusbarByColorPrimaryDark() {
        d(" setStatusbarByColorPrimaryDark()");
        SystemBarTintManager mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setStatusBarTintColor(R.color.default_main_color);//通知栏所需颜色
    }

    protected void setStatusBarColor(int color) {
        SystemBarTintManager mTintManager = new SystemBarTintManager(this);
        int statusBarColor = this.mContext.getResources().getColor(color);
        mTintManager.setStatusBarTintColor(statusBarColor);
        mTintManager.setStatusBarTintEnabled(true);
    }

    public void showSnackBar(View topView, String msg) {
        if (null != msg && !AbStrUtil.isEmpty(msg)) {
            Snackbar sBar = Snackbar.make(topView, msg, Snackbar.LENGTH_SHORT);
            Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout) sBar.getView();
//            ve.setAlpha(0.9F);
            sBar.show();
         /*   TSnackbar.make(topView, msg, TSnackbar.LENGTH_LONG)
                    .show();*/
        }

    }

    /**
     * todo 设置
     */
    public void setImageHeader() {

    }

    public void showProgressDialog(@Nullable String content) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new MaterialDialog.Builder(this)
                    .theme(Theme.LIGHT)
                    .content(content == null ? "请稍后..." : content)
                    .progress(true, 0)
                    .cancelable(false)
                    .show();
        } else {
            this.mProgressDialog.setContent(content == null ? "请稍后..." : content);
        }

        if (!this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }

    }

    public void closeProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            try {
                this.mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        this.startActivity(intent);
    }

    public void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        this.startActivity(intent);
    }

    public void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        this.startActivity(intent);
        this.finish();
    }

    public void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        this.startActivity(intent);
        this.finish();
    }

    public void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        this.startActivityForResult(intent, requestCode);
    }

    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        this.startActivityForResult(intent, requestCode);
    }

    /**
     * 在要跳转的activity中写个static actionStart(Context,v1,v2){
     * intent i = new intent();
     * i.putExtra("",v1);
     * i.putextra("",v2);
     * Context.startAcitvity(intent);
     * }方法，
     *
     * @param intent
     */
    protected void readyGo(Intent intent) {
        this.startActivity(intent);
    }

    protected void readyGoThenKill(Intent intent) {
        this.startActivity(intent);
        this.finish();
    }

    protected void readyGoForResult(Intent intent, int requestCode) {
        this.startActivityForResult(intent, requestCode);
    }

    /**
     * 日志
     *
     * @param log
     */
    protected void d(String log) {
        if (!AbStrUtil.isEmpty(log)) {
            if (!AbStrUtil.isEmpty(TAG)) {
                AbLogUtil.d(TAG, log);
            } else {
                AbLogUtil.d("FaBaseActivity", log);
            }
        }
    }

    /**
     * 日志
     *
     * @param log
     */
    protected void e(String log) {
        if (!AbStrUtil.isEmpty(log)) {
            if (!AbStrUtil.isEmpty(TAG)) {
                AbLogUtil.e(TAG, log);
            } else {
                AbLogUtil.e("FaBaseActivity", log);
            }
        }
    }

    Toast mToast;

    public void showToast(final String text) {
        if (!AbStrUtil.isEmpty(text)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    } else {
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });

        }
    }

    /**
     * 默认会设置转场动画
     *
     * @return
     */
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    protected FaBaseActivity.TransitionMode getOverridePendingTransitionMode() {
        return this.mTransitionMode;
    }

    /**
     * activity 的转场动画,
     */
    public static enum TransitionMode {
        LEFT,//0
        RIGHT,//1
        TOP,//2
        BOTTOM,//3
        SCALE,//4
        FADE,//5
        DEFAULT;//6

        private TransitionMode() {
        }
    }
    // TODO: 2016/7/14 UI适配相关 
    private float mUiWidth = 670, mUiHeight =1334;

    //设置UI设计稿的尺寸
    public void setUiHeight(){
        this.mUiHeight =670;
    }

    public void getUiWidth(){
        this.mUiWidth = 1334;
    }

    public  int getWidthSize(int size){
        return (int) (size * scaleWidth);
    }
    public int getHightSize(int size){
        return (int) (size * scaleHeight);
    }

    public  float getTextSize(int pxSize){
        return (pxSize*scaleHeight) / mScreenDensity;
    }

    public  void setViewSize(int width, int height, View v){
        int paramWidth = getWidthSize(width);
        int paramHeight = getHightSize(height);
        ViewGroup.MarginLayoutParams params
                = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        if (width != INVALID){
            params.width = paramWidth;
        }
        if (height != INVALID){
            params.height = paramHeight;
        }
        v.setLayoutParams(params);
    }

    public void setViewPadding(int left, int top, int right, int bottom,
                                      View v){
        left = getWidthSize(left);
        top = getHightSize(top);
        right = getWidthSize(right);
        bottom = getWidthSize(bottom);
        v.setPadding(left, top, right, bottom);
    }


    public void setViewMargin(int left, int top, int right, int bottom,
                                     View v){
        int paramLeft = getWidthSize(left);
        int paramTop =  getHightSize(top);
        int paramRight = getWidthSize(right);
        int paramBottom = getHightSize(bottom);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                v.getLayoutParams();
        if (left != INVALID){
            params.leftMargin = paramLeft;
        }
        if (right != INVALID){
            params.rightMargin = paramRight;
        }
        if (top != INVALID){
            params.topMargin = paramTop;
        }
        if (bottom != INVALID){
            params.bottomMargin = paramBottom;
        }
        v.setLayoutParams(params);
    }




}
