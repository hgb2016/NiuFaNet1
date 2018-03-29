package com.dream.NiuFaNet.Ui.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
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
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
