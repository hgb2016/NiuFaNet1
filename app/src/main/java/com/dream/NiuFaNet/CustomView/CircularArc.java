package com.dream.NiuFaNet.CustomView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Utils.DensityUtil;

public class CircularArc extends View {

    private Paint mPaint;
    private int mWidth;

    /**
     * 圆的宽度
     */
    private int mCircleWidth = 3;

    public CircularArc(Context context) {
        this(context, null);
    }

    public CircularArc(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mPaint = new Paint();
    }

    public CircularArc(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setAntiAlias(true);//取消锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setColor(Color.CYAN);

        /**
         * 这是一个居中的圆
         */
        float x = 0;
        float y = DensityUtil.dip2px(112);
        float y1 = DensityUtil.dip2px(165);
        float height = (y1 - y)*2;

        RectF oval = new RectF( x, y, mWidth, height);
        canvas.drawArc(oval,180,180,false,mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getWidth();
        mWidth = width;

    }
}