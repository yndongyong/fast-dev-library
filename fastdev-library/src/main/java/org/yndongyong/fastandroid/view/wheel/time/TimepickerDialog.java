package org.yndongyong.fastandroid.view.wheel.time;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.view.dialog.IosDialog;

import java.util.Calendar;

/**
 * 时间选择器
 * Created by Dong on 2016/5/22.
 */
public class TimepickerDialog {
    /**
     * 
     * @param context a activity
     * @param title
     * @param positiveButton
     * @param listener
     * @return
     */
    public static IosDialog createDialog(Context context, String title, String positiveButton,
                                         final OnTimePickerResultListener listener) {
        final WheelMain wheelMain;
        LayoutInflater inflater1 = LayoutInflater.from(context);
        final View timepickerview1 = inflater1.inflate(R.layout.timepicker,
                null);
        ScreenInfo screenInfo1 = new ScreenInfo((Activity) context);
        wheelMain = new WheelMain(timepickerview1);
        wheelMain.screenheight = screenInfo1.getHeight();
        Calendar calendar1 = Calendar.getInstance();

        int year1 = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year1, month1, day1);

        IosDialog dialog = new IosDialog(context)
                .builder()
                .setTitle(title)
                .setView(timepickerview1)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog.setPositiveButton(positiveButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onResult(wheelMain.getTime());
                }
            }
        });

        return dialog;
    }
    public interface OnTimePickerResultListener{
        void onResult(String timeStr);
    }
}
