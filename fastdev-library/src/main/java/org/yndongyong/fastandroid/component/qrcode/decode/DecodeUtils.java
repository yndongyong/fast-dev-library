package org.yndongyong.fastandroid.component.qrcode.decode;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Dong on 2016/5/22.
 */
public class DecodeUtils {
    public static final int DECODE_MODE_ZBAR = 10001;
    public static final int DECODE_MODE_ZXING = 10002;
    public static final int DECODE_DATA_MODE_ALL = 10003;
    public static final int DECODE_DATA_MODE_QRCODE = 10004;
    public static final int DECODE_DATA_MODE_BARCODE = 10005;
    private int mDataMode;
    private ImageScanner mImageScanner;

    public DecodeUtils(int dataMode)
    {
        this.mImageScanner = new ImageScanner();
        this.mImageScanner.setConfig(0, 256, 3);
        this.mImageScanner.setConfig(0, 257, 3);
        this.mDataMode = (dataMode != 0 ? dataMode : 10003);
    }

    public String decodeWithZbar(byte[] data, int width, int height, Rect crop) {
        changeZBarDecodeDataMode();

        Image barcode = new Image(width, height, "Y800");
        barcode.setData(data);
        if (null != crop) {
            barcode.setCrop(crop.left, crop.top, crop.width(), crop.height());
        }

        int result = this.mImageScanner.scanImage(barcode);
        String resultStr = null;

        if (result != 0) {
            SymbolSet syms = this.mImageScanner.getResults();
            for (Symbol sym : syms) {
                resultStr = sym.getData();
            }
        }

        return resultStr;
    }

    public String decodeWithZxing(byte[] data, int width, int height, Rect crop) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(changeZXingDecodeDataMode());

        Result rawResult = null;

        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, width, height, crop.left, crop.top, crop
                .width(), crop.height(), false);

        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException localReaderException) {
            }
            finally {
                multiFormatReader.reset();
            }
        }

        return rawResult != null ? rawResult.getText() : null;
    }

    public String decodeWithZbar(Bitmap bitmap) {
        changeZBarDecodeDataMode();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Image barcode = new Image(width, height, "Y800");

        int size = width * height;
        int[] pixels = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        byte[] pixelsData = new byte[size];
        for (int i = 0; i < size; i++) {
            pixelsData[i] = ((byte)pixels[i]);
        }

        barcode.setData(pixelsData);

        int result = this.mImageScanner.scanImage(barcode);
        String resultStr = null;

        if (result != 0) {
            SymbolSet syms = this.mImageScanner.getResults();
            for (Symbol sym : syms) {
                resultStr = sym.getData();
            }
        }

        return resultStr;
    }

    public String decodeWithZxing(Bitmap bitmap) {
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(changeZXingDecodeDataMode());

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        Result rawResult = null;
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);

        if (source != null) {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decodeWithState(binaryBitmap);
            } catch (ReaderException localReaderException) {
            }
            finally {
                multiFormatReader.reset();
            }
        }

        return rawResult != null ? rawResult.getText() : null;
    }

    private Map<DecodeHintType, Object> changeZXingDecodeDataMode() {
        Map hints = new EnumMap(DecodeHintType.class);
        Collection decodeFormats = new ArrayList();

        switch (this.mDataMode) {
            case 10003:
                decodeFormats.addAll(DecodeFormatManager.getBarCodeFormats());
                decodeFormats.addAll(DecodeFormatManager.getQrCodeFormats());
                break;
            case 10004:
                decodeFormats.addAll(DecodeFormatManager.getQrCodeFormats());
                break;
            case 10005:
                decodeFormats.addAll(DecodeFormatManager.getBarCodeFormats());
        }

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        return hints;
    }

    private void changeZBarDecodeDataMode() {
        switch (this.mDataMode) {
            case 10003:
                this.mImageScanner.setConfig(0, 0, 1);
                break;
            case 10004:
                this.mImageScanner.setConfig(0, 0, 0);

                this.mImageScanner.setConfig(25, 0, 0);
                this.mImageScanner.setConfig(38, 0, 0);
                this.mImageScanner.setConfig(128, 0, 0);
                this.mImageScanner.setConfig(39, 0, 0);
                this.mImageScanner.setConfig(93, 0, 0);
                this.mImageScanner.setConfig(34, 0, 0);
                this.mImageScanner.setConfig(35, 0, 0);
                this.mImageScanner.setConfig(13, 0, 0);
                this.mImageScanner.setConfig(8, 0, 0);
                this.mImageScanner.setConfig(10, 0, 0);
                this.mImageScanner.setConfig(14, 0, 0);
                this.mImageScanner.setConfig(12, 0, 0);
                this.mImageScanner.setConfig(9, 0, 0);
                this.mImageScanner.setConfig(1, 0, 0);

                this.mImageScanner.setConfig(64, 0, 1);
                this.mImageScanner.setConfig(57, 0, 1);

                break;
            case 10005:
                this.mImageScanner.setConfig(0, 0, 0);

                this.mImageScanner.setConfig(25, 0, 1);
                this.mImageScanner.setConfig(38, 0, 1);
                this.mImageScanner.setConfig(128, 0, 1);
                this.mImageScanner.setConfig(39, 0, 1);
                this.mImageScanner.setConfig(93, 0, 1);
                this.mImageScanner.setConfig(34, 0, 1);
                this.mImageScanner.setConfig(35, 0, 1);
                this.mImageScanner.setConfig(13, 0, 1);
                this.mImageScanner.setConfig(8, 0, 1);
                this.mImageScanner.setConfig(10, 0, 1);
                this.mImageScanner.setConfig(14, 0, 1);
                this.mImageScanner.setConfig(12, 0, 1);
                this.mImageScanner.setConfig(9, 0, 1);
                this.mImageScanner.setConfig(1, 0, 1);

                this.mImageScanner.setConfig(64, 0, 0);
                this.mImageScanner.setConfig(57, 0, 0);
        }
    }

    public int getDataMode()
    {
        return this.mDataMode;
    }

    public void setDataMode(int dataMode) {
        this.mDataMode = dataMode;
    }

    static
    {
        System.loadLibrary("iconv");
    }
}
