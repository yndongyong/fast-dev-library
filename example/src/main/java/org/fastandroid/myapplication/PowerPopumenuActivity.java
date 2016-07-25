package org.fastandroid.myapplication;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.yndongyong.fastandroid.base.FaBaseActivity;
import org.yndongyong.fastandroid.component.powerpopmenu.adapter.BaseRecyclerViewAdapter;
import org.yndongyong.fastandroid.component.powerpopmenu.powerpopmenu.PowerPopMenu;
import org.yndongyong.fastandroid.component.powerpopmenu.powerpopmenu.PowerPopMenuModel;
import org.yndongyong.fastandroid.component.powerpopmenu.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

@EActivity
public class PowerPopumenuActivity extends FaBaseActivity {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_power_popuemenu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerPopMenuModel item1 = new PowerPopMenuModel();
        item1.text = "董志永";
        item1.resId = R.mipmap.ic_empty;
        mList.add(item1);
        PowerPopMenuModel item2 = new PowerPopMenuModel();
        item2.text = "有四个字";
        item2.url = "http://www.jxms.net/Article/UploadFiles/201108/20110825143503340.jpg";
        mList.add(item2);
        PowerPopMenuModel item3 = new PowerPopMenuModel();
        item3.text = "二一-一-";
        mList.add(item3);

        mPowerPopMenu = new PowerPopMenu(mContext, LinearLayoutManager.VERTICAL, PowerPopMenu.POP_UP_TO_DOWN);
        mPowerPopMenu.setIsShowIcon(true);
        mPowerPopMenu.setListResource(mList);
        mPowerPopMenu.setOnItemClickListener(new OnItemClickLis());
    }

    @ViewById
    Button btn_up_to_down;

    private PowerPopMenu mPowerPopMenu;

    private List<PowerPopMenuModel> mList = new ArrayList<>();

    @Click
    public void btn_up_to_down(View view) {
        mPowerPopMenu.show(view);
    }
    private class OnItemClickLis implements BaseRecyclerViewAdapter.OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            ToastUtils.showMessage(mContext, mList.get(position).text);
            mPowerPopMenu.dismiss();
        }
    }

}
