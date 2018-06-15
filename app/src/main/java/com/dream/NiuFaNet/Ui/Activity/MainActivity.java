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
import com.dream.NiuFaNet.Bean.CalInviteBean;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.EditCount;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.MessageLayBean;
import com.dream.NiuFaNet.Bean.ShowCountBean;
import com.dream.NiuFaNet.Bean.VersionBean;
import com.dream.NiuFaNet.Bean.VoiceRvBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ChatContract;
import com.dream.NiuFaNet.Contract.MessageContract;
import com.dream.NiuFaNet.Contract.ShowNoticeCountContract;
import com.dream.NiuFaNet.Contract.VersionUpdateContract;
import com.dream.NiuFaNet.Contract.VoiceContentContract;
import com.dream.NiuFaNet.CustomView.ApplyFrendView;
import com.dream.NiuFaNet.CustomView.AudioAnimView;
import com.dream.NiuFaNet.CustomView.MessageChildView1;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Listener.NoShortClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ChatPresenter;
import com.dream.NiuFaNet.Presenter.MessagePresenter;
import com.dream.NiuFaNet.Presenter.ShowNoticeCountPresenter;
import com.dream.NiuFaNet.Presenter.VersionUpdatePresenter;
import com.dream.NiuFaNet.Presenter.VoiceContentPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Fragment.CalenderFragment;
import com.dream.NiuFaNet.Ui.Fragment.ContactFragment;
import com.dream.NiuFaNet.Ui.Fragment.FunctionFragment;
import com.dream.NiuFaNet.Ui.Fragment.MainFragment;
import com.dream.NiuFaNet.Ui.Fragment.ProgramFragment;
import com.dream.NiuFaNet.Ui.Fragment.ProjectFragment;
import com.dream.NiuFaNet.Ui.Service.SendAlarmBroadcast;
import com.dream.NiuFaNet.Utils.AppManager;
import com.dream.NiuFaNet.Utils.BlurBuilder;
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
import butterknife.BindBool;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivityRelay implements VersionUpdateContract.View, MessageContract.View, VoiceContentContract.View,ChatContract.View,ShowNoticeCountContract.View{


    @Bind(R.id.bot_lay)
    LinearLayout bot_lay;
    @Bind(R.id.bot_voice)
    RelativeLayout bot_voice;
    @Bind(R.id.voice_iv)
    ImageView voice_iv;
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
    @Bind(R.id.mine_relay)
    RelativeLayout mBackRelay;
    @Bind(R.id.main_lay)
    LinearLayout mMainLay;
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
    @Bind(R.id.contact_img)
    ImageView contact_img;
    @Bind(R.id.contact_tv)
    TextView contact_tv;
    @Bind(R.id.fridensnum_tv)
    TextView fridensnum_tv;
    @Bind(R.id.projectnum_tv)
    TextView projectnum_tv;
    private View statutView;
    private Fragment recommendFra, scheduleFra, progamFra, contactFra;;
    private FragmentManager mFragmentManager;
    private Fragment currentFragment;
    private Bitmap contactnormal, contactselect, minenormal, mineselect, progselect, prognomal;
    private Bitmap mainnormal, mainselect, schedulenormal, schedulselect;



    private String audioResultStr;

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
    @Inject
    ShowNoticeCountPresenter showNoticeCountPresenter;
    private String szImei;


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
        showNoticeCountPresenter.attachView(this);
        EventBus.getDefault().register(this);
        mFragmentManager = this.getSupportFragmentManager();
        initBitmap();
        statutView = XuniKeyWord.initStateView(this);
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
        initMsgDialog();

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
            mine_iv.setImageResource(R.mipmap.niu);
        }
    }

    private void getMessage(boolean isFrend, boolean isCal) {
        message_lay.removeAllViews();
        messageLays.clear();
        if (isCal) {
            messagePresenter.getCalInviteList(CommonAction.getUserId());
        }
        if (isFrend) {
           showNoticeCountPresenter.showNoticeCount(CommonAction.getUserId());
        }
        if (CommonAction.getIsLogin()) {
            showNoticeCountPresenter.getEditCount(CommonAction.getUserId());
        }
    }
    public void getEditProjectCount(){
        if (CommonAction.getIsLogin()) {
            showNoticeCountPresenter.getEditCount(CommonAction.getUserId());
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

        contactnormal = ImgUtil.readBitMap(this, R.mipmap.icon_bar04);
        contactselect = ImgUtil.readBitMap(this, R.mipmap.icon_bar04_click);
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
           // functionFra = mFragmentManager.findFragmentByTag("function");
            progamFra = mFragmentManager.findFragmentByTag("program");
            contactFra=mFragmentManager.findFragmentByTag("contact");
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
        progamFra = new ProjectFragment();
       //functionFra = new FunctionFragment();
         contactFra=new ContactFragment();

    }

    private void addFragment() {

        mFragmentManager.beginTransaction()
                .add(R.id.main_fra, recommendFra, "recommend")
                .show(recommendFra)
                .add(R.id.main_fra, scheduleFra, "schedule")
                .hide(scheduleFra)
                .add(R.id.main_fra, contactFra, "contact")
                .hide(contactFra)
                .add(R.id.main_fra, progamFra, "program")
                .hide(progamFra)
                .commit();
        currentFragment = recommendFra;
    }

    private void initbottomItem() {
        contact_img.setImageBitmap(contactnormal);
        main_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
        schedule_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
        contact_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
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
            case "contact":
                if (currentFragment != contactFra) {
                    mFragmentManager.beginTransaction()
                            .show(contactFra)
                            .hide(currentFragment)
                            .commitAllowingStateLoss();
                    currentFragment = contactFra;
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

            bot_voice.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (CommonAction.getIsLogin()) {
                    audio_lay.setVisibility(View.GONE);
                    Log.e("tag","onShortClick");
                    BlurBuilder.snapShotWithoutStatusBar(mActivity);
                    IntentUtils.toActivityWithTag(VoiceActivity.class, mActivity, "main");
                       // getActivity().overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                    mActivity.overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                    } else {
                        DialogUtils.getLoginTip(mActivity).show();
                    }
                }
            });



    }



    @SuppressLint("NewApi")
    @OnClick({R.id.textinput_relay, R.id.voiceinput_relay, R.id.close_iv, R.id.contact_lay, R.id.mine_lay, R.id.main_lay, R.id.schedule_lay, R.id.mine_relay, R.id.messagetip_relay})
    public void onClick(View v) {
        String them = CommonAction.getThem();
        switch (v.getId()) {
            case R.id.contact_lay:
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.white).init();
                if (CommonAction.getIsLogin()) {
                    CommonAction.refreshContact();
                    getEditProjectCount();
                    refreshContact(them);
                    mine_iv.setVisibility(View.GONE);
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.mine_lay:
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.white).init();
//                refreshMine(them);
                if (CommonAction.getIsLogin()) {
                    mine_iv.setVisibility(View.VISIBLE);
                    refreshProgram(them);
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.main_lay:
                refreshMain();
                getEditProjectCount();
                CommonAction.refreshLogined();
                mine_iv.setVisibility(View.VISIBLE);
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.white).init();
                break;
            case R.id.schedule_lay:
                getEditProjectCount();
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.white).init();
                if (CommonAction.getIsLogin()) {
                    reffreshSchedule();
                    CommonAction.refreshCal();
                    mine_iv.setVisibility(View.VISIBLE);
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.mine_relay:
                IntentUtils.toActivity(MineActivity1.class, mActivity);
                break;
            case R.id.messagetip_relay://展示新消息
                /*PopWindowUtil.backgroundAlpaha(mActivity, 0.5f);
                dialog.showAsDropDown(title_relay, Gravity.CENTER, 0, 0);*/
                IntentUtils.toActivity(NewMessageActivity.class,mActivity);
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

    private void refreshContact(String them) {
        initbottomItem();
        contact_img.setImageBitmap(contactselect);
        contact_tv.setTextColor(ResourcesUtils.getColor(R.color.table_textcolor));
        if (them.equals(Const.Candy)) {
//            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_candy));
            statutView.setBackgroundColor(getResources().getColor(R.color.main_botcandy));
        } else if (them.equals(Const.Black)) {
//            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_black));
            statutView.setBackgroundColor(getResources().getColor(R.color.main_botblack));
        }
        startFragment("contact");

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
            refreshContact(CommonAction.getThem());
        } else if (eventStr.equals("2")) {
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
        messagePresenter.replySchedule(busBean.getId(), busBean.getMethod(), CommonAction.getUserId(),"");
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
                    if (Integer.parseInt(webNames[0]) > Integer.parseInt(currentNames[0])) {//有更高版本的apk
                        DialogUtils.getVersionDialog(version, url, this);
                                Log.e("voice", Integer.parseInt(currentNames[0])+","+Integer.parseInt(webNames[0]));
                    }else if (Integer.parseInt(webNames[1]) > Integer.parseInt(currentNames[1])) {//有更高版本的apk
                        DialogUtils.getVersionDialog(version, url, this);
                        Log.e("voice", Integer.parseInt(currentNames[1])+","+Integer.parseInt(webNames[1]));
                    }else if (Integer.parseInt(webNames[2]) > Integer.parseInt(currentNames[2])) {//有更高版本的apk
                        DialogUtils.getVersionDialog(version, url, this);
                        Log.e("voice", Integer.parseInt(currentNames[2])+","+Integer.parseInt(webNames[2]));
                    }else {

                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void showError() {
        //ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
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
                   /* messagetip_relay.setVisibility(View.VISIBLE);
                    for (int i = 0; i < data.size(); i++) {
                        ApplyFrendView mView = new ApplyFrendView(mActivity);
                        mView.setData(data.get(i));
                        message_lay.addView(mView);
                        MessageLayBean messageLayBean = new MessageLayBean();
                        messageLayBean.setId(data.get(i).getId());
                        messageLayBean.setType("frends");
                        messageLays.add(messageLayBean);
                    }
                    message_numtv.setText("您有" + messageLays.size() + "条消息");*/
                   fridensnum_tv.setVisibility(View.VISIBLE);
                   fridensnum_tv.setText(data.size());
                   CommonAction.refreshContact();
                } else {
                    fridensnum_tv.setVisibility(View.GONE);
                   // fridensnum_tv.setText(data.size());
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
                    message_numtv.setText(messageLays.size() + "条新邀请");
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

    @Override
    public void showData(ShowCountBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            if (!dataBean.getShowCount().equals("0")){
                fridensnum_tv.setVisibility(View.VISIBLE);
                fridensnum_tv.setText(dataBean.getShowCount());
                CommonAction.refreshContact();
            }else {
                fridensnum_tv.setVisibility(View.GONE);

            }
        }
    }
    @Override
    public void showEditCount(EditCount editCount) {
        if (editCount.getError().equals(Const.success)){
            if (editCount.getIsEditCount()!=null&&!editCount.getIsEditCount().isEmpty()){
                int i = Integer.parseInt(editCount.getIsEditCount());
                if (i>0){
                    projectnum_tv.setText(i+"");
                    projectnum_tv.setVisibility(View.VISIBLE);
                }else {
                    projectnum_tv.setVisibility(View.GONE);
                }
            }
        }
    }

}
