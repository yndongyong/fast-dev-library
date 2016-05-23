package org.yndongyong.fastandroid.component.qrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.base.FaBaseSwipeBackActivity;
import org.yndongyong.fastandroid.utils.AbStrUtil;

/**
 * Created by Dong on 2016/5/22.
 */
public class ResultActivity extends FaBaseSwipeBackActivity {

    public static final String BUNDLE_KEY_SCAN_RESULT = "BUNDLE_KEY_SCAN_RESULT";
    ImageView resultImage;
    TextView resultType;
    TextView resultContent;
    private Bitmap mBitmap;
    private int mDecodeMode;
    private String mResultStr;
    private String mDecodeTime;

    public ResultActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getIntent().getExtras();
        if(extras != null) {
            byte[] compressedBitmap = extras.getByteArray("BARCODE_BITMAP");
            if(compressedBitmap != null) {
                this.mBitmap = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, (BitmapFactory.Options)null);
                this.mBitmap = this.mBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }

            this.mResultStr = extras.getString("BUNDLE_KEY_SCAN_RESULT");
            this.mDecodeMode = extras.getInt("DECODE_MODE");
            this.mDecodeTime = extras.getString("DECODE_TIME");
        }

        this.initViewsAndEvents();
    }

    protected int getContentViewLayoutID() {
        return R.layout.activity_result;
    }

    protected void initViewsAndEvents() {
        this.resultImage = (ImageView)findViewById(R.id.result_image);
        this.resultType = (TextView)findViewById(R.id.result_type);
        this.resultContent = (TextView)findViewById(R.id.result_content);
        this.setTitle("扫描结果");
        StringBuilder sb = new StringBuilder();
        sb.append("扫描方式:\t\t");
        if(this.mDecodeMode == 10001) {
            sb.append("ZBar扫描");
        } else if(this.mDecodeMode == 10002) {
            sb.append("ZXing扫描");
        }

        if(!AbStrUtil.isEmpty(this.mDecodeTime)) {
            sb.append("\n\n扫描时间:\t\t");
            sb.append(this.mDecodeTime);
        }

        sb.append("\n\n扫描结果:");
        this.resultType.setText(sb.toString());
        this.resultContent.setText(this.mResultStr);
        if(null != this.mBitmap) {
            this.resultImage.setImageBitmap(this.mBitmap);
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        if(null != this.mBitmap && !this.mBitmap.isRecycled()) {
            this.mBitmap.recycle();
            this.mBitmap = null;
        }

    }
}
