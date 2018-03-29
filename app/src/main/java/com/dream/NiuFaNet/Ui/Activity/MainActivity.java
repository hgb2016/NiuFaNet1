package com.dream.NiuFaNet.Ui.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Adapter.VoiceContentAdapter;
import com.dream.NiuFaNet.Base.BaseActivityRelay;
import com.dream.NiuFaNet.Bean.ApplyBeFrendBean;
import com.dream.NiuFaNet.Bean.BusBean.DownloadBean;
import com.dream.NiuFaNet.Bean.BusBean.MessageBus;
import com.dream.NiuFaNet.Bean.BusBean.RefreshBean;
import com.dream.NiuFaNet.Bean.BusBean.ReturnBean;
import com.dream.NiuFaNet.Bean.CalInviteBean;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.MessageLayBean;
import com.dream.NiuFaNet.Bean.VersionBean;
import com.dream.NiuFaNet.Bean.VoiceRvBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ChatContract;
import com.dream.NiuFaNet.Contract.MessageContract;
import com.dream.NiuFaNet.Contract.VersionUpdateContract;
import com.dream.NiuFaNet.Contract.VoiceContentContract;
import com.dream.NiuFaNet.CustomView.ApplyFrendView;
import com.dream.NiuFaNet.CustomView.AudioAnimView;
import com.dream.NiuFaNet.CustomView.MessageChildView1;
import com.dream.NiuFaNet.Listener.NoShortClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ChatPresenter;
import com.dream.NiuFaNet.Presenter.MessagePresenter;
import com.dream.NiuFaNet.Presenter.VersionUpdatePresenter;
import com.dream.NiuFaNet.Presenter.VoiceContentPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Fragment.CalenderFragment;
import com.dream.NiuFaNet.Ui.Fragment.FunctionFragment;
import com.dream.NiuFaNet.Ui.Fragment.MainFragment;
import com.dream.NiuFaNet.Ui.Fragment.MainFragment1;
import com.dream.NiuFaNet.Ui.Fragment.ProgramFragment;
import com.dream.NiuFaNet.Ui.Service.SendAlarmBroadcast;
import com.dream.NiuFaNet.Utils.AppManager;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.GlideCircleTransform;
import com.dream.NiuFaNet.Utils.ImgUtil;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.JsonParser;
import com.dream.NiuFaNet.Utils.PopWindowUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.dream.NiuFaNet.Utils.XuniKeyWord;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.sunflower.FlowerCollector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivityRelay implements VersionUpdateContract.View, MessageContract.View, VoiceContentContract.View,ChatContract.View{


    @Bind(R.id.bot_lay)
    LinearLayout bot_lay;
    @Bind(R.id.bot_voice)
    RelativeLayout bot_voice;
    @Bind(R.id.voice_iv)
    ImageView voice_iv;
    @Bind(R.id.function_img)
    ImageView function_img;
    @Bind(R.id.mine_img)
    ImageView mine_img;
    @Bind(R.id.main_img)
    ImageView main_img;
    @Bind(R.id.schedule_img)
    ImageView schedule_img;
    @Bind(R.id.voiceinput_relay)
    RelativeLayout voiceinput_relay;

    @Bind(R.id.audio_lay)
    RelativeLayout audio_lay;
    @Bind(R.id.audioview_left)
    AudioAnimView audioview_left;
    @Bind(R.id.audioview_right)
    AudioAnimView audioview_right;
    @Bind(R.id.audiores_tv)
    TextView audiores_tv;

    @Bind(R.id.schedule_tv)
    TextView schedule_tv;
    @Bind(R.id.main_tv)
    TextView main_tv;
    @Bind(R.id.function_tv)
    TextView function_tv;
    @Bind(R.id.mine_tv)
    TextView mine_tv;
    @Bind(R.id.mine_iv)
    ImageView mine_iv;
    @Bind(R.id.messagetip_relay)
    RelativeLayout messagetip_relay;
    @Bind(R.id.title_relay)
    RelativeLayout title_relay;
    @Bind(R.id.message_numtv)
    TextView message_numtv;
    @Bind(R.id.main_fra)
    FrameLayout mMainFra;
    @Bind(R.id.voiceContent_rv)
    RecyclerView mVoiceContentRv;
    @Bind(R.id.close_iv)
    RelativeLayout mCloseIv;
    @Bind(R.id.back_relay)
    RelativeLayout mBackRelay;
    @Bind(R.id.main_lay)
    LinearLayout mMainLay;
    @Bind(R.id.function_lay)
    LinearLayout mFunctionLay;
    @Bind(R.id.schedule_lay)
    LinearLayout mScheduleLay;
    @Bind(R.id.mine_lay)
    LinearLayout mMineLay;
    @Bind(R.id.input_tv)
    TextView mInputTv;
    @Bind(R.id.input_lay)
    LinearLayout mInputLay;
    @Bind(R.id.textinput_relay)
    RelativeLayout mTextinputRelay;

    private View statutView;
    private Fragment recommendFra, scheduleFra, functionFra, progamFra;
    private FragmentManager mFragmentManager;
    private Fragment currentFragment;
    private Bitmap functionnormal, functionselect, minenormal, mineselect, progselect, prognomal;
    private Bitmap mainnormal, mainselect, schedulenormal, schedulselect;


    private SpeechRecognizer mIat;// 语音听写
    private String audioResultStr;
    private boolean isUp, isInit, isOver,isMove,isCancle;
    private String TAG = "tag";
    private Dialog permissionDialog;
    private List<VoiceRvBean.BodyBean> voiceContentList = new ArrayList<>();
    private VoiceContentAdapter mContentAdapter;
    @Inject
    VersionUpdatePresenter versionUpdatePresenter;
    @Inject
    MessagePresenter messagePresenter;
    @Inject
    VoiceContentPresenter mVoiceContentPresenter;
    @Inject
    ChatPresenter chatPresenter;

    private String szImei;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 210:
                    if (isUp && !audioResultStr.isEmpty()&&!isCancle) {
                        audio_lay.setVisibility(View.GONE);
                        isOver = true;
                        Log.e("tag", "audioResultStr=" + audioResultStr);
                        mLoadingDialog.show();
                        chatPresenter.getChatAnswer(CommonAction.getUserId(),audioResultStr,"app",szImei);
                    }
                    break;
                case 211:
                    if (isUp && !audioResultStr.isEmpty()&&!isCancle) {
                        audio_lay.setVisibility(View.GONE);
                        isOver = true;
                        Log.e("tag", "audioResultStr=" + audioResultStr);
                        mLoadingDialog.show();
                        chatPresenter.getChatAnswer(CommonAction.getUserId(),audioResultStr,"app",szImei);
                    }
                    break;
            }
        }
    };

    //首页布局
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).init();
    }

    @Override
    public void initView() {

        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        versionUpdatePresenter.attachView(this);
        messagePresenter.attachView(this);
        mVoiceContentPresenter.attachView(this);
        chatPresenter.attachView(this);

        EventBus.getDefault().register(this);
        mFragmentManager = this.getSupportFragmentManager();
        initBitmap();
        statutView = XuniKeyWord.initStateView(this);
       // initmTat();
        SpUtils.savaUserInfo(Const.mainStatu, "1");
        permissionDialog = DialogUtils.showPermissionTip(mActivity);
        if (CommonAction.getIsLogin()) {
            String userId = CommonAction.getUserId();
            Log.e("tag", "userId=" + userId);
            JPushInterface.setAlias(mContext, 0, userId);
        }

        //您可以这样说的内容初始化
        mContentAdapter = new VoiceContentAdapter(this,voiceContentList,R.layout.rvitem_voicecontent);
        RvUtils.setOptionnoLine(mVoiceContentRv,this);
        mVoiceContentRv.setAdapter(mContentAdapter);

        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        szImei = TelephonyMgr.getDeviceId();

        applyPermission();
        //忽略电池优化
//        isIgnoreBatteryOption(this);
        initMsgDialog();
//        String sha1 = Sha1Utils.getSHA1(this);
//        Log.e("tag","sha1="+sha1);

    }

    private LinearLayout message_lay;
    private PopupWindow dialog;

    //消息弹窗
    private void initMsgDialog() {
        View messageDialog = LayoutInflater.from(mContext).inflate(R.layout.fragmentmessagereceive, null);
        dialog = PopWindowUtil.getPopupWindow(mActivity, messageDialog, R.style.top2bot, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        message_lay = (LinearLayout) messageDialog.findViewById(R.id.message_lay);
        dialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpaha(mActivity, 1f);
            }
        });
        dialog.setContentView(messageDialog);
    }

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

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Activity activity) {
        String packageName = activity.getPackageName();
        PowerManager pm = (PowerManager) activity
                .getSystemService(Context.POWER_SERVICE);
        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 针对N以上的Doze模式
     *
     * @param activity
     */
    public static void isIgnoreBatteryOption(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = activity.getPackageName();
                PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
// intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    activity.startActivityForResult(intent, 110);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void refreshView() {

        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(this);
        if (permissionDialog.isShowing()) {
            permissionDialog.dismiss();
        }
        Log.e("tag", "isLogin=" + CommonAction.getIsLogin());
        messagetip_relay.setVisibility(View.GONE);
        if (CommonAction.getIsLogin()) {
            toLogin();
            getMessage(true, true);
            SendAlarmBroadcast.startAlarmService(mActivity);
        } else {
            ViewGroup.LayoutParams layoutParams = mine_iv.getLayoutParams();
            layoutParams.width = DensityUtil.dip2px(35);
            mine_iv.setImageResource(R.mipmap.icon_user);
        }
    }

    private void getMessage(boolean isFrend, boolean isCal) {
        message_lay.removeAllViews();
        messageLays.clear();
        if (isCal) {
            messagePresenter.getCalInviteList(CommonAction.getUserId());
        }
        if (isFrend) {
            messagePresenter.applyBeFrend(CommonAction.getUserId());
        }
    }

    private void toLogin() {
        String thirdType = (String) SpUtils.getParam(Const.thirdType, Const.bdUser);
        if (thirdType.equals("bduser")) {
            String itUserIcon = (String) SpUtils.getParam(Const.userHeadUrl, "");
            loginSet(itUserIcon);

        } else {
            String tdUserIcon = (String) SpUtils.getParam(Const.userHeadUrl, "");
            loginSet(tdUserIcon);
        }

    }

    public void loginSet(String headUrl) {
        Log.e("tag", "headurl=" + headUrl);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.dip2px(35), DensityUtil.dip2px(35));
        mine_iv.setLayoutParams(params);
        if (!headUrl.isEmpty()) {
            Glide.with(this).load(headUrl).transform(new GlideCircleTransform(mActivity)).into(mine_iv);
        } else {
            mine_iv.setImageResource(R.mipmap.niu);
        }
    }

    private void initBitmap() {

        functionnormal = ImgUtil.readBitMap(this, R.mipmap.icon_bar04);
        functionselect = ImgUtil.readBitMap(this, R.mipmap.icon_bar04_click);
        minenormal = ImgUtil.readBitMap(this, R.mipmap.icon_bar03);
        mineselect = ImgUtil.readBitMap(this, R.mipmap.icon_bar03_click);
        mainnormal = ImgUtil.readBitMap(this, R.mipmap.icon_bar01);
        mainselect = ImgUtil.readBitMap(this, R.mipmap.icon_bar01_click);
        schedulenormal = ImgUtil.readBitMap(this,R.mipmap.icon_bar02);
        schedulselect = ImgUtil.readBitMap(this, R.mipmap.icon_bar02_click);
    }
    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPause(this);
        super.onPause();
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


    @Override
    public void initDatas() {
        if (savedInstanceState != null) {
            recommendFra = mFragmentManager.findFragmentByTag("recommend");
            scheduleFra = mFragmentManager.findFragmentByTag("schedule");
            functionFra = mFragmentManager.findFragmentByTag("function");
            progamFra = mFragmentManager.findFragmentByTag("program");
        } else {
            initFragment();
        }
        addFragment();
        versionUpdatePresenter.requestVersion("1", "");
        mVoiceContentPresenter.voicePrompt();
    }

    private void initFragment() {
        recommendFra = new MainFragment();
        scheduleFra = new CalenderFragment();
        functionFra = new FunctionFragment();
        progamFra = new ProgramFragment();

    }

    private void addFragment() {

        mFragmentManager.beginTransaction()
                .add(R.id.main_fra, recommendFra, "recommend")
                .show(recommendFra)
                .add(R.id.main_fra, scheduleFra, "schedule")
                .hide(scheduleFra)
                .add(R.id.main_fra, functionFra, "function")
                .hide(functionFra)
                .add(R.id.main_fra, progamFra, "program")
                .hide(progamFra)
                .commit();
        currentFragment = recommendFra;
    }

    private void initbottomItem() {
        function_img.setImageBitmap(functionnormal);
        main_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
        schedule_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
        function_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
        mine_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
        mine_img.setImageBitmap(minenormal);
        main_img.setImageBitmap(mainnormal);
        schedule_img.setImageBitmap(schedulenormal);
    }

    public void startFragment(String s) {
        switch (s) {
            case "recommend":
                if (currentFragment != recommendFra) {
                    mFragmentManager.beginTransaction()
                            .show(recommendFra)
                            .hide(currentFragment)
                            .commitAllowingStateLoss();
                    currentFragment = recommendFra;
                    SpUtils.setParam(Const.mainStatu, "1");
                }

                break;
            case "schedule":
                if (currentFragment != scheduleFra) {
                    mFragmentManager.beginTransaction()
                            .show(scheduleFra)
                            .hide(currentFragment)
                            .commitAllowingStateLoss();
                    currentFragment = scheduleFra;
                }

                break;
            case "function":
                if (currentFragment != functionFra) {
                    mFragmentManager.beginTransaction()
                            .show(functionFra)
                            .hide(currentFragment)
                            .commitAllowingStateLoss();
                    currentFragment = functionFra;
                }
                break;
            case "program":
                if (currentFragment != progamFra) {
                    mFragmentManager.beginTransaction()
                            .show(progamFra)
                            .hide(currentFragment)
                            .commitAllowingStateLoss();
                    currentFragment = progamFra;
                }
                break;
        }
    }

    //录音按钮的监听
    @Override
    public void eventListener() {

        bot_voice.setOnTouchListener(new NoShortClickListener() {
            float temY = 0;

            @Override
            public void onACTION_DOWN(float startY) {
                //启动录音功能，6.0需要重新申请权限
                Log.e("tag","onACTION_DOWN");
                isUp = false;
                isInit = false;
                isOver = false;
                isMove = true;
                isCancle = false;
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)//权限未授予
                {

                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO}, 250
                    );
                } else//已授予权限
                {
                    mIatResults.clear();
                    audiores_tv.setText("");
                    temY = startY;
                    starWrite();
                }
            }

            @Override
            public void onShortClick() {
                audio_lay.setVisibility(View.GONE);
                String mainStatu = (String) SpUtils.getParam(Const.mainStatu, "1");
                Log.e("tag","onShortClick");
                if (mainStatu.equals("3") || mainStatu.equals("4")) {
                    IntentUtils.toActivityWithTag(InputGetActivity.class, mActivity, "main");
                } else {
                    toChat("dial");
                }
            }

            @Override
            public void onACTION_UP() {
                audio_lay.setVisibility(View.GONE);
                audiores_tv.setText("");
                isUp = true;
                if (audioResultStr != null && isInit) {
                    String mainStatu = (String) SpUtils.getParam(Const.mainStatu, "1");
                    Message message = Message.obtain();
                    if (mainStatu.equals("3") || mainStatu.equals("4")) {
                        message.what = 211;
                    } else {
                        message.what = 210;
                    }
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void onACTION_MOVE(MotionEvent motionEvent) {
                float move =motionEvent .getY() - temY;
                Log.e("tag","move="+move);
                if (Math.abs(move)>=100){
                    isCancle = true;
                    audio_lay.setVisibility(View.GONE);
                    audiores_tv.setText("");
                }
            }
        });
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
        audio_lay.setVisibility(View.VISIBLE);
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
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
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
        Log.e("tag", "resultStr=" + resultBuffer.toString());
        String mainStatu = (String) SpUtils.getParam(Const.mainStatu, "1");
        Message message = Message.obtain();
        if (mainStatu.equals("3") || mainStatu.equals("4")) {
            message.what = 211;
        } else {
            message.what = 210;
        }
        mHandler.sendMessage(message);

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


    @SuppressLint("NewApi")
    @OnClick({R.id.textinput_relay, R.id.voiceinput_relay, R.id.close_iv, R.id.function_lay, R.id.mine_lay, R.id.main_lay, R.id.schedule_lay, R.id.back_relay, R.id.messagetip_relay})
    public void onClick(View v) {
        String them = CommonAction.getThem();
        switch (v.getId()) {
            case R.id.function_lay:
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.white).init();
                refreshFunction(them);
                break;
            case R.id.mine_lay:
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.white).init();
//                refreshMine(them);
                if (CommonAction.getIsLogin()) {
                    refreshProgram(them);
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.main_lay:
                refreshMain();
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.white).init();
                break;
            case R.id.schedule_lay:
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.white).init();
                if (CommonAction.getIsLogin()) {
                    reffreshSchedule();
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.back_relay:
                IntentUtils.toActivity(MineActivity.class, mActivity);
                break;
            case R.id.messagetip_relay://展示新消息
                PopWindowUtil.backgroundAlpaha(mActivity, 0.5f);
                dialog.showAsDropDown(title_relay, Gravity.CENTER, 0, 0);
                break;
            case R.id.close_iv:
                audio_lay.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void refreshMain() {
        initbottomItem();
        main_img.setImageBitmap(mainselect);
        main_tv.setTextColor(ResourcesUtils.getColor(R.color.table_textcolor));
        startFragment("recommend");
        statutView.setBackgroundColor(getResources().getColor(R.color.statu_diwen));
        SpUtils.savaUserInfo(Const.mainStatu, "1");
    }

    private void reffreshSchedule() {
        initbottomItem();
        schedule_img.setImageBitmap(schedulselect);
        schedule_tv.setTextColor(ResourcesUtils.getColor(R.color.table_textcolor));
        statutView.setBackgroundColor(getResources().getColor(R.color.statu_diwen));
        startFragment("schedule");
        SpUtils.savaUserInfo(Const.mainStatu, "3");
    }

    private void refreshMine(String them) {
        initbottomItem();
        mine_img.setImageBitmap(mineselect);
        mine_tv.setTextColor(ResourcesUtils.getColor(R.color.table_textcolor));
//                CommonAction.setThem(mActivity, root_lay);
        if (them.equals(Const.Candy)) {
//            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_candy));
            statutView.setBackgroundColor(getResources().getColor(R.color.them_color2));
        } else if (them.equals(Const.Black)) {
//            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_black));
            statutView.setBackgroundColor(getResources().getColor(R.color.them_color7));
        }
        startFragment("mine");
        SpUtils.savaUserInfo(Const.mainStatu, "2");
    }

    private void refreshProgram(String them) {
        initbottomItem();
        mine_img.setImageBitmap(mineselect);
        mine_tv.setTextColor(ResourcesUtils.getColor(R.color.table_textcolor));
       /* if (them.equals(Const.Candy)) {
//            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_candy));
            statutView.setBackgroundColor(getResources().getColor(R.color.them_color2));
        } else if (them.equals(Const.Black)) {
//            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_black));
            statutView.setBackgroundColor(getResources().getColor(R.color.them_color7));
        }*/
        startFragment("program");
        SpUtils.savaUserInfo(Const.mainStatu, "4");
    }

    private void refreshFunction(String them) {
        initbottomItem();
        function_img.setImageBitmap(functionselect);
        function_tv.setTextColor(ResourcesUtils.getColor(R.color.table_textcolor));
        if (them.equals(Const.Candy)) {
//            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_candy));
            statutView.setBackgroundColor(getResources().getColor(R.color.main_botcandy));
        } else if (them.equals(Const.Black)) {
//            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_black));
            statutView.setBackgroundColor(getResources().getColor(R.color.main_botblack));
        }
        startFragment("function");

        SpUtils.savaUserInfo(Const.mainStatu, "2");
    }

    private void toChat(String tag) {
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra(Const.intentTag, tag);
        startActivityForResult(intent, 102);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        SpUtils.savaUserInfo(Const.mainStatu, "1");
        if (mIat!=null){
            mIat.destroy();
        }
        //记录页已打开过
        SpUtils.setParam(Const.is_first, true);
    }


    private long exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                AppManager.getAppManager().AppExit();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void returnMain() {
        initbottomItem();
        startFragment("recommend");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent(RefreshBean busBean) {
        String eventStr = busBean.getEventStr();
        if (eventStr.equals("1")) {
            refreshFunction(CommonAction.getThem());
        } else if (eventStr.equals("2")) {
//            refreshMine(CommonAction.getThem());
            refreshProgram(CommonAction.getThem());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent1(DownloadBean busBean) {
        String eventStr = busBean.getEventStr();
        if (eventStr.equals("fail")) {
            ToastUtils.Toast_short("更新失败");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent2(DownloadBean busBean) {
        String eventStr = busBean.getEventStr();
        if (eventStr.equals("doing")) {
            ToastUtils.Toast_short("已在后台进行更新...");
        }
    }

  /*  @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent2(ReturnBean busBean) {
        String eventStr = busBean.getEventStr();
        if (eventStr.equals("return")) {
            returnMain();
        }
    }*/

    private String itemId = "";

    //答复日程邀请
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent2(CalInviteBean.DataBean busBean) {
        itemId = busBean.getId();
        Log.e("tag", "itemId=" + itemId);
        messagePresenter.replySchedule(busBean.getId(), busBean.getMethod(), CommonAction.getUserId());
    }

    //答复好友邀请
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent2(ApplyBeFrendBean.DataBean busBean) {
        itemId = busBean.getId();
        Log.e("tag", "itemId=" + itemId);
        messagePresenter.replyFrendsApply(busBean.getId(), busBean.getMethod());
    }

    //系统消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent3(MessageBus busBean) {
        String eventStr = busBean.getType();
        if (eventStr != null) {
            switch (eventStr) {
                case "1"://好友申请
                    getMessage(true, false);
                    break;
                case "2"://邀请参加日程
                    getMessage(false, true);
                    break;
            }
        } else {
            Log.e("tag", "eventStr=" + eventStr);
        }
    }

    @Override
    public void showData(VersionBean dataBean) {//主线程的操作
        if (dataBean.getError().equals(Const.success)) {
            String url = dataBean.getUrl();
            String version = dataBean.getVersion();
            Log.e("tag", "apkUrl=" + url);
            Log.e("tag", "version=" + version);
            if (url != null && !url.isEmpty() && version != null && !version.isEmpty()) {//对url和version进行判断
                PackageInfo packageInfo = null;
                try {
                    packageInfo = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0);
                    final String localVersionName = packageInfo.versionName;
                    Log.e("tag", "localVersionName=" + localVersionName);
                    int localVersionCode = packageInfo.versionCode;
                    String[] currentNames = localVersionName.split("\\.");
                    String[] webNames = version.split("\\.");
                    if (currentNames.length == webNames.length) {
                        for (int j = 0; j < webNames.length; j++) {
                            if (Integer.parseInt(webNames[j]) > Integer.parseInt(currentNames[j])) {//有更高版本的apk
                                DialogUtils.getVersionDialog(version, url, this);
                                break;
                            }
                        }
                    } else {
                        ToastUtils.Toast_short("服务器异常，暂不能检测更新！");
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
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
    public void showApplyFrendData(ApplyBeFrendBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<ApplyBeFrendBean.DataBean> data = dataBean.getData();
            if (data != null) {
                if (data.size() > 0) {
                    messagetip_relay.setVisibility(View.VISIBLE);
                    for (int i = 0; i < data.size(); i++) {
                        ApplyFrendView mView = new ApplyFrendView(mActivity);
                        mView.setData(data.get(i));
                        message_lay.addView(mView);
                        MessageLayBean messageLayBean = new MessageLayBean();
                        messageLayBean.setId(data.get(i).getId());
                        messageLayBean.setType("frends");
                        messageLays.add(messageLayBean);
                    }
                    message_numtv.setText("您有" + messageLays.size() + "条消息");
                } else {

                }
            }
        } else {
            ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
        }
    }

    private List<MessageLayBean> messageLays = new ArrayList<>();

    @Override
    public void showCIData(CalInviteBean dataBean) {

        if (dataBean.getError().equals(Const.success)) {
            List<CalInviteBean.DataBean> data = dataBean.getData();
            if (data != null) {
                if (data.size() > 0) {
                    messagetip_relay.setVisibility(View.VISIBLE);
                    for (int i = 0; i < data.size(); i++) {
                        MessageChildView1 mView = new MessageChildView1(mActivity);
                        mView.setData(data.get(i));
                        message_lay.addView(mView);
                        MessageLayBean messageLayBean = new MessageLayBean();
                        messageLayBean.setId(data.get(i).getId());
                        messageLayBean.setType("schedule");
                        messageLays.add(messageLayBean);
                    }
                    message_numtv.setText("您有" + messageLays.size() + "条消息");
                }
            }
        }
    }

    @Override
    public void showFrendsApply(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            if (!itemId.isEmpty()) {
                for (int i = 0; i < messageLays.size(); i++) {
                    if (messageLays.get(i).getType().equals("frends")) {
                        if (messageLays.get(i).getId().equals(itemId)) {
                            message_lay.removeViewAt(i);
                            messageLays.remove(i);
                        }
                    }
                }
                int childCount = message_lay.getChildCount();
                if (childCount == 0) {
                    dialog.dismiss();
                    messagetip_relay.setVisibility(View.GONE);
                } else {
                    message_numtv.setText("您有" + messageLays.size() + "条消息");
                }
            }
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showReplySchedule(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            if (!itemId.isEmpty()) {
                for (int i = 0; i < messageLays.size(); i++) {
                    if (messageLays.get(i).getType().equals("schedule")) {
                        if (messageLays.get(i).getId().equals(itemId)) {
                            message_lay.removeViewAt(i);
                            messageLays.remove(i);
                            CommonAction.refreshLogined();
                        }
                    }
                }
                int childCount = message_lay.getChildCount();
                if (childCount == 0) {
                    dialog.dismiss();
                    messagetip_relay.setVisibility(View.GONE);
                } else {
                    messagetip_relay.setVisibility(View.VISIBLE);
                    message_numtv.setText("您有" + messageLays.size() + "条消息");
                }
            }
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    public void applyPermission() {
        IntentUtils.applyWakePermission(mActivity);
        IntentUtils.applyWakePermission1(mActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 110) {
                //TODO something
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 110) {
                ToastUtils.Toast_short("请开启忽略电池优化~");
            }
        }
    }

    //录音弹窗内容的回调
    @Override
    public void showVoiceContentData(VoiceRvBean dataBean) {
        List<VoiceRvBean.BodyBean> body = dataBean.getBody();
        if (body!=null&&body.size()>0){
            voiceContentList.clear();
            voiceContentList.addAll(body);
            mContentAdapter.notifyDataSetChanged();
        }
    }

    //录音完文字请求的回调
    @Override
    public void showData(ChatBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            ChatBean.BodyBean body = dataBean.getBody();
            if (body!=null){
                String type = body.getType();
                if (type!=null){
                    Intent intent = null;
                    switch (type){
                        case "1"://聊天
                            ChatBean.BodyBean bean = new ChatBean.BodyBean();
                            bean.setFdType("1");
                            String temp = "";
                            if (audioResultStr != null) {
                                bean.setAnswer(audioResultStr);
                                temp = audioResultStr;
                            } else {
                                bean.setAnswer("");
                            }
                            intent = new Intent(mContext, ChatActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bean", bean);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            audioResultStr = "";
                            break;
                        case "2"://日程
                            List<InputGetBean> scheduleData = body.getScheduleData();
                            if (scheduleData!=null){
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
            }
        }
    }

}
