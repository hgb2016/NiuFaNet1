package com.dream.NiuFaNet.Ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
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
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.BusBean.LoginBus;
import com.dream.NiuFaNet.Bean.BusBean.RefreshCalBean;
import com.dream.NiuFaNet.Bean.BusBean.RefreshProBean;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.MarkDateBean;
import com.dream.NiuFaNet.Bean.WeekBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalenderMainContract;
import com.dream.NiuFaNet.Contract.MarkDateContract;
import com.dream.NiuFaNet.CustomView.CalenderRvUtils.CalendarRecyclerView;
import com.dream.NiuFaNet.CustomView.CalenderUtils.CalendarView;
import com.dream.NiuFaNet.CustomView.Emptyview_RvSchedule;
import com.dream.NiuFaNet.CustomView.MessageRightPopView;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalenderMainPresenter;
import com.dream.NiuFaNet.Presenter.MarkDatePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.CalenderDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.CalenderLogActivity;
import com.dream.NiuFaNet.Ui.Activity.EditCalenderActivity;
import com.dream.NiuFaNet.Ui.Activity.FriendCalenderActivity;
import com.dream.NiuFaNet.Ui.Activity.MyFrendsActivity;
import com.dream.NiuFaNet.Ui.Activity.NewCalenderActivity;
import com.dream.NiuFaNet.Ui.Activity.NewTodoActivity;
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.DateUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.PopWindowUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.StringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;
import com.haibin.calendarview.CalendarLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16 0016.
 */
