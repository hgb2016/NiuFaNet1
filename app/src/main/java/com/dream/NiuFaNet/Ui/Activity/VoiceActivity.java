package com.dream.NiuFaNet.Ui.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.MultiLayoutsBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BusBean.RefreshBean;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.FunctionBean;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.VoiceRvBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ChatContract;
import com.dream.NiuFaNet.Contract.FunctionContract;
import com.dream.NiuFaNet.Contract.VoiceContentContract;
import com.dream.NiuFaNet.CustomView.AudioAnimView;
import com.dream.NiuFaNet.CustomView.CircularAnim;
import com.dream.NiuFaNet.CustomView.HeadService;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.CustomView.VoiceLineView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ChatPresenter;
import com.dream.NiuFaNet.Presenter.FunctionPresenter;
import com.dream.NiuFaNet.Presenter.VoiceContentPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.BlurBuilder;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.JsonParser;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.dream.NiuFaNet.Utils.XuniKeyWord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kevin.wraprecyclerview.WrapRecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.inject.Inject;
import butterknife.Bind;
import butterknife.OnClick;
/**
 * Created by hou on 2018/4/25.
 */

public class VoiceActivity extends CommonActivity implements VoiceContentContract.View, ChatContract.View, FunctionContract.View {
    @Bind(R.id.voice_lay)
    LinearLayout voice_lay;
    @Bind(R.id.audiores_tv)
    TextView audiores_tv;
    @Bind(R.id.audioview_left)
    AudioAnimView audioview_left;
    @Bind(R.id.audioview_right)
    AudioAnimView audioview_right;
    @Bind(R.id.voice_tv)
    TextView voice_tv;
    @Bind(R.id.tip_tv)
    TextView tip_tv;
    @Bind(R.id.voicline)
    VoiceLineView voiceLineView;
    @Bind(R.id.bg_relay)
    RelativeLayout bg_relay;
    @Bind(R.id.caculate_include)
    View caculate_include;
    @Bind(R.id.search_include)
    View search_include;
    @Bind(R.id.calendar_include)
    View calendar_include;
    @Bind(R.id.main_lay)
    LinearLayout main_lay;
    @Bind(R.id.calculate_lay)
    LinearLayout calculate_lay;
    @Bind(R.id.calendar_lay)
    LinearLayout calendar_lay;
    @Bind(R.id.cancel_relay)
    RelativeLayout cancel_relay;
    @Bind(R.id.chat_relay)
    RelativeLayout chat_relay;
    @Bind(R.id.chat_rv)
    WrapRecyclerView chat_rv;
    @Bind(R.id.right_voice)
    ImageView right_voice;
    @Bind(R.id.right_voice_relay)
    RelativeLayout voice_relay;
    @Bind(R.id.help_relay)
    RelativeLayout help_relay;
    @Bind({R.id.calculate_lv})
    MyListView calculate_lv;
    @Bind({R.id.search_lv})
    MyListView search_lv;
    @Inject
    FunctionPresenter functionPresenter;

    @Bind(R.id.volume_lay)
    RelativeLayout volume_lay;
    @Bind(R.id.voicetip_tv)
    TextView voicetip_tv;

