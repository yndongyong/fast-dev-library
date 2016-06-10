package org.yndongyong.fastandroid.okhttp.upload;

import android.content.Context;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;


import org.yndongyong.fastandroid.okhttp.OkHttpUtils;
import org.yndongyong.fastandroid.okhttp.callback.Callback;
import org.yndongyong.fastandroid.okhttp.callback.StringCallback;
import org.yndongyong.fastandroid.okhttp.request.RequestCall;

import java.io.File;
import java.util.HashMap;


public class UploadManager {

    private static final String TAG = UploadManager.class.getSimpleName();

    private Context mContext;
//    private DbUtils db;

     public UploadManager(Context appContext) {
//        ColumnConverterFactory.registerColumnConverter(HttpHandler.State.class, new HttpHandlerStateConverter());
        mContext = appContext;
//        db = DbUtils.create(mContext);
//        try {
//            uploadInfoList = db.findAll(Selector.from(UploadInfo.class));
//        } catch (DbException e) {
//            LogUtils.e(e.getMessage(), e);
//        }
    }

    public int getUploadInfoListCount() {
        return map.size();
    }

    public UploadInfo getUploadInfo(Object key) {
        return map.get(key);
    }

    private HashMap<String, UploadInfo> map = new HashMap<String, UploadInfo>();


    public UploadInfo getUploadInfo(String id) {
        return map.get(id);
    }

    public void addNewUpload(String id, String url, String fileName, String target,
                             final StringCallback callback) {
        UploadInfo uploadInfo = this.addNewUpload(url, fileName, target, callback);
        map.put(id, uploadInfo);
    }

    public UploadInfo addNewUpload(String url, String fileName, String target, final StringCallback callback) {
//        L.d("addNewUpload() ");
//        L.d("addNewUpload() url: " + url);
//        L.d("addNewUpload() target: " + target);
        final UploadInfo uploadInfo = new UploadInfo();
        uploadInfo.setUploadUrl(url);
        uploadInfo.setFileName(fileName);
        uploadInfo.setFileSavePath(target);

        RequestCall requestCall = OkHttpUtils
                .post()
                .addFile("file", fileName, new File(target))
                .url(url)
                .build();
//        requestCall.connTimeOut(20*1000);
//        requestCall.writeTimeOut(20*1000);
        
        uploadInfo.setRequestCall(requestCall);
        uploadInfo.setStatus(Callback.UPLOAD_STATUS_PENDING);
    //在传递进来的callback 中回调中通过tag回调刷新界面即可
        requestCall.execute(new ManagerCallBack(uploadInfo,callback));
        return uploadInfo;
    }
//    public void addNewUpload(String url, String fileName, String target, final RequestCallBack<String> callback) /*throws DbException*/ {
//    	final UploadInfo uploadInfo = new UploadInfo();
//    	uploadInfo.setUploadUrl(url);
//    	uploadInfo.setFileName(fileName);
//    	uploadInfo.setFileSavePath(target);
//    	HttpUtils http = new HttpUtils();
//    	http.configRequestThreadPoolSize(maxUploadThread);
//    	RequestParams params = new RequestParams();
//    	params.addBodyParameter("file", new File(target));
//    	HttpHandler<String> handler = http.send(HttpMethod.POST, url, params, new ManagerCallBack(uploadInfo, callback));
//    	uploadInfo.setHandler(handler);
//    	uploadInfo.setState(handler.getState());
//    	uploadInfoList.add(uploadInfo);
////        db.saveBindingId(uploadInfo);
//    }

    public void removeUpload(Object key) {
        UploadInfo uploadInfo = map.get(key);
        removeUpload(uploadInfo);
    }

    public void removeUpload(UploadInfo uploadInfo) /*throws DbException*/ {
        Call call = uploadInfo.getRequestCall().getCall();
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        map.remove(uploadInfo);
//        db.delete(uploadInfo);
    }

    public void stopUpload(int index) /*throws DbException*/ {
        UploadInfo uploadInfo = map.get(index);
        stopUpload(uploadInfo);
    }

    public void stopUpload(UploadInfo uploadInfo) /*throws DbException*/ {
        Call call = uploadInfo.getRequestCall().getCall();
        if (call != null && !call.isCanceled()) {
            call.cancel();
        } else {
            uploadInfo.setStatus(Callback.UPLOAD_STATUS_CANCEL);
        }
//        db.saveOrUpdate(uploadInfo);
    }

    public void stopAllUpload() /*throws DbException*/ {
        for (UploadInfo uploadInfo : map.values()) {
            Call call = uploadInfo.getRequestCall().getCall();
            if (call != null && !call.isCanceled()) {
                call.cancel();
            } else {
                uploadInfo.setStatus(Callback.UPLOAD_STATUS_CANCEL);
            }
        }
//        db.saveOrUpdateAll(uploadInfoList);
    }

    public void backupUploadInfoList()  {
        for (UploadInfo uploadInfo : map.values()) {
            Call call = uploadInfo.getRequestCall().getCall();
            if (call != null) {
                // TODO: 2016/1/3 备份操作 
                // db.saveOrUpdateAll(uploadInfoList);
            }
        }

    }

    public class ManagerCallBack extends StringCallback {
        private UploadInfo uploadInfo;
        private Callback<String> baseCallBack;

        private ManagerCallBack(UploadInfo uploadInfo, Callback<String> baseCallBack) {
            this.baseCallBack = baseCallBack;
            this.uploadInfo = uploadInfo;
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
            uploadInfo.setStatus(Callback.UPLOAD_STATUS_START);
            if (baseCallBack != null) {
                baseCallBack.onBefore(request);
            }
        }

        @Override
        public void onError(Request request, Exception e) {
            uploadInfo.setStatus(Callback.UPLOAD_STATUS_FAIL);
            if (baseCallBack != null) {
                baseCallBack.onError(request, e);
            }
        }

        @Override
        public void onResponse(String response) {
            uploadInfo.setStatus(Callback.UPLOAD_STATUS_SUCCESS);
            if (baseCallBack != null) {
                baseCallBack.onResponse(response);
            }
        }

        
        @Override
        public void inProgress(float progress) {
            super.inProgress(progress);
            uploadInfo.setStatus(Callback.UPLOAD_STATUS_LOADING);
            uploadInfo.setInProgress(progress);
            if (baseCallBack != null) {
                baseCallBack.inProgress(progress);
            }
        }
    }
}
