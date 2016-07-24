package org.yndongyong.fastandroid.component.powerpopmenu.powerpopmenu;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Dong on 2016/07/24.
 */
public class PowerPopMenuModel implements Serializable {

    //image source
    public int resId = 0;
    public String url = "";
    public Bitmap bitmap;

    public String text;
}
