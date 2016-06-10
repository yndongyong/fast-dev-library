package org.fastandroid.myapplication;

import android.support.v7.widget.RecyclerView;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Dong on 2016/5/15.
 */
public class UserInfoAdapter extends BGARecyclerViewAdapter<UserEntity> {
    public UserInfoAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_my_activity_record);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, UserEntity userEntity) {
        helper.setText(R.id.tv_my_activity_record_title, userEntity.getUsername());
        helper.setText(R.id.tv_my_activity_record_time, userEntity.getUsername());
    }
}