    private SpeechRecognizer mIat;// 语音听写
    private String TAG = "tag";
    private String audioResultStr;
    private boolean isUp, isInit, isOver, isMove;
    private Dialog permissionDialog;
    private String status = "2";
    private String szImei;
    private String tag;
    private SpeechSynthesizer mTts;// 语音合成
    private int voice=0;
 /*    0 小燕 青年女声 中英文（普通话） xiaoyan
     1 默认 小宇 青年男声 中英文（普通话） xiaoyu
     2 凯瑟琳 青年女声 英文 catherine
     3 亨利 青年男声 英文 henry
     4 玛丽 青年女声 英文 vimary
     5 小研 青年女声 中英文（普通话） vixy
     6 小琪 青年女声 中英文（普通话） vixq xiaoqi
     7 小峰 青年男声 中英文（普通话） vixf
     8 小梅 青年女声 中英文（粤语） vixm xiaomei
     9 小莉 青年女声 中英文（台湾普通话） vixl xiaolin
     10 小蓉 青年女声 汉语（四川话） vixr xiaorong
     11 小芸 青年女声 汉语（东北话） vixyun xiaoqian
     12 小坤 青年男声 汉语（河南话） vixk xiaokun
     13 小强 青年男声 汉语（湖南话） vixqa xiaoqiang
     14 小莹 青年女声 汉语（陕西话） vixying
     15 小新 童年男声 汉语（普通话） vixx xiaoxin
     16 楠楠 童年女声 汉语（普通话） vinn nannan
     17 老孙 老年男声 汉语（普通话）*/
    private String[] voiceName = {"xiaoyan", "xiaoyu", "catherine", "henry",
            "vimary", "vixy", "xiaoqi", "vixf", "xiaomei", "xiaolin",
            "xiaorong", "xiaoqian", "xiaokun", "xiaoqiang", "vixying",
            "xiaoxin", "nannan", "vils"};
    @Inject
    VoiceContentPresenter mVoiceContentPresenter;
    @Inject
    ChatPresenter chatPresenter;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
              /*  case 210:
                    if (isUp && !audioResultStr.isEmpty()&&!isMove) {

                        Log.e("tag", "audioResultStr=" + audioResultStr);
                        isOver = true;
                        String intentTag = getIntent().getStringExtra(Const.intentTag);
                        if (intentTag.equals("main")){
                            Intent intent = new Intent(mContext, NewCalenderActivity.class);
                            intent.putExtra("text",audioResultStr);
                            startActivity(intent);
                        }else {
                            Intent intent = getIntent();
                            intent.putExtra("text",audioResultStr);
                            setResult(333,intent);
                        }
                        finish();
                        audioResultStr = "";

                    }
                    break;*/
                case 210:

