package com.dream.NiuFaNet.Ui.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import android.util.Log;

import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalendarDetail2Contract;
import com.dream.NiuFaNet.Contract.CalenderMainContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalendarDetail2Presenter;
import com.dream.NiuFaNet.Presenter.CalenderMainPresenter;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by acer-pc on 2016/4/21.
 */
public class AlarmService extends Service implements CalenderMainContract.View,CalendarDetail2Contract.View {

    @Inject
    CalenderMainPresenter calenderMainPresenter;
    @Inject
    CalendarDetail2Presenter detail2Presenter;
    @Override
    public void onCreate() {
        super.onCreate();
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        calenderMainPresenter.attachView(this);
        detail2Presenter.attachView(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("remind","onStartCommand");
        Calendar calendar = Calendar.getInstance();
        String startDate = DateFormatUtil.getTime(calendar.getTimeInMillis(), Const.Y_M_D);
        calenderMainPresenter.getCalenders(CommonAction.getUserId(), startDate + Const.startTime, startDate + Const.endTime);
        return START_NOT_STICKY;
    }

    public void schedule(Context context, long remindTime, CalendarDetailBean.DataBean dataBean) {
        Intent myIntent = new Intent(context, AlarmAlertBroadcastReceiver.class);
       /* Bundle bundle = new Bundle();
        bundle.putSerializable("alarm",dataBean);
        myIntent.putExtras(bundle);*/
//        myIntent.putExtra("alarm",dataBean);
        String s = new Gson().toJson(dataBean);
        Log.e("remind","dataBean="+ s);
//        SpUtils.savaUserInfo(Const.dataBean,"");
//        SpUtils.savaUserInfo(Const.dataBean,new Gson().toJson(dataBean));
        MyApplication.getInstance().setTime_data(s);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // TODO: 2017/12/9 0009
        //19以上手机版本不支持setrepeat
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, remindTime,0,pendingIntent);
        Log.e("remind","setRepeating");
    }

    @Override
    public void showData(CalenderedBean dataBean) {

        if (dataBean.getError().equals(Const.success)){
            List<CalenderedBean.DataBean> data = dataBean.getData();
            if (data!=null){
                for (int i = 0; i <data.size() ; i++) {
                    String beginTime = data.get(i).getBeginTime();
                    if (beginTime!=null){
                        Date time = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
                        Calendar calendar = Calendar.getInstance();
                        if (time.getTime()>=calendar.getTimeInMillis()){
                            detail2Presenter.getCalendarDetail(CommonAction.getUserId(),data.get(i).getScheduleId());
                        }
                    }
                }
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
    public void showData(CalendarDetailBean dataBean) {
        if (dataBean.getError().equals(Const.success)){

            CalendarDetailBean.DataBean data = dataBean.getData();
            if (data!=null){
                List<CalendarDetailBean.DataBean.RemindBean> remind = data.getRemind();
                for (int i = 0; i <remind.size() ; i++) {
                    String remindTime = remind.get(i).getRemindTime();
                    Log.e("tag","remindTime="+remindTime);
                    Date remindDate = DateFormatUtil.getTime(remindTime, Const.YMD_HMS);
                    Calendar calendar = Calendar.getInstance();
                    if (remindDate.getTime()>=calendar.getTimeInMillis()){
                        schedule(getApplicationContext(),remindDate.getTime(),data);
                        break;
                    }
                }
            }
        }
    }
}
