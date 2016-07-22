package org.fastandroid.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.yndongyong.fastandroid.image.GlideRoundTransform;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Dong on 2016/7/18.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> implements SectionIndexer {

    private Context mContext;


    private List<PhotoEntity> mData;

    public PhotoAdapter(Context context, List<PhotoEntity> data) {
        mContext = context;
        mData = data;
    }


    //开放数据接口,让使用到adapter的地方可以操作mData
    public List<PhotoEntity> getDatas() {
        return mData;
    }

    public void setData(List<PhotoEntity> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
    }


    class PhotoViewHolder extends RecyclerView.ViewHolder {
        // item展示图片
        ImageView mImage;

        // 头部的文字
        TextView mTextView;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.iv_photo);

            mTextView = (TextView) itemView.findViewById(R.id.tv_photo_title);
        }
    }
    
    
    /*public PhotoAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_photo_content);
    }*/

    protected void fillData(BGAViewHolderHelper helper, int position, PhotoEntity photoEntity) {

        // 根据position获取分类的年月日 的int值
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类年月日 int的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            TextView tvName = helper.getView(R.id.tv_photo_title);
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(photoEntity.getDescription());

            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)
                    helper.getView(R.id.tv_photo_title).getLayoutParams();
            lp.setFullSpan(true);

        } else {
            helper.setVisibility(R.id.tv_photo_title, View.GONE);

        }


        ImageView imageView = helper.getView(R.id.iv_photo);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
//
//        layoutParams.height = (int) ((Math.random() +1)*layoutParams.width);
//        imageView.setLayoutParams(layoutParams);


        Glide
                .with(mContext)
                .load(photoEntity.getUrl())
                .transform(new GlideRoundTransform(mContext, 4))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_empty)
                .dontAnimate()
//                .centerCrop()
//                .priority(Priority.IMMEDIATE)
                .bitmapTransform(new GlideRoundTransform(mContext, 4))
                .fitCenter()
//                .crossFade()
                .into(imageView);
    }


    //SectionIndexer
    @Override
    public Object[] getSections() {
        return new Object[0];
    }


    //根据 年月日的int型值，判断第一次出现的值。
    @Override
    public int getPositionForSection(int sectionIndex) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            int sortStr = (int) getDatas().get(i).getTimes();
            if (sortStr == sectionIndex) {
                return i;
            }
        }

        return -1;

    }

    //根据 当前位置 获取分类的描述 的年月日 int型
    @Override
    public int getSectionForPosition(int position) {
        //根据这个字段得到年月日
        return (int) getDatas().get(position).getTimes();

    }

    // 头部类型
    public final int VIEW_TYPE_HEADER = 0;
    // item类型
    public final int VIEW_TYPE_IMAGE = 1;

    @Override
    public PhotoAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoViewHolder viewHolder;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                viewHolder = new PhotoViewHolder
                        (LayoutInflater.from(mContext).inflate(R.layout.item_photo_head, parent, false));
                return viewHolder;
            case VIEW_TYPE_IMAGE:
                viewHolder = new PhotoViewHolder
                        (LayoutInflater.from(mContext).inflate(R.layout.item_photo_content, parent,
                                false));
                return viewHolder;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(PhotoAdapter.PhotoViewHolder holder, int position) {

        PhotoEntity photoEntity = mData.get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager
                        .LayoutParams) holder.mTextView.getLayoutParams();
                // 占满一行
                lp.setFullSpan(true);

                holder.mTextView.setLayoutParams(lp);
                holder.mTextView.setText(photoEntity.getDescription());
                // TODO: 2016/7/20 第一条显示文本和图片可以在这里做文章 

               /* Glide
                        .with(mContext)
                        .load(photoEntity.getUrl())
                        .transform(new GlideRoundTransform(mContext, 4))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ic_empty)
                        .dontAnimate()
//                .centerCrop()
//                .priority(Priority.IMMEDIATE)
                        .bitmapTransform(new GlideRoundTransform(mContext, 4))
                        .fitCenter()
//                .crossFade()
                        .into(holder.mImage);*/

                break;
            case VIEW_TYPE_IMAGE:

//                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.mImage.getLayoutParams();
//
//                layoutParams.height = layoutParams.width;
//                holder.mImage.setLayoutParams(layoutParams);

                Glide
                        .with(mContext)
                        .load(photoEntity.getUrl())
                        .transform(new GlideRoundTransform(mContext, 4))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ic_empty)
                        .dontAnimate()
//                .centerCrop()
//                .priority(Priority.IMMEDIATE)
                        .bitmapTransform(new GlideRoundTransform(mContext, 4))
                        .fitCenter()
//                        .override(lp1.width, lp1.height)
//                .crossFade()
                        .into(holder.mImage);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 根据position获取分类的年月日 的int值
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类年月日 int置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