                    if (isUp && !audioResultStr.isEmpty() && !isMove) {
                        isOver = true;
                        Log.e("tag", "audioResultStr=" + audioResultStr);
                        // mLoadingDialog.show();
                        //chatPresenter.getChatAnswer(CommonAction.getUserId(),audioResultStr,"app",szImei);
                    }
                    break;
                case 211:
                    if (isUp && !audioResultStr.isEmpty() && !isMove) {
                        isOver = true;
                        Log.e("tag", "audioResultStr=" + audioResultStr);
                        // mLoadingDialog.show();
                        //  chatPresenter.getChatAnswer(CommonAction.getUserId(),audioResultStr,"app",szImei);
                    }
            }
        }
    };

    private Handler voiceHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                voiceLineView.setVolume(voice);

        }
    };
    private ChatRvAdapter chatRvAdapter;
    private HeadService headService;
    private List<ChatBean.BodyBean> listData = new ArrayList<>();
    private List<FunctionBean.BodyBean.CalculateBean> dataList = new ArrayList<>();
    private List<FunctionBean.BodyBean.FindBean> dataList1 = new ArrayList<>();
    private CalculateAdapter calculateAdapter;
    private FindAdapter findAdapter;
    private boolean force = false;

    private boolean isAlive = true;
    @Override
    public int getLayoutId() {
        return R.layout.activity_voice;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        bg_relay.setBackground(new BitmapDrawable(BlurBuilder.blur(bg_relay)));
        chatPresenter.attachView(this);
        mVoiceContentPresenter.attachView(this);
        functionPresenter.attachView(this);
        initmTat();
        permissionDialog = DialogUtils.showPermissionTip(mActivity);
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        szImei = TelephonyMgr.getDeviceId();
        RvUtils.setOptionnoLine(chat_rv, this);
        RvUtils.setRvParam_RelayParent(chat_rv);
        chatRvAdapter = new ChatRvAdapter(this, listData, new int[]{R.layout.rvitem_question, R.layout.view_rightitem});
        headService = new HeadService(this);
        calculateAdapter = new CalculateAdapter(mContext, dataList, R.layout.rvitem_onlytext);
        findAdapter = new FindAdapter(mContext, dataList1, R.layout.rvitem_onlytext);
        calculate_lv.setAdapter(calculateAdapter);
        search_lv.setAdapter(findAdapter);
        chat_rv.addHeaderView(headService);
        chat_rv.setAdapter(chatRvAdapter);
        main_lay.setVisibility(View.VISIBLE);
        final String voiceType = CommonAction.getVoiceType();
        if (voiceType.equals(Const.vTyep_On)) {
            right_voice.setImageResource(R.mipmap.horn_open);
        } else if (voiceType.equals(Const.vTyep_Off)) {
            right_voice.setImageResource(R.mipmap.horn_close);
        }
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

    @Override
    public void initDatas() {
        //获取缓存的聊天数据
        String dataCache = (String) SpUtils.getParam(Const.dataCache, "");
        if (!dataCache.isEmpty()) {
            List<ChatBean.BodyBean> dataList = new Gson().fromJson(dataCache, new TypeToken<List<ChatBean.BodyBean>>() {
            }.getType());
            if (dataList != null) {
                listData.addAll(dataList);
                chatRvAdapter.notifyDataSetChanged();
                chat_rv.smoothScrollToPosition(listData.size());
            }
        }
        mVoiceContentPresenter.voicePrompt();
        //获取推荐帮助语音
        getRecommendData();

    }

    private void getRecommendData() {
        String userId = CommonAction.getUserId();

        if (userId.isEmpty()) {
            functionPresenter.getFunctionData("type");
        } else {
            functionPresenter.getFunctionData("type");
        }
    }

    @Override
    public void finish() {
        super.finish();
        BlurBuilder.recycle();
        overridePendingTransition(android.view.animation.Animation.INFINITE, android.view.animation.Animation.INFINITE);
    }

    @Override
    public void eventListener() {
        voice_relay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                String voiceType = CommonAction.getVoiceType();
                if (voiceType.equals(Const.vTyep_Off)) {
                    right_voice.setImageResource(R.mipmap.horn_open);
                    SpUtils.setParam(Const.voiceType, Const.vTyep_On);
                    mTts.startSpeaking(ResourcesUtils.getString(R.string.voicefirst), mSynListener);
                } else if (voiceType.equals(Const.vTyep_On)) {
                    right_voice.setImageResource(R.mipmap.horn_close);
                    SpUtils.setParam(Const.voiceType, Const.vTyep_Off);
                    mTts.stopSpeaking();
                }
            }
        });
        status = "2";
        tip_tv.setVisibility(View.VISIBLE);
        voice_tv.setText("我说完了");
        //启动录音功能，6.0需要重新申请权限
        isUp = false;
        isInit = false;
        isOver = false;
        isMove = false;
        force = false;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)//权限未授予
        {

            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO}, 250
                );
        } else//已授予权限
        {
            mIatResults.clear();
            audiores_tv.setText("");
            starWrite();
        }
    }

    @OnClick({R.id.back_relay, R.id.voice_lay, R.id.calendar_lay, R.id.calculate_lay, R.id.search_lay, R.id.cancel_relay, R.id.help_relay})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_relay:
                mTts.stopSpeaking();
                finish();
                break;
            case R.id.voice_lay:
                if (status.equals("1")) {
                    force = false;
                    tip_tv.setVisibility(View.VISIBLE);
                    voice_tv.setText("我说完了");
                    //启动录音功能，6.0需要重新申请权限
                    isUp = false;
                    isInit = false;
                    isOver = false;
                    isMove = false;
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)//权限未授予
                    {
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO}, 250
                        );
                    } else//已授予权限
                    {
                        mIatResults.clear();
                        audiores_tv.setText("");
                        starWrite();
                    }
                    status = "2";
                    CircularAnim.hide(voicetip_tv).go();
                    CircularAnim.show(voiceLineView).go();
                   // voiceLineView.setVisibility(View.VISIBLE);
                } else if (status.equals("2")) {

                    force = true;
                    voice_tv.setText("点我说话");
                    tip_tv.setVisibility(View.GONE);
                    // voice_lay.setVisibility(View.VISIBLE);
                    audiores_tv.setText("");
                    isUp = true;
                    mIat.stopListening();
                    status = "1";
                    voice=0;
                    CircularAnim.hide(voiceLineView).go();
                    CircularAnim.show(voicetip_tv).go();
                   // voicetip_tv.setVisibility(View.VISIBLE);
                   // voiceLineView.setVisibility(View.GONE);
                }
                break;
            case R.id.calculate_lay:
                tag = "1";
                main_lay.setVisibility(View.GONE);
                caculate_include.setVisibility(View.VISIBLE);
                cancel_relay.setVisibility(View.VISIBLE);
                break;
            case R.id.calendar_lay:
                tag = "4";
                main_lay.setVisibility(View.GONE);
                calendar_include.setVisibility(View.VISIBLE);
                cancel_relay.setVisibility(View.VISIBLE);
                break;
            case R.id.search_lay:
                tag = "2";
                main_lay.setVisibility(View.GONE);
                search_include.setVisibility(View.VISIBLE);
                cancel_relay.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel_relay:
                cancel_relay.setVisibility(View.GONE);
                main_lay.setVisibility(View.VISIBLE);
                if (tag.equals("1")) {
                    caculate_include.setVisibility(View.GONE);
                } else if (tag.equals("4")) {
                    calendar_include.setVisibility(View.GONE);
                } else if (tag.equals("2")) {
                    search_include.setVisibility(View.GONE);
                }
                break;

            case R.id.help_relay:
                mTts.stopSpeaking();
                chat_relay.setVisibility(View.GONE);
                main_lay.setVisibility(View.VISIBLE);
                help_relay.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mTts.stopSpeaking();
