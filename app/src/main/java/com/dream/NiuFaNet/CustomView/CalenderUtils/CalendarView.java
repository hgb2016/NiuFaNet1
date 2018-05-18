/****************
 * mmp
 *******************/
package com.dream.NiuFaNet.CustomView.CalenderUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.dream.NiuFaNet.CustomView.CalenderRvUtils.ChildRvReLay;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Utils.SpUtils;

/**
 * 日历控件，支持旧历、节气、日期标注、点击操作 （参考网络上的日历控件改写）
 *
 * @author wangcccong
 * @version 1.406 create at: Mon, 03 Sep. 2014 
 * <br>update at: Mon, 23 Sep. 2014 
 * &nbsp;&nbsp;新增日期标注和点击操作
 */
public class CalendarView extends LinearLayout implements OnTouchListener,
        AnimationListener, OnGestureListener {

    private TitleListener titleListener;
    private MoveListener moveListener;
    public interface TitleListener {
        void setTitle(String titleStr,Calendar calendar);
    }
    public void setTitle(TitleListener titleListener){
        this.titleListener = titleListener;
    }

    public interface MoveListener{
        void moveListener(Calendar calendar);
    }

    public void setOnMoveListener(MoveListener moveListener){
        this.moveListener = moveListener;
    }
    /**
     * 点击日历
     */
    public interface OnCalendarViewListener {
        void onCalendarItemClick(CalendarView view, Date date);
    }

    /** 顶部控件所占高度 */
    private final static int TOP_HEIGHT = 40;
    /** 日历item中默认id从0xff0000开始 */
    private final static int DEFAULT_ID = 0xff0000;

    // 判断手势用
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    // 屏幕宽度和高度
    private int screenWidth;

    // 动画
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;
    private ViewFlipper viewFlipper;
    private GestureDetector mGesture = null;

    /** 上一月 */
    private GridView gView1;
    /** 当月 */
    private GridView gView2;
    /** 下一月 */
    private GridView gView3;

    boolean bIsSelection = false;// 是否是选择事件发生
    private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
    private Calendar calSelected = Calendar.getInstance(); // 选择的日历
    private CalendarGridViewAdapter gAdapter;
    private CalendarGridViewAdapter gAdapter1;
    private CalendarGridViewAdapter gAdapter3;

    private Calendar selectCalendar;
    private boolean isBotscroll;
    private boolean isToday;

    private LinearLayout mMainLayout;
    private TextView mTitle; // 显示年月

    private int iMonthViewCurrentMonth = 0; // 当前视图月
    private int iMonthViewCurrentYear = 0; // 当前视图年

    public static final int caltitleLayoutID = 66; // title布局ID
    public static final int calLayoutID = 55; // 日历布局ID
    private Context mContext;
    private boolean isTopScroll;

    private OnCalendarViewListener mListener;

    /** 标注日期 */
    private  List<Date> markDates;
    private String userId;
    public CalendarView(Context context,String userId) {
        super(context);
        this.userId = userId;
        mContext = context;
        markDates = new ArrayList<Date>();
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
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
        LinearLayout.LayoutParams main_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mMainLayout.setLayoutParams(main_params);
        mMainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mMainLayout.setOrientation(LinearLayout.VERTICAL);
        addView(mMainLayout);

        mTitle = new TextView(mContext);

        // 底部显示日历
        viewFlipper = new ViewFlipper(mContext);
        RelativeLayout.LayoutParams fliper_params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        fliper_params.addRule(RelativeLayout.BELOW, caltitleLayoutID);
        mMainLayout.addView(viewFlipper, fliper_params);
        generateClaendarGirdView(true);

    }

    public void locationToday(boolean isToday){
        generateClaendarGirdView(true);
        this.isToday = isToday;
    }

    /** 生成底部日历 */
    private void generateClaendarGirdView(boolean isFirst) {
        Calendar tempSelected1 = Calendar.getInstance(); // 临时
        Calendar tempSelected2 = Calendar.getInstance(); // 临时
        Calendar tempSelected3 = Calendar.getInstance(); // 临时
        if (!isFirst){
            tempSelected1.setTime(calStartDate.getTime());
            tempSelected2.setTime(calStartDate.getTime());
            tempSelected3.setTime(calStartDate.getTime());
        }else {
            calStartDate = Calendar.getInstance();
            iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月
            iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);// 得到当前日历显示的年
        }

        gView1 = new CalendarGridView(mContext);
        tempSelected1.add(Calendar.MONTH, -1);
        gAdapter1 = new CalendarGridViewAdapter(mContext, tempSelected1, markDates,userId);
        gView1.setAdapter(gAdapter1);// 设置菜单Adapter
        gView1.setId(calLayoutID);

        gView2 = new CalendarGridView(mContext);
        gAdapter = new CalendarGridViewAdapter(mContext, tempSelected2, markDates,userId);
        gView2.setAdapter(gAdapter);// 设置菜单Adapter
        gView2.setId(calLayoutID);

        gView3 = new CalendarGridView(mContext);
        tempSelected3.add(Calendar.MONTH, 1);
        gAdapter3 = new CalendarGridViewAdapter(mContext, tempSelected3, markDates,userId);
        gView3.setAdapter(gAdapter3);// 设置菜单Adapter
        gView3.setId(calLayoutID);

        gView2.setOnTouchListener(this);
        gView1.setOnTouchListener(this);
        gView3.setOnTouchListener(this);

        if (viewFlipper.getChildCount() != 0) {
            viewFlipper.removeAllViews();
        }

        viewFlipper.addView(gView2);
        viewFlipper.addView(gView3);
        viewFlipper.addView(gView1);

        String title = calStartDate.get(Calendar.YEAR)
                + "年"
                + NumberHelper.LeftPad_Tow_Zero(calStartDate
                .get(Calendar.MONTH) + 1) + "月";
        Log.e("cal","title="+title);
        if (titleListener!=null){
            titleListener.setTitle(title,calStartDate);
        }
        Log.e("tag","isBotscroll="+isBotscroll);
        Log.e("tag","isTopScroll="+isTopScroll);
        if (isBotscroll){
            setScroll(selectCalendar);
        }

        if (isTopScroll){
            setScroll(calStartDate);
            if (moveListener!=null){
                moveListener.moveListener(calStartDate);
            }
        }

    }

    // 上一个月
    private void setPrevViewItem() {
        iMonthViewCurrentMonth--;// 当前选择月--
        // 如果当前月为负数的话显示上一年
        if (iMonthViewCurrentMonth == -1) {
            iMonthViewCurrentMonth = 11;
            iMonthViewCurrentYear--;
        }
        calStartDate.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
        calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth); // 设置月
        calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear); // 设置年
    }

    // 下一个月
    private void setNextViewItem() {
        iMonthViewCurrentMonth++;
        if (iMonthViewCurrentMonth == 12) {
            iMonthViewCurrentMonth = 0;
            iMonthViewCurrentYear++;
        }
        calStartDate.set(Calendar.DAY_OF_MONTH, 1);
        calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
        calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
    }

    /**
     * 设置标注的日期
     *
     * @param markDates
     */
    public void setMarkDates(List<Date> markDates) {
        this.markDates.clear();
        this.markDates.addAll(markDates);
        gAdapter.notifyDataSetChanged();
        gAdapter1.notifyDataSetChanged();
        gAdapter3.notifyDataSetChanged();
    }

    public void setScroll(Calendar calendar){
        calSelected.setTime(calendar.getTime());

        gAdapter.setSelectedDate(calSelected);
        gAdapter.notifyDataSetChanged();

        gAdapter1.setSelectedDate(calSelected);
        gAdapter1.notifyDataSetChanged();

        gAdapter3.setSelectedDate(calSelected);
        gAdapter3.notifyDataSetChanged();

    }

    /**
     * 设置点击日历监听
     *
     * @param listener
     */
    public void setOnCalendarViewListener(OnCalendarViewListener listener) {
        this.mListener = listener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGesture.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                toNextMonth();
                isTopScroll = true;
                isBotscroll = false;
                return true;

            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                toPrevMonth();
                isTopScroll = true;
                isBotscroll = false;
                return true;

            }
        } catch (Exception e) {
            // nothing
        }
        return false;
    }

    public void toPrevMonth() {
        viewFlipper.setInAnimation(slideRightIn);
        viewFlipper.setOutAnimation(slideRightOut);
        viewFlipper.showPrevious();
        setPrevViewItem();
    }

    public void moveCalendarView(boolean isBotscroll,Calendar calendar){
        boolean isLeft = (boolean) SpUtils.getParam(Const.leftScroll, false);
        boolean isRight = (boolean) SpUtils.getParam(Const.rightScroll, false);
        if (isLeft){
            toPrevMonth();
        }else if (isRight){
            toNextMonth();
        }
        this.isBotscroll = isBotscroll;
        isTopScroll = false;
        selectCalendar = calendar;
    }
    public void toNextMonth() {
        viewFlipper.setInAnimation(slideLeftIn);
        viewFlipper.setOutAnimation(slideLeftOut);
        viewFlipper.showNext();
        setNextViewItem();
    }

    public void refreshData(Calendar calendar){
        calStartDate = calendar;
        this.userId = CommonAction.getUserId();
        generateClaendarGirdView(false);
        setScroll(calendar);
    }
    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        // 得到当前选中的是第几个单元格
        int pos = gView2.pointToPosition((int) e.getX(), (int) e.getY());
        LinearLayout txtDay = (LinearLayout) gView2.findViewById(pos
                + DEFAULT_ID);
        if (txtDay != null) {
            if (txtDay.getTag() != null) {
                Date date = (Date) txtDay.getTag();
                calSelected.setTime(date);

                gAdapter.setSelectedDate(calSelected);
                gAdapter.notifyDataSetChanged();

                gAdapter1.setSelectedDate(calSelected);
                gAdapter1.notifyDataSetChanged();

                gAdapter3.setSelectedDate(calSelected);
                gAdapter3.notifyDataSetChanged();
                if (mListener != null)
                    mListener.onCalendarItemClick(this, date);
            }
        }
        return false;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
        generateClaendarGirdView(false);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

}

