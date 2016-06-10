package org.fastandroid.myapplication;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Dong on 2016/6/2.
 */
public class DouBanLoadView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private int mDuration = 3000;
    ValueAnimator valueAnimator;
    private float animatorValue = 0f;
    private TimeInterpolator timeInterpolator = new DecelerateInterpolator ();
    
    
    public DouBanLoadView(Context context) {
        this(context,null);
    }

    public DouBanLoadView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public DouBanLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle();
    }

    void initStyle() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔锋

        initAnimator();
    }
    public void start() {
        valueAnimator .start();
    }

    private void initAnimator() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
            valueAnimator.start();
        } else {
            valueAnimator = ValueAnimator.ofFloat(0, 855);
            valueAnimator.setDuration(mDuration);
            valueAnimator.setInterpolator(timeInterpolator);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animatorValue = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
//            valueAnimator.start();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        doubanDraw(canvas);
    }

    private void doubanDraw(Canvas canvas) {
        float point = Math.min(mWidth,mHeight)*0.06f/2;
//        float point = 28;
        float r = point*(float) Math.sqrt(2);
        RectF rectF = new RectF(-r,-r,r,r);
        canvas.save();
        
        //旋转画布
        if (animatorValue>135)
            canvas.rotate(animatorValue - 135);

        float startAngle = 0;
        float sweepAngle = 0;

        if (animatorValue < 135) {
            startAngle = animatorValue + 5;
            sweepAngle = 170 + animatorValue / 5;
        }else if (animatorValue <270){
            startAngle = 135 + 5;
            sweepAngle = 170 + animatorValue / 5;
        } else if (animatorValue < 630) {
            startAngle = 135 + 5;
            sweepAngle = 260 - (animatorValue - 270) / 5;
        } else if (animatorValue <720) {
            startAngle = 135-(animatorValue-630)/2+5;
            sweepAngle = 260-(animatorValue-270)/5;
        }else{
//          转完两圈
            startAngle = 135-(animatorValue-630)/2-(animatorValue-720)/6+5;
            sweepAngle = 170;
        }
        canvas.drawArc(rectF, startAngle, sweepAngle, false, mPaint);
        canvas.drawPoints(new float[]{-point, -point, point, -point}, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            start();    
        }
        return true;
    }
    
}
