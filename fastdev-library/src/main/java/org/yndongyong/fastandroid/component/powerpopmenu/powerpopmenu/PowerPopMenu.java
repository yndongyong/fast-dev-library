package org.yndongyong.fastandroid.component.powerpopmenu.powerpopmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.component.powerpopmenu.adapter.BaseRecyclerViewAdapter;
import org.yndongyong.fastandroid.component.powerpopmenu.utils.DensityUtil;
import org.yndongyong.fastandroid.component.powerpopmenu.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/07/24.
 */
public class PowerPopMenu {

    private Context mContext;

    private View mParent;
    private LinearLayout mPopWindowLayout;
    private LinearLayout mContentLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PopupWindow mPopupWindow;

    private int mAnimationTime = 300;
    /**
     * pop从上往下弹
     */
    public static final int POP_UP_TO_DOWN = 1;
    /**
     * pop从下往上弹
     */
    public static final int POP_DOWN_TO_UP = 2;
    /**
     * pop在上方或下方
     */
    private int mUpOrDown = POP_DOWN_TO_UP;
    /**
     * RecyclerView 水平或者竖直显示
     */
    private int mOrientation = LinearLayoutManager.HORIZONTAL;

    private BaseRecyclerViewAdapter mAdapter;
    private List<PowerPopMenuModel> mList = new ArrayList<>();
    private boolean mIsShowIcon = false;

    private View mControlView = null;
    private Animation mControlViewOpenAnimation;
    private Animation mControlViewCloseAnimation;
    private boolean mIsShowControlViewAnim;

    private static final String POWER_POP_MENU_DEFAULT_BG_COLOR = "#60000000";
    /**
     * 默认风格，及使用RecyclerView
     */
    private static final int CONTENT_LAYOUT_STYLE_DEFAULT = 1;
    /**
     * 自定义view风格，及添加的view
     */
    private static final int CONTENT_LAYOUT_STYLE_ADD_VIEW = 2;
    private int mContentLayoutStyle = CONTENT_LAYOUT_STYLE_DEFAULT;

    private static final int CONTENT_LAYOUT_DEFAULT_WIDTH = 150;
    private static final int CONTENT_LAYOUT_DEFAULT_HEIGHT = 50;
    private int mContentLayoutWidth = CONTENT_LAYOUT_DEFAULT_WIDTH;
    private int mContentLayoutHeight = CONTENT_LAYOUT_DEFAULT_HEIGHT;

    private int mScreenH = 0;
    private int mScreenW = 0;
    private Animation mPopMenuCloseContentAnimation = null;
    private Animation mUpToDownAnimation = null;
    private Animation mDownToUpAnimation = null;
    private int mStyleId;
    private PopupWindow.OnDismissListener mOnDismissListener;

    public PowerPopMenu(Context context) {
        mContext = context;
        initData();
        initView();
    }

    /**
     * @param context
     * @param orientation LinearLayoutManager.HORIZONTAL 水平，LinearLayoutManager.VERTICAL
     *                    竖直
     * @param upOrDown    PowerPopMenu.POP_UP_TO_DOWN pop从上往下弹，PowerPopMenu.POP_DOWN_TO_UP pop从下往上弹
     */
    public PowerPopMenu(Context context, int orientation, int upOrDown) {
        mContext = context;
        setPopWindowPlace(orientation, upOrDown);
        initData();
        initView();
    }

    private void setPopWindowPlace(int orientation, int upOrDown) {
        if (orientation == LinearLayoutManager.HORIZONTAL || orientation == LinearLayoutManager.VERTICAL) {
            mOrientation = orientation;
        }
        if (upOrDown == POP_UP_TO_DOWN || upOrDown == POP_DOWN_TO_UP) {
            mUpOrDown = upOrDown;
        }
    }

    @SuppressWarnings("deprecation")
    private void initData() {
        mScreenW = ScreenUtils.getScreemWidth(mContext);
        mScreenH = ScreenUtils.getScreemHeight(mContext);

        initAnimation();
    }

    private void initView() {
        mPopWindowLayout = (LinearLayout) View.inflate(mContext, R.layout.view_power_pop_menu, null);
        mContentLayout = (LinearLayout) mPopWindowLayout.findViewById(R.id.layout_content);
        mParent = ((Activity) mContext).findViewById(android.R.id.content);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(mOrientation);

        mRecyclerView = new RecyclerView(mContext);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setRecyclerViewBackgroundColor(Color.WHITE);

        setRecyclerViewLayoutParams();
    }

