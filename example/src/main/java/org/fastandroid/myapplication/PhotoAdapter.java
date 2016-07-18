package org.fastandroid.myapplication;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.yndongyong.fastandroid.image.GlideRoundTransform;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Dong on 2016/7/18.
 */
public class PhotoAdapter extends BGARecyclerViewAdapter<PhotoEntity> implements SectionIndexer {


    public PhotoAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_photo_content);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, PhotoEntity photoEntity) {

        // 根据position获取分类的年月日 的int值
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类年月日 int的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            TextView tvName = helper.getView(R.id.tv_photo_title);
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(photoEntity.getDescription());
            ImageView photo = helper.getView(R.id.iv_photo);
            
            photo.setVisibility(View.GONE);
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) helper.getView(R.id.ll_container).getLayoutParams();
            lp.setFullSpan(true);
        } else {
            helper.setVisibility(R.id.tv_photo_title, View.GONE);
            ImageView imageView = helper.getView(R.id.iv_photo);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();

            layoutParams.width = (int) (Math.random() * (150 - 100) + 100);
            imageView.setLayoutParams(layoutParams);

            Glide
                    .with(mContext)
                    .load(photoEntity.getUrl())
                    .transform(new GlideRoundTransform(mContext, 4))
                    .centerCrop()
                    .into(imageView);
        }
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
}
