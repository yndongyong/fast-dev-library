package org.yndongyong.fastandroid.utils;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 管理全部的Activity
 * Created by Dong on 2016/5/11.
 */
public class FaActivityUtil {
    private static List<AppCompatActivity> list = new ArrayList();

    public FaActivityUtil() {
    }

    public static void addActivity(AppCompatActivity activity) {
        list.add(activity);
    }

    public static void removeActivity(AppCompatActivity activity) {
        list.remove(activity);
    }

    public static void finishAllActivity() {
        Iterator iterator = list.iterator();

        while(iterator.hasNext()) {
            AppCompatActivity activity = (AppCompatActivity)iterator.next();
            if(activity != null) {
                try {
                    activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        list.clear();
    }
}
