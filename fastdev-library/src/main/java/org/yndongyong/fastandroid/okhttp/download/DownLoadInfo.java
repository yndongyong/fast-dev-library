package org.yndongyong.fastandroid.okhttp.download;


import org.yndongyong.fastandroid.okhttp.request.RequestCall;

/**、
 * 下载信息的bean
 * Created by Dong on 2016/1/3.
 */
public class DownLoadInfo {
    
    private String id;//为了完成进度回调,局部刷新view，改id必须为view绑定的data的id;
    
    private RequestCall requestCall;

    private float inProgress;//进度

    private int status;//onerror,onrespone,onbefore,onfter...
    
    private String fileUrl;//下载地址

    private String fileName; //文件名

    private String fileSize;//文件大小

    private String fileSavePath; // 本地地址/保存地址

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownLoadInfo that = (DownLoadInfo) o;

        return id.equals(that.id);

    }
    public float getInProgress() {
        return inProgress;
    }

    public void setInProgress(float inProgress) {
        this.inProgress = inProgress;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }
}
