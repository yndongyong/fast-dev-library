package org.yndongyong.fastandroid.component.image_display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/6/2.
 */
public class PicScanModel implements Serializable {
    private String url;
    private int res;
    private String remark;

    public PicScanModel() {
    }

    public PicScanModel(String url) {
        this.url = url;
    }

    public int getRes() {
        return this.res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static List<PicScanModel> fromArray(String[] images) {
        List result = new ArrayList();
        for (String image : images) {
            PicScanModel psm = new PicScanModel(image);
            result.add(psm);
        }
        return result;
    }
}
