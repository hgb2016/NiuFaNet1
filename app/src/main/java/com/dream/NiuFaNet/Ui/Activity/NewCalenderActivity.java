package com.dream.NiuFaNet.Ui.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Bean.CommonBean1;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Bean.ProgramListBean;
import com.dream.NiuFaNet.Bean.TimeTipBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalendarDetailContract;
import com.dream.NiuFaNet.Contract.NewCalenderContract;
import com.dream.NiuFaNet.Contract.ProgramContract;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Listener.SoftKeyBoardListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalendarDetailPresenter;
import com.dream.NiuFaNet.Presenter.NewCalenderPresenter;
import com.dream.NiuFaNet.Presenter.ProgramPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Service.SendAlarmBroadcast;
import com.dream.NiuFaNet.Utils.BitmapUtils;
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.CustomHelper;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.Dialog.RemindDialog;
import com.dream.NiuFaNet.Utils.GlideRoundTransform;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/18 0018.
 */
public class NewCalenderActivity extends CommonActivity implements NewCalenderContract.View, ProgramContract.View, CalendarDetailContract.View,TakePhoto.TakeResultListener, InvokeListener {

    @Bind(R.id.bot_lay)
    LinearLayout bot_lay;
    @Bind(R.id.tiptime_tv)
    TextView tiptime_tv;
    @Bind(R.id.title_tv)
    EditText title_tv;
    @Bind(R.id.startime_tv1)
    TextView startime_tv1;
    @Bind(R.id.startime_tv2)
    TextView startime_tv2;
    @Bind(R.id.endtime_tv1)
    TextView endtime_tv1;
    @Bind(R.id.endtime_tv2)
    TextView endtime_tv2;
    @Bind(R.id.projectname_tv)
    TextView projectname_tv;
    @Bind(R.id.newcal_sv)
    ScrollView newcal_sv;
    @Bind(R.id.pw_iv)
    ImageView pw_iv;
    @Bind(R.id.voice_iv)
    ImageView voice_iv;
    @Bind(R.id.address_edt)
    EditText address_edt;
    @Bind(R.id.beizu_edt)
    EditText beizu_edt;
    @Bind(R.id.particepant_gv)
    MyGridView peples_gv;
    @Bind(R.id.mfile_gv)
    MyGridView mfile_gv;
    @Bind(R.id.addpeople_iv)
    ImageView addpeople_iv;
    @Bind(R.id.root_lay)
    LinearLayout root_lay;
    @Bind(R.id.particepant_tv)
    TextView particepant_tv;
    @Bind(R.id.work_iv)
    ImageView work_iv;
    @Bind(R.id.personal_iv)
    ImageView personal_iv;
    @Bind(R.id.iv_more)
    ImageView iv_more;
    @Bind(R.id.address_relay)
    LinearLayout address_lay;
    @Bind(R.id.remindtype_lay)
    LinearLayout remindtype_lay;
    @Bind(R.id.beizu_relay)
    LinearLayout note_lay;
    @Bind(R.id.apendix_lay)
    LinearLayout apendix_lay;
    @Bind(R.id.duringtime_tv)
    TextView duringtime_tv;
    @Bind(R.id.calendar_title_tv)
    TextView calendar_title_tv;
    @Bind(R.id.voice_bot_relay)
    RelativeLayout voice_bot_relay;
    private String edtstatus="1";
    private boolean isShowRemindDialog;
    private Dialog loadingDialog;
    private List<TimeTipBean> tipList = new ArrayList<>();
    private TimePickerView dateDialog;
    private FilesAdapter filesAdapter;
    private int tempTag;
    private String isCompaney = "1";
    private long startDate, endDate;
    private List<CalendarDetailBean.DataBean.participantBean> participantBeanList = new ArrayList<>();
    private List<CalendarDetailBean.DataBean.participantBean> tempPart = new ArrayList<>();
    private List<CalendarDetailBean.DataBean.participantBean> origParts = new ArrayList<>();
    private List<CalendarDetailBean.DataBean.PicBean> fileBeanList = new ArrayList<>();
    private Map<String,CalendarDetailBean.DataBean.participantBean> mMap = new HashMap<>();
    private PeoPlesAdapter peoPlesAdapter;
    private InputMethodManager imm;
    private List<CalendarDetailBean.DataBean.RemindBean> remindList = new ArrayList<>();
    private Dialog projectDialog;
    private int picItemWidth;
    private String scheduleId;
    private boolean isyue;
    private Runnable svRunnable = new Runnable() {
        @Override
        public void run() {
            newcal_sv.fullScroll(ScrollView.FOCUS_DOWN);
            beizu_edt.clearFocus();
            address_edt.clearFocus();
            title_tv.clearFocus();
//            getCurrentFocus().clearFocus();
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 24:
                    String content = (String) msg.obj;
                    title_tv.setText(content);
                    break;
            }
        }
    };

    @Inject
    NewCalenderPresenter newCalenderPresenter;
    @Inject
    ProgramPresenter programPresenter;
    @Inject
    CalendarDetailPresenter calendarDetailPresenter;
    private boolean isup =false;
    private CustomPopWindow mCustomPopWindow;
    //提醒方式集合
    private ArrayList<Remind> lists=new ArrayList();
    @Override
    public int getLayoutId() {
        return R.layout.activity_addnewcalender;
    }//布局文件

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        newCalenderPresenter.attachView(this);
        programPresenter.attachView(this);
        calendarDetailPresenter.attachView(this);
        peoPlesAdapter = new PeoPlesAdapter(this, participantBeanList, R.layout.gvitem_timg_btext);
        filesAdapter = new FilesAdapter(this, fileBeanList, R.layout.gvitem_imgclose);
        peples_gv.setAdapter(peoPlesAdapter);
        mfile_gv.setAdapter(filesAdapter);

        dateDialog = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                long time = date.getTime();
                String dateStr = DateFormatUtil.getTime(time, "MM月dd日  HH:mm");
                Log.e("tag", "dateStr=" + dateStr);
                String[] strings = null;
                if (dateStr.contains("  ")) {
                    strings = dateStr.split("  ");
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(time));
                String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
                if (dateStr != null) {
                    switch (tempTag) {
                        case 1://开始日期
                            if (null != strings) {
                                startime_tv1.setText(strings[0] + "(周" + weekDay+")");
                                startime_tv2.setText(strings[1]);
                                startDate = time;
                                remindList.clear();
                                remindList.addAll(getReminds(startDate));
                            }
                            break;
                        case 2://结束日期
                            if (null != strings) {
                                endtime_tv1.setText(strings[0] + "(周" + weekDay+")");
                                endtime_tv2.setText(strings[1]);
                                endDate = time;
                            }
                            break;
                    }
                    duringtime_tv.setText(CalculateTimeUtil.getTimeExpend(startDate,endDate));
                }
                Log.e("tag", "dateStr=" + dateStr);
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setContentTextSize(16)//滚轮文字大小
                .setTitleSize(13)//标题文字大小
                .setCancelText("取消")//取消按钮文字
                .setLabel(" 年", "月", "日", "时", "分", "秒")
                .isCyclic(true)//是否循环滚动
                .setLineSpacingMultiplier(2.0f)
                .build();
        loadingDialog = DialogUtils.initLoadingDialog(this);
        projectDialog = initProjectDialog(this);
        imm = ImmUtils.getImm(this);

    }

    private CalendarDetailBean.DataBean origData;
    private String tag;

    @Override
    public void initDatas() {
        projectId = getIntent().getStringExtra("projectId");
        projectTitle = getIntent().getStringExtra("projectTitle");

        initDataView();
        initTitle();
        initScheduleData();

        Map<String, String> map = MapUtils.getMap();
        map.put("type","all");
        loadingDialog.show();
        programPresenter.getProgramList(CommonAction.getUserId(),map);
        Log.e("tag","userId="+CommonAction.getUserId());
        int screenWidth = DensityUtil.getScreenWidth(mActivity);
        picItemWidth = screenWidth - DensityUtil.dip2px(50);
        lists.add(new Remind("无", false));
        lists.add(new Remind("应用内", true));
        lists.add(new Remind("短信", false));
        lists.add(new Remind("电话", false));
    }

    private void initTitle() {
        String text = getIntent().getStringExtra("text");
        if (text != null) {
            loadingDialog.show();
            newCalenderPresenter.getInputWord(text);
        }
    }
    private void initScheduleData() {
        List<InputGetBean> scheduleData = (List<InputGetBean>) getIntent().getSerializableExtra("scheduleData");
        if (scheduleData!=null&&scheduleData.size()>0){
            showWordData(scheduleData);
        }
    }
    //更多 是收起还是展开
    private void isup() {
        if (!isup){
            iv_more.setImageResource(R.mipmap.down);
            address_lay.setVisibility(View.GONE);
            apendix_lay.setVisibility(View.GONE);
            note_lay.setVisibility(View.GONE);
            remindtype_lay.setVisibility(View.GONE);
        }else {
            iv_more.setImageResource(R.mipmap.up);
            address_lay.setVisibility(View.VISIBLE);
            apendix_lay.setVisibility(View.VISIBLE);
            note_lay.setVisibility(View.VISIBLE);
            remindtype_lay.setVisibility(View.GONE);
        }
    }
    private void initDataView() {
        origData = (CalendarDetailBean.DataBean) getIntent().getExtras().getSerializable("data");
        MyFrendBean.DataBean yuedata = (MyFrendBean.DataBean) getIntent().getExtras().getSerializable("yuedata");
        tag = getIntent().getStringExtra(Const.intentTag);
        if(tag!=null) {
            if (tag.equals("edit")) {
                isup = true;
                calendar_title_tv.setText("编辑日程");
            } else {
                isup = false;
                calendar_title_tv.setText("新建日程");

            }
        }
        //更多是否展开
        isup();
        if (origData != null) {
            initDataorige();
        } else {
            initDataNew();
        }

        //初始化参与人
        if (yuedata!=null){
            participantBeanList.clear();
            CalendarDetailBean.DataBean.participantBean bean = new CalendarDetailBean.DataBean.participantBean();
            bean.setUserId(yuedata.getFriendId());
            bean.setHeadUrl(yuedata.getHeadUrl());
            if (yuedata.getFriendRemark()!=null&&!yuedata.getFriendRemark().isEmpty()) {
                bean.setUserName(yuedata.getFriendRemark());
            }else {
                bean.setUserName(yuedata.getFriendName());
            }
            bean.setSouce(true);
            bean.setDelete(true);
            participantBeanList.add(bean);
            origParts.add(bean);
//            particepant_tv.setText("参与人("+participantBeanList.size()+")");
            peoPlesAdapter.notifyDataSetChanged();
            isyue = true;
        }
    }
    //选择附件 弹框
    private void showPopChooseApendix(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_chooseapendix,null);
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCustomPopWindow!=null){
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()){
                    case R.id.back_relay:
                        mCustomPopWindow.dissmiss();
                        break;
                    case R.id.picture_lay:
                        final CustomHelper customHelper = CustomHelper.INSTANCE(getTakePhoto(), "600", "600", false, "200", "200");
                        customHelper.onClick(false);
                        break;
                    case R.id.photo_lay:
                        final CustomHelper customHelper1 = CustomHelper.INSTANCE(getTakePhoto(), "600", "600", false, "200", "200");
                        customHelper1.onClick(true);
                        break;
                }
                //Toast.makeText(HomeActivity.this,showContent,Toast.LENGTH_SHORT).show();
            }
        };
        contentView.findViewById(R.id.back_relay).setOnClickListener(listener);
        contentView.findViewById(R.id.picture_lay).setOnClickListener(listener);
        contentView.findViewById(R.id.photo_lay).setOnClickListener(listener);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .setAnimationStyle(R.style.ActionSheetDialogStyle)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(v, Gravity.BOTTOM,0,0);
    }

    private void initDataNew() {

        //初始化起始和终止日期
        String date = getIntent().getStringExtra("date");
        if (date != null) {
            long startTime = Long.parseLong(date);
            startDate = startTime;
            String dateStr = DateFormatUtil.getTime(startTime, "MM月dd日");
            String dateStr1 = DateFormatUtil.getTime(startTime, "HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(startTime));
            String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            startime_tv1.setText(dateStr + "(周" + weekDay+")");
            startime_tv2.setText(dateStr1);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Date endD = calendar.getTime();
            endDate = endD.getTime();
            String dateStr2 = DateFormatUtil.getTime(endD, "MM月dd日");
            String dateStr3 = DateFormatUtil.getTime(endD, "HH:mm");
            String weekDay1 = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            endtime_tv1.setText(dateStr2 + "(周" + weekDay1+")");
            endtime_tv2.setText(dateStr3);
            duringtime_tv.setText(CalculateTimeUtil.getTimeExpend(startDate,endDate));
        }

        if (projectTitle!=null){
            projectname_tv.setText(projectTitle);
        }

        tipList.clear();
        TimeTipBean tp = new TimeTipBean();
        tp.setMinute(Const.defaultTip_Minute);
        tp.setTimeStr(Const.defaultTip);
        tp.setSelect(true);
        tipList.add(tp);
    }

    private void initDataorige() {
        //标题
        title_tv.setText(origData.getTitle());
        title_tv.setSelection(title_tv.getText().length());
        scheduleId = origData.getScheduleId();
        //起始日期
        String beginTime = origData.getBeginTime();
        Log.e("tag", "beginTime=" + beginTime);
        Date starT1 = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
        startDate = starT1.getTime();
        String start = DateFormatUtil.getTime(starT1, "MM月dd日");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(starT1);
        startime_tv1.setText(start + "(周" + Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK))+")");
        startime_tv2.setText(DateFormatUtil.getTime(starT1, "HH:mm"));

        //结束日期
        String endTime = origData.getEndTime();
        Log.e("tag", "endTime=" + endTime);
        Date endD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
        endDate = endD.getTime();
        String end = DateFormatUtil.getTime(endD, "MM月dd日");
        calendar.setTime(endD);
        endtime_tv1.setText(end + "(周" + Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK))+")");
        endtime_tv2.setText(DateFormatUtil.getTime(endD, "HH:mm"));

        duringtime_tv.setText(CalculateTimeUtil.getTimeExpend(startDate,endDate));
        //是否个人
        String type = origData.getType();
        if (type.equals("2")) {
           // pw_iv.setImageResource(R.mipmap.offclick_private);
            personal_iv.setImageResource(R.mipmap.check_green);
            work_iv.setImageResource(R.mipmap.icon_checkempty);
            isCompaney = "2";
        } else {
          //  pw_iv.setImageResource(R.mipmap.offclick_work);
            personal_iv.setImageResource(R.mipmap.icon_checkempty);
            work_iv.setImageResource(R.mipmap.check_green);
            isCompaney = "1";
        }


        //提醒
        List<CalendarDetailBean.DataBean.RemindBean> remind = origData.getRemind();
        if (remind != null) {
            StringBuffer buff = new StringBuffer();
            tipList.clear();
            for (int i = 0; i < remind.size(); i++) {
                buff.append(remind.get(i).getDescription() + "、");
                for (int j = 0; j < RemindDialog.tipStrs.length; j++) {
                    String description = remind.get(i).getDescription().trim();
                    String tipStr = RemindDialog.tipStrs[j].trim();
                    if (description.equals(tipStr)) {
                        TimeTipBean tp = new TimeTipBean();
                        tp.setMinute(RemindDialog.tipMinutes[j]);
                        tp.setSelect(true);
                        tp.setTimeStr(tipStr);
                        tipList.add(tp);
                    }
                }
            }
            if (buff.toString().length()!=0){
                tiptime_tv.setText(buff.toString().substring(0, buff.toString().length() - 1));
            }else {
                tiptime_tv.setText("");
            }
        }

        //初始化参与人
        List<CalendarDetailBean.DataBean.participantBean> participant = origData.getParticipant();
        if (participant != null) {
//            particepant_tv.setText("参与人("+participant.size()+")");
            participantBeanList.clear();
            participantBeanList.addAll(participant);
            for (int i = 0; i < participantBeanList.size(); i++) {
                participantBeanList.get(i).setDelete(true);
                participantBeanList.get(i).setSouce(true);
            }
            origParts.addAll(participant);
            peoPlesAdapter.notifyDataSetChanged();
        }
        //地址+备注
        if (origData.getAddress() != null) {
            address_edt.setText(origData.getAddress());
        }
        if (origData.getRemark() != null) {
            beizu_edt.setText(origData.getRemark());
        }

        String projectTitle = origData.getProjectTitle();
        if (projectTitle != null) {
            projectname_tv.setText(projectTitle);
        }
        //文档
        if (tag != null && tag.equals(Const.edit)) {
            List<CalendarDetailBean.DataBean.PicBean> origDataPic = origData.getPic();
            if (origDataPic != null) {
                fileBeanList.clear();
                fileBeanList.addAll(origDataPic);
                for (int i = 0; i < fileBeanList.size(); i++) {
                    fileBeanList.get(i).setDelete(true);
                }
            }
        }
