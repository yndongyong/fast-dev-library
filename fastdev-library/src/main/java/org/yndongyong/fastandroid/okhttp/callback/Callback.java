package org.yndongyong.fastandroid.okhttp.callback;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public abstract class Callback<T> {
    public static final int DOWNLOAD_STATUS_PENDING = 101;//还没有开始准备下载
    public static final int DOWNLOAD_STATUS_START = 102;
    public static final int DOWNLOAD_STATUS_LOADING = 103;
    public static final int DOWNLOAD_STATUS_FAIL = 104;
    public static final int DOWNLOAD_STATUS_SUCCESS = 105;
    public static final int DOWNLOAD_STATUS_CANCEL = 106;


    public static final int UPLOAD_STATUS_PENDING = 201;//还没有开始准备上传
    public static final int UPLOAD_STATUS_START = 202;
    public static final int UPLOAD_STATUS_LOADING = 203;
    public static final int UPLOAD_STATUS_FAIL = 204;
    public static final int UPLOAD_STATUS_SUCCESS = 205;
    public static final int UPLOAD_STATUS_CANCEL = 206;

    /**
     * UI Thread
     *
     * @param request
     */
    public void onBefore(Request request) {
    }

    /**
     * UI Thread
     *
     * @param
     */
    public void onAfter() {
    }

    /**
     * UI Thread
     *
     * @param progress
     */
    public void inProgress(float progress) {

    }

    /**
     * Thread Pool Thread
     *
     * @param response
     */
    public abstract T parseNetworkResponse(Response response) throws IOException;

    public abstract void onError(Request request, Exception e);

    public abstract void onResponse(T response);

    //dong add
    protected Object userTag;

    public Object getUserTag() {
        return userTag;
    }

    public void setUserTag(Object userTag) {
        this.userTag = userTag;
    }

    public static Callback CALLBACK_DEFAULT = new Callback() {

        @Override
        public Object parseNetworkResponse(Response response) throws IOException {
            return null;
        }

        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(Object response) {

        }

    };

}