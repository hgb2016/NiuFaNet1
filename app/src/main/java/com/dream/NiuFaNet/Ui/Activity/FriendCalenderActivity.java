package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.MainCalendarBean;
import com.dream.NiuFaNet.Bean.MarkDateBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.WorkVisibleBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalenderMainContract;
import com.dream.NiuFaNet.Contract.MarkDateContract;
import com.dream.NiuFaNet.Contract.WorkVisibleContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalenderMainPresenter;
import com.dream.NiuFaNet.Presenter.MarkDatePresenter;
import com.dream.NiuFaNet.Presenter.WorkVisiblePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.DateUtil;
import com.dream.NiuFaNet.Utils.GlideCircleTransform;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.group.GroupItemDecoration;
import com.dream.NiuFaNet.group.GroupRecyclerAdapter;
import com.dream.NiuFaNet.group.GroupRecyclerView;
import com.google.gson.Gson;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/3.
 */

public class FriendCalenderActivity extends CommonActivity implements MarkDateContract.View,CalenderMainContract.View ,WorkVisibleContract.View,CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener{
    @Bind(R.id.calendarView)
    com.haibin.calendarview.CalendarView mCalendarView;
    @Bind(R.id.username_tv)
    TextView username_tv;
    @Bind(R.id.head_iv)
    ImageView head_iv;
    @Bind(R.id.date_tv)
    TextView date_tv;
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.back_relay)
    RelativeLayout back_relay;
    @Bind(R.id.work_rv)
    GroupRecyclerView work_rv;
    @Bind(R.id.no_caltv)
    TextView no_caltv;
    @Inject
    CalenderMainPresenter calenderMainPresenter;
    @Inject
    MarkDatePresenter markDatePresenter;
    @Inject
    WorkVisiblePresenter mWorkVisiblePresenter;
    private MyFrendBean.DataBean tempData;
    private String userId;
    private WorkAdapter workAdapter;
    private List<CalenderedBean.DataBean> dataList = new ArrayList<>();
    private Calendar mCalendar;
    private boolean isVisible,isWorkVisible;

    @Override
    public int getLayoutId() {
        return R.layout.activity_frendcalender;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        calenderMainPresenter.attachView(this);
        mWorkVisiblePresenter.attachView(this);
        markDatePresenter.attachView(this);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        date_tv.setText(mCalendarView.getCurYear()+"年"+mCalendarView.getCurMonth()+"月");
        RvUtils.setOptionnoLine(work_rv, getParent());
        work_rv.setLayoutManager(new LinearLayoutManager(this));
        GroupItemDecoration<String, CalenderedBean.DataBean> itemDecoration = new GroupItemDecoration<>();
        itemDecoration.setChildItemOffset(1);
        work_rv.addItemDecoration(itemDecoration);
        workAdapter = new WorkAdapter(this, mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日", dataList);
        mCalendarView.setOnMonthChangeListener(new com.haibin.calendarview.CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                //标记日期
                String begindate=year+"-"+(month-1)+"-"+30;
                String enddate=year+"-"+month+"-"+30;
                markDatePresenter.toGetDateMark(userId,begindate,enddate);
            }
        });
    }

    @Override
    public void initDatas() {
        MyFrendBean.DataBean data = (MyFrendBean.DataBean) getIntent().getExtras().getSerializable("data");

        if (data!=null){
            tempData = data;
            userId = data.getFriendId();
            if (data.getFriendRemark()!=null&&!data.getFriendRemark().isEmpty()){
                title_tv.setText(data.getFriendRemark()+"的日程");
                username_tv.setText(data.getFriendRemark());
            }else {
                title_tv.setText(data.getFriendName()+"的日程");
                username_tv.setText(data.getFriendName());
            }


            String headUrl = data.getHeadUrl();
            loginHead(headUrl);
            if (userId.equals(CommonAction.getUserId())){
                isVisible = true;
                loadData();
            }else {
                mWorkVisiblePresenter.getWorkVisible(userId);
            }
        }
        //标记日期
        String begindate=mCalendarView.getCurYear()+"-"+(mCalendarView.getCurMonth()-1)+"-"+30;
        String enddate=mCalendarView.getCurYear()+"-"+(mCalendarView.getCurMonth()+1)+"-"+30;
        markDatePresenter.toGetDateMark(userId,begindate,enddate);
    }
    private void loadData() {
        String beginTime =mCalendarView.getCurYear()+"-"+mCalendarView.getCurMonth()+"-"+mCalendarView.getCurDay();
        calenderMainPresenter.getCalenders(userId,beginTime+ Const.startTime,beginTime+Const.endTime);
    }
    @Override
    public void eventListener() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showData(CalenderedBean dataBean) {
        Log.i("data",new Gson().toJson(dataBean));
        if (dataBean.getError().equals(Const.success)){
            List<CalenderedBean.DataBean> data = dataBean.getData();
            if (data!=null&&data.size()>0){
                no_caltv.setVisibility(View.GONE);
                dataList.clear();
                dataList.addAll(data);
                workAdapter = new WorkAdapter(this, mCalendar.getMonth() + "月" + mCalendar.getDay() + "日", dataList);
                work_rv.setAdapter(workAdapter);
                work_rv.notifyDataSetChanged();
            }else {
                no_caltv.setVisibility(View.VISIBLE);
                dataList.clear();
                dataList.addAll(data);
                workAdapter = new WorkAdapter(this, mCalendar.getMonth() + "月" + mCalendar.getDay() + "日", dataList);
                work_rv.setAdapter(workAdapter);
                work_rv.notifyDataSetChanged();
            }
        }else {

        }
    }


    @OnClick({R.id.back_relay,R.id.invite_relay,R.id.head_iv})
    public  void OnClick(View view){
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
            case R.id.invite_relay:
                Intent intent = new Intent(mContext,NewCalenderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("yuedata",tempData);
                intent.putExtras(bundle);
                String current=mCalendar.getYear()+"-"+mCalendar.getMonth()+"-"+mCalendar.getDay();
                Date currendate = DateFormatUtil.getTime(current, Const.Y_M_D);
                long currentime;
                if (mCalendarView.getCurDay()==mCalendar.getDay()&&mCalendarView.getCurYear()==mCalendar.getYear()&&mCalendarView.getCurMonth()==mCalendar.getMonth()){
                    currentime =System.currentTimeMillis();
                }else {
                    currentime=currendate.getTime();
                }
                intent.putExtra("date",String.valueOf(currentime));
                startActivity(intent);
                break;
            case  R.id.head_iv:
                Intent intent1=new Intent(mActivity,FriendDetailActivity.class);
                intent1.putExtra("friendid",tempData.getFriendId());
                startActivity(intent1);
                break;
        }
    }
    public void loginHead(String headUrl) {
        if (!headUrl.isEmpty()) {
            Glide.with(this).load(headUrl).transform(new GlideCircleTransform(mActivity)).into(head_iv);
        } else {
            head_iv.setImageResource(R.mipmap.niu);
        }
    }
    // WorkVisibleContract
    @Override
    public void showAddData(CommonBean dataBean) {

    }

    @Override
    public void showDeleteData(CommonBean dataBean) {

    }

    @Override
    public void showVisiblesData(WorkVisibleBean dataBean) {
        List<WorkVisibleBean.DataBean> data = dataBean.getData();

        if (data!=null&&data.size()>0){
            for (int i = 0; i <data.size() ; i++) {
                if (data.get(i).getUserId().equals(CommonAction.getUserId())){
                    isWorkVisible = true;
                }
            }
        }
        loadData();
    }
    //选择日期触发事件
    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        date_tv.setText(calendar.getYear()+"年"+calendar.getMonth()+"月");
        mCalendar=calendar;
        String beginTime=calendar.getYear()+"-"+calendar.getMonth()+"-"+calendar.getDay();
        calenderMainPresenter.getCalenders(userId,beginTime+ Const.startTime,beginTime+Const.endTime);

    }
    private com.haibin.calendarview.Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        //calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new com.haibin.calendarview.Calendar.Scheme());
        return calendar;
    }
    //改变年份 处罚事件
    @Override
    public void onYearChange(int year) {

    }
    //显示标记日期
    @Override
    public void showData(MarkDateBean dataBean) {
        if (dataBean!=null) {
            List<MarkDateBean.DataBean> list=dataBean.getData();
            List<com.haibin.calendarview.Calendar> schemes = new ArrayList<>();
            for (int i=0;i<list.size();i++){
                String[] split = list.get(i).getShowDate().split("-");
                schemes.add(getSchemeCalendar(Integer.parseInt(split[0]),Integer.parseInt(split[1]), Integer.parseInt(split[2]), 0xFFedc56d, ""));
            }
            mCalendarView.setSchemeDate(schemes);
        }
    }



    public class WorkAdapter extends GroupRecyclerAdapter<String, CalenderedBean.DataBean> {

        public WorkAdapter(Context context, String s, List<CalenderedBean.DataBean> dataList) {
            super(context);
            LinkedHashMap map = new LinkedHashMap<>();
            List<String> titles = new ArrayList<>();
            map.put(s,dataList);
            titles.add(s);
            resetGroups(map,titles);
        }

        @Override
        protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
            return new WorkViewHolder(mInflater.inflate(R.layout.rvitem_calwork, parent, false));
        }

        @Override
        protected void onBindViewHolder(RecyclerView.ViewHolder holder, Object item, int position) {
            WorkViewHolder h= (WorkViewHolder) holder;
            final CalenderedBean.DataBean dataBean= (CalenderedBean.DataBean) item;
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
            if (isVisible||isContais){
                h.mTitle.setText(dataBean.getTitle());
                isClick = true;
            }else {
                if (isWorkVisible&&type.equals("1")){
                    h.mTitle.setText(dataBean.getTitle());
                    isClick = true;
                }else {
                    h.mTitle.setText("已安排");
                    isClick = false;
                }
            }
            Date beDa = DateFormatUtil.getTime(dataBean.getBeginTime(), Const.YMD_HMS);
            String beStr = DateFormatUtil.getTime(beDa, Const.HM);
            String beStr1=DateFormatUtil.getTime(beDa, Const.MD_HM3);
            Date enDa = DateFormatUtil.getTime(dataBean.getEndTime(), Const.YMD_HMS);
            String endStr = DateFormatUtil.getTime(enDa, Const.HM);
            String endStr1=DateFormatUtil.getTime(enDa, Const.MD_HM3);
            int dayExpend = CalculateTimeUtil.getDayExpend(beDa.getTime(), enDa.getTime());
            if (dayExpend>=1){
                h.mStarttime.setText(beStr1+" — ");
                h.mEndtime.setText(endStr1);
            }else {
                h.mStarttime.setText(beStr+" — ");
                h.mEndtime.setText(endStr);
            }
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            if (calendar.getTimeInMillis()>enDa.getTime()){
                h.mTitle.setTextColor(ResourcesUtils.getColor(R.color.outdatecolor));
                h.dot_iv.setImageResource(R.drawable.shape_circle_dot);
            }else {
                h.mTitle.setTextColor(ResourcesUtils.getColor(R.color.black));
                h.dot_iv.setImageResource(R.mipmap.dot2);
            }
            final boolean finalIsClick = isClick;
            h.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (finalIsClick){
                        String scheduleId = dataBean.getScheduleId();
                        if (scheduleId!=null&&!scheduleId.isEmpty()){
                            Intent intent = new Intent(getApplicationContext(), CalenderDetailActivity.class);
                            intent.putExtra(Const.scheduleId, scheduleId);
                            intent.putExtra(Const.userId, userId);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
        }


        private class WorkViewHolder extends RecyclerView.ViewHolder {
            private TextView mStarttime,
                    mEndtime,mTitle;
            private ImageView dot_iv;

            private WorkViewHolder(View itemView) {
                super(itemView);
                mStarttime = (TextView) itemView.findViewById(R.id.starttime_tv);
                mEndtime = (TextView) itemView.findViewById(R.id.endtime_tv);
                mTitle = (TextView) itemView.findViewById(R.id.work_title);
                dot_iv = (ImageView) itemView.findViewById(R.id.dot_iv);
            }
        }
    }

}