//        addFileIcon();
        filesAdapter.notifyDataSetChanged();
    }

    private void addFileIcon() {
        CalendarDetailBean.DataBean.PicBean addBean = new CalendarDetailBean.DataBean.PicBean();
        addBean.setAdd(true);
        fileBeanList.add(addBean);
    }

    @Override
    public void eventListener() {
        //输入法到底部的间距(按需求设置)
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                if (edtstatus.equals("1")) {
                    voice_bot_relay.setVisibility(View.VISIBLE);
                }else {
                    voice_bot_relay.setVisibility(View.GONE);
                }
            }

            @Override
            public void keyBoardHide(int height) {
                voice_bot_relay.setVisibility(View.GONE);
            }
        });


       /* root_lay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                root_lay.getWindowVisibleDisplayFrame(r);
                //r.top 是状态栏高度
                int screenHeight = root_lay.getRootView().getHeight();
                int softHeight = screenHeight - r.bottom;
                View currentFocus = getCurrentFocus();
                int screenHeight1 = DensityUtil.getScreenHeight(mActivity);
                int[] position = new int[2];
                Rect rect = new Rect();
                if (currentFocus != null && currentFocus instanceof EditText) {
                    currentFocus.getGlobalVisibleRect(rect);
                }
                if (softHeight > 100) {//当输入法高度大于100判定为输入法打开
                    int dimen = screenHeight1 - position[1];
                    if (rect.bottom > (screenHeight1 - softHeight)) {
                        root_lay.scrollTo(0, softHeight + paddingBottom - (screenHeight1 - rect.bottom));
                    }
                } else {//否则判断为输入法隐藏了
                    root_lay.scrollTo(0, 0);
                }
            }
        });*/
    }

    private boolean isRemindSelect;

    @OnClick({R.id.voice_bot_relay,R.id.title_tv,R.id.address_edt,R.id.beizu_edt,R.id.addpicture,R.id.remindtype_lay,R.id.more_lay,R.id.address_relay, R.id.beizu_relay,R.id.recorde_lay, R.id.remind_lay, R.id.addpeople_iv, R.id.choose_project, R.id.submit_relay, R.id.close_iv, R.id.startime_lay, R.id.endtime_lay, R.id.personal_lay,R.id.work_lay,R.id.pw_iv, R.id.camara_lay, R.id.pic_lay, R.id.voice_iv})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.voice_bot_relay:
                ImmUtils.hideImm(mActivity, imm);
                IntentUtils.toActivityWithTag(PopVoiceActivity.class, mActivity, "newcal", 333);
                break;
            case R.id.remind_lay:
                ImmUtils.hideImm(mActivity, imm);
              /*  new RemindDialog(mActivity) {
                    @Override
                    public void selectResult(String result, List<TimeTipBean> tipbeanList) {
                        tipList.clear();
                        tipList.addAll(tipbeanList);
                        tiptime_tv.setText(result);
                    }
                };*/
                //
                Intent intent=new Intent();
                intent.putExtra("remind","1");
                intent.setClass(getApplicationContext(),SetRemindActivity.class);
                startActivityForResult(intent, Const.REMIND);
                isRemindSelect = true;
                break;
            case R.id.close_iv:
                ImmUtils.hideImm(mActivity, imm);
                finish();
                break;
            case R.id.title_tv:
                edtstatus="1";
                title_tv.setFocusable(true);
                ImmUtils.showImm(mActivity, title_tv,imm);
                break;
            case R.id.recorde_lay:
                ToastUtils.Toast_short("暂未开放");
                break;
            case R.id.address_relay:
                edtstatus="2";
                voice_bot_relay.setVisibility(View.GONE);
                address_edt.setFocusable(true);
                address_edt.setFocusableInTouchMode(true);
                address_edt.requestFocus();
                ImmUtils.showImm(mActivity, address_edt,imm);
                break;
            case R.id.beizu_relay:
                edtstatus="3";
                voice_bot_relay.setVisibility(View.GONE);
                beizu_edt.setFocusable(true);
                beizu_edt.setFocusableInTouchMode(true);
                beizu_edt.requestFocus();
                ImmUtils.showImm(mActivity, beizu_edt,imm);
                break;

            case R.id.address_edt:
                edtstatus="2";
                address_edt.setFocusable(true);
                address_edt.setFocusableInTouchMode(true);
                address_edt.requestFocus();
                ImmUtils.showImm(mActivity, address_edt,imm);
                break;
            case R.id.beizu_edt:
                edtstatus="3";
                beizu_edt.setFocusable(true);
                beizu_edt.setFocusableInTouchMode(true);
                beizu_edt.requestFocus();
                ImmUtils.showImm(mActivity, beizu_edt,imm);
                break;
            case R.id.submit_relay://创建日程、编辑日程
                ImmUtils.hideImm(mActivity, imm);
                String title = title_tv.getText().toString();
                String address = address_edt.getText().toString();
                String remark = beizu_edt.getText().toString();
                if (origData == null) {
                    origData = new CalendarDetailBean.DataBean();
                }
                origData.setTitle(title);
                origData.setUserId(CommonAction.getUserId());
                origData.setAddress(address);
                origData.setBeginTime(DateFormatUtil.getTime(startDate, Const.YMD_HMS));
                origData.setEndTime(DateFormatUtil.getTime(endDate, Const.YMD_HMS));
                origData.setType(isCompaney);
                origData.setRemark(remark);

                if (isyue){
                    origData.setParticipant(participantBeanList);
                }else {
                    if (isAdd) {
                        tempPart.clear();
                        for (int i = 0; i < participantBeanList.size(); i++) {
                            if (!participantBeanList.get(i).isSouce()){
                                tempPart.add(participantBeanList.get(i));
                            }
                        }
                        origData.setParticipant(tempPart);
                    } else {
                        origData.setParticipant(new ArrayList<CalendarDetailBean.DataBean.participantBean>());
                    }
                }

                //项目id
                if (projectId != null) {
                    origData.setProjectId(projectId);
                }
                if (projectTitle != null) {
                    origData.setProjectTitle(projectTitle);
                }

                Log.e("tag", "tag=" + tag);
                if (title.isEmpty()) {
                    ToastUtils.Toast_short("请输入标题");
                } else if (startDate>endDate){
                    ToastUtils.Toast_short("时间段有误，请重新选择");
                } else {
                    origData.setRemind(getReminds(startDate));
                    String data = new Gson().toJson(origData);
                    Log.e("tag", "data=" + data);
                    loadingDialog.show();
                    if (tag != null) {
                        if (tag.equals("edit")) {//编辑日程
                            calendarDetailPresenter.edtCalender(HttpUtils.getBody(data), HttpUtils.getRequestBodyParts("file", getFiles()));
                        } else {
                            newCalenderPresenter.addCalender(HttpUtils.getBody(data), HttpUtils.getRequestBodyParts("file", getFiles()));
                        }
                    } else {

                        newCalenderPresenter.addCalender(HttpUtils.getBody(data), HttpUtils.getRequestBodyParts("file", getFiles()));
                    }
                }
                break;
            case R.id.startime_lay:
                ImmUtils.hideImm(mActivity, imm);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate);
               /* dateDialog.setDate(calendar);
                dateDialog.show();
                tempTag = 1;*/
                initCustomTimePicker(calendar,"1");
                pvCustomTime.show();
                break;
            case R.id.endtime_lay:
                ImmUtils.hideImm(mActivity, imm);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(endDate);
               /* dateDialog.setDate(calendar1);
                dateDialog.show();
                tempTag = 2;*/
                initCustomTimePicker(calendar1,"2");
                pvCustomTime.show();
                break;
            case R.id.pw_iv:
                ImmUtils.hideImm(mActivity, imm);
                if (isCompaney.equals("1")) {
                    pw_iv.setImageResource(R.mipmap.offclick_private);
                    isCompaney = "2";
                } else {
                    pw_iv.setImageResource(R.mipmap.offclick_work);
                    isCompaney = "1";
                }
                break;
            case R.id.work_lay:
                ImmUtils.hideImm(mActivity, imm);
                work_iv.setImageResource(R.mipmap.check_green);
                personal_iv.setImageResource(R.mipmap.icon_checkempty);
                isCompaney = "1";
                break;
            case R.id.personal_lay:
                ImmUtils.hideImm(mActivity, imm);
                personal_iv.setImageResource(R.mipmap.check_green);
                work_iv.setImageResource(R.mipmap.icon_checkempty);
                isCompaney = "2";
                break;
            case R.id.pic_lay:
                final CustomHelper customHelper = CustomHelper.INSTANCE(getTakePhoto(), "600", "600", false, "200", "200");
                customHelper.onClick(false);
