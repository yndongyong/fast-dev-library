package org.yndongyong.fastandroid.component.powerpopmenu.powerpopmenu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.component.powerpopmenu.adapter.BaseRecyclerViewAdapter;
import org.yndongyong.fastandroid.component.powerpopmenu.adapter.BaseViewHolder;
import org.yndongyong.fastandroid.component.powerpopmenu.utils.DensityUtil;
import org.yndongyong.fastandroid.image.GlideCircleTransform;


/**
 * Created by Dong on 2016/07/24.
 */
public class PowerPopMenuAdapter extends BaseRecyclerViewAdapter<PowerPopMenuModel> {

    private static final int ITEM_VIEW_HEIGHT = 50;
    private int mOrientation = LinearLayoutManager.HORIZONTAL;
    private boolean mIsShowIcon = false;

    public PowerPopMenuAdapter(Context context) {
        super(context);
    }

    /**
     * @param orientation 水平:LinearLayoutManager.HORIZONTAL，竖直:LinearLayoutManager.VERTICAL
     */
    public void setOrientation(int orientation) {
        if (orientation == LinearLayoutManager.HORIZONTAL
                || orientation == LinearLayoutManager.VERTICAL) {
            mOrientation = orientation;
        }
    }

    public void setIsShowIcon(boolean isShowIcon) {
        mIsShowIcon = isShowIcon;
    }

    @Override
    public int getItemViewHeight() {
        return DensityUtil.dip2px(mContext, ITEM_VIEW_HEIGHT);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_power_pop_menu, viewGroup, false);
        PowerPopViewHolder viewHolder = new PowerPopViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        super.onBindViewHolder(baseViewHolder, i);
        PowerPopViewHolder holder = (PowerPopViewHolder) baseViewHolder;

        // 文本
        holder.textTv.setText(mList.get(i).text);

        // 图标显示
        if (mIsShowIcon) {
            int resid = mList.get(i).resId;
            if (resid != 0) {
                holder.iconIv.setImageResource(resid);
                holder.iconIv.setVisibility(View.VISIBLE);
            } else if (!TextUtils.isEmpty(mList.get(i).url)) {
                holder.iconIv.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(mList.get(i).url)
                        .priority(Priority.IMMEDIATE)
//                        .override(30,30)
                        .transform(new GlideCircleTransform(mContext))
                        .into(holder.iconIv);

            } else if (mList.get(i).bitmap != null) {
                holder.iconIv.setImageBitmap(mList.get(i).bitmap);
                holder.iconIv.setVisibility(View.VISIBLE);
            } else {
                holder.iconIv.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.iconIv.setVisibility(View.GONE);
        }

        // 分割线
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            holder.vLine.setVisibility(View.GONE);
            if (i < mList.size() - 1) {
                holder.hLine.setVisibility(View.VISIBLE);
            } else {
                holder.hLine.setVisibility(View.GONE);
            }
        } else {
            holder.hLine.setVisibility(View.GONE);
            if (i < mList.size() - 1) {
                holder.vLine.setVisibility(View.VISIBLE);
            } else {
                holder.vLine.setVisibility(View.GONE);
            }
        }

    }

    private class PowerPopViewHolder extends BaseViewHolder {
        public LinearLayout layout;
        public ImageView iconIv;
        public TextView textTv;
        public View vLine;
        public View hLine;

        public PowerPopViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            iconIv = (ImageView) itemView.findViewById(R.id.iv_icon);
            textTv = (TextView) itemView.findViewById(R.id.tv_text);
            vLine = itemView.findViewById(R.id.v_line);
            hLine = itemView.findViewById(R.id.h_line);
        }
    }
}
