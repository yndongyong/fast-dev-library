package org.fastandroid.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import org.yndongyong.fastandroid.utils.AbImageUtil;
import org.yndongyong.fastandroid.utils.AbScreenUtils;

/**
 * Created by Dong on 2016/6/1.
 */
public class CustomView extends View {
    private int mWidth;
    private int mHeight;

    private Paint mPaint;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
        initStyle();
    }

    @TargetApi(21)
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initStyle();
    }

    void initStyle() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//开启抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(0xaa9e9394);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);//讲坐标原点从0，0移动到view的中心
        canvas.drawPoint(0, 0, mPaint);
        //画坐标轴
        canvas.drawPoints(new float[]{
                -mWidth / 2 * 0.8f, 0,
                mWidth / 2 * 0.8f, 0,
                0, -mHeight / 2 * 0.8f,
                0, mHeight / 2 * 0.8f}, mPaint);

        mPaint.setStrokeWidth(4);
        //将坐标轴连线
        canvas.drawLines(new float[]{
                -mWidth/2*0.8f,0,mWidth/2*0.8f,0,
                0,-mHeight/2*0.8f,0,mHeight/2*0.8f
        }, mPaint);
        
        //画箭头
       /* mPaint.setStrokeWidth(6);
        canvas.drawLines(new float[]{
                
                
        }, mPaint);*/
        //画x轴箭头
//        canvas.scale(-1,1);//翻转x轴
        Path xpath = new Path();
        xpath.moveTo(mWidth/2*0.8f*0.95f,-mWidth/2*0.8f*0.05f);
        xpath.lineTo(mWidth/2*0.8f,0);
        xpath.lineTo(mWidth/2*0.8f*0.95f,mWidth/2*0.8f*0.05f);
        canvas.drawPath(xpath,mPaint);
        //画y轴箭头
//        canvas.scale(1,-1);//翻转y轴
        Path ypath = new Path();
        ypath.moveTo( -mWidth / 2 * 0.8f * 0.05f,mHeight / 2 * 0.8f * 0.95f);
        ypath.lineTo(0,mHeight/2*0.8f);
        ypath.lineTo(mWidth/2*0.8f*0.05f,mHeight/2*0.8f*0.95f);
        
        canvas.drawPath(ypath,mPaint);
        //绘制矩形
        mPaint.setStyle(Paint.Style.STROKE);
        float l = -mWidth/8;
        float t = -mHeight/8;
        float r = mWidth / 8;
        float b = mHeight / 8;
        canvas.drawRect(l, t, r, b,mPaint);
    }
}
