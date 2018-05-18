package com.dream.NiuFaNet.Ui.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dream.NiuFaNet.Ui.Activity.MainActivity;

public class ContentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent it=new Intent(context,MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
        Toast.makeText(context,"我自启动成功了哈",Toast.LENGTH_LONG).show();
        //开机启动

    }
}
