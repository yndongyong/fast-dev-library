package org.yndongyong.fastandroid.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.yndongyong.fastandroid.R;

/**
 * Created by Dong on 2016/6/10.
 */
public class DYProgressHUD {
    
    //定义这个指示器的几种样式
    public enum Style{
        LOADING_NORMAL,LOADING_WITH_LABEl,SUCCESS_WITH_LABEL,ERROR_WITH_LABEL
    }
    public static Dialog createLoading(Context context, String label) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.tipImg);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.hud_loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(label);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.hud_loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 是否可以用“返回键”取消 false：不可以
        loadingDialog.setCanceledOnTouchOutside(true);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }
    
}
