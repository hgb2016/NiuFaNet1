package com.dream.NiuFaNet.Utils;

/**
 * Created by sunnetdesign3 on 2016/12/28.
 */
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.dream.NiuFaNet.R;


/**
 * 通知栏工具类
 * @author liuweile
 */
public class NotifyUtil {
    public static final int NOTIFY_ID_APP=1001;//版本更新通知id

    public static NotificationManager getNotificationManager(Context context){
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }

    /**
     * 发送通知栏
     * @param context 上下文
     * @param title 标题
     * @param content 内容
     * @param notifyId 通知id
     * @param intent 意图
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void sendNotify(Context context, String title, String content, int notifyId, Intent intent){
        NotificationManager manager=getNotificationManager(context);
        PendingIntent contentIntent = PendingIntent.getActivity(context, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.logo);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        manager.notify(notifyId,builder.build());
    }

    /**
     * 在通知栏显示进度条的通知
     * @param context 上下文
     * @param manager  通知管理
     * @param progress 实时进度
     * @param title  标题
     * @param notifyId 通知的id标识
     * @param intent  意图
     */
    public static void sendProgressNotify(Context context, NotificationManager manager, int progress, String title, int notifyId, Intent intent){
        int maxProgress=100;
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.notify_progress);
        //自定义的通知栏布局
        remoteViews.setProgressBar(R.id.downloadbar,maxProgress,progress,false);//把当前已经下载的数据长度设置为进度条的当前刻度
        remoteViews.setTextViewText(R.id.result, progress+"%");

        //也就是不确定什么时候会发生，可能是未来的某个时间发生，可以理解为一种延迟的Intent，但是它一般会由别的程序触发
        //用于包装意图，如：点击通知栏时 发出意图
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifyId,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.logo);
        builder.setTicker(title);

        builder.setContentTitle(title);
        builder.setContentText(title);
        builder.setContentIntent(pendingIntent);
        builder.setContent(remoteViews);

        if(progress == maxProgress){//下载成功后就提示用户是否安装下载成功的软件
            builder.setAutoCancel(true);
            builder.setDefaults(Notification.DEFAULT_SOUND);
            manager.notify(notifyId, builder.build());// 发出通知
        }else{
            manager.notify(notifyId, builder.build());// 发出通知
        }
    }

    /**
     * 发送app更新
     */
    public static void sendAppVersionNotify(Context context, NotificationManager manager, int progress){
        String title= "下载进度";
        sendProgressNotify(context,manager,progress,title,NOTIFY_ID_APP,new Intent());
    }
}