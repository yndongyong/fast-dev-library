package org.yndongyong.fastandroid.okhttp.upload;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;


import java.util.List;

public class UploadService extends Service {

    private static final String TAG = UploadService.class.getSimpleName();
    
	private static UploadManager mUploadManager;

    public static UploadManager getUploadManager(Context appContext) {
//    	Log.d(TAG, "getUploadManager() UploadService.isServiceRunning(appContext):" + UploadService.isServiceRunning(appContext));
//        if (!UploadService.isServiceRunning(appContext)) {
//            Intent downloadSvr = new Intent("upload.service.action");
//            appContext.startService(downloadSvr);
//        }
        if (UploadService.mUploadManager == null) {
            UploadService.mUploadManager = new UploadManager(appContext);
        }
        return mUploadManager;
    }

    public UploadService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        if (mUploadManager != null) {
                mUploadManager.stopAllUpload();
                mUploadManager.backupUploadInfoList();
        }
        super.onDestroy();
    }

    public static boolean isServiceRunning(Context context) {
        boolean isRunning = false;

        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (serviceList == null || serviceList.size() == 0) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(UploadService.class.getName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
