/****************
 * mmp
 *******************/
package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

/**
 * 日历控件，支持旧历、节气、日期标注、点击操作 （参考网络上的日历控件改写）
 *
 * @author wangcccong
 * @version 1.406 create at: Mon, 03 Sep. 2014 
 * <br>update at: Mon, 23 Sep. 2014 
 * &nbsp;&nbsp;新增日期标注和点击操作
 */
public class CommonFlipperView extends LinearLayout implements OnTouchListener, AnimationListener, OnGestureListener {


    // 屏幕宽度和高度
    private int screenWidth;

    // 动画
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;
    private ViewFlipper viewFlipper;
    private GestureDetector mGesture = null;

    private LinearLayout mMainLayout;

    private Context mContext;

    public CommonFlipperView(Context context) {
        super(context);
    }

    public CommonFlipperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    // 初始化相关工作
    protected void init() {
        // 得到屏幕的宽度
        screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;

        // 滑动的动画
        slideLeftIn = new TranslateAnimation(screenWidth, 0, 0, 0);
        slideLeftIn.setDuration(400);
        slideLeftIn.setAnimationListener(this);
        slideLeftOut = new TranslateAnimation(0, -screenWidth, 0, 0);
        slideLeftOut.setDuration(400);
        slideLeftOut.setAnimationListener(this);
        slideRightIn = new TranslateAnimation(-screenWidth, 0, 0, 0);
        slideRightIn.setDuration(400);
        slideRightIn.setAnimationListener(this);
        slideRightOut = new TranslateAnimation(0, screenWidth, 0, 0);
        slideRightOut.setDuration(400);
        slideRightOut.setAnimationListener(this);

        // 手势操作
        mGesture = new GestureDetector(mContext, this);

        // 获取到当前日期
//        UpdateStartDateForMonth();

        // 绘制界面
        setOrientation(LinearLayout.HORIZONTAL);
        mMainLayout = new LinearLayout(mContext);
        LayoutParams main_params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mMainLayout.setLayoutParams(main_params);
        mMainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mMainLayout.setOrientation(LinearLayout.VERTICAL);
        addView(mMainLayout);

        // 底部显示日历
        viewFlipper = new ViewFlipper(mContext);
        RelativeLayout.LayoutParams fliper_params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mMainLayout.addView(viewFlipper, fliper_params);
        

    }
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}