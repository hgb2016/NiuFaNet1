package com.dream.NiuFaNet.Ui.Activity;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;


public class DateCalculateActivity extends CommonActivity {
    @Bind(R.id.day_tv)
    TextView day_tv;
    @Bind(R.id.date_tv)
    TextView date_tv;
    @Bind(R.id.starttime_relay)
    RelativeLayout starttime_relay;
    @Bind(R.id.endtime_relay)
    RelativeLayout endtime_relay;
    @Bind(R.id.starttime_tv)
    TextView starttime_tv;
    @Bind(R.id.endtime_tv)
    TextView endtime_tv;
    @Bind(R.id.daynum_lay)
    LinearLayout daynum_lay;
    @Bind(R.id.daynum_edt)
    EditText daynum_edt;
    @Bind(R.id.daystatus_tv)
    TextView daystatus_tv;
    @Bind(R.id.calculateresult_lay)
    LinearLayout calculateresult_lay;
    @Bind(R.id.normal_tv)
    TextView normal_tv;
    @Bind(R.id.work_tv)
    TextView work_tv;
    @Bind(R.id.change_tv)
    TextView change_tv;
    @Bind(R.id.datetime_tv)
    TextView datetime_tv;
    @Bind(R.id.date_relay)
    RelativeLayout date_relay;

    private TimePickerView dateDialog;
    private int tempTag;
    private long  startDate;
    private long endDate;
    private String startStr;
    private String endStr;
    private int status=1;
    private  boolean isbefore;
    @Override
    public int getLayoutId() {
        return R.layout.activity_datecalculate;
    }


    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);

    }

    public void changeStatus(int status){
        if (status==1){
            date_tv.setBackgroundColor(ResourcesUtils.getColor(R.color.bg_F5));
            day_tv.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
            day_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
            date_tv.setTextColor(ResourcesUtils.getColor(R.color.textcolor_ac));
            endtime_tv.setVisibility(View.VISIBLE);
            daynum_lay.setVisibility(View.GONE);
            change_tv.setText("结束日期");
        }else {
            endtime_tv.setVisibility(View.GONE);
            daynum_lay.setVisibility(View.VISIBLE);
            change_tv.setText("间隔天数");
            date_tv.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
            day_tv.setBackgroundColor(ResourcesUtils.getColor(R.color.bg_F5));
            day_tv.setTextColor(ResourcesUtils.getColor(R.color.textcolor_ac));
            date_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
        }

    }
    @OnClick({R.id.back_relay,R.id.day_tv,R.id.date_tv,R.id.starttime_relay,R.id.endtime_relay,R.id.reset_tv,R.id.calculate_tv,R.id.daystatus_tv})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.back_relay:
                finish();

                break;
            case R.id.reset_tv:

                break;
            case R.id.day_tv:
                status=1;
                changeStatus(status);
                break;
            case R.id.date_tv:
                status=2;
                changeStatus(status);
                break;
            case R.id.starttime_relay:
                tempTag = 0;
                String startStr1 = starttime_tv.getText().toString();
                Date stR = DateFormatUtil.getTime(startStr1, Const.Y_M_D);
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(stR);
                chooseTime(calendar);
                break;
            case R.id.endtime_relay:
                tempTag = 1;
                String endStr1 = endtime_tv.getText().toString();
                Date endD = DateFormatUtil.getTime(endStr1, Const.Y_M_D);
                Calendar calendar1=Calendar.getInstance();
                calendar1.setTime(endD);
                chooseTime(calendar1);
                dateDialog.show();
                break;
            case R.id.calculate_tv:
                if (status==1) {
                    calculateresult_lay.setVisibility(View.VISIBLE);
                    date_relay.setVisibility(View.GONE);
                    int timeExpend = CalculateTimeUtil.getDayExpend(startDate, endDate);
                    int days = CalculateTimeUtil.isWeekend(startStr, endStr);
                    normal_tv.setText(timeExpend + "天");
                    work_tv.setText(days + "天");
                }else {
                    calculateresult_lay.setVisibility(View.GONE);
                    date_relay.setVisibility(View.VISIBLE);
                    Calendar calendar2=Calendar.getInstance();
                    calendar2.setTime(DateFormatUtil.getTime(startStr,Const.Y_M_D));
                    String days = daynum_edt.getText().toString().trim();
                    int count=0;
                    if (!TextUtils.isEmpty(days)){
                        count=Integer.parseInt(days);
                    }
                    calendar2.add(Calendar.DAY_OF_YEAR,count);
                    Date time = calendar2.getTime();
                    datetime_tv.setText(DateFormatUtil.getTime(time,Const.Y_M_D));
                }
                break;
            case R.id.daystatus_tv:

                break;
        }
    }



    @Override
    public void initDatas() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH,0);
        calendar.set(Calendar.DAY_OF_WEEK,1);//设置为1号,当前日期既为本月第一天
        startDate=calendar.getTimeInMillis();
        String startTime = DateFormatUtil.getTime(calendar.getTimeInMillis(), Const.Y_M_D);
        starttime_tv.setText(startTime);
        startStr=starttime_tv.getText().toString().trim();
        calendar.set(Calendar.DAY_OF_WEEK,7);
        endDate=calendar.getTimeInMillis();
        String endTime = DateFormatUtil.getTime(calendar.getTimeInMillis(), Const.Y_M_D);
        endtime_tv.setText(endTime);
        endStr=endtime_tv.getText().toString().trim();
    }

    @Override
    public void eventListener() {

    }

    private void chooseTime(Calendar calendar) {
        dateDialog = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                long time = date.getTime();
                String dateStr = DateFormatUtil.getTime(time, Const.Y_M_D);
                Log.e("tag", "dateStr=" + dateStr);
                if (dateStr != null) {
                    if (tempTag == 0) {
                        startDate=time;
                        startStr=dateStr;
                        starttime_tv.setText(dateStr);
                    } else {
                        endDate=time;
                        endStr=dateStr;
                        endtime_tv.setText(dateStr);
                    }
                }
            }
        }).setDate(calendar).setType(new boolean[]{true, true, true, false, false, false})
                .setContentTextSize(16)//滚轮文字大小
                .setTitleSize(13)//标题文字大小
                .setCancelText("取消")//取消按钮文字
                .setLabel(" 年", "月", "日", "时", "分", "秒")
                .isCyclic(true)//是否循环滚动
                .setLineSpacingMultiplier(3.0f)
                .build();
        dateDialog.show();
    }

}
