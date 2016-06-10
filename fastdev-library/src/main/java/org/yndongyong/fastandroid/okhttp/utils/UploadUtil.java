package org.yndongyong.fastandroid.okhttp.utils;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.squareup.okhttp.Request;


import org.yndongyong.fastandroid.okhttp.callback.StringCallback;
import org.yndongyong.fastandroid.okhttp.upload.UploadManager;

import java.lang.ref.WeakReference;

/**
 * 上传的使用示例
 * Created by Dong on 2016/1/3.
 */
public class UploadUtil {

    private static final String TAG = UploadUtil.class.getSimpleName();

    public static void upload() {

        UploadRequestCallBack callback = new UploadRequestCallBack();
        String url = "http://10.180.120.157:9333/submit?collection=yixinimage";
//        UploadManager uploadManager = MyBaseApplication.getInstance().getUploadManager();
//        uploadManager.addNewUpload("1", url, "trace.txt",
//                Environment.getExternalStorageDirectory().getAbsolutePath() + "/trace.txt"
//                , callback);
    }

    public static class UploadRequestCallBack extends StringCallback {
        // message 属性
//        private Message message;
        public UploadRequestCallBack() {
        }

        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
            //message.setStatus(upload pending)
            // DBManager.update(message);
        }


        @Override
        public void onError(Request request, Exception e) {
            L.d(e.getMessage());
            //对应的数据库操作 update message 状态
//            message.setStatus(upload faile)
//            DBManager.update(message);
            refreshListItem();
        }

        @Override
        public void onResponse(String response) {
            L.d(response);
            refreshListItem();

            //message.setStatus(upload success)
//            DBManager.update(message);
        }

        @Override
        public void inProgress(float progress) {
            super.inProgress(progress);
            Log.d(TAG, "progress :" + progress);
            refreshListItem();
        }

        private void refreshListItem() {
            L.d("refreshListItem");
            if (userTag == null) {
                return;
            }
            WeakReference<Activity> tag = (WeakReference<Activity>) userTag;
            //得到view的软引用
            Activity act = tag.get();
            /*if (act instanceof MainActivity2) {
//                Log.d(TAG,"is MainActivity2");
                MainActivity2 ac = (MainActivity2) act;
                ac.reFreshView();
            } else {
                Log.d(TAG, "no MainActivity2");
            }*/
        }
    }
}
