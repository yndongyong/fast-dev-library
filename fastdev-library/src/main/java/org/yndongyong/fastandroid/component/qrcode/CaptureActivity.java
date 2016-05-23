package org.yndongyong.fastandroid.component.qrcode;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import org.yndongyong.fastandroid.R;
import org.yndongyong.fastandroid.base.FaBaseSwipeBackActivity;
import org.yndongyong.fastandroid.component.qrcode.camera.CameraManager;
import org.yndongyong.fastandroid.component.qrcode.utils.BeepManager;
import org.yndongyong.fastandroid.component.qrcode.utils.InactivityTimer;
import org.yndongyong.fastandroid.utils.AbStrUtil;

import java.io.IOException;

/**
 * Created by Dong on 2016/5/22.
 */
public class CaptureActivity extends FaBaseSwipeBackActivity implements SurfaceHolder.Callback {
    public static final int IMAGE_PICKER_REQUEST_CODE = 100;
    SurfaceView capturePreview;
    ImageView captureErrorMask;
    ImageView captureScanMask;
    FrameLayout captureCropView;
    Button capturePictureBtn;
    Button captureLightBtn;
    RadioGroup captureModeGroup;
    RelativeLayout captureContainer;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private boolean isLightOn;
    private InactivityTimer mInactivityTimer;
    private BeepManager mBeepManager;
    private int mQrcodeCropWidth = 0;
    private int mQrcodeCropHeight = 0;
    private int mBarcodeCropWidth = 0;
    private int mBarcodeCropHeight = 0;
    private ObjectAnimator mScanMaskObjectAnimator = null;
    private Rect cropRect;
    private int dataMode = 10004;

