/****************
 * mmp
 *******************/
package com.dream.NiuFaNet.CustomView.CalenderRvUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.ViewFlipper;


import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * 日历控件，支持旧历、节气、日期标注、点击操作 （参考网络上的日历控件改写）
 *
 * @author wangcccong
 * @version 1.406 create at: Mon, 03 Sep. 2014 
 * <br>update at: Mon, 23 Sep. 2014 
 * &nbsp;&nbsp;新增日期标注和点击操作
 */
public class CalendarRecyclerView extends LinearLayout implements OnTouchListener, AnimationListener, OnGestureListener {


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

    private ChildRvReLay viewRv1,viewRv2,viewRv3;
    private Context mContext;
    private String userId;

    // 判断手势用
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private boolean isMian;
    private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
    public CalendarRecyclerView(Context context) {
        super(context);
    }

    public CalendarRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        isMian = true;
        init();
    }
    public void setData(String userId){
        this.userId = userId;
        upLoadView(true);

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


        // 绘制界面
        setOrientation(LinearLayout.HORIZONTAL);
        mMainLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams main_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mMainLayout.setLayoutParams(main_params);
        mMainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mMainLayout.setOrientation(LinearLayout.VERTICAL);
        addView(mMainLayout);

        // 底部显示日历
        viewFlipper = new ViewFlipper(mContext);
        RelativeLayout.LayoutParams fliper_params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mMainLayout.addView(viewFlipper, fliper_params);

    }

    public void setLocation(Calendar c,String userId){
        calStartDate = c;
        this.userId = userId;
        upLoadView(false);
    }
    private void upLoadView(boolean isFirst) {

        Calendar tempSelected1 = Calendar.getInstance(); // 临时
        Calendar tempSelected2 = Calendar.getInstance(); // 临时
        Calendar tempSelected3 = Calendar.getInstance(); // 临时
        if (!isFirst){
            tempSelected1.setTime(calStartDate.getTime());
            tempSelected2.setTime(calStartDate.getTime());
            tempSelected3.setTime(calStartDate.getTime());
        }else {
            calStartDate = Calendar.getInstance();
        }
        tempSelected1.add(Calendar.DAY_OF_MONTH,-1);
        viewRv1 = new ChildRvReLay(mContext,tempSelected1,userId);

        viewRv2 = new ChildRvReLay(mContext,tempSelected2,userId);

        tempSelected3.add(Calendar.DAY_OF_MONTH,1);
        viewRv3 = new ChildRvReLay(mContext,tempSelected3,userId);


        viewRv2.setOnTouchListener(this);
        viewRv1.setOnTouchListener(this);
        viewRv3.setOnTouchListener(this);

        if (viewFlipper.getChildCount() != 0) {
            viewFlipper.removeAllViews();
        }
        viewFlipper.addView(viewRv2);
        viewFlipper.addView(viewRv3);
        viewFlipper.addView(viewRv1);

    }

    // 上一个月
    private void setPrevViewItem() {
        calStartDate.add(Calendar.DAY_OF_MONTH,-1);
    }

    // 下一个月
    private void setNextViewItem() {
        calStartDate.add(Calendar.DAY_OF_MONTH,1);
    }
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        upLoadView(false);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
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
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                viewFlipper.setInAnimation(slideLeftIn);
                viewFlipper.setOutAnimation(slideLeftOut);
                viewFlipper.showNext();
                setNextViewItem();
                scrollListener.onScrollView(calStartDate);
                return true;

            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                viewFlipper.setInAnimation(slideRightIn);
                viewFlipper.setOutAnimation(slideRightOut);
                viewFlipper.showPrevious();
                setPrevViewItem();
                scrollListener.onScrollView(calStartDate);
                return true;

            }
        } catch (Exception e) {
            // nothing
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGesture.onTouchEvent(motionEvent);
    }

    private ScrollListener scrollListener;
    public interface  ScrollListener{
        void onScrollView(Calendar c);
    };

    public void setScrollListener(ScrollListener scrollListener){
        this.scrollListener = scrollListener;
    }
}