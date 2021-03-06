package com.dream.NiuFaNet.Ui.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Adapter.ChatMainRvAdapter;
import com.dream.NiuFaNet.Adapter.VoiceContentAdapter;
import com.dream.NiuFaNet.Base.BaseActivityRelay;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.MultiLayoutsBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BusBean.RefreshBean;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.RecomendBean;
import com.dream.NiuFaNet.Bean.VoiceRvBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ChatContract;
import com.dream.NiuFaNet.Contract.VoiceContentContract;
import com.dream.NiuFaNet.CustomView.AudioAnimView;
import com.dream.NiuFaNet.CustomView.CustomeGridView;
import com.dream.NiuFaNet.CustomView.HeadService;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Listener.NoShortClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ChatPresenter;
import com.dream.NiuFaNet.Presenter.VoiceContentPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
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
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
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
 * Created by Administrator on 2017/8/3 0003.
 */
public class ChatActivity extends BaseActivityRelay implements ChatContract.View,VoiceContentContract.View{

    @Bind(R.id.chat_rv)
    WrapRecyclerView chat_rv;
    @Bind(R.id.voice_iv)
    ImageView voice_iv;
    @Bind(R.id.right_voice)
    ImageView right_voice;
    @Bind(R.id.more_relay)
    RelativeLayout more_relay;
    @Bind(R.id.send_tv)
    TextView send_tv;
    @Bind(R.id.input_tv)
    TextView input_tv;
    @Bind(R.id.audiores_tv)
    TextView audiores_tv;
    @Bind(R.id.input_edt)
    EditText input_edt;
    /*  @Bind(R.id.mid_gv)
      CustomeGridView mid_lay;*/
    @Bind(R.id.recom_gv)
    CustomeGridView recom_gv;
    @Bind(R.id.audio_lay)
    RelativeLayout audio_lay;
    @Bind(R.id.audioview_left)
    AudioAnimView audioview_left;
    @Bind(R.id.audioview_right)
    AudioAnimView audioview_right;
    @Bind(R.id.input_lay)
    LinearLayout input_lay;
    @Bind(R.id.right_voice_relay)
    RelativeLayout voice_relay;
    @Bind(R.id.back_relay)
    RelativeLayout mBackRelay;
    @Bind(R.id.userinfo_iv)
    ImageView mUserinfoIv;
    @Bind(R.id.voiceContent_rv)
    RecyclerView mVoiceContentRv;
    @Bind(R.id.close_iv)
    RelativeLayout mCloseIv;
    private boolean isHide = true;
    private boolean isHuawei = true;