//            ReturnUtil.returnMain();
        }
        return super.onKeyDown(keyCode, event);

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
                ToastUtils.Toast_short(mActivity, "初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    private void initmTat() {
        // 语音合成 1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        // 语音听写1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mIat = SpeechRecognizer.createRecognizer(this, mTtsInitListener);
        // 2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        // 语音识别应用领域（：iat，search，video，poi，music）
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        // 接收语言中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        //
        // 接受的语言是普通话
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置听写引擎（云端）
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);


        /**
         * 语音合成设置
         */
        mTts.setParameter(SpeechConstant.VOICE_NAME, voiceName[6]);// 设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
        //合成方言 普通话
        mTts.setParameter(SpeechConstant.ACCENT,"mandarin"); // 设置语言
        // 合成语言中文
        mTts.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mTts.setParameter(SpeechConstant.TEXT_ENCODING,"GB2312");

    }

    /**
     * 初始化参数开始听写
     *
     * @Description:
     */
    private void starWrite() {

//        mIat.setParameter(SpeechConstant.VAD_EOS,"10000");
       /* iatDialog.setListener(mRecognizerDialogListener);
        iatDialog.show();*/
//        audioDialog.show();
//        DialogUtils.setBespreadWindow(audioDialog, mActivity);

//        ToastUtils.Toast_short(mActivity, "请说话…");
//         3.开始听写
        voice_lay.setVisibility(View.VISIBLE);
        chat_rv.smoothScrollToPosition(listData.size());
        mIat.startListening(mRecoListener);
//         听写监听器


    }

    /**
     * 语音听写监听
     */

    private RecognizerListener mRecoListener = new RecognizerListener() {
        // 听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
        // 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        // 关于解析Json的代码可参见MscDemo中JsonParser类；
        // isLast等于true时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);

        }

        // 会话发生错误回调接口
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            int errorCode = error.getErrorCode();
            if (errorCode == 10118) {
                voice=0;
                ToastUtils.Toast_short(mActivity, "你好像没有说话哦");
                CircularAnim.hide(voiceLineView).go();
                CircularAnim.show(voicetip_tv).go();
              //  voicetip_tv.setVisibility(View.VISIBLE);
            } else if (errorCode == 20006) {//录音权限被拒绝

                Log.e("tag", "ErrorCode=" + errorCode);
                permissionDialog.show();
            } else {
                ToastUtils.Toast_short(mActivity, "操作失败，错误码：" + errorCode);
            }
        }

        // 开始录音
        public void onBeginOfSpeech() {
            Log.e(TAG, "点我说话");
        }

        // 结束录音
        public void onEndOfSpeech() {
            Log.e(TAG, "说话结束");
            voice_tv.setText("点我说话");
            tip_tv.setVisibility(View.GONE);
            voice_lay.setVisibility(View.VISIBLE);
            audiores_tv.setText("");
            isUp = true;
            if (audioResultStr != null && !audioResultStr.isEmpty() && isInit) {
                sendContent(audioResultStr);
                chat_relay.setVisibility(View.VISIBLE);
                main_lay.setVisibility(View.GONE);
                cancel_relay.setVisibility(View.GONE);
                caculate_include.setVisibility(View.GONE);
                calendar_include.setVisibility(View.GONE);
                search_include.setVisibility(View.GONE);
                help_relay.setVisibility(View.VISIBLE);
                force = false;

            }
            status = "1";
            voice=0;
            CircularAnim.hide(voiceLineView).go();
            CircularAnim.show(voicetip_tv).go();
           // voicetip_tv.setVisibility(View.VISIBLE);
        }

        // 扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {


        }

        //音量
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            if (volume > 0) {
                if (audioview_left != null && audioview_right != null) {
                    audioview_left.refreshView(volume);
                    audioview_right.refreshView(volume);
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
        audiores_tv.setText(audioResultStr);
        if (force) {
            if (audioResultStr != null && !audioResultStr.isEmpty()) {
                sendContent(audioResultStr);
            }
            chat_relay.setVisibility(View.VISIBLE);
            main_lay.setVisibility(View.GONE);
            cancel_relay.setVisibility(View.GONE);
            caculate_include.setVisibility(View.GONE);
            calendar_include.setVisibility(View.GONE);
            search_include.setVisibility(View.GONE);
            help_relay.setVisibility(View.VISIBLE);
            audiores_tv.setText("");
            force = false;
        }
        isInit = true;

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
        if (mIat != null) {
            mIat.destroy();
        }
        if (mTts != null) {
            mTts.destroy();
        }

    }

    @Override

    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
        mLoadingDialog.dismiss();
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showVoiceContentData(VoiceRvBean dataBean) {

    }

    @Override
    public void showData(ChatBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            ChatBean.BodyBean body = dataBean.getBody();
            if (body != null) {
                String url = body.getUrl();
                String type = body.getType();
                if (type != null) {
                    Intent intent = null;
                    switch (type) {
                        case "2"://日程
                            List<InputGetBean> scheduleData = body.getScheduleData();
                            if (scheduleData != null) {
                               /* ChatBean.BodyBean bodyBean = new ChatBean.BodyBean();
                                bodyBean.setFdType("1");
                                bodyBean.setAnswer("好的，已帮你创建日程。");
                                listData.add(bodyBean);
                                chatRvAdapter.notifyDataSetChanged();
                                chat_rv.smoothScrollToPosition(listData.size());*/
                                Log.i("myTag",new Gson().toJson(scheduleData)+"------");
                                //starSpeech("好的，已帮你创建日程。");
                                intent = new Intent(mContext, NewCalenderActivity.class);
                                intent.putExtra("scheduleData", (Serializable) scheduleData);
                                startActivity(intent);
                                mActivity.overridePendingTransition(R.anim.activity_open, R.anim.exitanim);
                                finish();
                                audioResultStr = "";
                            }
                            break;
                        case "3"://项目
                            String projectName = body.getAnswer();
                            intent = new Intent(mContext, NewProgramActivity.class);
                            if (projectName != null) {
                                intent.putExtra("projectName", projectName);
                            }
                            startActivity(intent);
                            mActivity.overridePendingTransition(R.anim.activity_open, R.anim.exitanim);
                            finish();
                            break;
                        case "1"://聊天
                            Log.i("myTag", new Gson().toJson(body.getRelated() + "回答"));
                            List<ChatBean.BodyBean.RelatedBean> relatedList = body.getRelated();
                            listData.add(body);
                            chatRvAdapter.notifyDataSetChanged();
                            if (relatedList != null && relatedList.size() > 0) {
                   /* recom_gv.setVisibility(View.VISIBLE);
//                    mid_lay.setVisibility(View.GONE);
                    relateData.clear();
                    relateData.addAll(relatedList);
                    relateGvAdapter.notifyDataSetChanged();*/
                                if (listData.size() >= 1) {
                                    chat_rv.smoothScrollToPosition(listData.size() - 1);
                                    int martopHeight = XuniKeyWord.initStateView(mActivity).getHeight() + DensityUtil.dip2px(65);
                                    int dy = DensityUtil.getScreenHeight(mActivity) - martopHeight * 4;
                                    //获取当前屏幕内容的高度
                                    int screenHeight = mActivity.getWindow().getDecorView().getHeight();
                                    //获取View可见区域的bottom
                                    Rect rect = new Rect();
                                    mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                                    int height = screenHeight - rect.bottom;
                                    boolean statu = height != 0;
                                    if (!statu) {
                                        chat_rv.smoothScrollBy(0, dy);
                                    } else {
                                        // TODO: 2017/11/2 0002
                                        if (height == 108) {//针对华为手机，这个108的是虚拟键高度，有可能不同的华为机虚拟键高度不是108
                                            chat_rv.smoothScrollBy(0, dy);
                                        }
                                    }
                                }

                            } else {
//                    recom_gv.setVisibility(View.GONE);
                                chat_rv.smoothScrollToPosition(listData.size());
                            }

                            String answer = body.getAnswer();
                            String voiceType = CommonAction.getVoiceType();
                            Log.i("voice", answer + "----" + voiceType);
                            if (answer != null && voiceType.equals(Const.vTyep_On)) {
                                Spanned spanned = Html.fromHtml(answer);
                                Log.i("myTag", spanned.toString() + "======");
                                starSpeech(spanned.toString());
                            }

                            if (url != null && !url.isEmpty()) {
                                //mTts.stopSpeaking();
                                Intent intent1 = new Intent(mContext, WebActivity.class);
                                intent1.putExtra(Const.webUrl, url);
                                startActivity(intent1);
                            }
                            break;
                    }

                }
            }
        }
    }

    /**
     * 初始化语音合成相关数据
     *
     * @Description:
     */
    public void starSpeech(String content) {

        // 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        Log.e(TAG, "mTts=" + mTts);

        // 设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        // 保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        // 如果不需要保存合成音频，注释该行代码
      //  mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        // mTts.startSpeaking("科大讯飞，让世界聆听我们的声音", mSynListener);
        // 3.开始合成
        mTts.startSpeaking(content, mSynListener);
        // 合成监听器
        //

    }

    //获取推荐语音帮助列表
    @Override
    public void showData(FunctionBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<FunctionBean.BodyBean.CalculateBean> calculateBeanList = dataBean.getBody().getCalculate();
            List<FunctionBean.BodyBean.FindBean> findBeans = dataBean.getBody().getFind();
            List<FunctionBean.BodyBean.LaborBean> laborBeans = dataBean.getBody().getLabor();
            dataList.clear();
            dataList1.clear();
            dataList.addAll(calculateBeanList);
            dataList1.addAll(findBeans);
            for (FunctionBean.BodyBean.LaborBean laborBean : laborBeans) {
                FunctionBean.BodyBean.FindBean findBean = new FunctionBean.BodyBean.FindBean();
                findBean.setUrl(laborBean.getUrl());
                findBean.setQuestion(laborBean.getQuestion());
                dataList1.add(findBean);
            }
            calculateAdapter.notifyDataSetChanged();
            findAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.Toast_short("服务器异常");
        }
    }


    private class ChatRvAdapter extends MultiLayoutsBaseAdapter<ChatBean.BodyBean> {

        public ChatRvAdapter(Context context, List<ChatBean.BodyBean> data, int[] layoutIds) {
            super(context, data, layoutIds);
        }

        @Override
        public int getItemType(int position) {
            int viewType = 0;
            List<ChatBean.BodyBean.RelatedBean> related = listData.get(position).getRelated();
            if (listData.get(position).getFdType() != null) {
                viewType = 1;
            }
            return viewType;
        }

        @Override
        public void onBinds(RVBaseHolder holder, ChatBean.BodyBean baseBean, int position, final int itemType) {
            String answer = baseBean.getAnswer();

            final String url = baseBean.getUrl();
            TextView leftTv = holder.getView(R.id.chat_content);
            if (itemType == 0) {
//                chat_contenttv.setMovementMethod(ScrollingMovementMethod.getInstance());//滚动
//                leftTv.setMovementMethod(LinkMovementMethod.getInstance());//超链接点击有效

                if (answer != null) {
                    if (answer.contains("\n")) {
                        answer = answer.replace("\n", "<br>");
                    }
                    if (answer.contains("\r")) {
                        answer = answer.replace("\r", "");
                    }

                    leftTv.setText(Html.fromHtml(answer));

                    CharSequence text = leftTv.getText();
                    if (text instanceof Spanned) {
                        int end = text.length();
                        SpannedString sp = (SpannedString) leftTv.getText();
                        URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
                        SpannableStringBuilder style = new SpannableStringBuilder(text);
                        style.clearSpans(); // should clear old spans
                        for (URLSpan temp : urls) {
                            URLSpan myURLSpan = new URLSpan(temp.getURL());
                            style.setSpan(myURLSpan, sp.getSpanStart(temp), sp.getSpanEnd(temp), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            style.setSpan(new ForegroundColorSpan(ResourcesUtils.getColor(R.color.acolor)), sp.getSpanStart(temp), sp.getSpanEnd(temp), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//设置前景色为蓝色
                        }
                        leftTv.setText(style);
                    }
                }

                if (leftTv != null) {
                    leftTv.setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View view) {
                            if (url != null && !url.isEmpty() && itemType == 0) {
                                //直接跳转到相关链接
                                mTts.stopSpeaking();
                                Intent webIntent = new Intent(mContext, WebActivity.class);
                                webIntent.putExtra(Const.webUrl, url);
                                startActivity(webIntent);
                            }
                        }
                    });
                }
                List<ChatBean.BodyBean.RelatedBean> related = baseBean.getRelated();
                LinearLayout retate_lay = holder.getView(R.id.rotate_lay);
                if (related != null && related.size() > 0) {
                    retate_lay.setVisibility(View.VISIBLE);
                    ListView rvleft_lv = holder.getView(R.id.rvleft_lv);
                    rvleft_lv.setAdapter(new ChatRvAdapter.QuestionAdapter(mContext, related, R.layout.rvitem_questiontext));
                } else {
                    retate_lay.setVisibility(View.GONE);
                }

            } else if (itemType == 1) {

                final TextView right_contv = holder.getView(R.id.right_contv);
                right_contv.setText(answer);
                right_contv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                     /*   showCopyDialog(right_contv);
                        copyContent = right_contv.getText().toString();*/
                        return true;
                    }
                });
                String type = (String) SpUtils.getParam(Const.thirdType, Const.bdUser);
                String headUrl = (String) SpUtils.getParam(Const.userHeadUrl, "");
                String tdHeadUrl = (String) SpUtils.getParam(Const.userHeadUrl, "");
                ImageView view = holder.getView(R.id.right_headiv);
                view.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        /*Intent intent = new Intent(mContext, MineActivity.class);
                        startActivityForResult(intent, 101);*/
                        RefreshBean bean = new RefreshBean();
                        bean.setEventStr("2");
                        EventBus.getDefault().post(bean);
                        finish();
                    }
                });
                if (type.equals(Const.bdUser)) {
                    if (!headUrl.isEmpty()) {
                        holder.setImageByUrl(R.id.right_headiv, headUrl, true);
                    } else {
                        holder.setImageResource(R.id.right_headiv, R.drawable.niu);
                    }
                } else {
                    if (!tdHeadUrl.isEmpty()) {
                        holder.setImageByUrl(R.id.right_headiv, tdHeadUrl, true);
                    } else {
                        holder.setImageResource(R.id.right_headiv, R.drawable.niu);
                    }
                }
            }
        }

        private class QuestionAdapter extends CommonAdapter<ChatBean.BodyBean.RelatedBean> {

            public QuestionAdapter(Context context, List<ChatBean.BodyBean.RelatedBean> mDatas, int itemLayoutId) {
                super(context, mDatas, itemLayoutId);
            }

            @Override
            public void convert(BaseViewHolder helper, ChatBean.BodyBean.RelatedBean item, int position) {
                TextView only_tv = helper.getView(R.id.only_tv);
                only_tv.setTextSize(15);
                final String question = item.getQuestion();
                if (question != null) {
                    only_tv.setText((position + 1) + "." + question);
                    only_tv.setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View view) {
                            ChatBean.BodyBean bean = new ChatBean.BodyBean();
                            bean.setAnswer(question);
                            bean.setFdType("1");
                            listData.add(bean);
                            chatRvAdapter.notifyDataSetChanged();
                            chat_rv.smoothScrollToPosition(listData.size());
                            chatPresenter.getChatAnswer(CommonAction.getUserId(), question, Const.questionType, szImei);
                        }
                    });
                }

            }
        }
    }

    private void sendContent(String question) {
        if (question != null) {
            ChatBean.BodyBean bean = new ChatBean.BodyBean();
            bean.setAnswer(question);
            bean.setFdType("1");
            sendMSG(question, bean);
        }
    }

    private void sendMSG(String question, ChatBean.BodyBean bean) {
        listData.add(bean);
        chatRvAdapter.notifyDataSetChanged();
        chat_rv.smoothScrollToPosition(listData.size());
        chatPresenter.getChatAnswer(CommonAction.getUserId(), question, Const.questionType, szImei);
    }

    @Override
    protected void onStop() {
        super.onStop();
        String listcach = "";
        if (listData.size() <= Const.chatSize) {
            listcach = new Gson().toJson(listData);
        } else {
            listcach = new Gson().toJson(listData.subList(listData.size() - Const.chatSize, listData.size()));
        }
        SpUtils.savaUserInfo(Const.dataCache, listcach);
    }

    /**
     * 语音合成监听
     */
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        // 会话结束回调接口，没有错误时，error为null
        @SuppressLint("LongLogTag")
        public void onCompleted(SpeechError error) {
            if (error != null) {
                Log.e("mySynthesiezer complete code:", error.getErrorCode()
                        + "");
            } else {
                Log.e("mySynthesiezer complete code:", "0");
            }
        }

        // 缓冲进度回调
        // percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }

        // 开始播放
        public void onSpeakBegin() {

        }

        // 暂停播放
        public void onSpeakPaused() {

        }

        // 播放进度回调
        // percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }

        // 恢复播放回调接口
        public void onSpeakResumed() {

        }

        // 会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

        }
    };

    public class CalculateAdapter extends CommonAdapter<FunctionBean.BodyBean.CalculateBean> {

        public CalculateAdapter(Context context, List<FunctionBean.BodyBean.CalculateBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, FunctionBean.BodyBean.CalculateBean item, int position) {
            helper.setText(R.id.only_tv, item.getQuestion());
            final String url = item.getUrl();
            if (url != null && !url.isEmpty()) {
                helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        //直接跳转到相关链接
                        Intent webIntent = new Intent(getApplicationContext(), WebActivity.class);
                        webIntent.putExtra(Const.webUrl, url);
                        startActivity(webIntent);
                    }
                });
            }
        }
    }

    public class FindAdapter extends CommonAdapter<FunctionBean.BodyBean.FindBean> {

        public FindAdapter(Context context, List<FunctionBean.BodyBean.FindBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, FunctionBean.BodyBean.FindBean item, int position) {
            helper.setText(R.id.only_tv, item.getQuestion());
            final String url = item.getUrl();
            if (url != null && !url.isEmpty()) {
                helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        //直接跳转到相关链接
                        Intent webIntent = new Intent(getApplicationContext(), WebActivity.class);
                        webIntent.putExtra(Const.webUrl, url);
                        startActivity(webIntent);
                    }
                });
            }
        }
    }

}
