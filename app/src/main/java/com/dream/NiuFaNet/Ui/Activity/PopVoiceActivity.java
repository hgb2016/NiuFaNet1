package com.dream.NiuFaNet.Ui.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseActivity;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.RemindWordBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.RemindWordContract;
import com.dream.NiuFaNet.CustomView.AudioAnimView;
import com.dream.NiuFaNet.CustomView.CircularAnim;
import com.dream.NiuFaNet.CustomView.VoiceLineView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.RemindWordPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.JsonParser;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.Buffer;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hou on 2018/5/4.
 */

public class PopVoiceActivity extends Activity implements RemindWordContract.View {
    private int screenHeight;
    @Bind(R.id.voice_bot_relay)
    RelativeLayout voice_bot_relay;
    @Bind(R.id.tip_tv)
    TextView tip_tv;
    @Bind(R.id.audioview)
    AudioAnimView audioview;
    @Bind(R.id.audiores_tv)
    TextView audiores_tv;
    @Bind(R.id.voice_tv)
    TextView voice_tv;
    @Inject
    RemindWordPresenter remindWordPresenter;
    @Bind(R.id.volume_lay)
    RelativeLayout volume_lay;
    @Bind(R.id.voicetip_tv)
    TextView voicetip_tv;
    @Bind(R.id.voicline)
    VoiceLineView voiceLineView;


    private SpeechRecognizer mIat;// 语音听写
    private String audioResultStr;
    private boolean isUp, isInit, isOver,isMove;
    private String TAG = "tag";
    private Dialog permissionDialog;
    private String status="2";
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 210:
                    if (isUp && !audioResultStr.isEmpty()&&!isMove) {

                        Log.e("tag", "audioResultStr=" + audioResultStr);
                        isOver = true;
                        Intent intent = getIntent();
                        intent.putExtra("text",audioResultStr);
                        setResult(333,intent);
                        finish();
                        audioResultStr = "";

                    }
                    break;
            }
        }
    };
    private boolean isAlive=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popvoice);
        ButterKnife.bind(this);
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        remindWordPresenter.attachView(this);
        initView();
        initData();
        initmTat();
        initeventListener();
        permissionDialog = DialogUtils.showPermissionTip(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isAlive) {
                    voiceHandler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private Handler voiceHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            voiceLineView.setVolume(voice);

        }
    };
    private void initeventListener() {
        tip_tv.setVisibility(View.VISIBLE);
        voice_tv.setText("我说完了");
        //启动录音功能，6.0需要重新申请权限
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)//权限未授予
        {

            ActivityCompat.requestPermissions(PopVoiceActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 250
            );
        } else//已授予权限
        {
            mIatResults.clear();
            audiores_tv.setText("");
            starWrite();
        }
        voice_bot_relay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (status.equals("1")) {
                    tip_tv.setVisibility(View.VISIBLE);
                    voice_tv.setText("我说完了");
                    //启动录音功能，6.0需要重新申请权限
                    isUp = false;
                    isInit = false;
                    isOver = false;
                    isMove = false;
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)//权限未授予
                    {

                        ActivityCompat.requestPermissions(PopVoiceActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 250
                        );
                    } else//已授予权限
                    {
                        mIatResults.clear();
                        audiores_tv.setText("");
                        starWrite();
                    }
                    status="2";
                    CircularAnim.hide(voicetip_tv).go();
                    CircularAnim.show(voiceLineView).go();
                }else {
                    voice_tv.setText("点我说话");
                    tip_tv.setVisibility(View.GONE);
                   // voice_lay.setVisibility(View.VISIBLE);
                    audiores_tv.setText("");
                    isUp = true;
                    if (audioResultStr != null && isInit) {
                        Message message = Message.obtain();
                        message.what = 210;
                        mHandler.sendMessage(message);
                    }
                    status="1";
                    CircularAnim.hide(voiceLineView).go();
                    CircularAnim.show(voicetip_tv).go();
                }
            }
        });

    }

    //初始化数据
    private void initData() {
        this.overridePendingTransition(0, R.anim.dialog_phone_exit);
    }

    private void initView() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏显示
        screenHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();// 获取屏幕高度
        WindowManager.LayoutParams lp = getWindow().getAttributes();// //lp包含了布局的很多信息，通过lp来设置对话框的布局
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);// 将设置好属性的lp应用到对话框
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showData(RemindWordBean dataBean) {

    }
    /**
     * 初始化语音合成监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.e(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                // showTip("初始化失败,错误码：" + code);
                ToastUtils.Toast_short(PopVoiceActivity.this, "初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    private void initmTat() {
        // 语音听写1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mIat = SpeechRecognizer.createRecognizer(this, mTtsInitListener);
        // 2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        // 语音识别应用领域（：iat，search，video，poi，music）
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        // 接收语言中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 接受的语言是普通话
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        // 设置听写引擎（云端）
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
    }
    /**
     * 初始化参数开始听写
     *
     * @Description:
     */
    private void starWrite() {

//        mIat.setParameter(SpeechConstant.VAD_EOS,"10000");
        /*iatDialog.setListener(mRecognizerDialogListener);
        iatDialog.show();*/
//        audioDialog.show();
//        DialogUtils.setBespreadWindow(audioDialog, mActivity);

//        ToastUtils.Toast_short(mActivity, "请说话…");
//         3.开始听写

        mIat.startListening(mRecoListener);
//         听写监听器

    }

    private int voice=0;
    /**
     * 语音听写监听
     */
    private RecognizerListener mRecoListener = new RecognizerListener() {
        // 听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
        // 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        // 关于解析Json的代码可参见MscDemo中JsonParser类；
        // isLast等于true时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {
            if (!isLast && !isOver) {
                printResult(results);
            }
        }

        // 会话发生错误回调接口
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            int errorCode = error.getErrorCode();
            if (errorCode == 10118) {
                voice_tv.setText("点我说话");
                audiores_tv.setText("");
                tip_tv.setVisibility(View.GONE);
                CircularAnim.hide(voiceLineView).go();
                CircularAnim.show(voicetip_tv).go();
                ToastUtils.Toast_short(PopVoiceActivity.this, "你好像没有说话哦");

//                ToastUtils.Toast_short(mActivity, "你好像没有说话哦");
            } else if (errorCode == 20006) {//录音权限被拒绝

                Log.e("tag", "ErrorCode=" + errorCode);
                permissionDialog.show();
            } else {
                ToastUtils.Toast_short(PopVoiceActivity.this, "操作失败，错误码：" + errorCode);
            }