    private boolean isOver;
    private ClipboardManager cm;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 110:
                    if (isUp && !audioResultStr.isEmpty()&&!isMove) {
                        Log.e("tag", "audioResultStr=" + audioResultStr);
                        ChatBean.BodyBean bean = new ChatBean.BodyBean();
                        bean.setFdType("1");
                        String temp = "";
                        if (audioResultStr != null) {
                            bean.setAnswer(audioResultStr);
                            temp = audioResultStr;
                        } else {
                            bean.setAnswer("");
                        }
                        listData.add(bean);
                        chatRvAdapter.notifyDataSetChanged();
                        chat_rv.smoothScrollToPosition(listData.size());
                        audioResultStr = "";
                        isOver = true;
                        chatPresenter.getChatAnswer(CommonAction.getUserId(), temp, Const.questionType, szImei);
                    }
                    break;
            }
        }
    };

    private String audioResultStr;
    private boolean isUp, isInit,isMove;
    private static String TAG = ChatActivity.class.getSimpleName();
    // 0 小燕 青年女声 中英文（普通话） xiaoyan
    // 1 默认 小宇 青年男声 中英文（普通话） xiaoyu
    // 2 凯瑟琳 青年女声 英文 catherine
    // 3 亨利 青年男声 英文 henry
    // 4 玛丽 青年女声 英文 vimary
    // 5 小研 青年女声 中英文（普通话） vixy
    // 6 小琪 青年女声 中英文（普通话） vixq xiaoqi
    // 7 小峰 青年男声 中英文（普通话） vixf
    // 8 小梅 青年女声 中英文（粤语） vixm xiaomei
    // 9 小莉 青年女声 中英文（台湾普通话） vixl xiaolin
    // 10 小蓉 青年女声 汉语（四川话） vixr xiaorong
    // 11 小芸 青年女声 汉语（东北话） vixyun xiaoqian
    // 12 小坤 青年男声 汉语（河南话） vixk xiaokun
    // 13 小强 青年男声 汉语（湖南话） vixqa xiaoqiang
    // 14 小莹 青年女声 汉语（陕西话） vixying
    // 15 小新 童年男声 汉语（普通话） vixx xiaoxin
    // 16 楠楠 童年女声 汉语（普通话） vinn nannan
    // 17 老孙 老年男声 汉语（普通话）
    private String[] voiceName = {"xiaoyan", "xiaoyu", "catherine", "henry",
            "vimary", "vixy", "xiaoqi", "vixf", "xiaomei", "xiaolin",
            "xiaorong", "xiaoqian", "xiaokun", "xiaoqiang", "vixying",
            "xiaoxin", "nannan", "vils"};
    private SpeechSynthesizer mTts;// 语音合成
    private SpeechRecognizer mIat;// 语音听写
    private RecognizerDialog iatDialog;//听写动画
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();


    private ChatRvAdapter chatRvAdapter;
    private RelateGvAdapter relateGvAdapter;
    private RecommendGvAdapter recommendGvAdapter;
    private ChatMainRvAdapter constAdapter;
    private List<ChatBean.BodyBean> listData = new ArrayList<>();
    private List<ChatBean.BodyBean.RelatedBean> relateData = new ArrayList<>();
    private List<String> strList = new ArrayList<>();
    private HeadService headService;
    private InputMethodManager imm;

    private Dialog copyDialog, moreDialog;
    private RelativeLayout copy_relay;
    private LinearLayout mine_lay, function_lay, share_lay;
    private String copyContent;
    private Dialog permissionDialog;


    @Inject
    ChatPresenter chatPresenter;

    @Inject
    VoiceContentPresenter mVoiceContentPresenter;

    private List<VoiceRvBean.BodyBean> voiceContentList = new ArrayList<>();
    private VoiceContentAdapter mContentAdapter;

    private String szImei;
    private boolean isVoice;

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        chatPresenter.attachView(this);
        mVoiceContentPresenter.attachView(this);
        Log.e("tag", "TAG=" + TAG);
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        szImei = TelephonyMgr.getDeviceId();
        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        Log.e("tag", "szImei=" + szImei);
        SpUtils.setParam(Const.ID, szImei);
        // 语音合成 1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        // 语音听写1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mIat = SpeechRecognizer.createRecognizer(this, mTtsInitListener);
        // 1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        iatDialog = new RecognizerDialog(this, mTtsInitListener);

        RvUtils.setOptionnoLine(chat_rv, this);
        RvUtils.setRvParam_RelayParent(chat_rv);
        chatRvAdapter = new ChatRvAdapter(this, listData, new int[]{R.layout.rvitem_question, R.layout.view_rightitem});
        headService = new HeadService(this);
        chat_rv.addHeaderView(headService);
        chat_rv.setAdapter(chatRvAdapter);
//        audioDialog = initAudioDialog();

        //您可以这样说的内容初始化
        mContentAdapter = new VoiceContentAdapter(this,voiceContentList,R.layout.rvitem_voicecontent);
        RvUtils.setOptionnoLine(mVoiceContentRv,this);
        mVoiceContentRv.setAdapter(mContentAdapter);

        relateGvAdapter = new RelateGvAdapter(this, relateData, R.layout.gvitem_onlytext);
        recom_gv.setAdapter(relateGvAdapter);

