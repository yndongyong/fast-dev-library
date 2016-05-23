package org.yndongyong.fastandroid.component.qrcode;

import android.os.Handler;
import android.os.Message;

import org.yndongyong.fastandroid.component.qrcode.camera.CameraManager;
import org.yndongyong.fastandroid.component.qrcode.decode.DecodeThread;

/**
 * Created by Dong on 2016/5/22.
 */
public class CaptureActivityHandler extends Handler {
    private final CaptureActivity activity;
    private final DecodeThread decodeThread;
    private CaptureActivityHandler.State state;
    private final CameraManager cameraManager;

    public CaptureActivityHandler(CaptureActivity activity, CameraManager cameraManager) {
        this.activity = activity;
        this.decodeThread = new DecodeThread(activity);
        this.decodeThread.start();
        this.state = CaptureActivityHandler.State.SUCCESS;
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        this.restartPreviewAndDecode();
    }

    public void handleMessage(Message message) {
        switch(message.what) {
            case 266:
                this.restartPreviewAndDecode();
                break;
            case 286:
                this.state = CaptureActivityHandler.State.PREVIEW;
                this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), 276);
                break;
            case 296:
                this.state = CaptureActivityHandler.State.SUCCESS;
                this.activity.handleDecode((String)message.obj, message.getData());
        }

    }

    public void quitSynchronously() {
        this.state = CaptureActivityHandler.State.DONE;
        this.cameraManager.stopPreview();
        Message quit = Message.obtain(this.decodeThread.getHandler(), 306);
        quit.sendToTarget();

        try {
            this.decodeThread.join(500L);
        } catch (InterruptedException var3) {
            ;
        }

        this.removeMessages(296);
        this.removeMessages(286);
    }

    private void restartPreviewAndDecode() {
        if(this.state == CaptureActivityHandler.State.SUCCESS) {
            this.state = CaptureActivityHandler.State.PREVIEW;
            this.cameraManager.requestPreviewFrame(this.decodeThread.getHandler(), 276);
        }

    }

    private static enum State {
        PREVIEW,
        SUCCESS,
        DONE;

        private State() {
        }
    }
}
