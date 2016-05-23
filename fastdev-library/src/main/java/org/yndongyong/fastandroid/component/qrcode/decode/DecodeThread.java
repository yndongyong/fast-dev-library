/*
 * Copyright (C) 2008 ZXing authors
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

import android.os.Handler;
import android.os.Looper;

import org.yndongyong.fastandroid.component.qrcode.CaptureActivity;

import java.util.concurrent.CountDownLatch;

/**
 * This thread does all the heavy lifting of decoding the images.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class DecodeThread extends Thread {

    public static final String BARCODE_BITMAP = "BARCODE_BITMAP";
    public static final String DECODE_MODE = "DECODE_MODE";
    public static final String DECODE_TIME = "DECODE_TIME";
    private final CaptureActivity activity;
    private Handler handler;
    private final CountDownLatch handlerInitLatch;

    public DecodeThread(CaptureActivity activity)
    {
        this.activity = activity;
        this.handlerInitLatch = new CountDownLatch(1);
    }

    public Handler getHandler() {
        try {
            this.handlerInitLatch.await();
        }
        catch (InterruptedException localInterruptedException) {
        }
        return this.handler;
    }

    public void run()
    {
        Looper.prepare();
        this.handler = new DecodeHandler(this.activity);
        this.handlerInitLatch.countDown();
        Looper.loop();
    }

}