//        recommendGvAdapter = new RecommendGvAdapter(this, recommendList, R.layout.gvitem_onlytext);
//        mid_lay.setAdapter(recommendGvAdapter);

        right_voice = (ImageView) findViewById(R.id.right_voice);
        final String voiceType = CommonAction.getVoiceType();
        if (voiceType.equals(Const.vTyep_On)) {
            right_voice.setImageResource(R.mipmap.volume_on);
        } else if (voiceType.equals(Const.vTyep_Off)) {
            right_voice.setImageResource(R.mipmap.volume_off);
        }

        cm = (ClipboardManager) getSystemService(mContext.CLIPBOARD_SERVICE);

        initCopyDialog();
        initMoreDialog();
        permissionDialog = DialogUtils.showPermissionTip(mActivity);
    }

    @Override
    public void refreshView() {
        CommonAction.setThem(this, root_lay);
        String them = CommonAction.getThem();
        if (them.equals(Const.Candy)) {
            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_candy));
        } else if (them.equals(Const.Black)) {
            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_black));
        }
        if (permissionDialog.isShowing()) {
            permissionDialog.dismiss();
        }
    }

    private Dialog initCopyDialog() {
        copyDialog = new Dialog(this, R.style.progress_dialog);
        copyDialog.setContentView(R.layout.dialog_tip);
        copy_relay = (RelativeLayout) copyDialog.findViewById(R.id.copy_relay);
        copyDialog.setCancelable(true);
        return copyDialog;
    }

    private Dialog initMoreDialog() {
        moreDialog = new Dialog(this, R.style.progress_dialog);
        moreDialog.setContentView(R.layout.dialog_more);
        mine_lay = (LinearLayout) moreDialog.findViewById(R.id.mine_lay);
        share_lay = (LinearLayout) moreDialog.findViewById(R.id.share_lay);
        function_lay = (LinearLayout) moreDialog.findViewById(R.id.function_lay);
        moreDialog.setCancelable(true);
        return moreDialog;
    }

    private void showCopyDialog(TextView textView) {
        Window dialogWindow = copyDialog.getWindow();
//            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.RED));
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.dimAmount = 0f;
        int[] location = new int[2];
        textView.getLocationOnScreen(location);
        Rect out = new Rect();
        textView.getWindowVisibleDisplayFrame(out);
        lp.x = location[0];
        lp.y = location[1] - out.top - textView.getHeight();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        copyDialog.show();
    }

    private void showMoreDialog(RelativeLayout view) {
        Window dialogWindow = moreDialog.getWindow();
//            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.RED));
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.dimAmount = 0f;
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Rect out = new Rect();
        view.getWindowVisibleDisplayFrame(out);
        lp.x = location[0];
        lp.y = location[1] - out.top + view.getHeight();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        moreDialog.show();
    }

    @Override
    public void initDatas() {

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
        String question = getIntent().getStringExtra("question");
        if (question != null && !question.equals(Const.questionUrl)) {
            sendContent(question);
        }

        ChatBean.BodyBean bean = (ChatBean.BodyBean) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            voice_iv.setImageResource(R.drawable.dial);
            input_tv.setVisibility(View.VISIBLE);
            input_edt.setVisibility(View.GONE);
            send_tv.setVisibility(View.GONE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(voice_iv.getWindowToken(), 0);
            }
            sendMSG(bean.getAnswer(), bean);
//            isVoice = true;
        }

        /*String tag = getIntent().getStringExtra(Const.intentTag);
        if (tag != null && tag.equals("dial")) {
            input_edt.requestFocus();
//            imm.showInputMethodPicker();
        }*/

        mVoiceContentPresenter.voicePrompt();

    }

    @Override
    public void eventListener() {

       /* input_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mid_lay.setVisibility(View.GONE);
                }
            }
        });*/
        voice_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVoice) {
                    input_tv.setVisibility(View.GONE);
                    input_edt.setVisibility(View.VISIBLE);
                    send_tv.setVisibility(View.VISIBLE);
                    voice_iv.setImageResource(R.mipmap.icon_audio);
                    isVoice = false;
                } else {
                    voice_iv.setImageResource(R.drawable.dial);
                    input_tv.setVisibility(View.VISIBLE);
                    input_edt.setVisibility(View.GONE);
                    send_tv.setVisibility(View.GONE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(voice_iv.getWindowToken(), 0);
                    }
                    isVoice = true;
                }
            }
        });

        voice_relay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                String voiceType = CommonAction.getVoiceType();
                if (voiceType.equals(Const.vTyep_Off)) {
                    right_voice.setImageResource(R.mipmap.volume_on);
                    SpUtils.setParam(Const.voiceType, Const.vTyep_On);
                    mTts.startSpeaking(ResourcesUtils.getString(R.string.voicefirst), mSynListener);
                } else if (voiceType.equals(Const.vTyep_On)) {
                    right_voice.setImageResource(R.mipmap.volume_off);
                    SpUtils.setParam(Const.voiceType, Const.vTyep_Off);
                    mTts.stopSpeaking();
                }
            }
        });
        input_tv.setOnTouchListener(new NoShortClickListener() {
            float temY = 0;

            @Override
            public void onACTION_DOWN(float startY) {
                //启动相机拍照，6.0需要重新申请权限
                isUp = false;
                isInit = false;
                isOver = false;
                isMove = false;
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED)//权限未授予
                {

                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 250
                    );
                } else//已授予权限
                {
                    input_tv.setText("松开 结束");
                    mIatResults.clear();
                    audiores_tv.setText("");
                    mTts.stopSpeaking();
                    temY = startY;
                    starWrite();
                }
            }

            @Override
            public void onShortClick() {

            }

            @Override
            public void onACTION_UP() {
                input_tv.setText("按住 说话");
//                mid_lay.setVisibility(View.GONE);
                chat_rv.smoothScrollToPosition(listData.size());
                /*if (audioDialog.isShowing()) {
                    audioDialog.dismiss();

                }*/
                audio_lay.setVisibility(View.GONE);
                audiores_tv.setText("");
                isUp = true;
                if (audioResultStr != null && isInit) {
                    Message message = Message.obtain();
                    message.what = 110;
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void onACTION_MOVE(MotionEvent motionEvent) {
                float move =motionEvent .getY() - temY;
                Log.e("tag","move="+move);
                if (Math.abs(move)>=100){
                    input_tv.setText("按住 说话");
                    audio_lay.setVisibility(View.GONE);
                    isMove = true;
                }
            }
        });

        recom_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String question = relateData.get(i).getQuestion();
                sendContent(question);
            }
        });

       /* mid_lay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String question = recommendList.get(i).getQuestion();
                sendContent(question);
            }
        });*/
        copy_relay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (copyContent != null) {
                    cm.setText(copyContent);
                    copyDialog.dismiss();
                }
            }
        });

        share_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                moreDialog.dismiss();
