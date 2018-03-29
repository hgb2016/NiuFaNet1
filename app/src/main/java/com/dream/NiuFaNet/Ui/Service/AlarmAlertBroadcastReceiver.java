package com.dream.NiuFaNet.Ui.Service;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;

import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Ui.Activity.MainActivity;
import com.dream.NiuFaNet.Ui.Activity.RemindActivity;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by acer-pc on 2016/4/21.
 */
public class AlarmAlertBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmServiceIntent = new Intent(
                context,
                AlarmServiceBroadcastReceiver.class);
        context.sendBroadcast(alarmServiceIntent, null);
        Log.e("remind","AlarmAlertBroadcastReceiver——onReceive");

//        Bundle bundle = intent.getExtras();
//        CalendarDetailBean.DataBean alarm = (CalendarDetailBean.DataBean) bundle.getSerializable("alarm");
//        Log.e("remind","alarm="+alarm);
//        String dataBeanStr = (String) SpUtils.getParam(Const.dataBean, "");
        String dataBeanStr = MyApplication.getInstance().getTime_data();

        Log.e("remind","dataBeanStr="+dataBeanStr);
        if (!dataBeanStr.isEmpty()){
            CalendarDetailBean.DataBean dataBean = new Gson().fromJson(dataBeanStr, CalendarDetailBean.DataBean.class);
            Log.e("remind","dataBean="+new Gson().toJson(dataBean));
            if (dataBean!=null){
                showAlarmDialog(context,dataBean);
            }else {
                Log.e("remind","dataBean="+dataBean);
            }
        }
    }

    private void showAlarmDialog(Context context,CalendarDetailBean.DataBean dataBean) {
        Intent alarmIntent = new Intent(context, RemindActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("alarm",dataBean);
        alarmIntent.putExtras(bundle);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}
