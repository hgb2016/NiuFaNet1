package com.dream.NiuFaNet.Ui.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Adapter.CalDetailParticipantAdapter;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonActivity1;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BusBean.RefreshCalBean;
import com.dream.NiuFaNet.Bean.BusBean.RefreshProBean;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Bean.NewCalenderBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;
import com.dream.NiuFaNet.Bean.TimeTipBean;
import com.dream.NiuFaNet.Bean.UpFileBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalendarDetailContract;
import com.dream.NiuFaNet.Contract.ProgramDetailContract;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalendarDetailPresenter;
import com.dream.NiuFaNet.Presenter.ProgramDetailPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.BitmapUtils;
import com.dream.NiuFaNet.Utils.CheckForAllUtils;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.Dialog.RemindDialog;
import com.dream.NiuFaNet.Utils.GlidUtils;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.PopWindowUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.SpannableStringUtil;
import com.dream.NiuFaNet.Utils.StringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

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
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
public class CalenderDetailActivity extends CommonActivity implements CalendarDetailContract.View,ProgramDetailContract.View {


    @Inject
    CalendarDetailPresenter detailPresenter;
    @Inject
    ProgramDetailPresenter programDetailPresenter;

    @Bind(R.id.share_iv)
    ImageView share_iv;
    @Bind(R.id.username_tv)
    TextView username_tv;
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.beizu_tv)
    TextView beizu_tv;
    @Bind(R.id.beizu_edt)
    EditText beizu_edt;
    @Bind(R.id.address_tv)
    TextView address_tv;
    @Bind(R.id.remind_tv)
    TextView remind_tv;
    @Bind(R.id.duration_tv)
    TextView duration_tv;
    @Bind(R.id.duration_endtv)
    TextView duration_endtv;
    @Bind(R.id.pic_gv)
    MyGridView pic_gv;
    @Bind(R.id.title_edt)
    EditText title_edt;
    @Bind(R.id.address_edt)
    EditText address_edt;
    @Bind(R.id.address_relay)
    LinearLayout address_relay;
    @Bind(R.id.beizu_relay)
    LinearLayout beizu_relay;
    @Bind(R.id.line_beizu)
    View line_beizu;
    @Bind(R.id.line_address)
    View line_address;
    @Bind(R.id.line_project)
    View line_project;
    @Bind(R.id.jia_iv)
    ImageView jia_iv;
    @Bind(R.id.bot_tv)
    TextView bot_tv;
    @Bind(R.id.project_tv)
    TextView project_tv;
    @Bind(R.id.bot_lay)
    LinearLayout bot_lay;
    @Bind(R.id.detail_sv)
    ScrollView detail_sv;
    @Bind(R.id.project_relay)
    LinearLayout project_relay;
    @Bind(R.id.particepant_gv)
    GridView particepant_gv;
    @Bind(R.id.particepant_tv)
    TextView particepant_tv;
    @Bind(R.id.particepant_lay)
    LinearLayout particepant_lay;
    @Bind(R.id.file_lay)
    LinearLayout file_lay;
    @Bind(R.id.partsedt_tv)
    TextView partsedt_tv;
    @Bind(R.id.invite_relay)
    RelativeLayout invite_relay;
    @Bind(R.id.remind_relay)
    RelativeLayout remind_relay;
    @Bind(R.id.more_relay)
    RelativeLayout more_relay;
    @Bind(R.id.more_iv)
    ImageView more_iv;
    @Bind(R.id.remind_line)
    View remind_line;
    @Bind(R.id.edt_tv)
    TextView edt_tv;
    @Bind(R.id.pw_tv)
    TextView pw_tv;
    @Bind(R.id.type_relay)
    RelativeLayout type_relay;
    @Bind(R.id.line_type)
    View line_type;

    private boolean isRemind;
    private InputMethodManager imm;
    private TimePickerView dateDialog;
    private Dialog loadingDialog;
    private PicAdapter picAdapter;
    private CalDetailParticipantAdapter participantAdapter;

    private PopupWindow morepop;
    private int tempTag;
    private long startDate, endDate;
    private List<CalendarDetailBean.DataBean.PicBean> picBeanList = new ArrayList<>();
    private List<CalendarDetailBean.DataBean.participantBean> participantList = new ArrayList<>();
    private List<CalendarDetailBean.DataBean.participantBean> tempParts = new ArrayList<>();

    private Map<String,CalendarDetailBean.DataBean.participantBean> map = new HashMap<>();
    private boolean isEdit;
    private String scheduleId;
    private String userId;

    private CalendarDetailBean.DataBean tempData;
    private String beaginTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_calenderdetail;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        detailPresenter.attachView(this);
        programDetailPresenter.attachView(this);
        initTopPopwindow();

        imm = ImmUtils.getImm(mActivity);
        dateDialog = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                long time = date.getTime();
                String dateStr = DateFormatUtil.getTime(time, "yyyy.MM.dd HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(time));
                String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
                if (dateStr != null) {
                    switch (tempTag) {
                        case 1:
                            duration_tv.setText(dateStr + "(周" + weekDay + ")~");
                            startDate = time;
                            break;
                        case 2:
                            duration_endtv.setText(dateStr + "(周" + weekDay + ")");
                            endDate = time;
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
        picAdapter = new PicAdapter(mContext, picBeanList, R.layout.gvitem_imgclose);
        pic_gv.setAdapter(picAdapter);

        participantAdapter = new CalDetailParticipantAdapter(this,participantList,R.layout.gvitem_timg_btext);
        particepant_gv.setAdapter(participantAdapter);

    }

    @Override
    public void initDatas() {
        scheduleId = getIntent().getStringExtra(Const.scheduleId);
        userId = getIntent().getStringExtra(Const.userId);
        Log.e("tag", "userId=" + userId);
        Log.e("tag", "scheduleId=" + scheduleId);
        if (userId==null){
            userId = CommonAction.getUserId();
        }
        reloadData();

        setView();
    }

    private void setView() {
        if (!userId.equals(CommonAction.getUserId())){
            more_iv.setVisibility(View.GONE);
            invite_relay.setVisibility(View.GONE);
            edt_tv.setVisibility(View.GONE);
            partsedt_tv.setVisibility(View.GONE);
        }else {
            more_iv.setVisibility(View.VISIBLE);
            invite_relay.setVisibility(View.VISIBLE);
            edt_tv.setVisibility(View.VISIBLE);
            partsedt_tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void eventListener() {
    }


    @OnClick({R.id.back_relay,R.id.particepant_lay,R.id.more_iv, R.id.share_iv,R.id.project_relay, R.id.invite_relay, R.id.edt_tv, R.id.duration_tv, R.id.duration_endtv, R.id.camara_lay, R.id.pic_lay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.more_iv:

                PopWindowUtil.backgroundAlpaha(mActivity, 0.5f);
                morepop.showAsDropDown(more_relay);

                break;
            case R.id.share_iv:
//                    DialogUtils.shareDialogcal(mActivity).show();
                break;
            case R.id.invite_relay:
                shareToWechat();
                break;
            case R.id.edt_tv:
                new RemindDialog(mActivity) {
                    @Override
                    public void selectResult(String result, List<TimeTipBean> tipBeanList) {
                        if (tipBeanList != null) {
                            tipList.clear();
                            tipList.addAll(tipBeanList);
                        }
                        remind_tv.setText(result);
                        tempData.setRemind(getReminds(startDate));
                        tempData.setParticipant(new ArrayList<CalendarDetailBean.DataBean.participantBean>());
                        String data = new Gson().toJson(tempData);
                        Log.e("tag", "data=" + data);
                        loadingDialog.show();
                        detailPresenter.edtCalender(HttpUtils.getBody(data), HttpUtils.getRequestBodyParts("file", getFiles()));
                        isRemind = true;
                    }
                };
                break;
            case R.id.duration_tv:
                if (isEdit) {
                    ImmUtils.hideImm(mActivity, imm);
                    dateDialog.show();
                    tempTag = 1;
                }
                break;
            case R.id.duration_endtv:
                if (isEdit) {
                    ImmUtils.hideImm(mActivity, imm);
                    dateDialog.show();
                    tempTag = 2;
                }
                break;
            case R.id.pic_lay:
                IntentUtils.toPicture(mActivity);
                break;
            case R.id.camara_lay:
                IntentUtils.toCamare(mActivity);
                break;
            case R.id.particepant_lay:
                if (userId.equals(CommonAction.getUserId())){
                    toParticipants();
                }
                break;
            case R.id.project_relay:
                if (mProjectId != null) {
                    IntentUtils.toActivityWithTag(ProgramDetailActivity.class, mActivity, mProjectId, 006);
                }
                break;
        }
    }

    private void shareToWechat() {
        String titleStr = title_tv.getText().toString();
        String title = CommonAction.getUserName() + "邀请您参加 "+ "\""+titleStr+"\"";
        String content = "\""+titleStr+"\""+",日程开始时间：" + beaginTime;
        String headUrl = (String) SpUtils.getParam(Const.userHeadUrl, "");
        String clickUrl = Const.wechatFrend_ShareClickUrl + CommonAction.getUserId()+"&scheduleId="+scheduleId;
        DialogUtils.showShareWX(mActivity, Wechat.NAME, title, content,headUrl, clickUrl);
    }

    private void toParticipants() {
        Intent intent = new Intent(mContext,ParticipantsActivity.class);
        Bundle bundle = new Bundle();
        tempData.setParticipant(participantList);
        bundle.putSerializable("data", tempData);
        bundle.putString("scheduleId",scheduleId);
        intent.putExtras(bundle);
        startActivityForResult(intent,101);
    }

    private List<TimeTipBean> tipList = new ArrayList<>();

    private List<CalendarDetailBean.DataBean.RemindBean> getReminds(long startDate) {
        List<CalendarDetailBean.DataBean.RemindBean> remindBeanList = new ArrayList<>();
        for (int i = 0; i < tipList.size(); i++) {
            if (tipList.get(i).isSelect()) {
                CalendarDetailBean.DataBean.RemindBean remindBean = new CalendarDetailBean.DataBean.RemindBean();
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(startDate));
                cal.add(Calendar.MINUTE, -tipList.get(i).getMinute());
                long beforeTime = cal.getTime().getTime();
                String time = DateFormatUtil.getTime(beforeTime, Const.YMD_HMS);
                remindBean.setRemindTime(time);
                remindBean.setDescription(tipList.get(i).getTimeStr());
                remindBeanList.add(remindBean);
            }
        }
        return remindBeanList;
    }

    private List<File> getFiles() {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < picBeanList.size() - 1; i++) {
            File file = picBeanList.get(i).getFile();
            if (file != null) {
                files.add(file);
            }
        }
        Log.e("tag", "files.size()=" + files.size());
        return files;
    }

    private void initTopPopwindow() {
        View moreview = LayoutInflater.from(this).inflate(R.layout.dialog_calmore, null);
        LinearLayout change_lay = (LinearLayout) moreview.findViewById(R.id.change_lay);
        LinearLayout copy_lay = (LinearLayout) moreview.findViewById(R.id.copy_lay);
        LinearLayout delete_lay = (LinearLayout) moreview.findViewById(R.id.delete_lay);
        morepop = PopWindowUtil.getPopupWindow(this, moreview, R.style.top2botAnimation, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        morepop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpaha(mActivity, 1f);
            }
        });
        //编辑
        change_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morepop.dismiss();
                Bundle bundle = new Bundle();
                if (tempData!=null){
                    bundle.putSerializable("data",tempData);
                    Intent intent = new Intent(mActivity,NewCalenderActivity.class);
                    intent.putExtra(Const.intentTag,"edit");
                    intent.putExtras(bundle);
                    startActivityForResult(intent,123);
                }
            }
        });
        //复制
        if (!isEdit) {
            copy_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    morepop.dismiss();
                    Intent intent = new Intent(mContext, NewCalenderActivity.class);
                    Bundle bundle = new Bundle();
                    intent.putExtra(Const.intentTag,"copy");
                    bundle.putSerializable("data", (Serializable) tempData);
                    intent.putExtras(bundle);
                    startActivity(intent);
//                    finish();
                }
            });
        }
        //删除
        delete_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                DialogUtils.showDeleteDialog(mActivity, new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        morepop.dismiss();
                        loadingDialog.show();
                        detailPresenter.deleteCalendar(scheduleId);
                    }
                });
            }
        });
    }

    private List<CalendarDetailBean.DataBean.RemindBean> remindBeanList = new ArrayList<>();
    private String mProjectId;
    @Override
    public void showData(CalendarDetailBean dataBean) {
        loadingDialog.dismiss();
        if (dataBean.getError().equals(Const.success)) {
            CalendarDetailBean.DataBean data = dataBean.getData();
            if (data != null) {
                tempData = data;
                Log.e("tag","data="+new Gson().toJson(data));
                String status = data.getStatus();
                if (status!=null){
                    if (status.equals("3")){
                        ToastUtils.Toast_short("该日程已被删除");
                        CommonAction.refreshLogined();
                        finish();
                    }else {
                        String projectId = data.getProjectId();

                        if (projectId!=null&&!projectId.isEmpty()){
                            mProjectId = projectId;
                            programDetailPresenter.getProjectProgramDetail(projectId);
                        }

                        //创建人
                        String createTime = data.getCreateTime();
                        String beginTime = data.getBeginTime();
                        Date beginDate = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
                        String beginStr = DateFormatUtil.getTime(beginDate, Const.YMD_HM);
                        this.beaginTime = beginStr;
                        String endTime = data.getEndTime();
                        Date tempR = DateFormatUtil.getTime(createTime, Const.YMD_HMS);
                        String tempTime = DateFormatUtil.getTime(tempR, "MM.dd HH:mm");
                        username_tv.setText("该日程" + data.getCreateUserName() + "在" + tempTime + "创建");

                        //起始时间
                        Date tempbd = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
                        startDate = tempbd.getTime();
                        String startTime = DateFormatUtil.getTime(tempbd, Const.YMD_HM2);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(tempbd);
                        String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

                        //结束时间
                        Date tempnd = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
                        endDate = tempnd.getTime();
                        String endDateStr = DateFormatUtil.getTime(tempnd, Const.YMD_HM2);
                        calendar.setTime(tempnd);
                        String weekDay1 = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));


                        //日程类型
                        String type = data.getType();
                        if (type!=null&&!type.isEmpty()){
                            type_relay.setVisibility(View.VISIBLE);
                            line_type.setVisibility(View.VISIBLE);
                            if (type.equals("1")){
                                pw_tv.setText("工作");
                            }else if (type.equals("2")){
                                pw_tv.setText("私人");
                            }
                        }else {
                            type_relay.setVisibility(View.GONE);
                            line_type.setVisibility(View.GONE);
                        }
                        title_tv.setText(data.getTitle());
                        title_edt.setText(data.getTitle());

                        duration_tv.setText(startTime + "(周" + weekDay + ")~");
                        duration_endtv.setText(endDateStr + "(周" + weekDay1 + ")");
                        List<CalendarDetailBean.DataBean.RemindBean> remind = data.getRemind();
                        StringBuffer buff = new StringBuffer();
                        remindBeanList.clear();
                        if (remind!=null&&remind.size()>0){
                            remind_relay.setVisibility(View.VISIBLE);
                            for (int i = 0; i < remind.size(); i++) {
                                String description = remind.get(i).getDescription();
                                String remindTime = remind.get(i).getRemindTime();
                                Log.e("tag", "remindTime=" + remindTime);
                                buff.append(description + "、");
                                CalendarDetailBean.DataBean.RemindBean bean = new CalendarDetailBean.DataBean.RemindBean();
                                bean.setRemindTime(remindTime.substring(0, remindTime.length() - 2));
                                bean.setDescription(description);
                                remindBeanList.add(bean);
                            }
                        }else {
                            remind_relay.setVisibility(View.GONE);
                        }
                        if (buff.toString().length()>0){
                            remind_tv.setText(buff.toString().substring(0, buff.toString().length() - 1));
                        }else {
                            remind_tv.setText("");
                        }

                        //初始化参与人
                        List<CalendarDetailBean.DataBean.participantBean> participant = data.getParticipant();
                        participantList.clear();
                        tempParts.clear();
                /*if (!CommonAction.getUserId().equals(data.getCreateUserId())){
                }*/
                        CalendarDetailBean.DataBean.participantBean bean = new CalendarDetailBean.DataBean.participantBean();
                        bean.setUserId(data.getCreateUserId());
                        bean.setUserName(data.getCreateUserName());
                        bean.setHeadUrl(data.getCreateHeadUrl());
                        participantList.add(bean);

                        if (participant!=null){
                            participantList.addAll(participant);
                        }
                        if (participantList.size()>0){
                            particepant_lay.setVisibility(View.VISIBLE);
                            tempParts.addAll(participantList);
                            for (int i = 0; i < tempParts.size(); i++) {
                                map.put(tempParts.get(i).getUserId(),tempParts.get(i));
                            }
//                    particepant_tv.setText("参与人("+participantList.size()+")");
                            participantAdapter.notifyDataSetChanged();
                        }else {
                            particepant_lay.setVisibility(View.GONE);
                        }


                        //地址
                        String address = data.getAddress();
                        if (address !=null&&!address.isEmpty()){
                            address_edt.setText(address);
                            address_tv.setText(address);
                        }else {
                            address_relay.setVisibility(View.GONE);
                            line_address.setVisibility(View.GONE);
                        }

                        //备注
                        String remark = data.getRemark();
                        if (remark !=null&&!remark.isEmpty()){
//                    beizu_tv.setText(remark);
                            SpannableStringUtil.setTelUrl(mActivity,remark,beizu_tv);
                            beizu_edt.setText(remark);
                        }else {
                            beizu_relay.setVisibility(View.GONE);
                            line_beizu.setVisibility(View.GONE);
                        }

                        List<CalendarDetailBean.DataBean.PicBean> picList = data.getPic();
                        if (picBeanList != null&&picList.size()>0) {
                            file_lay.setVisibility(View.VISIBLE);
                            picBeanList.clear();
                            picBeanList.addAll(picList);
                            picAdapter.notifyDataSetChanged();
                            pictures.clear();
                            for (int i = 0; i <picList.size() ; i++) {
                                pictures.add(picList.get(i).getImgUrl());
                            }
                        }else {
                            file_lay.setVisibility(View.GONE);
                        }

                    }
                }

            }

        }
    }

    private ArrayList<String> pictures = new ArrayList<>();
    @Override
    public void deletePicResult(CommonBean dataBean, int position) {
        if (dataBean.getError().equals(Const.success)) {
            picBeanList.remove(position);
            picAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void edtCalendar(NewCalResultBean dataBean) {
        ToastUtils.Toast_short(dataBean.getMessage());
        loadingDialog.dismiss();
        if (dataBean.getError().equals(Const.success)) {
            bot_lay.setVisibility(View.GONE);
            jia_iv.setVisibility(View.VISIBLE);
            bot_tv.setText("邀请好友");
            mHandler.removeCallbacks(svRunnable);
            refreshData();
            if (!isRemind) {
                isEdit = false;
                String titleEdt = title_edt.getText().toString();
                String addressEdt = address_edt.getText().toString();
                String beizuEdt = beizu_edt.getText().toString();
                title_edt.setVisibility(View.GONE);
                beizu_edt.setVisibility(View.GONE);
                address_edt.setVisibility(View.GONE);
                bot_lay.setVisibility(View.GONE);
                title_tv.setVisibility(View.VISIBLE);
                beizu_tv.setVisibility(View.VISIBLE);
                address_tv.setVisibility(View.VISIBLE);
                title_tv.setText(titleEdt);
                picBeanList.remove(picBeanList.size() - 1);
                for (int i = 0; i < picBeanList.size(); i++) {
                    picBeanList.get(i).setDelete(false);
                }
                if (beizuEdt.isEmpty()){
                    beizu_relay.setVisibility(View.GONE);
                    line_beizu.setVisibility(View.GONE);
                }else {
                    beizu_tv.setText(beizuEdt);
                }

                if (addressEdt.isEmpty()){
                    address_relay.setVisibility(View.GONE);
                    line_address.setVisibility(View.GONE);

                }else {
                    address_tv.setText(addressEdt);
                }
                picAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtils.Toast_short("修改失败");
        }
        Log.e("tag", "dataBean=" + dataBean.toString());
        isRemind = false;
    }

    private void refreshData() {
        CommonAction.refreshPro();
        CommonAction.refreshCal();
    }

    @Override
    public void deleteCalResult(CommonBean dataBean) {
        ToastUtils.Toast_short(dataBean.getMessage());
        if (dataBean.getError().equals(Const.success)){
            refreshData();
            finish();
        }
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Const.CAMERA) {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, Const.CAMERA);
        } else {
            Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(picture, Const.PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.PICTURE && data != null) {
            parseDate(data, Const.PICTURE, mActivity);

        }
        if (requestCode == Const.CAMERA && data != null) {
            parseDate(data, Const.CAMERA, mActivity);
        }
        if (requestCode==123&&resultCode==123){
            reloadData();
        }
        if (requestCode == 101 && resultCode == 102 ) {
            reloadData();
        }
        if (requestCode==006){
            reloadData();
        }
    }

    private void reloadData() {
        loadingDialog.show();
        detailPresenter.getCalendarDetail(userId,scheduleId);
    }

    private void parseDate(Intent data, int picture, Activity mActivity) {
        Map<Bitmap, File> map = BitmapUtils.parseData(data, picture, mActivity);
        CalendarDetailBean.DataBean.PicBean upFileBean = new CalendarDetailBean.DataBean.PicBean();
        for (Map.Entry<Bitmap, File> mFile : map.entrySet()) {
            Bitmap key = mFile.getKey();
            File value = mFile.getValue();
            upFileBean.setBitmap(key);
            upFileBean.setFile(value);
            upFileBean.setDelete(true);
            upFileBean.setAdd(false);
        }
        picBeanList.remove(picBeanList.size() - 1);
        picBeanList.add(upFileBean);
        CalendarDetailBean.DataBean.PicBean addBean = new CalendarDetailBean.DataBean.PicBean();
        addBean.setAdd(true);
        addBean.setDelete(false);
        picBeanList.add(addBean);
        picAdapter.notifyDataSetChanged();
    }

    @Override
    public void showData(ProgramDetailBean dataBean) {
        loadingDialog.dismiss();
        if (dataBean.getError().equals(Const.success)){
            String name = dataBean.getData().getName();
            if (name!=null){
                project_relay.setVisibility(View.VISIBLE);
                line_project.setVisibility(View.VISIBLE);
                tempData.setProjectTitle(name);
                project_tv.setText(name);
            }else {
                project_relay.setVisibility(View.GONE);
                line_project.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showEdtData(CommonBean dataBean) {
        loadingDialog.dismiss();

    }

    @Override
    public void showDeleteData(CommonBean dataBean) {
        loadingDialog.dismiss();

    }

    @Override
    public void showDownload(CommonBean dataBean) {

    }

    private class PicAdapter extends CommonAdapter<CalendarDetailBean.DataBean.PicBean> {

        public PicAdapter(Context context, List<CalendarDetailBean.DataBean.PicBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final CalendarDetailBean.DataBean.PicBean item, final int position) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.dip2px(60), DensityUtil.dip2px(60));
            final ImageView only_iv = helper.getView(R.id.only_iv);
            TextView close_iv = helper.getView(R.id.close_iv);
            only_iv.setLayoutParams(params);
            if (item.isDelete()) {
                close_iv.setVisibility(View.VISIBLE);
            } else {
                close_iv.setVisibility(View.GONE);
            }
            if (item.getImgUrl() != null) {
                Glide.with(mActivity).load(item.getImgUrl()).into(only_iv);
            } else if (item.getBitmap() != null) {
                only_iv.setImageBitmap(item.getBitmap());
            } else if (item.isAdd()) {
                only_iv.setImageResource(R.mipmap.addpicture);
            }
            close_iv.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (item.getId() != null) {
                        loadingDialog.show();
                        detailPresenter.deletePic(item.getId(), position);
                    } else {
                        picBeanList.remove(picBeanList.get(position));
                        picAdapter.notifyDataSetChanged();
                    }
                }
            });
            if (item.isAdd()) {
                only_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentUtils.toPicture(mActivity);
                    }
                });
            }

            helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                   /* Intent intent = new Intent(mActivity, SpaceImageDetailActivity.class);
                    intent.putExtra("picUrl", item.getImgUrl());
                    int[] location = new int[2];
                    only_iv.getLocationOnScreen(location);
                    intent.putExtra("locationX", location[0]);
                    intent.putExtra("locationY", location[1]);

                    intent.putExtra("width", only_iv.getWidth());
                    intent.putExtra("height", only_iv.getHeight());
                    startActivity(intent);
                    overridePendingTransition(0, 0);*/

                    new PhotoPagerConfig.Builder(mActivity)
                            .setBigImageUrls( pictures)                //大图片url,可以是sd卡res，asset，网络图片.
                            .setSavaImage(true)                        //开启保存图片，默认false
                            .setPosition(position)                     //默认展示第2张图片
                            .setSaveImageLocalPath("Pictures")        //这里是你想保存大图片到手机的地址,可在手机图库看到，不传会有默认地址
                            .setOpenDownAnimate(true)                 //是否开启下滑关闭activity，默认开启。类似微信的图片浏览，可下滑关闭一样
                            .build();

                }
            });

        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    private Runnable svRunnable = new Runnable() {
        @Override
        public void run() {
            detail_sv.fullScroll(ScrollView.FOCUS_DOWN);
            beizu_edt.clearFocus();
            address_edt.clearFocus();
            title_edt.clearFocus();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(svRunnable);
    }

}