//                permission();
                DialogUtils.showShareDialog(mActivity);
            }
        });
        function_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                moreDialog.dismiss();
                RefreshBean bean = new RefreshBean();
                bean.setEventStr("1");
                EventBus.getDefault().post(bean);
                finish();
            }
        });
        mine_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                moreDialog.dismiss();
                RefreshBean bean = new RefreshBean();
                bean.setEventStr("2");
                EventBus.getDefault().post(bean);
                finish();
            }
        });
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

    @OnClick({R.id.send_tv, R.id.userinfo_iv, R.id.back_relay,R.id.close_iv})
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (null == mTts || null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            ToastUtils.Toast_short(mActivity, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        switch (v.getId()) {
            case R.id.send_tv:
                String content = input_edt.getText().toString();
                if (!content.isEmpty()) {
                    ChatBean.BodyBean bean = new ChatBean.BodyBean();
                    bean.setAnswer(content);
                    bean.setFdType("1");
                    listData.add(bean);
                    chatRvAdapter.notifyDataSetChanged();
                    chat_rv.smoothScrollToPosition(listData.size());
                    input_edt.setText("");
//                    mid_lay.setVisibility(View.GONE);
                    chatPresenter.getChatAnswer(CommonAction.getUserId(), content, Const.questionType, szImei);
                }
                break;
            case R.id.userinfo_iv:
                /*Intent intent = new Intent(mContext, MineActivity.class);
                startActivity(intent);*/

                showMoreDialog(more_relay);

                break;
            case R.id.back_relay:
                mTts.stopSpeaking();
//                ReturnUtil.returnMain();
                finish();
                break;
            case R.id.close_iv:
                audio_lay.setVisibility(View.GONE);
                break;
            default:
                break;
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
        mTts.setParameter(SpeechConstant.VOICE_NAME, voiceName[6]);// 设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
        mTts.setParameter(SpeechConstant.TEXT_ENCODING,"utf-8");
        // 设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        // 保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        // 如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        // 3.开始合成
        mTts.startSpeaking(content, mSynListener);
        // 合成监听器
        //

    }

    /**
     * 初始化参数开始听写
     *
     * @Description:
     */
    private void starWrite() {
        // 2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        // 语音识别应用领域（：iat，search，video，poi，music）
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        // 接收语言中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 接受的语言是普通话
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        // 设置听写引擎（云端）
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mIat.setParameter(SpeechConstant.VAD_EOS, "10000");
        /*iatDialog.setListener(mRecognizerDialogListener);
        iatDialog.show();*/
//        audioDialog.show();
//        DialogUtils.setBespreadWindow(audioDialog, mActivity);

        audio_lay.setVisibility(View.VISIBLE);
        recom_gv.setVisibility(View.GONE);
//        mid_lay.setVisibility(View.GONE);
        chat_rv.smoothScrollToPosition(listData.size());
//        ToastUtils.Toast_short(mActivity, "请说话…");
//         3.开始听写
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
            if (!isLast && !isOver) {
                printResult(results);
            }
        }

        // 会话发生错误回调接口
        public void onError(SpeechError error) {
            int errorCode = error.getErrorCode();
            if (errorCode == 10118) {
//                ToastUtils.Toast_short(mActivity, "你好像没有说话哦");
            } else if (errorCode == 20006) {//录音权限被拒绝
                audio_lay.setVisibility(View.GONE);
                Log.e("tag", "ErrorCode=" + errorCode);
                permissionDialog.show();
            } else {
                ToastUtils.Toast_short(mActivity, "操作失败，错误码：" + errorCode);
            }

        }// 获取错误码描述}

        // 开始录音
        public void onBeginOfSpeech() {
            Log.e(TAG, "开始说话");
//            ToastUtils.Toast_short(mActivity, "开始说话");
        }

        // 结束录音
        public void onEndOfSpeech() {
            Log.e(TAG, "说话结束");
//            ToastUtils.Toast_short(mActivity, "说话结束");

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
            }
        }

    };

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            ToastUtils.Toast_short(mActivity, error.getPlainDescription(true));
        }

    };
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
//        input_edt.setText(resultBuffer.toString());
//        input_edt.setSelection(input_edt.length());
        Log.e("tag", "resultStr=" + resultBuffer.toString());

        Message message = Message.obtain();
        message.what = 110;
        mHandler.sendMessage(message);
        isInit = true;