    public CaptureActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        mTransitionMode = TransitionMode.RIGHT;
        super.onCreate(savedInstanceState);
        this.initViewsAndEvents();
    }

    protected int getContentViewLayoutID() {
        return R.layout.activity_capture;
    }

    protected void initViewsAndEvents() {
        this.capturePreview = (SurfaceView)findViewById(R.id.capture_preview);
        this.captureErrorMask = (ImageView)findViewById( R.id.capture_error_mask);
        this.captureScanMask = (ImageView)findViewById( R.id.capture_scan_mask);
        this.captureCropView = (FrameLayout)findViewById( R.id.capture_crop_view);
        this.capturePictureBtn = (Button)findViewById( R.id.capture_picture_btn);
        this.captureLightBtn = (Button)findViewById( R.id.capture_light_btn);
        this.captureModeGroup = (RadioGroup)findViewById( R.id.capture_mode_group);
        this.captureContainer = (RelativeLayout)findViewById( R.id.capture_container);
        this.hasSurface = false;
        this.mInactivityTimer = new InactivityTimer(this);
        this.mBeepManager = new BeepManager(this);
        this.initCropViewAnimator();
        this.captureLightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(CaptureActivity.this.isLightOn) {
                    CaptureActivity.this.cameraManager.setTorch(false);
                    CaptureActivity.this.captureLightBtn.setSelected(false);
                } else {
                    CaptureActivity.this.cameraManager.setTorch(true);
                    CaptureActivity.this.captureLightBtn.setSelected(true);
                }

                CaptureActivity.this.isLightOn = !CaptureActivity.this.isLightOn;
            }
        });
        this.captureModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PropertyValuesHolder bar2qrWidthVH;
                PropertyValuesHolder bar2qrHeightVH;
                ValueAnimator valueAnimator;
                if(checkedId == R.id.capture_mode_barcode) {
                    bar2qrWidthVH = PropertyValuesHolder.ofFloat("width", new float[]{1.0F, (float)CaptureActivity.this.mBarcodeCropWidth / (float)CaptureActivity.this.mQrcodeCropWidth});
                    bar2qrHeightVH = PropertyValuesHolder.ofFloat("height", new float[]{1.0F, (float)CaptureActivity.this.mBarcodeCropHeight / (float)CaptureActivity.this.mQrcodeCropHeight});
                    valueAnimator = ValueAnimator.ofPropertyValuesHolder(new PropertyValuesHolder[]{bar2qrWidthVH, bar2qrHeightVH});
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Float fractionW = (Float)animation.getAnimatedValue("width");
                            Float fractionH = (Float)animation.getAnimatedValue("height");
                            RelativeLayout.LayoutParams parentLayoutParams = (RelativeLayout.LayoutParams)CaptureActivity.this.captureCropView.getLayoutParams();
                            parentLayoutParams.width = (int)((float)CaptureActivity.this.mQrcodeCropWidth * fractionW.floatValue());
                            parentLayoutParams.height = (int)((float)CaptureActivity.this.mQrcodeCropHeight * fractionH.floatValue());
                            CaptureActivity.this.captureCropView.setLayoutParams(parentLayoutParams);
                        }
                    });
                    valueAnimator.addListener(new Animator.AnimatorListener() {
                        public void onAnimationStart(Animator animation) {
                        }

                        public void onAnimationEnd(Animator animation) {
                            CaptureActivity.this.initCrop();
                            CaptureActivity.this.setDataMode(10005);
                        }

                        public void onAnimationCancel(Animator animation) {
                        }

                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    valueAnimator.start();
                } else if(checkedId == R.id.capture_mode_qrcode) {
                    bar2qrWidthVH = PropertyValuesHolder.ofFloat("width", new float[]{1.0F, (float)CaptureActivity.this.mQrcodeCropWidth / (float)CaptureActivity.this.mBarcodeCropWidth});
                    bar2qrHeightVH = PropertyValuesHolder.ofFloat("height", new float[]{1.0F, (float)CaptureActivity.this.mQrcodeCropHeight / (float)CaptureActivity.this.mBarcodeCropHeight});
                    valueAnimator = ValueAnimator.ofPropertyValuesHolder(new PropertyValuesHolder[]{bar2qrWidthVH, bar2qrHeightVH});
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Float fractionW = (Float)animation.getAnimatedValue("width");
                            Float fractionH = (Float)animation.getAnimatedValue("height");
                            RelativeLayout.LayoutParams parentLayoutParams = (RelativeLayout.LayoutParams)CaptureActivity.this.captureCropView.getLayoutParams();
                            parentLayoutParams.width = (int)((float)CaptureActivity.this.mBarcodeCropWidth * fractionW.floatValue());
                            parentLayoutParams.height = (int)((float)CaptureActivity.this.mBarcodeCropHeight * fractionH.floatValue());
                            CaptureActivity.this.captureCropView.setLayoutParams(parentLayoutParams);
                        }
                    });
                    valueAnimator.addListener(new Animator.AnimatorListener() {
                        public void onAnimationStart(Animator animation) {
                        }

                        public void onAnimationEnd(Animator animation) {
                            CaptureActivity.this.initCrop();
                            CaptureActivity.this.setDataMode(10004);
                        }

                        public void onAnimationCancel(Animator animation) {
                        }

                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    valueAnimator.start();
                }

            }
        });
        
        this.capturePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                readyGoForResult(xxx,requestCode));
            }
        });
    }

    private void initCropViewAnimator() {
        this.mQrcodeCropWidth = this.getResources().getDimensionPixelSize(R.dimen
                .qrcode_crop_width);
        this.mQrcodeCropHeight = this.getResources().getDimensionPixelSize(R.dimen.qrcode_crop_height);
        this.mBarcodeCropWidth = this.getResources().getDimensionPixelSize(R.dimen.barcode_crop_width);
        this.mBarcodeCropHeight = this.getResources().getDimensionPixelSize(R.dimen.barcode_crop_height);
    }

    public Handler getHandler() {
        return this.handler;
    }

    public CameraManager getCameraManager() {
        return this.cameraManager;
    }

    public void initCrop() {
        int cameraWidth = this.cameraManager.getCameraResolution().y;
        int cameraHeight = this.cameraManager.getCameraResolution().x;
        int[] location = new int[2];
        this.captureCropView.getLocationInWindow(location);
        int cropLeft = location[0];
        int cropTop = location[1];
        int cropWidth = this.captureCropView.getWidth();
        int cropHeight = this.captureCropView.getHeight();
        int containerWidth = this.captureContainer.getWidth();
        int containerHeight = this.captureContainer.getHeight();
        int x = cropLeft * cameraWidth / containerWidth;
        int y = cropTop * cameraHeight / containerHeight;
        int width = cropWidth * cameraWidth / containerWidth;
        int height = cropHeight * cameraHeight / containerHeight;
        this.setCropRect(new Rect(x, y, width + x, height + y));
    }

    protected void onResume() {
        super.onResume();
        this.cameraManager = new CameraManager(this.getApplication());
        this.handler = null;
        if(this.hasSurface) {
            this.initCamera(this.capturePreview.getHolder());
        } else {
            this.capturePreview.getHolder().addCallback(this);
        }

        this.mInactivityTimer.onResume();
    }

    protected void onPause() {
        if(this.handler != null) {
            this.handler.quitSynchronously();
            this.handler = null;
        }

        this.mBeepManager.close();
        this.mInactivityTimer.onPause();
        this.cameraManager.closeDriver();
        if(!this.hasSurface) {
            this.capturePreview.getHolder().removeCallback(this);
        }

        if(null != this.mScanMaskObjectAnimator && this.mScanMaskObjectAnimator.isStarted()) {
            this.mScanMaskObjectAnimator.cancel();
        }

        super.onPause();
    }

    protected void onDestroy() {
        this.mInactivityTimer.shutdown();
        super.onDestroy();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if(holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }

        if(!this.hasSurface) {
            this.hasSurface = true;
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.hasSurface = false;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.initCamera(holder);
    }

    public void handleDecode(String result, Bundle bundle) {
        this.mInactivityTimer.onActivity();
        this.mBeepManager.playBeepSoundAndVibrate();
        if(!AbStrUtil.isEmpty(result) && AbStrUtil.isUrl(result)) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(result));
            this.startActivity(intent);
        } else {
//            bundle.putString("BUNDLE_KEY_SCAN_RESULT", result);
//            this.readyGo(ResultActivity.class, bundle);
            Intent intent=new Intent();
            intent.putExtra("content",result);
            setResult(RESULT_OK,intent);
            finish();
        }

    }

    private void onCameraPreviewSuccess() {
        this.initCrop();
        this.captureErrorMask.setVisibility(View.GONE);
        ViewHelper.setPivotX(this.captureScanMask, 0.0F);
        ViewHelper.setPivotY(this.captureScanMask, 0.0F);
        this.mScanMaskObjectAnimator = ObjectAnimator.ofFloat(this.captureScanMask, "scaleY", new float[]{0.0F, 1.0F});
        this.mScanMaskObjectAnimator.setDuration(2000L);
        this.mScanMaskObjectAnimator.setInterpolator(new DecelerateInterpolator());
        this.mScanMaskObjectAnimator.setRepeatCount(-1);
        this.mScanMaskObjectAnimator.setRepeatMode(1);
        this.mScanMaskObjectAnimator.start();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if(surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        } else if(this.cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
        } else {
            try {
                this.cameraManager.openDriver(surfaceHolder);
                if(this.handler == null) {
                    this.handler = new CaptureActivityHandler(this, this.cameraManager);
                }

                this.onCameraPreviewSuccess();
            } catch (IOException var3) {
                Log.w(TAG, var3);
                this.displayFrameworkBugMessageAndExit();
            } catch (RuntimeException var4) {
                Log.w(TAG, "Unexpected error initializing camera", var4);
                this.displayFrameworkBugMessageAndExit();
            }

        }
    }

    private void displayFrameworkBugMessageAndExit() {
        this.captureErrorMask.setVisibility(View.VISIBLE);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.cancelable(true);
        builder.title(R.string.app_name);
        builder.content(R.string.tips_open_camera_error);
        builder.positiveText(R.string.btn_ok);
        builder.callback(new MaterialDialog.ButtonCallback() {
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                CaptureActivity.this.finish();
            }
        });
        builder.show();
    }

    public Rect getCropRect() {
        return this.cropRect;
    }

    public void setCropRect(Rect cropRect) {
        this.cropRect = cropRect;
    }

    public int getDataMode() {
        return this.dataMode;
    }

    public void setDataMode(int dataMode) {
        this.dataMode = dataMode;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1) {
            if(requestCode == 100) {
                ;
            }

        }
    }
}
