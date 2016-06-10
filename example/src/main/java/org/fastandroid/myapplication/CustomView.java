package org.fastandroid.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

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
        canvas.translate(mWidth / 2, mHeight / 2);//将坐标原点从0，0移动到view的中心
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
        /*mPaint.setStyle(Paint.Style.STROKE);
        float l = -mWidth/8;
        float t = -mHeight/8;
        float r = mWidth / 8;
        float b = mHeight / 8;
        canvas.drawRect(l, t, r, b,mPaint);*/

        
        //画布平移
       /* canvas.translate(200, 200);//移动画布的坐标原点
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(l, t, r, b,mPaint);*/
        
        //画布旋转
       /* canvas.rotate(90);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(l, t, r, b,mPaint);*/
        //缩放
       /* canvas.scale(0.5f,0.5f);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(l, t, r, b,mPaint);*/
        //错切
       /* canvas.skew(0.5f,0.5f);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(l, t, r, b,mPaint);*/
        
        //画布的保存
        /*mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        canvas.drawCircle(400, 0, 100, mPaint);
        int saveCount = canvas.save();
        mPaint.setColor(Color.BLUE);
        canvas.rotate(90);
        canvas.drawCircle(400, 0, 100, mPaint);
        canvas.restoreToCount(saveCount);
        mPaint.setColor(0xaa9e9394);
        canvas.drawCircle(600, 0, 100, mPaint);*/

        //豆瓣加载动画
        
        //绘制连个点和半圆弧
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.GREEN);

        float p = Math.min(mHeight, mWidth) * 0.2f/2;
        float r = p * (float) Math.sqrt(2);
        RectF rect = new RectF(-r, -r, r, r);
       /* canvas.drawArc(rect, 0, 180, false, mPaint);
        
        canvas.drawPoints(new float[]{
               -p,-p,p,-p 
        },mPaint);*/
        
        //旋转动画是的一帧是一个270的圆弧
        
        canvas.drawArc(rect,-180,270,false,mPaint);
    }
}