//        ToastUtils.Toast_short(mActivity,resultBuffer.toString());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 250) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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
    public void showData(ChatBean dataBean) {

        if (dataBean != null) {
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
                                intent = new Intent(mContext, NewCalenderActivity.class);
                                intent.putExtra("scheduleData", (Serializable) scheduleData);
                                startActivity(intent);
                                audioResultStr = "";
                            }
                            break;
                        case "3"://项目
                            IntentUtils.toActivity(NewProgramActivity.class, mActivity);
                            break;
                    }
                }
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
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(voice_iv.getWindowToken(), 0);
                        }
                    }

                } else {
//                    recom_gv.setVisibility(View.GONE);
                    chat_rv.smoothScrollToPosition(listData.size());
                }

                String answer = body.getAnswer();
                String voiceType = CommonAction.getVoiceType();
                if (answer != null && voiceType.equals(Const.vTyep_On)) {
                    Spanned spanned = Html.fromHtml(answer);
                    starSpeech(spanned.toString());
                }

                if (url != null && !url.isEmpty()) {
                    mTts.stopSpeaking();
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra(Const.webUrl, url);
                    startActivity(intent);
                }
            }
        }

    }

    @Override
    public void showVoiceContentData(VoiceRvBean dataBean) {
        List<VoiceRvBean.BodyBean> body = dataBean.getBody();
        if (body!=null&&body.size()>0){
            voiceContentList.clear();
            voiceContentList.addAll(body);
            mContentAdapter.notifyDataSetChanged();
        }
    }

    private class RelateGvAdapter extends CommonAdapter<ChatBean.BodyBean.RelatedBean> {

        public RelateGvAdapter(Context context, List<ChatBean.BodyBean.RelatedBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, ChatBean.BodyBean.RelatedBean item, int position) {
            String logogram = item.getLogogram();
            if (logogram != null) {
                helper.setText(R.id.gvonly_tv, logogram);
            }
        }
    }

    private class RecommendGvAdapter extends CommonAdapter<RecomendBean.BodyBean> {

        public RecommendGvAdapter(Context context, List<RecomendBean.BodyBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, RecomendBean.BodyBean item, int position) {
            String logogram = item.getQuestion();
            if (logogram != null) {
                helper.setText(R.id.gvonly_tv, logogram);
            }
        }
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {

    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent(LoginBus busBean) {
        String eventStr = busBean.getEventStr();
        if (eventStr.equals(Const.refresh)) {
            chatRvAdapter.notifyDataSetChanged();
        }
    }*/

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
                    rvleft_lv.setAdapter(new QuestionAdapter(mContext, related, R.layout.rvitem_questiontext));
                } else {
                    retate_lay.setVisibility(View.GONE);
                }

            } else if (itemType == 1) {

                final TextView right_contv = holder.getView(R.id.right_contv);
                right_contv.setText(answer);
                right_contv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showCopyDialog(right_contv);
                        copyContent = right_contv.getText().toString();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (!CommonAction.getIsLogin()) {
                listData.clear();
            }
            chatRvAdapter.notifyDataSetChanged();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (isShouldHideInput(input_lay, ev)) {

            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();


            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTts!=null){
            mTts.destroy();
        }
        if (mIat!=null){
            mIat.destroy();
        }
    }
}