    private void initAnimation() {
        mStyleId = R.style.popupwindow_fade;
        initPopMenuCloseContentAnimation();

        //开启动画
        mUpToDownAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation
                .RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        mUpToDownAnimation.setDuration(mAnimationTime);
        mDownToUpAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mDownToUpAnimation.setDuration(mAnimationTime);
    }

    private void initPopMenuCloseContentAnimation() {
        if (mUpOrDown == POP_UP_TO_DOWN) {
            mPopMenuCloseContentAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation
                    .RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);

        } else if (mUpOrDown == POP_DOWN_TO_UP) {
            mPopMenuCloseContentAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation
                    .RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        mPopMenuCloseContentAnimation.setDuration(mAnimationTime);
//        mPopMenuCloseContentAnimation.setDuration(200);
        mPopMenuCloseContentAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPopupWindow.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setRecyclerViewLayoutParams() {
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            mContentLayoutWidth = LayoutParams.MATCH_PARENT;
            mContentLayoutHeight = DensityUtil.dip2px(mContext, CONTENT_LAYOUT_DEFAULT_HEIGHT);
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            mContentLayoutHeight = LayoutParams.WRAP_CONTENT;
            mContentLayoutWidth = LayoutParams.WRAP_CONTENT;
        }
        mRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(mContentLayoutWidth, mContentLayoutHeight));
    }

    private void create(int w, int h) {
        mPopupWindow = new PopupWindow(mPopWindowLayout, w, h, true);
        mPopupWindow.setFocusable(true);
        mPopWindowLayout.setFocusable(true);
        mPopWindowLayout.setFocusableInTouchMode(true);

        mPopWindowLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });

        mPopWindowLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopupWindow.setAnimationStyle(mStyleId);
        if (mOnDismissListener != null) {
            mPopupWindow.setOnDismissListener(mOnDismissListener);
        }
    }

    @SuppressWarnings("unchecked")
    private void initAdapter() {
        mAdapter = new PowerPopMenuAdapter(mContext);
        mAdapter.setList(mList);
        ((PowerPopMenuAdapter) mAdapter).setIsShowIcon(getIsShowIcon());
        ((PowerPopMenuAdapter) mAdapter).setOrientation(mOrientation);

        setAdapter(mAdapter);
    }

    public void setAnimationTime(int time) {
        mAnimationTime = time;
    }

    public int getAnimationTime() {
        return mAnimationTime;
    }

    public void setOpenAnimation(int animId) {
        if (mUpOrDown == POP_UP_TO_DOWN) {
            mUpToDownAnimation = AnimationUtils.loadAnimation(mContext, animId);
        } else if (mUpOrDown == POP_DOWN_TO_UP) {
            mDownToUpAnimation = AnimationUtils.loadAnimation(mContext, animId);
        }
    }

    public void setCloseAnimation(int animId) {
        mPopMenuCloseContentAnimation = AnimationUtils.loadAnimation(mContext, animId);
    }

    public void setUpOpenPopMenuAnimation(Animation animation) {
        mUpToDownAnimation = animation;
    }

    public void setDownOpenPopMenuAnimation(Animation animation) {
        mDownToUpAnimation = animation;
    }

    public void setPopMenuAnimationStyle(int id) {
        mStyleId = id;
    }

    public void setPopMenuCloseContentAnimation(Animation animation) {
        mPopMenuCloseContentAnimation = animation;
    }

    public void setRecyclerViewBackgroundColor(int color) {
        mRecyclerView.setBackgroundColor(color);
    }

    public void setRecyclerViewBackgroundResource(int resid) {
        mRecyclerView.setBackgroundResource(resid);
    }

    public void setPopMenuBackgroundColor(int color) {
        mContentLayout.setBackgroundColor(color);
    }

    public void setPopMenuBackgroundResource(int resid) {
        mContentLayout.setBackgroundResource(resid);
    }

    public void setPopMenuBackgroundTransparent(boolean isTransparent) {
        if (isTransparent) {
            mPopWindowLayout.setBackgroundColor(Color.TRANSPARENT);
        } else {
            mPopWindowLayout.setBackgroundColor(Color.parseColor(POWER_POP_MENU_DEFAULT_BG_COLOR));
        }
    }

    /**
     * 使用默认的PowerPopAdapter才有效
     *
     * @param l
     */
    public void setOnItemClickListener(BaseRecyclerViewAdapter.OnItemClickListener l) {
        if (mAdapter != null && mAdapter instanceof BaseRecyclerViewAdapter) {
            mAdapter.setOnItemClickListener(l);
        }
    }

    /**
     * 使用默认的PowerPopAdapter才有效
     *
     * @param l
     */
    public void setOnItemLongClickListener(BaseRecyclerViewAdapter.OnItemLongClickListener l) {
        if (mAdapter != null && mAdapter instanceof BaseRecyclerViewAdapter) {
            mAdapter.setOnItemLongClickListener(l);
        }
    }

    public void setAdapter(BaseRecyclerViewAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(adapter);
        mContentLayout.addView(mRecyclerView);
    }

    public void setIsShowIcon(boolean isShowIcon) {
        mIsShowIcon = isShowIcon;
    }

    public boolean getIsShowIcon() {
        return mIsShowIcon;
    }

    public void setWidth(int width) {
        if (mContentLayoutStyle == CONTENT_LAYOUT_STYLE_DEFAULT) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mRecyclerView.getLayoutParams();
            params.width = mContentLayoutWidth = width;
            mRecyclerView.setLayoutParams(params);
        }
    }

    public void setView(View view) {
        mContentLayoutStyle = CONTENT_LAYOUT_STYLE_ADD_VIEW;
        mContentLayout.removeAllViews();
        addView(view);
    }

    public void addView(View view) {
        mContentLayoutStyle = CONTENT_LAYOUT_STYLE_ADD_VIEW;
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        mContentLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT));
        mContentLayout.setOrientation(LinearLayout.VERTICAL);
        mContentLayout.addView(view);
    }

    public void addView(View view, int width, int height) {
        view.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        addView(view);
    }

    /**
     * @param view
     * @param params 设置view的宽高
     */
    public void addView(View view, ViewGroup.LayoutParams params) {
        view.setLayoutParams(params);
        addView(view);
    }

    /**
     * 批量添加菜单项
     */
    public void setListResource(List<PowerPopMenuModel> list) {
        if (list == null) {
            return;
        }
        mList.clear();
        mList.addAll(list);

        mContentLayoutStyle = CONTENT_LAYOUT_STYLE_DEFAULT;
        initAdapter();
    }

    /**
     * 单个添加菜单项
     */
    public void addListItemResource(PowerPopMenuModel item) {
        if (item == null) {
            return;
        }
        mList.add(item);

        mContentLayoutStyle = CONTENT_LAYOUT_STYLE_DEFAULT;
        initAdapter();
    }

    /**
     * 设置控制控件的动画
     *
     * @param view       控制控件
     * @param openAnim   打开动画
     * @param closeAnim  关闭动画
     * @param isShowAnim 是否显示动画
     */
    public void setControlViewAnim(View view, Animation openAnim, Animation closeAnim, boolean isShowAnim) {
        mControlView = view;
        mControlViewOpenAnimation = openAnim;
        mControlViewCloseAnimation = closeAnim;
        mIsShowControlViewAnim = isShowAnim;
    }

    /**
     * @param view        控制控件
     * @param openAnimId  打开动画id
     * @param closeAnimId 关闭动画id
     * @param isShowAnim  是否显示动画
     */
    public void setControlViewAnim(View view, int openAnimId, int closeAnimId, boolean isShowAnim) {
        Animation openAnim = AnimationUtils.loadAnimation(mContext, openAnimId);
        Animation closeAnim = AnimationUtils.loadAnimation(mContext, closeAnimId);
        setControlViewAnim(view, openAnim, closeAnim, isShowAnim);
    }

    /**
     * 控制控件的开启动画
     */
    private void controlViewOpenAnim() {
        if (mControlView == null || !mIsShowControlViewAnim) {
            return;
        }
        mControlView.startAnimation(mControlViewOpenAnimation);
    }

    /**
     * 控制控件的关闭动画
     */
    private void controlViewCloseAnim() {
        if (mControlView == null || !mIsShowControlViewAnim) {
            return;
        }
        mControlView.startAnimation(mControlViewCloseAnimation);
    }

    public void dismiss() {
        if (mPopupWindow == null || !mPopupWindow.isShowing()) {
            return;
        }
        controlViewCloseAnim();
        mContentLayout.startAnimation(mPopMenuCloseContentAnimation);
//      mPopWindowLayout.startAnimation(mPopMenuCloseBackgroundAnimation);
    }

    public void show() {
        mIsShowControlViewAnim = false;
        mUpOrDown = POP_DOWN_TO_UP;
        initPopMenuCloseContentAnimation();
        View parent = mParent;
        refreshContentPlace(parent);
        create(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        controlViewOpenAnim();
        setRecyclerViewVerticalHeight(null);
        mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
        mContentLayout.startAnimation(mDownToUpAnimation);
    }

    public void show(View parent) {
        refreshContentPlace(parent);

        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            if (mUpOrDown == POP_UP_TO_DOWN) {
                operationUpHorizonatal(parent);
            } else if (mUpOrDown == POP_DOWN_TO_UP) {
                operationDownHorizonatal(parent);
            }
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            if (mUpOrDown == POP_UP_TO_DOWN) {
                operationUpVertical(parent);
            } else if (mUpOrDown == POP_DOWN_TO_UP) {
                operationDownVertical(parent);
            }
        }
    }

    private void refreshContentPlace(View parent) {
        if (mUpOrDown == POP_UP_TO_DOWN) {
            mPopWindowLayout.setGravity(Gravity.TOP);
        } else if (mUpOrDown == POP_DOWN_TO_UP) {
            mPopWindowLayout.setGravity(Gravity.BOTTOM);
        }
    }

    private void operationUpHorizonatal(View parent) {
        create(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        controlViewOpenAnim();
        mPopupWindow.showAsDropDown(parent);
        mContentLayout.startAnimation(mUpToDownAnimation);
    }

    private void operationUpVertical(View parent) {
        create(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        controlViewOpenAnim();
        setRecyclerViewVerticalHeight(parent);

        mPopupWindow.showAsDropDown(parent, 0, 0);
        mContentLayout.startAnimation(mUpToDownAnimation);

        updateContentLayoutPlace(parent);
    }

    private void operationDownHorizonatal(View parent) {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int y = location[1];
        create(LayoutParams.MATCH_PARENT, y);

        controlViewOpenAnim();
        mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
        mContentLayout.startAnimation(mDownToUpAnimation);
    }

    private void operationDownVertical(View parent) {
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int y = location[1];
        create(LayoutParams.MATCH_PARENT, y);

        controlViewOpenAnim();
        setRecyclerViewVerticalHeight(parent);
        mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
        mContentLayout.startAnimation(mDownToUpAnimation);

        updateContentLayoutPlace(parent);
    }


    @SuppressWarnings("deprecation")
    private void setRecyclerViewVerticalHeight(View btnView) {
        if (mAdapter == null || mOrientation == LinearLayoutManager.HORIZONTAL || mContentLayoutStyle !=
                CONTENT_LAYOUT_STYLE_DEFAULT) {
            return;
        }
        int totalHeight = 0;
        int viewCount = mAdapter.getItemCount();
        totalHeight = mAdapter.getItemViewHeight() * viewCount;
        // 可以显示的区域高度
        int canShowHeight = ScreenUtils.getScreemHeight(mContext) - ScreenUtils.getStatusBarHeight(mContext)
                - ScreenUtils.getTitleBarHeight(mContext);
        if (btnView != null) {
            canShowHeight = canShowHeight - btnView.getHeight();
        }

        if (totalHeight > canShowHeight) {
            totalHeight = canShowHeight;
        }
        ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
        params.height = mContentLayoutHeight = totalHeight;
        mRecyclerView.setLayoutParams(params);
    }

    private void updateContentLayoutPlace(final View parent) {
        if (mOrientation != LinearLayoutManager.VERTICAL) {
            return;
        }
        mContentLayout.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                parent.getLocationOnScreen(location);
                int x = location[0];

                LayoutParams contentParams = (LayoutParams) mContentLayout.getLayoutParams();
                int contentLayoutW = mContentLayout.getMeasuredWidth();

                if (x + contentLayoutW >= mScreenW) {
                    int gravity = Gravity.RIGHT;
                    if (mUpOrDown == POP_UP_TO_DOWN) {
                        gravity = Gravity.RIGHT;
                    } else if (mUpOrDown == POP_DOWN_TO_UP) {
                        gravity = Gravity.RIGHT | Gravity.BOTTOM;
                    }
                    mPopWindowLayout.setGravity(gravity);
                } else {
                    contentParams.leftMargin = x;
                    mContentLayout.setLayoutParams(contentParams);
                }
            }
        });
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener l) {
        mOnDismissListener = l;
        if (mPopupWindow != null) {
            mPopupWindow.setOnDismissListener(l);
        }
    }

}
