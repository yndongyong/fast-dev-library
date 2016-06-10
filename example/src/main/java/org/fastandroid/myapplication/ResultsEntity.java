package org.fastandroid.myapplication;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dong on 2016/6/10.
 */
public class ResultsEntity {
    @SerializedName("_id")
    public String id;
    @SerializedName("content")
    public String content;
    @SerializedName("publishedAt")
    public String publishedAt;
    @SerializedName("title")
    public String title;

    @Override
    public String toString() {
        return "ResultsEntity{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
