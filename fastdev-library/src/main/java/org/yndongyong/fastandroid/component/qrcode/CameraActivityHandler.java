package org.yndongyong.fastandroid.component.qrcode;

import android.os.Handler;
import android.os.Message;

import org.yndongyong.fastandroid.component.qrcode.camera.CameraManager;
import org.yndongyong.fastandroid.component.qrcode.simple.CaptureSimpleActivity;
import org.yndongyong.fastandroid.component.qrcode.simple.DecodeThread;
import org.yndongyong.fastandroid.component.qrcode.utils.Constants;


/**
 * This class handles all the messaging which comprises the state machine for capture.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CameraActivityHandler extends Handler {

    private final CaptureSimpleActivity activity;
    private final DecodeThread decodeThread;
    private State state;
    private final CameraManager cameraManager;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CameraActivityHandler(CaptureSimpleActivity activity,
                                 CameraManager cameraManager) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity);
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        this.cameraManager = cameraManager;
        cameraManager.startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case Constants.ID_RESTART_PREVIEW:
                restartPreviewAndDecode();
                break;
            case Constants.ID_DECODE_SUCCESS:
                state = State.SUCCESS;
                activity.handleDecode((String) message.obj, message.getData());
                break;
            case Constants.ID_DECODE_FAILED:
                // We're decoding as fast as possible, so when one decode fails, start another.
                state = State.PREVIEW;
                cameraManager.requestPreviewFrame(decodeThread.getHandler(), Constants.ID_DECODE);
                break;
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(),
                Constants.ID_QUIT);
        quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause() will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(Constants.ID_DECODE_SUCCESS);
        removeMessages(Constants.ID_DECODE_FAILED);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), Constants.ID_DECODE);
        }
    }

}
