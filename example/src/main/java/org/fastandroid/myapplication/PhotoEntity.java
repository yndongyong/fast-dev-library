package org.fastandroid.myapplication;

import java.io.Serializable;

/**
 * Created by Dong on 2016/7/18.
 */
public class PhotoEntity implements Serializable {

    private String url;
    private String description;

    private long times;

    public PhotoEntity(String url, String description, long times) {
        this.url = url;
        this.description = description;
        this.times = times;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public PhotoEntity(String url, String description) {

        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
