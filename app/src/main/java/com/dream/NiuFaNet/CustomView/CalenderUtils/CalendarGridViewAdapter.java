package com.dream.NiuFaNet.CustomView.CalenderUtils;

import android.content.Context;
import android.graphics.Color;

import androidx.recyclerview.widget.OrientationHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.MainCalendarBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalenderMainContract;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalenderMainPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.DateUtil;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * 显示日期的adapter
 */
public class CalendarGridViewAdapter extends BaseAdapter implements CalenderMainContract.View {

    @Inject
    CalenderMainPresenter calenderMainPresenter;

    /**
     * 日历item中默认id从0xff0000开始
     */
    private final static int DEFAULT_ID = 0xff0000;
    private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历
    private Calendar calSelected = Calendar.getInstance(); // 选择的日历
    private String currentMonth = "";
    private int firstDays;
    /**
     * 标注的日期
     */
    private List<Date> markDates;

    private Context mContext;

    private Calendar calToday = Calendar.getInstance(); // 今日
    private ArrayList<MainCalendarBean> titles;

    private ArrayList<MainCalendarBean> getDates() {

        Calendar mCal = calStartDate;
        mCal.set(Calendar.DATE, 1);
        int weekDay = mCal.get(Calendar.DAY_OF_WEEK)-Calendar.MONDAY;
        if (weekDay<0){
            weekDay = 6;
        }
        int currentMonthLastDay = DateUtil.getCurrentMonthLastDay(mCal);
        int days = weekDay + currentMonthLastDay;
        int size = 35;
        if (days<=28){//闰月的时候
            size = 28;
        }else if (days>35&&days<=42){
            size = 42;
        }

        UpdateStartDateForMonth();

        ArrayList<MainCalendarBean> alArrayList = new ArrayList<MainCalendarBean>();
        for (int i = 1; i <= size; i++) {
            MainCalendarBean bean = new MainCalendarBean();
            bean.setDate(calStartDate.getTime());
            alArrayList.add(bean);
            calStartDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        return alArrayList;
    }

    // construct
    public CalendarGridViewAdapter(Context context, Calendar cal, List<Date> dates, String userId) {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        calenderMainPresenter.attachView(this);
        calStartDate = cal;
        this.mContext = context;
        titles = getDates();
        this.markDates = dates;

        initData(userId);

    }

    public void initData(String userId) {
        String starDate = DateFormatUtil.getTime(titles.get(0).getDate(), "yyyy-MM-dd");
        String endDate = DateFormatUtil.getTime(titles.get(titles.size() - 1).getDate(), "yyyy-MM-dd");
        calenderMainPresenter.getCalenders(userId, starDate + Const.startTime, endDate + Const.endTime);
    }

    public CalendarGridViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 整个Item
        LinearLayout itemLayout = new LinearLayout(mContext);
        itemLayout.setId(position + DEFAULT_ID);
        itemLayout.setGravity(Gravity.CENTER);
        itemLayout.setOrientation(OrientationHelper.VERTICAL);
        itemLayout.setBackgroundColor(Color.WHITE);

        MainCalendarBean dataBean = (MainCalendarBean) getItem(position);
        Date myDate = dataBean.getDate();
        itemLayout.setTag(myDate);
        Calendar calCalendar = Calendar.getInstance();
        calCalendar.setTime(myDate);

        //添加标识lay
        LinearLayout.LayoutParams lay_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lay_params.height = DensityUtil.dip2px(5);
        LinearLayout topLay = new LinearLayout(mContext);
        topLay.setLayoutParams(lay_params);
        itemLayout.addView(topLay);

        // 显示日期day
        TextView textDay = new TextView(mContext);// 日期
        LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textDay.setGravity(Gravity.CENTER_HORIZONTAL);
        int day = myDate.getDate(); // 日期

        if (day == 1 && currentMonth.isEmpty()) {
            long time = calCalendar.getTime().getTime();
            String mm = DateFormatUtil.getTime(time, "MM");
            currentMonth = mm;
        }

        long time = calCalendar.getTime().getTime();
        String mm = DateFormatUtil.getTime(time, "MM");
        if (mm.equals(currentMonth)) {
            textDay.setTextColor(Color.BLACK);
        } else {
            textDay.setTextColor(ResourcesUtils.getColor(R.color.cldcontent));
        }
        textDay.setTextSize(14);
        textDay.setText(String.valueOf(day));
        textDay.setId(position + DEFAULT_ID);
        itemLayout.addView(textDay, text_params);
        // 显示公历
        TextView chineseDay = new TextView(mContext);
        LinearLayout.LayoutParams chinese_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chineseDay.setGravity(Gravity.CENTER_HORIZONTAL);
        chineseDay.setTextSize(9);
        CalendarUtil calendarUtil = new CalendarUtil(calCalendar);
        chineseDay.setText(calendarUtil.toString());
        itemLayout.addView(chineseDay, chinese_params);

        ImageView isWork = new ImageView(mContext);
        LinearLayout.LayoutParams isWork_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        isWork_params.topMargin = 5;
        isWork_params.bottomMargin = 5;
        isWork.setLayoutParams(isWork_params);
        isWork.setImageResource(R.drawable.shape_circle_cal);
        itemLayout.addView(isWork);

        // 如果是当前日期则显示不同颜色
        if (equalsDate(calToday.getTime(), myDate)) {
            topLay.setBackgroundColor(ResourcesUtils.getColor(R.color.main_botcandy));
            if (dataBean.isWork()) {
                isWork.setImageResource(R.drawable.shape_circle_cal);
            } else {
                isWork.setImageResource(R.drawable.shape_circle_cal2);
            }
        } else {
            topLay.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
            if (dataBean.isWork()) {
                isWork.setImageResource(R.drawable.shape_circle_cal);
            } else {
                isWork.setImageResource(R.drawable.shape_circle_cal1);
            }
        }

        // 选择的item
        if (equalsDate(calSelected.getTime(), myDate) && !equalsDate(calToday.getTime(), myDate)) {
            itemLayout.setBackgroundColor(ResourcesUtils.getColor(R.color.layou_mrbg));
            topLay.setBackgroundColor(ResourcesUtils.getColor(R.color.layou_mrbg));
            if (dataBean.isWork()) {
                isWork.setImageResource(R.drawable.shape_circle_cal);
            } else {
                isWork.setImageResource(R.drawable.shape_circle_cal2);
            }

            if (position==0){
                SpUtils.savaUserInfo(Const.leftScroll, true);
            }else {
                SpUtils.savaUserInfo(Const.leftScroll, false);
            }

            if (position==titles.size()-1){
                SpUtils.savaUserInfo(Const.rightScroll, true);
            }else {
                SpUtils.savaUserInfo(Const.rightScroll, false);
            }

        }

       /* if (equalsDate(calSelected.getTime(), myDate)) {
            SpUtils.savaUserInfo(Const.scrollPosition, position);
        }*/



       /* // 这里用于比对是不是比当前日期小，如果比当前日期小则显示浅灰色
        if (!CalendarUtil.compare(myDate, calToday.getTime())) {
            itemLayout.setBackgroundColor(Color.argb(0xff, 0xee, 0xee, 0xee));
            textDay.setTextColor(Color.argb(0xff, 0xc0, 0xc0, 0xc0));
            chineseDay.setTextColor(Color.argb(0xff, 0xc0, 0xc0, 0xc0));
        } else {
            chineseDay.setTextColor(Color.argb(0xff, 0xc2, 0xa5, 0x3d));
            chineseDay.setTextColor(Color.argb(0xff, 0x60, 0x3b, 0x07));
            // 设置背景颜色
            if (equalsDate(calSelected.getTime(), myDate)) {
                // 选择的
                itemLayout.setBackgroundColor(Color.argb(0xff, 0xdc, 0xe2, 0xff));
            } else {
                if (equalsDate(calToday.getTime(), myDate)) {
                    // 当前日期faedda
                    itemLayout.setBackgroundColor(Color.argb(0xff, 0xfa, 0xed, 0xda));
                }
            }
        }*/

        /** 设置标注日期颜色 */
        if (markDates != null) {
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            for (Date date : markDates) {
                if (format.format(myDate).equals(format.format(date))) {
                    itemLayout.setBackgroundColor(ResourcesUtils.getColor(R.color.layou_mrbg));
                    break;
                }
            }
        }

        return itemLayout;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @SuppressWarnings("deprecation")
    private Boolean equalsDate(Date date1, Date date2) {
        if (date1.getYear() == date2.getYear()
                && date1.getMonth() == date2.getMonth()
                && date1.getDate() == date2.getDate()) {
            return true;
        } else {
            return false;
        }

    }

    // 根据改变的日期更新日历
    // 填充日历控件用
    private void UpdateStartDateForMonth() {
        calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天

        // 星期一是2 星期天是1 填充剩余天数
        int iDay = 0;
        int iFirstDayOfWeek = Calendar.MONDAY;
        int iStartDay = iFirstDayOfWeek;
        if (iStartDay == Calendar.MONDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
            if (iDay < 0)
                iDay = 6;
        }
        if (iStartDay == Calendar.SUNDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
            if (iDay < 0)
                iDay = 6;
        }
        Log.e("tag","iDay="+iDay);
        calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
        firstDays = iDay;
//        calStartDate.add(Calendar.DAY_OF_MONTH, -1);// 周日第一位
    }

    public void setSelectedDate(Calendar cal) {
        calSelected = cal;
    }

    @Override
    public void showData(CalenderedBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<CalenderedBean.DataBean> data = dataBean.getData();
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    String beginTime = data.get(i).getBeginTime();
                    Date timeb = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
                    String endTime = data.get(i).getEndTime();
                    Date endD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
                    long startL = timeb.getTime();
                    long endL = endD.getTime();
                    for (int j = 0; j < titles.size(); j++) {
                        Date currentD = titles.get(j).getDate();
                        String ymd = DateFormatUtil.getTime(currentD, Const.Y_M_D);
                        Date startDL = DateFormatUtil.getTime(ymd + Const.endTime, Const.YMD_HMS);
                        long startTL = startDL.getTime();
                        Date endDd = DateFormatUtil.getTime(ymd + Const.startTime, Const.YMD_HMS);
                        long endDL = endDd.getTime();
                        if (startTL >= startL && endDL <= endL) {
                            titles.get(j).setWork(true);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }


}