public class CalenderFragment extends BaseFragmentV4 implements CalenderMainContract.View, com.haibin.calendarview.CalendarView.OnDateSelectedListener,
        com.haibin.calendarview.CalendarView.OnYearChangeListener,MarkDateContract.View{

    @Bind(R.id.curent_datetv)
    TextView curent_datetv;
    @Bind(R.id.work_lv)
    MyListView work_lv;
    @Bind(R.id.root_lay)
    LinearLayout root_lay;
    @Bind(R.id.calender_tv)
    TextView calender_tv;
    @Bind(R.id.tudo_tv)
    TextView tudo_tv;
    @Inject
    CalenderMainPresenter calenderMainPresenter;
    @Inject
    MarkDatePresenter markDatePresenter;
   // TextView mTextCurrentDay;
    @Bind(R.id.calendarView)
    com.haibin.calendarview.CalendarView mCalendarView;
    @Bind(R.id.add_iv)
    ImageView add_iv;
    RelativeLayout mRelativeTool;
    private int mYear;
    @Bind(R.id.calendarLayout)
    CalendarLayout mCalendarLayout;
    private PopupWindow popupWindow;
    @Bind(R.id.curentdate_relay)
    RelativeLayout curentdate_relay;
    @Bind(R.id.calendar_relay)
    RelativeLayout calendar_relay;
    @Bind(R.id.todo_lv)
    MyListView todo_lv;
    @Bind(R.id.complete_relay)
    RelativeLayout complete_relay;
    @Bind(R.id.date_tv)
    TextView date_tv;
    @Bind(R.id.no_caltv)
    ImageView  no_caltv;
    @Bind(R.id.tip_tv)
    TextView tip_tv;
    private boolean flag=false;
    private CustomPopWindow mCustomPopWindow;
    private List<CalenderedBean.DataBean> dataList = new ArrayList<>();
    private WorkAdapter workAdapter;
    private com.haibin.calendarview.Calendar calendar1;
    private List<MarkDateBean.DataBean> markDateList=new ArrayList<>();
    private List<MarkDateBean.DataBean> tempList=new ArrayList<>();
    private ArrayList<com.haibin.calendarview.Calendar> schemes = new ArrayList<>();
    @Override
    public void initView() {
       DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        calenderMainPresenter.attachView(this);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        EventBus.getDefault().register(this);
        mCalendarView.setOnMonthChangeListener(new com.haibin.calendarview.CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                //标记日期
                String begindate=year+"-"+(month-1)+"-"+30;
                String enddate=year+"-"+month+"-"+30;
                markDatePresenter.toGetDateMark(CommonAction.getUserId(),begindate,enddate);
            }
        });

        markDatePresenter.attachView(this);
        mCalendarView.setSchemeDate(schemes);
        ArrayList lists=new ArrayList();
        lists.add("1");
        lists.add("1");
        lists.add("1");
        workAdapter = new WorkAdapter(getContext(), dataList, R.layout.rvitem_calwork);
        work_lv.setAdapter(workAdapter);
       // work_lv.setEmptyView(R.layout.view_addschedule_empty);
        todo_lv.setAdapter(new TodoAdapter(getContext(),lists,R.layout.view_tudo));
        isCalenderOrTudo(flag);

    }


    @Override
    public void initResume() {

    }

    @Override
    public void initEvents() {

    }

    @Override
    public void initDta() {
        mYear = mCalendarView.getCurYear();
        curent_datetv.setText(mYear+"年"+mCalendarView.getCurMonth()+"月");
        String beginTime =mCalendarView.getCurYear()+"-"+mCalendarView.getCurMonth()+"-"+mCalendarView.getCurDay();

        //标记日期
        if (CommonAction.getUserId()!=null&&!CommonAction.getUserId().isEmpty()) {
            calenderMainPresenter.getCalenders(CommonAction.getUserId(),beginTime+ Const.startTime,beginTime+Const.endTime);
            String begindate = mCalendarView.getCurYear() + "-" + (mCalendarView.getCurMonth() - 1) + "-" + 30;
            String enddate = mCalendarView.getCurYear() + "-" + (mCalendarView.getCurMonth() + 1) + "-" + 30;
            markDatePresenter.toGetDateMark(CommonAction.getUserId(), begindate, enddate);
        }
        getDate(tip_tv);
    }
    //温馨提示语!
    private void getDate(TextView tv){
        Date d = new Date();
        if (d.getHours()<6) {
            tv.setText("夜深了，早点回家休息。");
        }else if(d.getHours()<10){
            tv.setText("早安,记得吃早餐噢。");
        }else if(d.getHours()<12){
            tv.setText("优秀是一种习惯，加油。");
        }else if(d.getHours()<14){
            tv.setText("午间小憩，补充能量。");
        }else if(d.getHours()<19){
            tv.setText("下午好，抖擞精神继续努力。");
        } else if(d.getHours()<22){
            tv.setText("晚上好，加班辛苦了，我陪你。");
        }else if(d.getHours()<24){
            tv.setText("夜深了，早点回家休息。");
        }

    }
    private com.haibin.calendarview.Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        return calendar;
    }
    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_nfcalender, null);

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
                workAdapter.notifyDataSetChanged();
            }else {
                no_caltv.setVisibility(View.VISIBLE);
                dataList.clear();
                workAdapter.notifyDataSetChanged();
            }
        }else {

        }
    }

    @Override
    public void showError() {
       // Log.i("myTag","----------------------------------");
       ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {

    }

    @Override
    public void onDateSelected(com.haibin.calendarview.Calendar calendar, boolean isClick) {
        calendar1=calendar;
        getCalendarData(calendar1);
        RandomBg();
    }
    //随机背景图
    private void RandomBg() {
        int i = (int) (Math.random() * 3);
        switch (i){
            case 0:
                no_caltv.setImageResource(R.drawable.calender_1);
                break;
            case 1:
                no_caltv.setImageResource(R.drawable.calender_2);
                break;
            case 2:
                no_caltv.setImageResource(R.drawable.calender_3);
                break;
            case 3:
                no_caltv.setImageResource(R.drawable.calender_4);
                break;
        }
    }
    public void  getCalendarData(com.haibin.calendarview.Calendar calendar1){
        curent_datetv.setText(this.calendar1.getYear()+"年"+ this.calendar1.getMonth()+"月");
        date_tv.setText(this.calendar1.getMonth()+"月"+ this.calendar1.getDay()+"日");
        String starDate = this.calendar1.getYear()+"-"+ this.calendar1.getMonth()+"-"+ this.calendar1.getDay();
        Date date=new Date();
        String selectDate = this.calendar1.getYear()+"-"+ this.calendar1.getMonth()+"-"+ this.calendar1.getDay()+"  "+date.getHours()+":"+date.getMinutes();
        tempCalendar.setTime(DateFormatUtil.getTime(selectDate,Const.YMD_HM));
        calenderMainPresenter.getCalenders(CommonAction.getUserId(), starDate + Const.startTime, starDate + Const.endTime);
    }

    @Override
    public void showData(MarkDateBean dataBean) {
       // Log.i("tag","是否缓存"+mCalendarView.isDrawingCacheEnabled()+new Gson().toJson(dataBean));
        if (dataBean!=null) {
            markDateList.clear();
            markDateList.addAll(dataBean.getData());
            schemes.clear();
            for (int i=0;i<markDateList.size();i++){
                String[] split = markDateList.get(i).getShowDate().split("-");
                schemes.add(getSchemeCalendar(Integer.parseInt(split[0]),Integer.parseInt(split[1]), Integer.parseInt(split[2]), 0xFFedc56d, String.valueOf(i)));
            }
            mCalendarView.update();
        }
    }

    @Override
    public void onYearChange(int year) {

    }

    //日程安排
    private class WorkAdapter extends CommonAdapter<CalenderedBean.DataBean> {

        public WorkAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }
        @Override
        public void convert(BaseViewHolder holder, final CalenderedBean.DataBean dataBean, int position) {
            TextView work_title = holder.getView(R.id.work_title);
            TextView starttime_tv = holder.getView(R.id.starttime_tv);
            TextView  endtime_tv= holder.getView(R.id.endtime_tv);
            ImageView dot_iv = holder.getView(R.id.dot_iv);
            Date beDa = DateFormatUtil.getTime(dataBean.getBeginTime(), Const.YMD_HMS);
            String beStr = DateFormatUtil.getTime(beDa, Const.HM);
            String beStr1 = DateFormatUtil.getTime(beDa, Const.MD_HM3);
            Date enDa = DateFormatUtil.getTime(dataBean.getEndTime(), Const.YMD_HMS);
            int dayExpend = CalculateTimeUtil.getDayExpend(beDa.getTime(), enDa.getTime());
            String endStr = DateFormatUtil.getTime(enDa, Const.HM);
            String endStr1 = DateFormatUtil.getTime(enDa, Const.MD_HM3);
            if (dayExpend>=1){
                starttime_tv.setText(beStr1+" — ");
                endtime_tv.setText(endStr1);
            }else {
                starttime_tv.setText(beStr+" — ");
                endtime_tv.setText(endStr);
            }
            work_title.setText(dataBean.getTitle());
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            if (calendar.getTimeInMillis()>enDa.getTime()){
                work_title.setTextColor(ResourcesUtils.getColor(R.color.outdatecolor));
                dot_iv.setImageResource(R.drawable.shape_circle_dot);
            }else {
                work_title.setTextColor(ResourcesUtils.getColor(R.color.black));
                dot_iv.setImageResource(R.mipmap.dot1);
            }
            holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    String scheduleId = dataBean.getScheduleId();
                    if (scheduleId!=null&&!scheduleId.isEmpty()){
                        Intent intent = new Intent(getContext(), CalenderDetailActivity.class);
                        intent.putExtra(Const.scheduleId, scheduleId);
                        intent.putExtra(Const.userId, CommonAction.getUserId());
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }



    //待办安排
    private class TodoAdapter extends CommonAdapter {

        public TodoAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }
    private Calendar tempCalendar = Calendar.getInstance();
    //点击事件
    @OnClick({R.id.calender_tv,R.id.tudo_tv,R.id.up_tv,R.id.add_iv,R.id.today_iv,R.id.no_caltv})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.calender_tv:
              /*  flag=!flag;
                isCalenderOrTudo(flag);*/
                break;
            case  R.id.tudo_tv:
                flag=!flag;
                isCalenderOrTudo(flag);
                break;
            case R.id.up_tv:
                showPopMenu(v);
                break;
            case R.id.add_iv:
                if (!flag) {
                    if (CommonAction.getIsLogin()) {
                        String current=calendar1.getYear()+"-"+calendar1.getMonth()+"-"+calendar1.getDay();
                        Date currendate = DateFormatUtil.getTime(current, Const.Y_M_D);
                        Intent intent = new Intent(getContext(), NewCalenderActivity.class);
                        long currentime;
                        if (mCalendarView.getCurDay()==calendar1.getDay()&&mCalendarView.getCurYear()==calendar1.getYear()&&mCalendarView.getCurMonth()==calendar1.getMonth()){
                            currentime =System.currentTimeMillis();
                        }else {
                            currentime=tempCalendar.getTimeInMillis();
                        }
                        intent.putExtra("date", String.valueOf(currentime));
                        startActivityForResult(intent,155);
                        getActivity().overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                    } else {
                        DialogUtils.getLoginTip(getActivity()).show();
                    }
                }else {
                    startActivity(new Intent(getContext(), NewTodoActivity.class));
                    getActivity().overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                }
                break;
            case R.id.today_iv:
                mCalendarView.scrollToCurrent();
                break;
            case R.id.no_caltv:
                if (!flag) {
                    if (CommonAction.getIsLogin()) {
                        String current=calendar1.getYear()+"-"+calendar1.getMonth()+"-"+calendar1.getDay();
                        Date currendate = DateFormatUtil.getTime(current, Const.Y_M_D);
                        Intent intent = new Intent(getContext(), NewCalenderActivity.class);
                        long currentime;
                        if (mCalendarView.getCurDay()==calendar1.getDay()&&mCalendarView.getCurYear()==calendar1.getYear()&&mCalendarView.getCurMonth()==calendar1.getMonth()){
                            currentime =System.currentTimeMillis();
                        }else {
                            currentime=tempCalendar.getTimeInMillis();
                        }
                        intent.putExtra("date", String.valueOf(currentime));
                        startActivityForResult(intent,155);
                        getActivity().overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                    } else {
                        DialogUtils.getLoginTip(getActivity()).show();
                    }
                }else {
                    startActivity(new Intent(getContext(), NewTodoActivity.class));
                    getActivity().overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                }

                break;
        }

    }
    private void showPopMenu(View v) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_tudomenu,null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .enableBackgroundDark(true)
                .create()
                .showAsDropDown(v,0,0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 155:
                getCalendarData(calendar1);
                break;
        }
    }
    /**
     * 选择 未完成 和已完成事件
     * @param contentView
     */
    private void handleLogic(View contentView){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCustomPopWindow!=null){
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()){
                    case R.id.menu_todo:

                        break;
                    case R.id.menu_havedo:

                        break;
                }
                //Toast.makeText(HomeActivity.this,showContent,Toast.LENGTH_SHORT).show();
            }
        };
        contentView.findViewById(R.id.menu_todo).setOnClickListener(listener);
        contentView.findViewById(R.id.menu_havedo).setOnClickListener(listener);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void isCalenderOrTudo(boolean b) {
        if (!b){
            calender_tv.setTextSize(30);
            tudo_tv.setTextSize(15);
            complete_relay.setVisibility(View.VISIBLE);
            curentdate_relay.setVisibility(View.VISIBLE);
            mCalendarLayout.setVisibility(View.VISIBLE);
            complete_relay.setVisibility(View.GONE);
            todo_lv.setVisibility(View.GONE);
        }else {
            calender_tv.setTextSize(15);
            tudo_tv.setTextSize(30);
            curentdate_relay.setVisibility(View.GONE);
            complete_relay.setVisibility(View.GONE);
            mCalendarLayout.setVisibility(View.GONE);
            complete_relay.setVisibility(View.VISIBLE);
            todo_lv.setVisibility(View.VISIBLE);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(RefreshCalBean proBean) {
        if (proBean.getEventStr().equals(Const.refresh)) {
            //标记日期
            if (CommonAction.getIsLogin()) {
                getCalendarData(calendar1);
                com.haibin.calendarview.Calendar selectedCalendar = mCalendarView.getSelectedCalendar();
                String begindate = selectedCalendar.getYear() + "-" + (selectedCalendar.getMonth() - 1) + "-" + 30;
                String enddate = selectedCalendar.getYear() + "-" + (selectedCalendar.getMonth() + 1)+ "-" + 30;
                markDatePresenter.toGetDateMark(CommonAction.getUserId(), begindate, enddate);
            }
        }
    }
}
