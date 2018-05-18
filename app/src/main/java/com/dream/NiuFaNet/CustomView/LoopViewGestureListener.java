package com.dream.NiuFaNet.CustomView;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.contrarywind.view.WheelView;


/**
 * Created by hou on 2018/4/28.
 */

public class LoopViewGestureListener extends GestureDetector.SimpleOnGestureListener {
    private final WheelView wheelView;


    public LoopViewGestureListener(WheelView wheelView) {
        this.wheelView = wheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        wheelView.scrollBy(velocityY);
        return true;
    }
}
