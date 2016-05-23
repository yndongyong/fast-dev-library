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

package org.yndongyong.fastandroid.component.qrcode.decode;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.google.zxing.PlanarYUVLuminanceSource;

import org.yndongyong.fastandroid.component.qrcode.CaptureActivity;

import java.io.ByteArrayOutputStream;


final class DecodeHandler extends Handler {

    private static final String TAG = DecodeHandler.class.getSimpleName();
    private final CaptureActivity activity;
    private boolean running = true;
    private DecodeUtils mDecodeUtils = null;
    private int mDecodeMode = 10002;

    DecodeHandler(CaptureActivity activity) {
        this.activity = activity;
        this.mDecodeUtils = new DecodeUtils(10003);
    }

    public void handleMessage(Message message)
    {
        if (!this.running) {
            return;
        }
        switch (message.what) {
            case 276:
                decode((byte[])message.obj, message.arg1, message.arg2);
                break;
            case 306:
                this.running = false;
                Looper.myLooper().quit();
        }
    }

    private void decode(byte[] data, int width, int height)
    {
        long start = System.currentTimeMillis();

        Camera.Size size = this.activity.getCameraManager().getPreviewSize();
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                rotatedData[(x * size.height + size.height - y - 1)] = data[(x + y * size.width)];
            }
        }
        int tmp = size.width;
        size.width = size.height;
        size.height = tmp;

        String resultStr = null;
        Rect cropRect = this.activity.getCropRect();
        if (null == cropRect) {
            this.activity.initCrop();
        }
        cropRect = this.activity.getCropRect();

        this.mDecodeUtils.setDataMode(this.activity.getDataMode());

        String zbarStr = this.mDecodeUtils.decodeWithZbar(rotatedData, size.width, size.height, cropRect);
        String zxingStr = this.mDecodeUtils.decodeWithZxing(rotatedData, size.width, size.height, cropRect);

        if (!TextUtils.isEmpty(zbarStr)) {
            this.mDecodeMode = 10001;
            resultStr = zbarStr;
        } else if (!TextUtils.isEmpty(zxingStr)) {
            this.mDecodeMode = 10002;
            resultStr = zxingStr;
        }

        Handler handler = this.activity.getHandler();
        if (!TextUtils.isEmpty(resultStr)) {
            long end = System.currentTimeMillis();
            if (handler != null) {
                Message message = Message.obtain(handler, 296, resultStr);
                Bundle bundle = new Bundle();

                PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(rotatedData, size.width, size.height, cropRect.left, cropRect.top, cropRect
                        .width(), cropRect.height(), false);

                bundle.putInt("DECODE_MODE", this.mDecodeMode);
                bundle.putString("DECODE_TIME", end - start + "ms");

                bundleThumbnail(source, bundle);
                message.setData(bundle);
                message.sendToTarget();
            }
        }
        else if (handler != null) {
            Message message = Message.obtain(handler, 286);
            message.sendToTarget();
        }
    }

    private void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle)
    {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray("BARCODE_BITMAP", out.toByteArray());
    }

}
