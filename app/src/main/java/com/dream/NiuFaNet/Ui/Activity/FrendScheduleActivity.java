package com.dream.NiuFaNet.Ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Adapter.WeekAdapter;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.WeekBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalenderMainContract;
import com.dream.NiuFaNet.CustomView.CalenderRvUtils.CalendarRecyclerView;
import com.dream.NiuFaNet.CustomView.CalenderUtils.CalendarView;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalenderMainPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.GlideCircleTransform;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/14 0014.
 */
public class FrendScheduleActivity extends CommonActivity implements CalenderMainContract.View  {

    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.curent_datetv)
    TextView curent_datetv;
    @Bind(R.id.head_icon)
    RelativeLayout head_icon;
    @Bind(R.id.today_iv)
    ImageView today_iv;
    @Bind(R.id.week_rv)
    RecyclerView week_rv;
    @Bind(R.id.top_lay)
    LinearLayout top_lay;
    @Bind(R.id.work_rv)
    CalendarRecyclerView work_rv;
    @Bind(R.id.mine_iv)
    ImageView mine_iv;

    private WeekAdapter weekAdapter;
    private String[] weekStrs = new String[]{"一", "二", "三", "四", "五", "六", "日"};
    private List<WeekBean> weekList = new ArrayList<>();

    private CalendarView calendarView;
    @Inject
    CalenderMainPresenter calenderMainPresenter;

    private String userId;
    private MyFrendBean.DataBean tempData;
    @Override
    public int getLayoutId() {
        return R.layout.activity_frends_schedule;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        calenderMainPresenter.attachView(this);
        RvUtils.setOption(week_rv, this);
        weekAdapter = new WeekAdapter(this, weekList, R.layout.rvitem_onlytext_week);
        week_rv.setAdapter(weekAdapter);
       /* if (CommonAction.getIsLogin()){
            toLogin();
        }*/
    }

    @Override
    public void initDatas() {
        weekList.clear();
        for (int i = 0; i < weekStrs.length; i++) {
            WeekBean weekBean = new WeekBean();
            weekBean.setWeek(weekStrs[i]);
            weekList.add(weekBean);
        }
        weekAdapter.notifyDataSetChanged();
        MyFrendBean.DataBean data = (MyFrendBean.DataBean) getIntent().getExtras().getSerializable("data");
        if (data!=null){
            tempData = data;
            userId = data.getFriendId();
            title_tv.setText(data.getFriendName()+"的日程");
            String headUrl = data.getHeadUrl();
            loginSet(headUrl);

        }
        if (userId!=null){
            calendarView = new CalendarView(this,userId);
            top_lay.addView(calendarView);
            //设置标注日期
            List<Date> markDates = new ArrayList<Date>();
            markDates.add(new Date());
            calendarView.setMarkDates(markDates);
            work_rv.setData(userId);
        }

        Calendar calendar = Calendar.getInstance();
        String nowStr = DateFormatUtil.getTime(calendar.getTime(), "yyyy年MM月");
        curent_datetv.setText(nowStr);


    }

    private Calendar currentCal = Calendar.getInstance();

    @Override
    public void eventListener() {
        calendarView.setOnCalendarViewListener(new CalendarView.OnCalendarViewListener() {
            @Override
            public void onCalendarItemClick(CalendarView view, Date date) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                work_rv.setLocation(c,userId);
                currentCal = c;
            }
        });
        calendarView.setTitle(new CalendarView.TitleListener() {
            @Override
            public void setTitle(String titleStr, Calendar calendar) {
                curent_datetv.setText(titleStr);
            }
        });

        calendarView.setOnMoveListener(new CalendarView.MoveListener() {
            @Override
            public void moveListener(Calendar calendar) {
                work_rv.setLocation(calendar,userId);
                currentCal = calendar;
            }
        });
        work_rv.setScrollListener(new CalendarRecyclerView.ScrollListener() {
            @Override
            public void onScrollView(Calendar c) {
                calendarView.moveCalendarView(true, c);
                calendarView.setScroll(c);
                currentCal = c;
            }
        });
    }

    @Override
    public void showData(CalenderedBean dataBean) {

    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @OnClick({R.id.back_relay,R.id.today_iv,R.id.invite_lay,R.id.head_icon})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
            case R.id.today_iv:
                calendarView.locationToday(true);
                work_rv.setLocation(Calendar.getInstance(),userId);
                break;
            case R.id.head_icon:
//                IntentUtils.toActivity(MineActivity.class, mActivity);

                break;
            case R.id.invite_lay://约TA
                Intent intent = new Intent(mContext,NewCalenderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("yuedata",tempData);
                intent.putExtras(bundle);
                intent.putExtra("date",String.valueOf(currentCal.getTimeInMillis()));
                startActivity(intent);
                break;
        }

    }
    private void toLogin() {
        String thirdType = (String) SpUtils.getParam(Const.thirdType, Const.bdUser);
        String itUserIcon = (String) SpUtils.getParam(Const.userHeadUrl, "");
        loginSet(itUserIcon);
       /* if (thirdType.equals("bduser")) {

        } else {
            String tdUserIcon = (String) SpUtils.getParam(Const.tdHeadUrl, "");
            loginSet(tdUserIcon);
        }*/

    }

    public void loginSet(String headUrl) {
        Log.e("tag", "headurl=" + headUrl);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.dip2px(35), DensityUtil.dip2px(35));
        mine_iv.setLayoutParams(params);
        if (!headUrl.isEmpty()) {
            Glide.with(this).load(headUrl).transform(new GlideCircleTransform(mActivity)).into(mine_iv);
        } else {
            mine_iv.setImageResource(R.mipmap.niu);
        }
    }
}
