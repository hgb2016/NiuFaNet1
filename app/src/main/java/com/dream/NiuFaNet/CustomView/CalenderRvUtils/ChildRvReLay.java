package com.dream.NiuFaNet.CustomView.CalenderRvUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.WorkVisibleBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalenderMainContract;
import com.dream.NiuFaNet.Contract.WorkVisibleContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalenderMainPresenter;
import com.dream.NiuFaNet.Presenter.WorkVisiblePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.CalenderDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.NewCalenderActivity;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.StringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/11/21 0021.
 */
public class  ChildRvReLay extends RelativeLayout implements CalenderMainContract.View,WorkVisibleContract.View{

    private Context mContext;
    private Calendar mCalendar;
    private View noCalendarView;
    private LinearLayout dataLay;
    private LinearLayout midadd_lay;
    private List<CalenderedBean.DataBean> dataList = new ArrayList<>();
    private boolean isVisible,isWorkVisible;
    @Inject
    CalenderMainPresenter mainPresenter;
    @Inject
    WorkVisiblePresenter mWorkVisiblePresenter;
    private WorkAdapter workAdapter;
    private String userId;
    public ChildRvReLay(Context context, Calendar calendar,String userId) {
        super(context);
        mContext =context;
        mCalendar = calendar;
        this.userId = userId;
        init(userId);
        initView(userId);
        initEvent();
    }

    private void initEvent() {
        midadd_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonAction.getIsLogin()){
                    Intent intent = new Intent(mContext,NewCalenderActivity.class);
                    intent.putExtra("date",String.valueOf(mCalendar.getTimeInMillis()));
                    mContext.startActivity(intent);
                }else {
                    DialogUtils.getLoginTip(mContext).show();
                }
            }
        });
    }

    public ChildRvReLay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(String userId){

        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        mainPresenter.attachView(this);
        mWorkVisiblePresenter.attachView(this);
        setGravity(Gravity.CENTER_HORIZONTAL);
        if (userId.equals(CommonAction.getUserId())){
            isVisible = true;
            loadData();
        }else {
            mWorkVisiblePresenter.getWorkVisible(userId);
        }

    }

    private void initView(String userId) {

        noCalendarView = LayoutInflater.from(mContext).inflate(R.layout.view_addschedule_empty, null);
        midadd_lay = (LinearLayout) noCalendarView.findViewById(R.id.midadd_lay);
        dataLay = new LinearLayout(mContext);
        dataLay.setOrientation(OrientationHelper.VERTICAL);

        RecyclerView calRv = new RecyclerView(mContext);
        RvUtils.setOptionnoLine(calRv);
        calRv.setOverScrollMode(OVER_SCROLL_NEVER);
        workAdapter = new WorkAdapter(mContext,dataList,R.layout.rvitem_calwork);
        calRv.setAdapter(workAdapter);

        View work_top = LayoutInflater.from(mContext).inflate(R.layout.view_worktoplay, null);
        TextView day_tv = (TextView) work_top.findViewById(R.id.day_tv);
        TextView week_tv = (TextView) work_top.findViewById(R.id.week_tv);
        ImageView share_iv = (ImageView) work_top.findViewById(R.id.share_iv);
        day_tv.setText(DateFormatUtil.getTime(mCalendar.getTime(),"MM.dd"));
        week_tv.setText("周"+Week.getWeekDay(mCalendar.get(Calendar.DAY_OF_WEEK)));
        if (!userId.equals(CommonAction.getUserId())){
            share_iv.setVisibility(GONE);
        }
        share_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewCalenderActivity.class);
                intent.putExtra("date", String.valueOf(mCalendar.getTime().getTime()));
                mContext.startActivity(intent);
            }
        });
        dataLay.addView(work_top);
        dataLay.addView(calRv);
        addView(noCalendarView);
        addView(dataLay);
        noCalendarView.setVisibility(GONE);
        dataLay.setVisibility(GONE);

    }


    @Override
    public void showData(CalenderedBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
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
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

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

    private void loadData() {
        Date date = mCalendar.getTime();
        String beginTime = DateFormatUtil.getTime(date, "yyyy-MM-dd");
        mainPresenter.getCalenders(userId,beginTime+ Const.startTime,beginTime+Const.endTime);
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
            if (isVisible||isContais){
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
            }
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
                            intent.putExtra(Const.userId, userId);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
        }
    }
}
