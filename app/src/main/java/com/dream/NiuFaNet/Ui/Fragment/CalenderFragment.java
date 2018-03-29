package com.dream.NiuFaNet.Ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Adapter.WeekAdapter;
import com.dream.NiuFaNet.Base.BaseFragmentV4;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.BusBean.LoginBus;
import com.dream.NiuFaNet.Bean.BusBean.RefreshCalBean;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.WeekBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalenderMainContract;
import com.dream.NiuFaNet.CustomView.CalenderRvUtils.CalendarRecyclerView;
import com.dream.NiuFaNet.CustomView.CalenderUtils.CalendarView;
import com.dream.NiuFaNet.CustomView.MessageRightPopView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalenderMainPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.CalenderDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.MyFrendsActivity;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.PopWindowUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.StringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.haibin.calendarview.CalendarLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16 0016.
 */
public class CalenderFragment extends BaseFragmentV4 implements CalenderMainContract.View  , com.haibin.calendarview.CalendarView.OnDateSelectedListener,
        com.haibin.calendarview.CalendarView.OnYearChangeListener{

    @Bind(R.id.curent_datetv)
    TextView curent_datetv;
    @Bind(R.id.work_rv)
    CalendarRecyclerView work_rv;
    @Bind(R.id.root_lay)
    LinearLayout root_lay;

    @Inject
    CalenderMainPresenter calenderMainPresenter;
   // TextView mTextCurrentDay;
    @Bind(R.id.calendarView)
    com.haibin.calendarview.CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    @Bind(R.id.calendarLayout)
    CalendarLayout mCalendarLayout;
    private PopupWindow popupWindow;
    @Override
    public void initView() {
       DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        calenderMainPresenter.attachView(this);
        //设置标注日期
        List<Date> markDates = new ArrayList<Date>();
        markDates.add(new Date());
        work_rv.setData(CommonAction.getUserId());

        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);

        mYear = mCalendarView.getCurYear();
        curent_datetv.setText(mYear+"年"+mCalendarView.getCurMonth()+"月");
       // mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @Override
    public void initResume() {

    }

    @Override
    public void initEvents() {
        mCalendarView.setOnDateSelectedListener(new com.haibin.calendarview.CalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(com.haibin.calendarview.Calendar calendar, boolean isClick) {
                //work_rv.setLocation(calendar,CommonAction.getUserId());
            String starDate =calendar.getYear()+"-"+calendar.getMonth()+"-"+calendar.getDay();
            String endDate =calendar.getYear()+"-"+calendar.getMonth()+"-"+(calendar.getDay()+1);
             calenderMainPresenter.getCalenders(CommonAction.getUserId(), starDate + Const.startTime, endDate + Const.endTime);

            }
        });
    }

    @Override
    public void initDta() {

        List<com.haibin.calendarview.Calendar> schemes = new ArrayList<>();
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        schemes.add(getSchemeCalendar(year, month, 11, 0xFFedc56d, "记"));

        mCalendarView.setSchemeDate(schemes);
    }
    private com.haibin.calendarview.Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new com.haibin.calendarview.Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }
    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_nfcalender, null);

    }

    @Override
    public void showData(CalenderedBean dataBean) {
            Log.i("myTag",dataBean.getData().size()+"多少事件");
      /*  if (dataBean.getError().equals(Const.success)){
            List<CalenderedBean.DataBean> data = dataBean.getData();

            if (data!=null&&data.size()>0){
                noCalendarView.setVisibility(GONE);
                dataLay.setVisibility(VISIBLE);
                dataList.clear();
                dataList.addAll(data);
                workAdapter.notifyDataSetChanged();
            }else {
                dataLay.setVisibility(GONE);
                if (CommonAction.getUserId().equals(userId)){
                    noCalendarView.setVisibility(VISIBLE);
                }else {
                    noCalendarView.setVisibility(GONE);
                }
            }
        }else {
            dataLay.setVisibility(GONE);
            if (CommonAction.getUserId().equals(userId)){
                noCalendarView.setVisibility(VISIBLE);
            }else {
                noCalendarView.setVisibility(GONE);
            }
        }*/
    }

    @Override
    public void showError() {
       ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {

    }
   //
    @Override
    public void onDateSelected(com.haibin.calendarview.Calendar calendar, boolean isClick) {
        mYear = calendar.getYear();
        curent_datetv.setText(mYear+"年"+calendar.getMonth()+"月");
    }

    @Override
    public void onYearChange(int year) {

    }

    private class WorkAdapter extends RVBaseAdapter<CalenderedBean.DataBean> {

        public WorkAdapter(Context context, List<CalenderedBean.DataBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, final CalenderedBean.DataBean dataBean, int position) {
            String fileCount = dataBean.getFileCount();
            if (StringUtil.NoNullOrEmpty(fileCount)&&!fileCount.equals("0")){
//                holder.getView(R.id.att_iv).setVisibility(View.VISIBLE);
            }else {
                holder.getView(R.id.att_iv).setVisibility(View.GONE);
            }
            TextView work_title = holder.getView(R.id.work_title);
            ImageView dot_iv = holder.getView(R.id.dot_iv);
            boolean isContais = false;
            boolean isClick = false;
            String participantIDs = dataBean.getParticipantIDs();
            if (!participantIDs.isEmpty()){
                if (participantIDs.contains(",")){
                    String[] titles = participantIDs.split("\\,");
                    for (int i = 0; i < titles.length; i++) {
                        if (CommonAction.getUserId().equals(titles[i])){
                            isContais = true;
                        }
                    }
                }
            }
            String type = dataBean.getType();
         /*   if (isVisible||isContais){
                holder.setText(R.id.work_title,dataBean.getTitle());
                isClick = true;
            }else {
                if (isWorkVisible&&type.equals("1")){
                    holder.setText(R.id.work_title,dataBean.getTitle());
                    isClick = true;
                }else {
                    holder.setText(R.id.work_title,"已安排");
                    isClick = false;
                }
            }*/
            String beginTime = dataBean.getBeginTime();
            Date beDa = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
            String beStr = DateFormatUtil.getTime(beDa, Const.HM);
            String endTime = dataBean.getEndTime();
            Date endD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
            String endStr = DateFormatUtil.getTime(endD, Const.HM);
            // holder.setText(R.id.time_duration,beStr+"~"+endStr);
            holder.setText(R.id.starttime_tv,beStr);
            holder.setText(R.id.endtime_tv,endStr);
            Calendar calendar = Calendar.getInstance();
            if (calendar.getTimeInMillis()>endD.getTime()){
                work_title.setTextColor(ResourcesUtils.getColor(R.color.color6c));
                dot_iv.setImageResource(R.drawable.shape_circle_dot);
            }else {
                work_title.setTextColor(ResourcesUtils.getColor(R.color.color2a));
                dot_iv.setImageResource(R.drawable.shape_circle_voice);
            }
            final boolean finalIsClick = isClick;
            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (finalIsClick){
                        String scheduleId = dataBean.getScheduleId();
                        if (scheduleId!=null&&!scheduleId.isEmpty()){
                            Intent intent = new Intent(getContext(), CalenderDetailActivity.class);
                            intent.putExtra(Const.scheduleId, scheduleId);
                            intent.putExtra(Const.userId,CommonAction.getUserId());
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
