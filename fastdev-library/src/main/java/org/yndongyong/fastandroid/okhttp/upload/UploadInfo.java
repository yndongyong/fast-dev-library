package org.yndongyong.fastandroid.okhttp.upload;


import org.yndongyong.fastandroid.okhttp.request.RequestCall;

/**
 * 上传对象bean
 * Created by Dong on 2015/12/31.
 */
public class UploadInfo {
    
    private String id;//为了完成进度回调,局部刷新view，改id必须为view绑定的data的id;
    private RequestCall requestCall;
    
    private int status;//onerror,onrespone,onbefore,onfter...
    
    private float inProgress;//进度


    private String uploadUrl;//上传地址

    private String fileName; //文件名

    private String fileSavePath; // 本地地址/保存地址

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public RequestCall getRequestCall() {
        return requestCall;
    }

    public void setRequestCall(RequestCall requestCall) {
        this.requestCall = requestCall;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getInProgress() {
        return inProgress;
    }

    public void setInProgress(float inProgress) {
        this.inProgress = inProgress;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UploadInfo that = (UploadInfo) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
