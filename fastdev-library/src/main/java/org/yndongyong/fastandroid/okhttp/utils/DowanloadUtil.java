package org.yndongyong.fastandroid.okhttp.utils;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.squareup.okhttp.Request;


import org.yndongyong.fastandroid.okhttp.callback.FileCallBack;
import org.yndongyong.fastandroid.okhttp.download.DownLoadManager;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Dong on 2016/1/3.
 */
public class DowanloadUtil {

    private static final String TAG = UploadUtil.class.getSimpleName();
    
    public static void download() {
       
        
        String url = "https://cdn1.evernote.com/win5/public/Evernote_5.9.6.9494.exe";
        String filePath =  Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName ="Evernote_5.5.exe";
        
        DownloadRequestCallBack managerCallBack = new DownloadRequestCallBack(filePath,fileName);
        
//        DownLoadManager downLoadManager = MyBaseApplication.getDownLoadManager();
//        downLoadManager.addNewDowanload("1", url, fileName,filePath, managerCallBack);
    }

    static class DownloadRequestCallBack extends FileCallBack {
        // message 属性
//        private Message message;
        
//        private String filePath;//文件路径
//        private String fileName;//文件名
        

        public DownloadRequestCallBack(String destFileDir, String destFileName) {
            super(destFileDir, destFileName);
//            this.filePath = destFileDir;
//            this.fileName = destFileName;
//            this.message = message;
        }

        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
            //message.setStatus(download pending)
            // DBManager.update(message);
        }


        @Override
        public void onError(Request request, Exception e) {
            L.d(e.getMessage());
            //对应的数据库操作 update message 状态
//            message.setStatus(download faile)
//            DBManager.update(message);
            refreshListItem();
        }

        @Override
        public void onResponse(File response) {
            L.d(response.getName());
            refreshListItem();
            //update message info
            //message.setStatus(download success)
//            DBManager.update(message);
        }

        @Override
        public void inProgress(float progress) {
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
           /* if (act instanceof MainActivity2) {
                Log.d(TAG,"is MainActivity2");
                MainActivity2 ac = (MainActivity2) act;
                ac.reFreshView();
            } else {
                Log.d(TAG, "no MainActivity2");
            }*/
        }
    }
}
