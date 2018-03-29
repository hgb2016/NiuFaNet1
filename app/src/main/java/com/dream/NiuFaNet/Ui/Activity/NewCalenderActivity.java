package com.dream.NiuFaNet.Ui.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.BusBean.RefreshCalBean;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Bean.NewCalenderBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;
import com.dream.NiuFaNet.Bean.ProgramListBean;
import com.dream.NiuFaNet.Bean.TimeTipBean;
import com.dream.NiuFaNet.Bean.UpFileBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalendarDetailContract;
import com.dream.NiuFaNet.Contract.NewCalenderContract;
import com.dream.NiuFaNet.Contract.ProgramContract;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalendarDetail2Presenter;
import com.dream.NiuFaNet.Presenter.CalendarDetailPresenter;
import com.dream.NiuFaNet.Presenter.NewCalenderPresenter;
import com.dream.NiuFaNet.Presenter.ProgramPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Service.AlarmService;
import com.dream.NiuFaNet.Ui.Service.SendAlarmBroadcast;
import com.dream.NiuFaNet.Utils.BitmapUtils;
import com.dream.NiuFaNet.Utils.CustomHelper;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.Dialog.RemindDialog;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.ImgUtil;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.TakePhotoUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
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

import org.greenrobot.eventbus.EventBus;

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
import okhttp3.MultipartBody;

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
    private boolean isOpen;
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
        dateDialog = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                long time = date.getTime();
                String dateStr = DateFormatUtil.getTime(time, "yyyy年MM月dd日  HH:mm");
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
                                startime_tv1.setText(strings[0] + " 星期" + weekDay);
                                startime_tv2.setText(strings[1]);
                                startDate = time;
                                remindList.clear();
                                remindList.addAll(getReminds(startDate));
                            }
                            break;
                        case 2://结束日期
                            if (null != strings) {
                                endtime_tv1.setText(strings[0] + " 星期" + weekDay);
                                endtime_tv2.setText(strings[1]);
                                endDate = time;
                            }
                            break;
                    }
                }
                Log.e("tag", "dateStr=" + dateStr);
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setContentSize(16)//滚轮文字大小
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

    private void initDataView() {
        origData = (CalendarDetailBean.DataBean) getIntent().getExtras().getSerializable("data");
        MyFrendBean.DataBean yuedata = (MyFrendBean.DataBean) getIntent().getExtras().getSerializable("yuedata");
        tag = getIntent().getStringExtra(Const.intentTag);
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
            bean.setUserName(yuedata.getFriendName());
            bean.setSouce(true);
            bean.setDelete(true);
            participantBeanList.add(bean);
            origParts.add(bean);
//            particepant_tv.setText("参与人("+participantBeanList.size()+")");
            peoPlesAdapter.notifyDataSetChanged();
            isyue = true;
        }
    }

    private void initDataNew() {

        //初始化起始和终止日期
        String date = getIntent().getStringExtra("date");
        if (date != null) {
            long startTime = Long.parseLong(date);
            startDate = startTime;
            String dateStr = DateFormatUtil.getTime(startTime, "yyyy年MM月dd日");
            String dateStr1 = DateFormatUtil.getTime(startTime, "HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(startTime));
            String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            startime_tv1.setText(dateStr + " 星期" + weekDay);
            startime_tv2.setText(dateStr1);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Date endD = calendar.getTime();
            endDate = endD.getTime();
            String dateStr2 = DateFormatUtil.getTime(endD, "yyyy年MM月dd日");
            String dateStr3 = DateFormatUtil.getTime(endD, "HH:mm");
            String weekDay1 = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            endtime_tv1.setText(dateStr2 + " 星期" + weekDay1);
            endtime_tv2.setText(dateStr3);
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
        scheduleId = origData.getScheduleId();
        //起始日期
        String beginTime = origData.getBeginTime();
        Log.e("tag", "beginTime=" + beginTime);
        Date starT1 = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
        startDate = starT1.getTime();
        String start = DateFormatUtil.getTime(starT1, "yyyy年MM月dd日");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(starT1);
        startime_tv1.setText(start + " 星期" + Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)));
        startime_tv2.setText(DateFormatUtil.getTime(starT1, "HH:mm"));

        //结束日期
        String endTime = origData.getEndTime();
        Log.e("tag", "endTime=" + endTime);
        Date endD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
        endDate = endD.getTime();
        String end = DateFormatUtil.getTime(endD, "yyyy年MM月dd日");
        calendar.setTime(endD);
        endtime_tv1.setText(end + " 星期" + Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)));
        endtime_tv2.setText(DateFormatUtil.getTime(endD, "HH:mm"));

        //是否个人
        String type = origData.getType();
        if (type.equals("2")) {
            pw_iv.setImageResource(R.mipmap.offclick_private);
            isCompaney = "2";
        } else {
            pw_iv.setImageResource(R.mipmap.offclick_work);
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
        final int paddingBottom = DensityUtil.dip2px(5);

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

    @OnClick({R.id.address_relay, R.id.beizu_relay,R.id.recorde_lay, R.id.remind_relay, R.id.addpeople_iv, R.id.choose_project, R.id.submit_relay, R.id.close_iv, R.id.startime_lay, R.id.endtime_lay, R.id.pw_iv, R.id.camara_lay, R.id.pic_lay, R.id.voice_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remind_relay:
                ImmUtils.hideImm(mActivity, imm);
                new RemindDialog(mActivity) {
                    @Override
                    public void selectResult(String result, List<TimeTipBean> tipbeanList) {
                        tipList.clear();
                        tipList.addAll(tipbeanList);
                        tiptime_tv.setText(result);
                    }
                };
                isRemindSelect = true;
                break;
            case R.id.close_iv:
                finish();
                break;
            case R.id.recorde_lay:
                ToastUtils.Toast_short("暂未开放");
                break;
            case R.id.address_relay:
                address_edt.setFocusable(true);
                address_edt.setFocusableInTouchMode(true);
                address_edt.requestFocus();
                ImmUtils.showImm(mActivity, address_edt,imm);
                break;
            case R.id.beizu_relay:
                beizu_edt.setFocusable(true);
                beizu_edt.setFocusableInTouchMode(true);
                beizu_edt.requestFocus();
                ImmUtils.showImm(mActivity, beizu_edt,imm);
                break;
            case R.id.submit_relay://创建日程、编辑日程
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
                dateDialog.setDate(calendar);
                dateDialog.show();
                tempTag = 1;
                break;
            case R.id.endtime_lay:
                ImmUtils.hideImm(mActivity, imm);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(endDate);
                dateDialog.setDate(calendar1);
                dateDialog.show();
                tempTag = 2;
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
                projectDialog.show();
                isShowRemindDialog = true;
                break;
            case R.id.addpeople_iv:
                //跳转到通讯录
                IntentUtils.toActivityWithTag(MyFrendsActivity.class, mActivity, "newcal", 101);
                break;
        }
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
            finish();
        }
    }

    @Override
    public void showWordData(List<InputGetBean> dataBean) {
        if (dataBean != null && dataBean.size() > 0) {
            InputGetBean inputGetBean = dataBean.get(0);
            if (inputGetBean.getContent() != null) {
                title_tv.setText(inputGetBean.getContent());
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
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showError() {
        loadingDialog.dismiss();
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));

    }

    @Override
    public void complete() {
        loadingDialog.dismiss();
    }

    private List<CalendarDetailBean.DataBean.RemindBean> getReminds(long startDate) {
        List<CalendarDetailBean.DataBean.RemindBean> remindBeanList = new ArrayList<>();
        Log.e("tag","tipList.size()="+tipList.size());
        for (int i = 0; i < tipList.size(); i++) {
            if (tipList.get(i).isSelect()) {
                CalendarDetailBean.DataBean.RemindBean remindBean = new CalendarDetailBean.DataBean.RemindBean();
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
                participantBeanList.clear();
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
                    peopleBean.setUserName(dataBean.getFriendName());
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

        if (requestCode==111&&resultCode==111&&data!=null){
            String title = data.getStringExtra("title");
            if (title!=null){
                projectTitle = title;
                projectname_tv.setText(title);
                projectId = data.getStringExtra("projectId");
                Log.e("tag","projectId="+projectId);
            }
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
            TextView close_iv = helper.getView(R.id.close_iv);
            if (item.isDelete()) {
                close_iv.setVisibility(View.VISIBLE);
            } else {
                close_iv.setVisibility(View.GONE);
            }

            if (item.getImgUrl() != null) {
                Glide.with(mActivity).load(item.getImgUrl()).into(only_iv);
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
        ToastUtils.Toast_short("获取图片失败，请重试");
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
}
