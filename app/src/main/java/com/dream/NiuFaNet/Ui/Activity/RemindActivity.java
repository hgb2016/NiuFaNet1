package com.dream.NiuFaNet.Ui.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonActivity1;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.AppUtils;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.DateUtil;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.PopWindowUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/12/9 0009.
 */
public class RemindActivity extends CommonActivity1 {

    @Bind(R.id.caltitle_tv)
    TextView caltitle_tv;
    @Bind(R.id.start_timetv)
    TextView start_timetv;
    @Bind(R.id.know_tv)
    TextView know_tv;
    @Bind(R.id.status_tv)
    TextView status_tv;
    @Bind(R.id.toapp_lay)
    LinearLayout toapp_lay;

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private boolean isVibrator = false;
    private boolean isFront = false;
    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    public int getLayoutId() {
        return R.layout.activity_alarmwake;
    }

    @Override
    public void initView() {
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void initDatas() {
        playMusicAndVibrate(mContext);
        CalendarDetailBean.DataBean alarm = (CalendarDetailBean.DataBean) getIntent().getExtras().getSerializable("alarm");
        if (alarm!=null){
            caltitle_tv.setText(alarm.getTitle());
            String beginTime = alarm.getBeginTime();
            Date beginDate = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
            Calendar calendar = Calendar.getInstance();
            String timeDimen = DateUtil.getTimeDimen(beginDate.getTime(), calendar.getTimeInMillis());
            if (timeDimen.equals("0分钟")){
                start_timetv.setText("您的日程马上开始咯");
            }else {
                start_timetv.setText("您的日程在"+timeDimen+"后开始咯");
            }
            if (!AppUtils.isApplicationInBackground(this)){
                status_tv.setText("查看");
            }
        }
    }

    @Override
    public void eventListener() {
        know_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    mediaPlayer = null;
                    if(isVibrator){
                        vibrator.cancel();
                        isVibrator = false;
                    }
                    finish();
                }
            }
        });
        toapp_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    if(isVibrator){
                        vibrator.cancel();
                        isVibrator = false;
                    }
                }
              /*  Log.e("tag","isHou="+AppUtils.isApplicationInBackground(MyApplication.getInstance()));
                if (AppUtils.isApplicationInBackground(MyApplication.getInstance())){
                }*/
                IntentUtils.retoMain(mContext);
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {

            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();
            wl.release();
        }
    }
    /**
     * 播放音乐
     * @param context
     */
    private void playMusicAndVibrate(Context context){

        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
                mediaPlayer.reset();
        }try {
            mediaPlayer.setVolume(100f, 100f);
            mediaPlayer.setDataSource(context, ringtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();
            mediaPlayer.start();
            //new Thread(TimerRunnable).start();
            //----------定时器记录播放监听--------//
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    boolean isChanging = isForeground(RemindActivity.this);
                    if(!isChanging) {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            if(isVibrator){
                                vibrator.cancel();
                                isVibrator = false;
                            }
                        }
                        finish();

                        return;
                    }
                }
            };
            mTimer.schedule(mTimerTask, 0, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFront=false;
    }

    /**
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);

    }
    /**
     * 判断某个界面是否在前台
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }


}
