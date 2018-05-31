package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.contrarywind.view.WheelView;

/**
 * Created by hou on 2018/4/28.
 */

public class CustomWheelView1 extends WheelView {
    // 绘制几个条目，实际上第一项和最后一项Y轴压缩成0%了，所以可见的数目实际为9


    @Override
    public void smoothScroll(ACTION action) {
        super.smoothScroll(action);
    }

    @Override
    public void cancelFuture() {
        super.cancelFuture();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public int getItemsCount() {
        return super.getItemsCount();
    }

    @Override
    public void setLabel(String label) {
        super.setLabel(label);
    }

    @Override
    public void isCenterLabel(boolean isCenterLabel) {
        super.isCenterLabel(isCenterLabel);
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
    }

    @Override
    public int getTextWidth(Paint paint, String str) {
        return super.getTextWidth(paint, str);
    }

    @Override
    public void setIsOptions(boolean options) {
        super.setIsOptions(options);
    }

    @Override
    public void setTextColorOut(int textColorOut) {
        super.setTextColorOut(textColorOut);
    }

    @Override
    public void setTextColorCenter(int textColorCenter) {
        super.setTextColorCenter(textColorCenter);
    }

    @Override
    public void setTextXOffset(int textXOffset) {
        super.setTextXOffset(textXOffset);
    }

    @Override
    public void setDividerColor(int dividerColor) {
        super.setDividerColor(dividerColor);

    }

    @Override
    public void setDividerType(DividerType dividerType) {
        super.setDividerType(dividerType);
    }

    @Override
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        super.setLineSpacingMultiplier(lineSpacingMultiplier);
    }

    @Override
    public boolean isLoop() {
        return super.isLoop();
    }

    @Override
    public float getTotalScrollY() {
        return super.getTotalScrollY();
    }

    @Override
    public void setTotalScrollY(float totalScrollY) {
        super.setTotalScrollY(totalScrollY);
    }

    @Override
    public float getItemHeight() {
        return super.getItemHeight();
    }

    @Override
    public void setItemHeight(float itemHeight) {
        super.setItemHeight(itemHeight);
    }

    @Override
    public int getInitPosition() {
        return super.getInitPosition();
    }

    @Override
    public void setInitPosition(int initPosition) {
        super.setInitPosition(initPosition);
    }

    @Override
    public Handler getHandler() {
        return super.getHandler();
    }

    public CustomWheelView1(Context context) {
        super(context);
    }

    public CustomWheelView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
