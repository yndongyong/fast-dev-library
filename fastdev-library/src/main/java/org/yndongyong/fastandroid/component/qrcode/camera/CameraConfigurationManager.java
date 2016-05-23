/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yndongyong.fastandroid.component.qrcode.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


/**
 * A class which deals with reading, parsing, and setting the camera parameters which are used to
 * configure the camera hardware.
 */
final class CameraConfigurationManager {

    private static final String TAG = "CameraConfiguration";
    private final Context context;
    private Point screenResolution;
    private Point cameraResolution;

    CameraConfigurationManager(Context context)
    {
        this.context = context;
    }

    void initFromCameraParameters(Camera camera)
    {
        Camera.Parameters parameters = camera.getParameters();
        WindowManager manager = (WindowManager)this.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point theScreenResolution = new Point();
        theScreenResolution = getDisplaySize(display);

        this.screenResolution = theScreenResolution;
        Log.i("CameraConfiguration", "Screen resolution: " + this.screenResolution);

        Point screenResolutionForCamera = new Point();
        screenResolutionForCamera.x = this.screenResolution.x;
        screenResolutionForCamera.y = this.screenResolution.y;

        if (this.screenResolution.x < this.screenResolution.y) {
            screenResolutionForCamera.x = this.screenResolution.y;
            screenResolutionForCamera.y = this.screenResolution.x;
        }

        this.cameraResolution = CameraConfigurationUtils.findBestPreviewSizeValue(parameters, this.screenResolution);
        Log.i("CameraConfiguration", "Camera resolution x: " + this.cameraResolution.x);
        Log.i("CameraConfiguration", "Camera resolution y: " + this.cameraResolution.y);
    }

    @SuppressLint({"NewApi"})
    private Point getDisplaySize(Display display)
    {
        Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) {
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }

    void setDesiredCameraParameters(Camera camera, boolean safeMode) {
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null) {
            Log.w("CameraConfiguration", "Device error: no camera parameters are available. Proceeding without configuration.");
            return;
        }

        initializeTorch(parameters, safeMode);

        CameraConfigurationUtils.setFocus(parameters, true, true, safeMode);

        if (!safeMode) {
            CameraConfigurationUtils.setBarcodeSceneMode(parameters);
            CameraConfigurationUtils.setVideoStabilization(parameters);
            CameraConfigurationUtils.setFocusArea(parameters);
            CameraConfigurationUtils.setMetering(parameters);
        }

        parameters.setPreviewSize(this.cameraResolution.x, this.cameraResolution.y);
        Log.i("CameraConfiguration", "Final camera parameters: " + parameters.flatten());

        camera.setParameters(parameters);
        Camera.Parameters afterParameters = camera.getParameters();
        Camera.Size afterSize = afterParameters.getPreviewSize();
        if ((afterSize != null) && ((this.cameraResolution.x != afterSize.width) || (this.cameraResolution.y != afterSize.height))) {
            Log.w("CameraConfiguration", "Camera said it supported preview size " + this.cameraResolution.x + 'x' + this.cameraResolution.y + ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);

            this.cameraResolution.x = afterSize.width;
            this.cameraResolution.y = afterSize.height;
        }

        camera.setDisplayOrientation(90);
    }

    Point getCameraResolution() {
        return this.cameraResolution;
    }

    boolean getTorchState(Camera camera) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters != null) {
                String flashMode = parameters.getFlashMode();

                return (flashMode != null) && (
                        ("on"
                                .equals(flashMode)) ||
                                ("torch"
                                        .equals(flashMode)));
            }
        }

        return false;
    }

    void setTorch(Camera camera, boolean newSetting) {
        Camera.Parameters parameters = camera.getParameters();
        doSetTorch(parameters, newSetting, false);
        camera.setParameters(parameters);
    }

    private void initializeTorch(Camera.Parameters parameters, boolean safeMode) {
        doSetTorch(parameters, false, safeMode);
    }

    private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
        CameraConfigurationUtils.setTorch(parameters, newSetting);
        if (!safeMode)
            CameraConfigurationUtils.setBestExposure(parameters, newSetting);
    }

}