//                IntentUtils.toPicture(mActivity);
                break;
            case R.id.camara_lay:
//                IntentUtils.toCamare(mActivity);
                final CustomHelper customHelper1 = CustomHelper.INSTANCE(getTakePhoto(), "600", "600", false, "200", "200");
                customHelper1.onClick(true);
                break;
            case R.id.voice_iv:
                IntentUtils.toActivityWithTag(InputGetActivity.class, mActivity, "newcal", 333);
                break;
            case R.id.choose_project://项目选择
                ImmUtils.hideImm(mActivity, imm);
             /*   projectDialog.show();
                isShowRemindDialog = true;
                */
               //startActivity(new Intent(mContext,AddProjectActivity.class));

               IntentUtils.toActivityWithTag(AddProjectActivity.class,mActivity,projectname_tv.getText().toString().trim(),222);

                break;
            case R.id.addpeople_iv:
                ImmUtils.hideImm(mActivity, imm);
                Intent intent1=new Intent(mContext,MyFrendsActivity.class);
                intent1.putExtra( Const.intentTag,"newcal");
                Bundle bundle=new Bundle();
                bundle.putSerializable("peoplelist", (Serializable) participantBeanList);
                intent1.putExtra("people",bundle);
                startActivityForResult(intent1,101);
               // IntentUtils.toActivityWithTag(MyFrendsActivity.class, mActivity, "newcal", 101);
                break;
            case R.id.more_lay:
                isup=!isup;
                isup();
                break;
            case R.id.remindtype_lay:
                showChooseRemind(view);
                break;
            case R.id.addpicture:
                showPopChooseApendix(view);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        ImmUtils.hideImm(mActivity);
    }

    private List<File> getFiles() {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < fileBeanList.size(); i++) {
            File file = fileBeanList.get(i).getFile();
            if (file != null) {
                files.add(file);
            }
        }
        Log.e("tag", "files.size()=" + files.size());
        return files;
    }

    @Override
    public void showData(NewCalResultBean dataBean) {
        Log.e("tag", "dataBean=" + dataBean.toString());
        ToastUtils.Toast_short(dataBean.getMessage());
        if (dataBean.getError().equals(Const.success)) {
            CommonAction.refreshCal();
            CommonAction.refreshPro();
            SendAlarmBroadcast.startAlarmService(mActivity);
           // CalenderUtils.addCalendarEvent(mContext,title_tv.getText().toString().trim(),"",startDate,endDate);
            finish();
        }
    }
    //解析语音文字回调
    @Override
    public void showWordData(List<InputGetBean> dataBean) {
        if (dataBean != null && dataBean.size() > 0) {
            InputGetBean inputGetBean = dataBean.get(0);
            if (inputGetBean.getContent() != null) {
                title_tv.setText(inputGetBean.getContent());
                title_tv.setSelection(title_tv.getText().length());
                //是否覆盖当前时间
              if (inputGetBean.getIsDrawTime().equals("true")) {
                  String beginDate = inputGetBean.getBeginDate();
                  Date beginTime = (Date) DateFormatUtil.getTime(beginDate, Const.YMD_HMS);
                  startDate = beginTime.getTime();
                  String ymd = DateFormatUtil.getTime(beginTime, Const.NYR);
                  if (ymd != null) {
                      startime_tv1.setText(ymd);
                  }
                  String startT2 = DateFormatUtil.getTime(beginTime, Const.HM);
                  if (startT2 != null) {
                      startime_tv2.setText(startT2);
                  }
                  String endDate = inputGetBean.getEndDate();
                  Date endTime = DateFormatUtil.getTime(endDate, Const.YMD_HMS);
                  this.endDate = endTime.getTime();
                  String endT1 = DateFormatUtil.getTime(endTime, Const.NYR);
                  if (endT1 != null) {
                      endtime_tv1.setText(endT1);
                  }
                  String endT2 = DateFormatUtil.getTime(endTime, Const.HM);
                  if (endT2 != null) {
                      endtime_tv2.setText(endT2);
                  }
              }else {

              }

            }
        }
    }

    @Override
    public void showDleteParcipant(CommonBean dataBean,int position) {
        if (dataBean.getError().equals(Const.success)){
            for (int i = 0; i <origParts.size() ; i++) {
                if (origParts.get(i).getUserId().equals(participantBeanList.get(position).getUserId())){
                    origParts.remove(i);
                }
            }
            participantBeanList.remove(position);
            peoPlesAdapter.notifyDataSetChanged();


        }else {
            ToastUtils.Toast_short(mActivity,dataBean.getMessage());
        }
    }

    @Override
    public void showError() {
        loadingDialog.dismiss();
        ToastUtils.Toast_short(mActivity,ResourcesUtils.getString(R.string.failconnect));

    }

    @Override
    public void complete() {
        loadingDialog.dismiss();
    }

    private List<CalendarDetailBean.DataBean.RemindBean> getReminds(long startDate) {
        List<CalendarDetailBean.DataBean.RemindBean> remindBeanList = new ArrayList<>();
        Log.e("tag","tipList.size()="+tipList.size());
        for (int i = 0; i < tipList.size(); i++) {
            CalendarDetailBean.DataBean.RemindBean remindBean = new CalendarDetailBean.DataBean.RemindBean();
            if (tipList.get(i).getTimeStr().equals("不提醒")){
                remindBean.setRemindTime("1970-01-01 00:00:00");
                remindBean.setDescription(tipList.get(i).getTimeStr());
                remindBeanList.add(remindBean);
            }else if (tipList.get(i).isSelect()&&!tipList.get(i).getTimeStr().equals("不提醒")) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(startDate));
                cal.add(Calendar.MINUTE, -tipList.get(i).getMinute());
                long beforeTime = cal.getTime().getTime();
                String time = DateFormatUtil.getTime(beforeTime, "yyyy-MM-dd HH:mm:ss");
                remindBean.setRemindTime(time);
                remindBean.setDescription(tipList.get(i).getTimeStr());
                remindBeanList.add(remindBean);
            }
        }
        Log.e("tag","remindBeanList.size()="+remindBeanList.size());

        return remindBeanList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == Const.PICTURE && data != null) {
            parseDate(data, Const.PICTURE, mActivity);

        }
        if (requestCode == Const.CAMERA && data != null) {
            parseDate(data, Const.CAMERA, mActivity);
        }*/

        if (requestCode == 333 && data != null) {
            String text = data.getStringExtra("text");
            if (text != null) {
                newCalenderPresenter.getInputWord(text);
            }
        }

        if (requestCode == 101 && resultCode == 102 && data != null) {
            List<MyFrendBean.DataBean> selectdata = (List<MyFrendBean.DataBean>) data.getExtras().getSerializable("selectdata");
            if (selectdata != null&&selectdata.size()>0) {
                Log.e("tag","participantBeanList.size()="+participantBeanList.size());
                if (tag==null){
                    mMap.clear();
                }else {
                    for (Map.Entry<String, CalendarDetailBean.DataBean.participantBean> mm:mMap.entrySet()){
                        if (!mm.getValue().isSouce()){
                            mMap.remove(mm);
                        }
                    }
                }
                for (int i = 0; i < selectdata.size(); i++) {
                    MyFrendBean.DataBean dataBean = selectdata.get(i);
                    CalendarDetailBean.DataBean.participantBean peopleBean = new CalendarDetailBean.DataBean.participantBean();
                    peopleBean.setUserName(dataBean.getShowName());
                    peopleBean.setUserId(dataBean.getFriendId());
                    peopleBean.setHeadUrl(dataBean.getHeadUrl());
                    peopleBean.setEmpty(false);
                    peopleBean.setAdd(false);
                    peopleBean.setDelete(true);
                    peopleBean.setSouce(false);
                    mMap.put(dataBean.getFriendId(),peopleBean);
                }

                for (int i = 0; i <origParts.size() ; i++) {
                    if (origParts.get(i).isSouce()){
                        mMap.put(origParts.get(i).getUserId(),origParts.get(i));
                    }
                }

                Log.e("tag","map.size="+mMap.size());
                for (Map.Entry<String, CalendarDetailBean.DataBean.participantBean> mm:mMap.entrySet()){
                    participantBeanList.add(mm.getValue());
                }


//                particepant_tv.setText("参与人("+participantBeanList.size()+")");

                peoPlesAdapter.notifyDataSetChanged();
                isAdd = true;

            }
        }
        if (requestCode==222&&resultCode==222&&data!=null){
            String title = data.getStringExtra("title");
            if (title!=null){
                projectTitle = title;
                projectname_tv.setText(title);
                projectId = data.getStringExtra("projectId");
                Log.e("tag","projectId="+projectId);
            }
        }
        if (requestCode==111&&resultCode==111&&data!=null){
            String title = data.getStringExtra("title");
            if (title!=null){
                projectTitle = title;
                projectname_tv.setText(title);
                projectId = data.getStringExtra("projectId");
                Log.e("tag","projectId="+projectId);
            }
        }
        switch (resultCode){
            case Const.REMIND:
               String remind= data.getStringExtra("result");
                if (remind!=null) {
                    tipList.clear();
                    tipList.addAll((List<TimeTipBean>) data.getSerializableExtra("tiplist"));
                    tiptime_tv.setText(remind);
                    if (remind.equals("不提醒")) {
                        TimeTipBean timeTipBean = new TimeTipBean();
                        timeTipBean.setTimeStr("不提醒");
                        timeTipBean.setMinute(-1);
                        timeTipBean.setSelect(true);
                        tipList.add(timeTipBean);
                    }
                }
                break;
        }
    }

    private boolean isAdd;

    private void parseDate(Intent data, int picture, Activity mActivity) {
        Map<Bitmap, File> map = BitmapUtils.parseData(data, picture, mActivity);
        CalendarDetailBean.DataBean.PicBean upFileBean = new CalendarDetailBean.DataBean.PicBean();
        for (Map.Entry<Bitmap, File> mFile : map.entrySet()) {
            Bitmap key = mFile.getKey();
            File value = mFile.getValue();
            upFileBean.setBitmap(key);
            Log.e("tag", "value=" + value.getAbsolutePath());
            upFileBean.setFile(value);
        }
//        fileBeanList.remove(fileBeanList.size() - 1);
        upFileBean.setDelete(true);
        fileBeanList.add(upFileBean);
//        addFileIcon();
        filesAdapter.notifyDataSetChanged();
    }

    private List<ProgramListBean.DataBean> projectList = new ArrayList<>();

    @Override
    public void showData(ProgramListBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<ProgramListBean.DataBean> data = dataBean.getData();
            if (data != null) {
                projectList.clear();
                projectList.addAll(data);
            }
        }
    }

    @Override
    public void showData(CalendarDetailBean dataBean) {

    }

    @Override
    public void deletePicResult(CommonBean dataBean, int position) {
        if (dataBean.getError().equals(Const.success)) {
            fileBeanList.remove(position);
            filesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void edtCalendar(NewCalResultBean dataBean) {
        loadingDialog.dismiss();
        ToastUtils.Toast_short(dataBean.getMessage());
        if (dataBean.getError().equals(Const.success)) {
            setResult(123);
            CommonAction.refreshLogined();
            finish();
        }
    }

    @Override
    public void deleteCalResult(CommonBean dataBean) {

    }

    @Override
    public void validateProjectShowResult(CommonBean1 databean) {

    }

    private class PeoPlesAdapter extends CommonAdapter<CalendarDetailBean.DataBean.participantBean> {

        public PeoPlesAdapter(Context context, List<CalendarDetailBean.DataBean.participantBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final CalendarDetailBean.DataBean.participantBean item, final int position) {
            ImageView only_iv = helper.getView(R.id.only_iv);
            ImageView close_iv = helper.getView(R.id.close_iv);
            TextView only_tv = helper.getView(R.id.only_tv);
            only_tv.setMaxLines(1);
            only_tv.setEllipsize(TextUtils.TruncateAt.END);
            only_tv.setText(item.getUserName());
            if (item.isDelete()) {
                close_iv.setVisibility(View.VISIBLE);
            } else {
                close_iv.setVisibility(View.GONE);
            }

            String headUrl = item.getHeadUrl();
            if (headUrl != null && !headUrl.isEmpty()) {
                helper.setImageByUrl(R.id.only_iv, headUrl, true);
            } else {
                if (!item.isEmpty()){
                    only_iv.setImageResource(R.mipmap.niu);
                }
            }

            close_iv.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {

                    if (item.isSouce()){
                        newCalenderPresenter.deleteParticipant(scheduleId,item.getUserId(),position);
                    }else {
                        participantBeanList.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private class FilesAdapter extends CommonAdapter<CalendarDetailBean.DataBean.PicBean> {

        public FilesAdapter(Context context, List<CalendarDetailBean.DataBean.PicBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(final BaseViewHolder helper, final CalendarDetailBean.DataBean.PicBean item, final int position) {
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(picItemWidth / 5, picItemWidth / 5);
            ImageView only_iv = helper.getView(R.id.only_iv);
            ViewGroup.LayoutParams layoutParams = only_iv.getLayoutParams();
            layoutParams.width = picItemWidth/5;
            layoutParams.height = picItemWidth/5;
            only_iv.setLayoutParams(layoutParams);
            ImageView close_iv = helper.getView(R.id.close_iv);
            if (item.isDelete()) {
                close_iv.setVisibility(View.VISIBLE);
            } else {
                close_iv.setVisibility(View.GONE);
            }

            if (item.getImgUrl() != null) {
                Glide.with(mActivity).load(item.getImgUrl()).transform(new GlideRoundTransform(mContext,5)).into(only_iv);
            } else if (item.getBitmap() != null) {
                only_iv.setImageBitmap(item.getBitmap());
            }

            close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getId() != null) {
                        loadingDialog.show();
                        calendarDetailPresenter.deletePic(item.getId(), position);
                    } else {
                        fileBeanList.remove(position);
                    }
                    filesAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(svRunnable);
    }

    private String projectId;
    private String projectTitle;
    private boolean isOne;

    public Dialog initProjectDialog(final Context activity) {
        final Dialog dialog_tip = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View login = LayoutInflater.from(activity).inflate(R.layout.dialog_projectselect, null);
        ListView choose_projectrv = (ListView) login.findViewById(R.id.choose_projectrv);
        TextView cancle = (TextView) login.findViewById(R.id.cancel_tv);
        TextView sure = (TextView) login.findViewById(R.id.sure_tv);
        ImageView addproject_iv = (ImageView) login.findViewById(R.id.addproject_iv);
        dialog_tip.requestWindowFeature(Window.FEATURE_NO_TITLE);//（这句设置没有title）
        dialog_tip.setContentView(login);
        dialog_tip.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_tip.setCancelable(true);
//        RvUtils.setOptionnoLine(choose_projectrv);
        ProjectAdapter projectAdapter = new ProjectAdapter(this, projectList, R.layout.rvitem_projectselect);
        choose_projectrv.setAdapter(projectAdapter);
        //取消
        cancle.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
            }
        });
        //添加项目
        addproject_iv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                dialog_tip.dismiss();
                IntentUtils.toActivityWithTag(NewProgramActivity.class,mActivity,"newCal",111);
            }
        });

        sure.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (dialog_tip.isShowing()) {
                    dialog_tip.dismiss();
                }
                for (int i = 0; i < projectList.size(); i++) {
                    if (projectList.get(i).isSelect()) {
                        projectname_tv.setText(projectList.get(i).getName());
                        projectId = projectList.get(i).getId();
                        projectTitle = projectList.get(i).getName();
                        Log.e("pro","projectId="+projectId);
                        Log.e("pro","projectTitle="+projectTitle);
                        isOne = true;
                    }
                }

                if (!isOne){
                    projectTitle = "";
                    projectname_tv.setText("选择项目");
                    projectId = "";
                }
            }
        });

        return dialog_tip;
    }

    private class ProjectAdapter extends CommonAdapter<ProgramListBean.DataBean> {

        public ProjectAdapter(Context context, List<ProgramListBean.DataBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, final ProgramListBean.DataBean dataBean, final int position) {
            holder.setText(R.id.projectname_tv, dataBean.getName());
            ImageView select_iv = holder.getView(R.id.select_iv);
            View xuxian_view = holder.getView(R.id.xuxian_view);
            if (dataBean.isSelect()) {
                select_iv.setImageResource(R.drawable.shape_circle_select);
            } else {
                select_iv.setImageResource(R.drawable.shape_circle_noselect);
            }
            select_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!dataBean.isSelect()) {//未选择
                        for (int i = 0; i < projectList.size(); i++) {
                            if (position == i) {
                                projectList.get(i).setSelect(true);
                                isOne = true;
                            } else {
                                projectList.get(i).setSelect(false);
                            }
                        }
                    }else {//已选择
                        dataBean.setSelect(false);
                        isOne = false;
                    }
                    notifyDataSetChanged();

                }
            });
        }
    }


    //takePhoto

    //选择图片后的回调
    @Override
    public void takeSuccess(TResult result) {
        ArrayList<TImage> images = result.getImages();
        String compressPath = images.get(images.size() - 1).getCompressPath();
        if (compressPath != null) {
            File mFile = new File(compressPath);
            Log.e("tag","mFile.lenth="+mFile.length());
            CalendarDetailBean.DataBean.PicBean upFileBean = new CalendarDetailBean.DataBean.PicBean();
            Bitmap key = BitmapFactory.decodeFile(compressPath);
            upFileBean.setBitmap(key);
            upFileBean.setFile(mFile);
//        fileBeanList.remove(fileBeanList.size() - 1);
            upFileBean.setDelete(true);
            fileBeanList.add(upFileBean);
//        addFileIcon();
            filesAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        ToastUtils.Toast_short(mActivity,"获取图片失败，请重试");
    }

    @Override
    public void takeCancel() {
    }

    private InvokeParam invokeParam;

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    private TakePhoto takePhoto;

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    //选择提醒方式弹框
    private void showChooseRemind(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_remind,null);
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.sure_relay:

                        break;
                }
            }
        };
        contentView.findViewById(R.id.sure_relay).setOnClickListener(listener);
        MyListView remind_lv = (MyListView) contentView.findViewById(R.id.remind_lv);

        RemindRvAdapter remindRvAdapter = new RemindRvAdapter(getApplicationContext(), lists, R.layout.rvitem_remind);
        remind_lv.setAdapter(remindRvAdapter);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(v, Gravity.BOTTOM,0,0);
    }



    public class RemindRvAdapter extends CommonAdapter<Remind> {

        public RemindRvAdapter(Context context, ArrayList<Remind> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(final BaseViewHolder holder, final Remind remind, final int position) {
            TextView remind_tv = holder.getView(R.id.remind_tv);
            final ImageView remind_iv = holder.getView(R.id.remind_iv);
            holder.setText(R.id.remind_tv, remind.getRemindTime());
            if (remind.isSelected) {
                remind_tv.setTextColor(ResourcesUtils.getColor(R.color.blue_title));
                remind_iv.setImageResource(R.mipmap.check_green);
            } else {
                remind_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
                remind_iv.setImageResource(R.mipmap.emptycheck_2);
            }
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    remind.setSelected(!remind.isSelected);
                    if (position == 0) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            if (i != position) {
                                lists.get(i).setSelected(false);
                            }
                        }
                    } else {
                        lists.get(0).setSelected(false);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    class Remind {
        private String remindTime;
        private boolean isSelected;

        public Remind(String remindTime, boolean isSelected) {
            this.remindTime = remindTime;
            this.isSelected = isSelected;
        }

        public String getRemindTime() {
            return remindTime;
        }

        public void setRemindTime(String remindTime) {
            this.remindTime = remindTime;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
    //
    //语音弹框
    private void showvoice(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_voice,null);
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCustomPopWindow!=null){
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()){

                }
                //Toast.makeText(HomeActivity.this,showContent,Toast.LENGTH_SHORT).show();
            }
        };
        ImageView voice= (ImageView) contentView.findViewById(R.id.voice_iv);
        voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setAnimationStyle(R.anim.pickerview_slide_in_bottom)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .create()
                .showAtLocation(v, Gravity.BOTTOM,0,0);
    }
    /* //自定义时间选择框
    new OptionsPickerView.Builder(this,new OptionsPickerView.OnOptionsSelectListener(){

    });*/
    private TimePickerView pvCustomTime;
    private TextView start_tv1;
    private TextView start_tv2 ;
    private TextView end_tv1;
    private  TextView end_tv2 ;
    private View startselect_v;
    private  View endselect_v ;
    private String testatus;
    private void initCustomTimePicker(final Calendar selectedDate, final String status) {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */


        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {


            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentTextSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
               /*.animGravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
                        start_tv1 = (TextView) v.findViewById(R.id.starttime_tv1);
                        start_tv2 = (TextView) v.findViewById(R.id.starttime_tv2);
                        end_tv1 = (TextView) v.findViewById(R.id.endtime_tv1);
                        end_tv2 = (TextView) v.findViewById(R.id.endtime_tv2);
                        startselect_v = v.findViewById(R.id.startselect_v);
                        endselect_v = v.findViewById(R.id.endselect_v);
                        LinearLayout start_lay = (LinearLayout) v.findViewById(R.id.start_lay);
                        LinearLayout end_lay = (LinearLayout) v.findViewById(R.id.end_lay);
                        final CheckBox end_checkbox= (CheckBox) v.findViewById(R.id.end_checkbox);
                        final CheckBox start_checkbox= (CheckBox) v.findViewById(R.id.start_checkbox);
                        end_checkbox.setClickable(false);
                        start_checkbox.setClickable(false);
                        checkBoxStatus(end_checkbox, start_checkbox, status);
                        testatus=status;

                        String beginTime;
                        String endTime="";
                           if (origData!=null){
                                beginTime = origData.getBeginTime();
                                endTime = origData.getEndTime();
                           }else {
                                beginTime=DateFormatUtil.getTime(startDate,Const.YMD_HMS);
                               endTime=DateFormatUtil.getTime(endDate,Const.YMD_HMS);
                           }
                            final Date starT1 = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
                            start_tv1.setText(DateFormatUtil.getTime(starT1, Const.Y_M_D));
                            start_tv2.setText(DateFormatUtil.getTime(starT1, Const.HM));
                            final Date enD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
                            end_tv1.setText(DateFormatUtil.getTime(enD, Const.Y_M_D));
                            end_tv2.setText(DateFormatUtil.getTime(enD, Const.HM));

                            start_lay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    testatus = "1";
                                    checkBoxStatus(end_checkbox, start_checkbox, testatus);
                                    selectedDate.setTimeInMillis(starT1.getTime());
                                    pvCustomTime.setDate(selectedDate);
                                }
                            });
                            end_lay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    testatus = "2";
                                    checkBoxStatus(end_checkbox, start_checkbox, testatus);
                                    selectedDate.setTimeInMillis(enD.getTime());
                                    pvCustomTime.setDate(selectedDate);
                                }
                            });

                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String beginTime=start_tv1.getText().toString()+"  "+start_tv2.getText().toString();
                                String endTime=end_tv1.getText().toString()+"  "+end_tv2.getText().toString();
                                Date staD = DateFormatUtil.getTime(beginTime, Const.YMD_HM);
                                Date enD = DateFormatUtil.getTime(endTime, Const.YMD_HM);
                                if (staD.getTime()>enD.getTime()) {
                                    ToastUtils.Toast_short(mActivity,"开始时间不能大于结束时间");
                                }else {
                                    String dateStr = DateFormatUtil.getTime(staD, "MM月dd日");
                                    String dateStr1 = DateFormatUtil.getTime(staD, "HH:mm");
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(staD);
                                    String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
                                    startime_tv1.setText(dateStr + "(周" + weekDay+")");
                                    startime_tv2.setText(dateStr1);
                                    String dateStr2 = DateFormatUtil.getTime(enD, "MM月dd日");
                                    String dateStr3 = DateFormatUtil.getTime(enD, "HH:mm");
                                    Calendar calendar1 = Calendar.getInstance();
                                    calendar1.setTime(enD);
                                    startDate=staD.getTime();
                                    endDate=enD.getTime();
                                    String weekDay1 = Week.getWeekDay(calendar1.get(Calendar.DAY_OF_WEEK));
                                    endtime_tv1.setText(dateStr2 + "(周" + weekDay1+")");
                                    endtime_tv2.setText(dateStr3);

                                    duringtime_tv.setText(CalculateTimeUtil.getTimeExpend(startDate,endDate));
                                    pvCustomTime.dismiss();
                                }
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                }).setType(new boolean[]{true, true, true, true, true, false}).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        String ymd = DateFormatUtil.getTime(date, Const.Y_M_D);
                        String hm = DateFormatUtil.getTime(date, Const.HM);
                        if (testatus.equals("1")) {
                            start_tv1.setText(ymd);
                            start_tv2.setText(hm);
                        }else {
                            end_tv1.setText(ymd);
                            end_tv2.setText(hm);
                        }
                    }
                })
                .setContentTextSize(16)//滚轮文字大小
                .setOutSideCancelable(false)
                .setTitleSize(13)//标题文字大小
                .setCancelText("取消")//取消按钮文字
                .setLabel(" 年", "月", "日", "时", "分", "秒")
                .isCyclic(true)//是否循环滚动
                .setLineSpacingMultiplier(3.0f)
                .build();

    }

    private void checkBoxStatus(CheckBox end_checkbox, CheckBox start_checkbox, String status) {
        if (status.equals("1")){
            start_checkbox.setChecked(true);
            end_checkbox.setChecked(false);
            startselect_v.setBackgroundColor(getResources().getColor(R.color.blue_title));
            endselect_v.setBackgroundColor(getResources().getColor(R.color.white));

        }else {
            start_checkbox.setChecked(false);
            end_checkbox.setChecked(true);
            endselect_v.setBackgroundColor(getResources().getColor(R.color.blue_title));
            startselect_v.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }
}
