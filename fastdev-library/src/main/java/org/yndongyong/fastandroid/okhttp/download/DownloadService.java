package org.yndongyong.fastandroid.okhttp.download;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Dong on 2016/1/3.
 */
public class DownloadService extends Service {
    
    private static  DownLoadManager mDownLoadManager;
    
    public static DownLoadManager getDownLoadManager(Context appContext){
        if (DownloadService.mDownLoadManager == null) {
            DownloadService.mDownLoadManager = new DownLoadManager(appContext);
        }
        return mDownLoadManager;
    }

    public DownloadService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mDownLoadManager != null) {
            mDownLoadManager.stopAllDown();
            mDownLoadManager.backupDownloadInfoList();
        }
        
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
