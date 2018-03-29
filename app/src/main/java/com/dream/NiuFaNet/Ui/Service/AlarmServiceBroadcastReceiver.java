package com.dream.NiuFaNet.Ui.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by acer-pc on 2016/4/21.
 */
public class AlarmServiceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("remind","AlarmServiceBroadcastReceiver——onReceive");
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startService(serviceIntent);

    }
}