//            ToastUtils.Toast_short(mActivity, error.getPlainDescription(true));

        }// 获取错误码描述}

        // 开始录音
        public void onBeginOfSpeech() {
            Log.e(TAG, "开始说话");
//            ToastUtils.Toast_short(mActivity, "开始说话");
        }

        // 结束录音
        public void onEndOfSpeech() {
            Log.e(TAG, "说话结束");
            voice_tv.setText("点我说话");
            tip_tv.setVisibility(View.GONE);
//            ToastUtils.Toast_short(mActivity, "说话结束");
            isUp = true;
            if (audioResultStr != null && isInit) {

                Message message = Message.obtain();
                message.what = 210;
                mHandler.sendMessage(message);
            }
            status="1";
            CircularAnim.hide(voiceLineView).go();
            CircularAnim.show(voicetip_tv).go();
        }

        // 扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }

        //音量
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            if (volume > 0) {
                if (audioview != null ) {
                    audioview.refreshView(volume);
                }
                double db=0;
                if (volume>1) {
                    db = 20 * Math.log10(volume)+20;
                }
                voice= (int) db;
            }
        }

    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        audioResultStr = resultBuffer.toString();
        if (audiores_tv!=null){
            audiores_tv.setText(audioResultStr);
            Log.e("tag", "resultStr=" + resultBuffer.toString());
            Message message = Message.obtain();
            message.what = 210;
            mHandler.sendMessage(message);
            isInit = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 250) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                starWrite();

            } else {
                // Permission Denied
                ToastUtils.Toast_short("Permission Denied");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAlive=false;
        ButterKnife.unbind(this);
        if (mIat!=null){
            mIat.destroy();
        }
    }
}
