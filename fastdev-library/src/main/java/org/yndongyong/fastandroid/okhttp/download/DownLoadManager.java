package org.yndongyong.fastandroid.okhttp.download;

import android.content.Context;


import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;


import org.yndongyong.fastandroid.okhttp.OkHttpUtils;
import org.yndongyong.fastandroid.okhttp.callback.Callback;
import org.yndongyong.fastandroid.okhttp.callback.FileCallBack;
import org.yndongyong.fastandroid.okhttp.request.RequestCall;
import org.yndongyong.fastandroid.okhttp.upload.UploadInfo;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Dong on 2016/1/3.
 */
public class DownLoadManager {

    private HashMap<String, DownLoadInfo> map = new HashMap<String, DownLoadInfo>();

    private Context mContext;

    public DownLoadManager(Context appContext) {
        mContext = appContext;
    }

    public int getDownLoadInfoCount() {
        return map.size();
    }

    public DownLoadInfo getDownLoadInfo(Object key) {
        return map.get(key);
    }

    public DownLoadInfo getUploadInfo(String key) {
        return map.get(key);
    }

    public void addNewDowanload(String id, String url, String fileName, String target,
                             final FileCallBack callback) {
        DownLoadInfo downLoadInfo = this.addNewDowanload(url, fileName, target, callback);
        map.put(id, downLoadInfo);
    }

    /**
     * 
     * @param url
     * @param fileName 文件名
     * @param target 文件夹
     * @param callback
     * @return
     */
    public DownLoadInfo addNewDowanload(String url, String fileName, String target, final
    FileCallBack callback) {
        final DownLoadInfo downLoadInfo = new DownLoadInfo();
        downLoadInfo.setFileUrl(url);
        downLoadInfo.setFileName(fileName);
        downLoadInfo.setFileSavePath(target);

        RequestCall requestCall = OkHttpUtils
                .get()
                .url(url)
                .build();
//        requestCall.connTimeOut(20*1000);
//        requestCall.readTimeOut(20*1000);
        
        downLoadInfo.setRequestCall(requestCall);
        downLoadInfo.setStatus(Callback.DOWNLOAD_STATUS_PENDING);
        //在传递进来的callback 中回调中通过tag回调刷新界面即可
        requestCall.execute(new ManagerCallBack(downLoadInfo,callback));
        return downLoadInfo;
    }

    public void removeDownload(Object key) {
        DownLoadInfo downLoadInfo = map.get(key);
        removeDownload(downLoadInfo);
    }

    public void removeDownload(UploadInfo uploadInfo) /*throws DbException*/ {
        Call call = uploadInfo.getRequestCall().getCall();
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        map.remove(uploadInfo);
//        db.delete(uploadInfo);
    }

    public void stopUpload(Object key) /*throws DbException*/ {
        DownLoadInfo downLoadInfo = map.get(key);
        stopDownload(downLoadInfo);
    }

    public void stopDownload(DownLoadInfo downLoadInfo) /*throws DbException*/ {
        Call call = downLoadInfo.getRequestCall().getCall();
        if (call != null && !call.isCanceled()) {
            call.cancel();
            downLoadInfo.setStatus(Callback.DOWNLOAD_STATUS_CANCEL);
        }
//        db.saveOrUpdate(uploadInfo);
    }

    public void stopAllDown() /*throws DbException*/ {
        for (DownLoadInfo downLoadInfo : map.values()) {
            Call call = downLoadInfo.getRequestCall().getCall();
            if (call != null && !call.isCanceled()) {
                call.cancel();
                downLoadInfo.setStatus(Callback.DOWNLOAD_STATUS_CANCEL);
            } 
        }
//        db.saveOrUpdateAll(uploadInfoList);
    }

    public void backupDownloadInfoList()  {
        for (DownLoadInfo downLoadInfo : map.values()) {
            Call call = downLoadInfo.getRequestCall().getCall();
            if (call != null) {
                // TODO: 2016/1/3 备份操作 
                // db.saveOrUpdateAll(uploadInfoList);
            }
        }

    }
    
    
    public class ManagerCallBack extends FileCallBack {
        private DownLoadInfo downLoadInfo;
        private FileCallBack baseCallBack;

        public ManagerCallBack(DownLoadInfo downLoadInfo, FileCallBack baseCallBack) {
            super(baseCallBack.getDestFileDir(),baseCallBack.getDestFileName());
            this.baseCallBack = baseCallBack;
            this.downLoadInfo = downLoadInfo;
        }

        @Override
        public void setUserTag(Object userTag) {
            if (baseCallBack == null) return;
            baseCallBack.setUserTag(userTag);
        }

        @Override
        public Object getUserTag() {
            if (baseCallBack == null) return null;
            return baseCallBack.getUserTag();
        }

        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
            downLoadInfo.setStatus(Callback.DOWNLOAD_STATUS_START);
            if (baseCallBack != null) {
                baseCallBack.onBefore(request);
            }
        }

        @Override
        public void onError(Request request, Exception e) {
            downLoadInfo.setStatus(Callback.DOWNLOAD_STATUS_FAIL);
            if (baseCallBack != null) {
                baseCallBack.onError(request, e);
            }
        }

        @Override
        public void onResponse(File response) {
            downLoadInfo.setStatus(Callback.DOWNLOAD_STATUS_SUCCESS);
            if (baseCallBack != null) {
                baseCallBack.onResponse(response);
            }
        }

        @Override
        public void inProgress(float progress) {
            downLoadInfo.setStatus(Callback.DOWNLOAD_STATUS_LOADING);
            downLoadInfo.setInProgress(progress);
            if (baseCallBack != null) {
                baseCallBack.inProgress(progress);
            }
        }
    }
}
