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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Adapter.CalDetailParticipantAdapter;
import com.dream.NiuFaNet.Bean.ApplyBeFrendBean;
import com.dream.NiuFaNet.Bean.CalInviteBean;
import com.dream.NiuFaNet.Bean.CommonBean1;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.ConflictCalBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;
import com.dream.NiuFaNet.Bean.TimeTipBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalendarDetailContract;
import com.dream.NiuFaNet.Contract.EditScheduleInfoContract;
import com.dream.NiuFaNet.Contract.MessageContract;
import com.dream.NiuFaNet.Contract.ProgramDetailContract;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalendarDetailPresenter;
import com.dream.NiuFaNet.Presenter.EditScheduleInfoPresenter;
import com.dream.NiuFaNet.Presenter.MessagePresenter;
import com.dream.NiuFaNet.Presenter.ProgramDetailPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.BitmapUtils;
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.DutyPopwindowUtils;
import com.dream.NiuFaNet.Utils.GlideRoundTransform;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.PopWindowUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.SpannableStringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;

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
import butterknife.BindInt;
import butterknife.OnClick;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit2.http.Field;

/**
 * Created by Administrator on 2017/11/19 0019.
 */
public class CalenderDetailActivity extends CommonActivity implements MessageContract.View, CalendarDetailContract.View, ProgramDetailContract.View, EditScheduleInfoContract.View {


    @Inject
    CalendarDetailPresenter detailPresenter;
    @Inject
    ProgramDetailPresenter programDetailPresenter;
    @Inject
    EditScheduleInfoPresenter editScheduleInfoPresenter;
    @Inject
    MessagePresenter messagePresenter;
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
    MyGridView particepant_gv;
    @Bind(R.id.particepant_lay)
    LinearLayout particepant_lay;
    @Bind(R.id.file_lay)
    LinearLayout file_lay;
    @Bind(R.id.partsedt_tv)
    TextView partsedt_tv;
    @Bind(R.id.invite_relay)
    RelativeLayout invite_relay;
    @Bind(R.id.remind_relay)
    LinearLayout remind_relay;
    @Bind(R.id.more_relay)
    RelativeLayout more_relay;
    @Bind(R.id.more_tv)
    TextView more_tv;
    @Bind(R.id.remind_line)
    View remind_line;
    @Bind(R.id.edt_tv)
    TextView edt_tv;
    @Bind(R.id.work_iv)
    ImageView work_iv;
    @Bind(R.id.personal_iv)
    ImageView personal_iv;
    @Bind(R.id.type_relay)
    LinearLayout type_relay;
    @Bind(R.id.line_type)
    View line_type;
    @Bind(R.id.starttime_tv1)
    TextView starttime_tv1;
    @Bind(R.id.starttime_tv2)
    TextView starttime_tv2;
    @Bind(R.id.endtime_tv1)
    TextView endtime_tv1;
    @Bind(R.id.endtime_tv2)
    TextView endtime_tv2;
    @Bind(R.id.work_lay)
    LinearLayout work_lay;
    @Bind(R.id.personal_lay)
    LinearLayout personal_lay;
    @Bind(R.id.duringtime_tv)
    TextView duringtime_tv;
    @Bind(R.id.start_lay)
    LinearLayout start_lay;
    @Bind(R.id.end_lay)
    LinearLayout end_lay;
    @Bind(R.id.bot_relay1)
    RelativeLayout bot_relay1;
    @Bind(R.id.arrow1)
    ImageView arrow1;
    @Bind(R.id.arrow2)
    ImageView arrow2;
    @Bind(R.id.arrow3)
    ImageView arrow3;
    @Bind(R.id.arrow4)
    ImageView arrow4;
    @Bind(R.id.arrow5)
    ImageView arrow5;
    @Bind(R.id.arrow6)
    ImageView arrow6;
    @Bind(R.id.warn_lay)
    LinearLayout warn_lay;
    @Bind(R.id.conflict_lv)
    MyListView conflict_lv;
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

    private Map<String, CalendarDetailBean.DataBean.participantBean> map = new HashMap<>();
    private boolean isEdit;
    private String scheduleId;
    private String userId;

    private CalendarDetailBean.DataBean tempData;
    private String beaginTime;
    private String testatus;
    private CustomPopWindow mCustomPopWindow;
    private String status;
    private ArrayList<ConflictCalBean.DataBean> conflictCalList=new ArrayList<>();
    private ConflictAdapter conflictAdapter;
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
        editScheduleInfoPresenter.attachView(this);
        messagePresenter.attachView(this);
        initTopPopwindow();
        imm = ImmUtils.getImm(mActivity);
        dateDialog = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                long time = date.getTime();
                String dateStr = DateFormatUtil.getTime(time, "MM月dd日");
                String timeStr = DateFormatUtil.getTime(time, "HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(time));
                String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
                if (dateStr != null) {
                    switch (tempTag) {
                        case 1:
                            duration_tv.setText(dateStr + "(周" + weekDay + ")~");
                            starttime_tv1.setText(dateStr);
                            starttime_tv2.setText(timeStr);
                            startDate = time;
                            break;
                        case 2:
                            duration_endtv.setText(dateStr + "(周" + weekDay + ")");
                            endtime_tv1.setText(dateStr);
                            endtime_tv2.setText(timeStr);
                            endDate = time;
                            break;
                    }
                    duringtime_tv.setText(CalculateTimeUtil.getTimeExpend(startDate, endDate));
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
        picAdapter = new PicAdapter(mContext, picBeanList, R.layout.gvitem_imgclose);
        pic_gv.setAdapter(picAdapter);
        participantAdapter = new CalDetailParticipantAdapter(this, participantList, R.layout.gvitem_timg_btext);
        particepant_gv.setAdapter(participantAdapter);
        particepant_gv.setOnTouchBlankPositionListener(new MyGridView.OnTouchBlankPositionListener() {
            @Override
            public boolean onTouchBlankPosition() {
                if (userId.equals(CommonAction.getUserId())) {
                    toParticipants();
                }
                return true;
            }
        });
        //有时间冲突的日程
        conflictAdapter=new ConflictAdapter(mContext,conflictCalList,R.layout.view_conflict);
        conflict_lv.setAdapter(conflictAdapter);
    }

    @Override
    public void initDatas() {
        scheduleId = getIntent().getStringExtra(Const.scheduleId);
        userId = getIntent().getStringExtra(Const.userId);
        Log.e("tag", "userId=" + userId);
        Log.e("tag", "scheduleId=" + scheduleId);
        if (userId == null) {
            userId = CommonAction.getUserId();
        }
        reloadData();
        setView();
        String inviteCal = getIntent().getStringExtra("inviteCal");
        if (inviteCal != null && inviteCal.equals("inviteCal")) {
            showInviteCal();
        }
    }


    private void setView() {
        if (!userId.equals(CommonAction.getUserId())) {
            more_tv.setVisibility(View.GONE);
            invite_relay.setVisibility(View.GONE);
            edt_tv.setVisibility(View.GONE);
            partsedt_tv.setVisibility(View.GONE);
        } else {
            more_tv.setVisibility(View.VISIBLE);
            invite_relay.setVisibility(View.VISIBLE);
            edt_tv.setVisibility(View.GONE);
            partsedt_tv.setVisibility(View.GONE);
        }
    }

    @Override
    public void eventListener() {
    }

    /**
     * 日程邀请详情展示
     * 如果是从新的日程邀请 跳入详情 则详情部分不可点击
     */
    private void showInviteCal() {
        Map<String, String> params = MapUtils.getMapInstance();
        params.put("userId",CommonAction.getUserId());
        params.put("scheduleId",scheduleId);
        params.put("status","1");
        messagePresenter.validateSchedule(params);
        bot_relay1.setVisibility(View.VISIBLE);
        warn_lay.setVisibility(View.VISIBLE);
        more_relay.setVisibility(View.GONE);
        invite_relay.setVisibility(View.GONE);
        arrow1.setVisibility(View.GONE);
        arrow2.setVisibility(View.GONE);
        arrow3.setVisibility(View.GONE);
        arrow4.setVisibility(View.GONE);
        arrow5.setVisibility(View.GONE);
        arrow6.setVisibility(View.GONE);
        end_lay.setClickable(false);
        start_lay.setClickable(false);
        type_relay.setClickable(false);
        title_tv.setClickable(false);
        address_relay.setClickable(false);
        beizu_tv.setClickable(false);
        beizu_relay.setClickable(false);
        beizu_edt.setClickable(false);
        remind_relay.setClickable(false);
        particepant_lay.setClickable(false);
        project_relay.setClickable(false);
        duration_tv.setClickable(false);
        duration_endtv.setClickable(false);
        particepant_gv.setOnTouchBlankPositionListener(new MyGridView.OnTouchBlankPositionListener() {
            @Override
            public boolean onTouchBlankPosition() {

                return true;
            }
        });

    }

    @OnClick({R.id.accept_tv, R.id.reject_tv, R.id.end_lay, R.id.start_lay, R.id.type_relay, R.id.title_tv, R.id.address_relay, R.id.beizu_tv, R.id.remind_relay, R.id.back_relay, R.id.particepant_lay, R.id.more_tv, R.id.project_relay, R.id.invite_relay, R.id.edt_tv, R.id.duration_tv, R.id.duration_endtv, R.id.camara_lay, R.id.pic_lay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.type_relay:
                new DutyPopwindowUtils(view) {
                    @Override
                    public void selectNanListener() {
                        work_iv.setImageResource(R.mipmap.check_green);
                        personal_iv.setImageResource(R.mipmap.icon_checkempty);
                        work_lay.setVisibility(View.VISIBLE);
                        personal_lay.setVisibility(View.GONE);
                        CalendarDetailBean.DataBean dataBean = new CalendarDetailBean.DataBean();
                        dataBean.setType("1");
                        dataBean.setScheduleId(scheduleId);
                        dataBean.setUserId(CommonAction.getUserId());
                        mLoadingDialog.show();
                        editScheduleInfoPresenter.editScheduleInfo(new Gson().toJson(dataBean));
                    }

                    @Override
                    public void selectNvListener() {
                        work_iv.setImageResource(R.mipmap.icon_checkempty);
                        work_lay.setVisibility(View.GONE);
                        personal_lay.setVisibility(View.VISIBLE);
                        personal_iv.setImageResource(R.mipmap.check_green);
                        CalendarDetailBean.DataBean dataBean = new CalendarDetailBean.DataBean();
                        dataBean.setType("2");
                        dataBean.setScheduleId(scheduleId);
                        dataBean.setUserId(CommonAction.getUserId());
                        mLoadingDialog.show();
                        editScheduleInfoPresenter.editScheduleInfo(new Gson().toJson(dataBean));
                    }
                };
                break;
            case R.id.title_tv:
                Intent intent1 = new Intent(mActivity, InputActivity.class);
                intent1.putExtra("scheduleId", tempData.getScheduleId());
                intent1.putExtra("input", "event");
                intent1.putExtra("result", tempData.getTitle());
                startActivityForResult(intent1, 555);
                break;
            case R.id.address_relay:
                Intent intent2 = new Intent(mActivity, InputActivity.class);
                intent2.putExtra("scheduleId", tempData.getScheduleId());
                intent2.putExtra("input", "address");
                intent2.putExtra("result", tempData.getAddress());
                startActivityForResult(intent2, 555);
                break;
            case R.id.beizu_tv:
                Intent intent3 = new Intent(mActivity, InputActivity.class);
                intent3.putExtra("scheduleId", tempData.getScheduleId());
                intent3.putExtra("input", "note");
                intent3.putExtra("result", tempData.getRemark());
                startActivityForResult(intent3, 555);
                break;
            case R.id.back_relay:
                finish();
                break;
            case R.id.start_lay:
                final Calendar startDate = Calendar.getInstance();//系统当前时间
                String beginTime = tempData.getBeginTime();
                Date starT1 = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
                startDate.setTimeInMillis(starT1.getTime());
                initCustomTimePicker(startDate, "1");
                pvCustomTime.show();
                break;
            case R.id.end_lay:
                final Calendar endDate = Calendar.getInstance();//系统当前时间
                String endTime = tempData.getEndTime();
                Date endT = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
                endDate.setTimeInMillis(endT.getTime());
                initCustomTimePicker(endDate, "2");
                pvCustomTime.show();
                break;
            case R.id.more_tv:

                PopWindowUtil.backgroundAlpaha(mActivity, 0.5f);
                morepop.showAsDropDown(more_relay);

                break;
            case R.id.invite_relay:
                shareToWechat();
                break;
            case R.id.remind_relay:
                Intent intent = new Intent(mContext, SetRemindActivity1.class);
                Bundle bundle = new Bundle();
                intent.putExtra(Const.intentTag, "copy");
                bundle.putSerializable("data", (Serializable) tempData);
                intent.putExtras(bundle);
                startActivityForResult(intent, 555);
                break;
            case R.id.edt_tv:

             /*   new RemindDialog(mActivity) {
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
                };*/

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
                if (userId.equals(CommonAction.getUserId())) {
                    toParticipants();
                }
                break;
            case R.id.project_relay:
                if (mProjectId != null) {
                    Map<String, String> map = MapUtils.getMap();
                    map.put("userId", CommonAction.getUserId());
                    map.put("projectId", mProjectId);
                    detailPresenter.validateProjectShow(map);

                }
                break;
            case R.id.accept_tv:
                if (scheduleId != null) {
                    status = "1";
                    messagePresenter.replySchedule(scheduleId, "1", CommonAction.getUserId(), "");
                }
                break;
            case R.id.reject_tv:
                if (scheduleId != null) {
                    showInputReason(view, scheduleId);
                }
                break;
        }
    }

    private void shareToWechat() {
        String titleStr = title_tv.getText().toString();
        String title = CommonAction.getUserName() + "邀请您参加 " + "\"" + titleStr + "\"";
        String content = "\"" + titleStr + "\"" + ",日程开始时间：" + beaginTime;
        String headUrl = (String) SpUtils.getParam(Const.userHeadUrl, "");
        String clickUrl = Const.wechatFrend_ShareClickUrl + CommonAction.getUserId() + "&scheduleId=" + scheduleId;
        DialogUtils.showShareWX(mActivity, Wechat.NAME, title, content, headUrl, clickUrl);
    }

    private void toParticipants() {
        Intent intent = new Intent(mContext, ParticipantsActivity.class);
        Bundle bundle = new Bundle();
        tempData.setParticipant(participantList);
        bundle.putSerializable("data", tempData);
        bundle.putString("scheduleId", scheduleId);
        intent.putExtras(bundle);
        startActivityForResult(intent, 101);
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
                if (tempData != null) {
                    bundle.putSerializable("data", tempData);
                    Intent intent = new Intent(mActivity, NewCalenderActivity.class);
                    intent.putExtra(Const.intentTag, "edit");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 123);
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
                    intent.putExtra(Const.intentTag, "copy");
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
                morepop.dismiss();
                DialogUtils.showDeleteDialog(mActivity, new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        loadingDialog.show();
                        detailPresenter.deleteCalendar(scheduleId, CommonAction.getUserId());
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
                Log.e("tag", "data=" + new Gson().toJson(data));
                String status = data.getStatus();
                if (status != null) {
                    if (status.equals("3")) {
                        ToastUtils.Toast_short("该日程已被删除");
                        CommonAction.refreshLogined();
                        finish();
                    } else {
                        String projectId = data.getProjectId();

                        if (projectId != null && !projectId.isEmpty()) {
                            mProjectId = projectId;
                            programDetailPresenter.getProjectProgramDetail(projectId, CommonAction.getUserId(), "1");
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
                        String startTime = DateFormatUtil.getTime(tempbd, Const.MR);
                        String startTime1 = DateFormatUtil.getTime(tempbd, Const.HM);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(tempbd);
                        String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

                        //结束时间
                        Date tempnd = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
                        endDate = tempnd.getTime();
                        String endDateStr = DateFormatUtil.getTime(tempnd, Const.MR);
                        String endDateStr1 = DateFormatUtil.getTime(tempnd, Const.HM);
                        calendar.setTime(tempnd);
                        String weekDay1 = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

                        starttime_tv1.setText(startTime1);
                        starttime_tv2.setText(startTime + "(周" + weekDay + ")");
                        endtime_tv1.setText(endDateStr1);
                        endtime_tv2.setText(endDateStr + "(周" + weekDay1 + ")");
                        duringtime_tv.setText(CalculateTimeUtil.getTimeExpend(startDate, endDate));

                        //日程类型
                        String type = data.getType();
                        if (type != null && !type.isEmpty()) {
                            type_relay.setVisibility(View.VISIBLE);
                            line_type.setVisibility(View.VISIBLE);
                            if (type.equals("1")) {
                                work_iv.setImageResource(R.mipmap.check_green);
                                personal_iv.setImageResource(R.mipmap.icon_checkempty);
                                work_lay.setVisibility(View.VISIBLE);
                                personal_lay.setVisibility(View.GONE);
                            } else if (type.equals("2")) {
                                work_iv.setImageResource(R.mipmap.icon_checkempty);
                                work_lay.setVisibility(View.GONE);
                                personal_lay.setVisibility(View.VISIBLE);
                                personal_iv.setImageResource(R.mipmap.check_green);
                            }
                        } else {
                            type_relay.setVisibility(View.GONE);
                            line_type.setVisibility(View.GONE);
                        }
                        title_tv.setText(data.getTitle());
                        title_edt.setText(data.getTitle());
                        List<CalendarDetailBean.DataBean.RemindBean> remind = data.getRemind();
                        StringBuffer buff = new StringBuffer();
                        remindBeanList.clear();
                        if (remind != null && remind.size() > 0) {
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
                        } else {
                            remind_relay.setVisibility(View.GONE);
                        }
                        if (buff.toString().length() > 0) {
                            remind_tv.setText(buff.toString().substring(0, buff.toString().length() - 1));
                        } else {
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
                        bean.setStatus(data.getStatus());
                        participantList.add(bean);

                        if (participant != null) {
                            participantList.addAll(participant);
                        }
                        if (participantList.size() > 0) {
                            particepant_lay.setVisibility(View.VISIBLE);
                            tempParts.addAll(participantList);
                            for (int i = 0; i < tempParts.size(); i++) {
                                map.put(tempParts.get(i).getUserId(), tempParts.get(i));
                            }
//                    particepant_tv.setText("参与人("+participantList.size()+")");
                            participantAdapter.notifyDataSetChanged();
                        } else {
                            particepant_lay.setVisibility(View.GONE);
                        }


                        //地址
                        String address = data.getAddress();
                        if (address != null && !address.isEmpty()) {
                            address_relay.setVisibility(View.VISIBLE);
                            line_address.setVisibility(View.VISIBLE);
                            address_edt.setText(address);
                            address_tv.setText(address);
                        } else {
                            address_relay.setVisibility(View.GONE);
                            line_address.setVisibility(View.GONE);
                        }

                        //备注
                        String remark = data.getRemark();
                        if (remark != null && !remark.isEmpty()) {
                            beizu_relay.setVisibility(View.VISIBLE);
                            line_beizu.setVisibility(View.VISIBLE);
                            beizu_tv.setText("备注：" +"\n"+ remark);
                            // SpannableStringUtil.setTelUrl(mActivity,remark,beizu_tv);
                            // beizu_edt.setText(remark);
                        } else {
                            beizu_relay.setVisibility(View.GONE);
                            line_beizu.setVisibility(View.GONE);
                        }

                        List<CalendarDetailBean.DataBean.PicBean> picList = data.getPic();
                        if (picBeanList != null && picList.size() > 0) {
                            file_lay.setVisibility(View.VISIBLE);
                            picBeanList.clear();
                            picBeanList.addAll(picList);
                            picAdapter.notifyDataSetChanged();
                            pictures.clear();
                            for (int i = 0; i < picList.size(); i++) {
                                pictures.add(picList.get(i).getImgUrl());
                            }
                        } else {
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
                if (beizuEdt.isEmpty()) {
                    beizu_relay.setVisibility(View.GONE);
                    line_beizu.setVisibility(View.GONE);
                } else {
                    beizu_tv.setText("备注:" + beizuEdt);
                }

                if (addressEdt.isEmpty()) {
                    address_relay.setVisibility(View.GONE);
                    line_address.setVisibility(View.GONE);

                } else {
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
        if (dataBean.getError().equals(Const.success)) {
            refreshData();
            finish();
        }
    }

    //是否有看项目的权限
    @Override
    public void validateProjectShowResult(CommonBean1 databean) {
        if (databean.getError().equals(Const.success)) {
            if (databean.getIsShow().equals("true")) {
                IntentUtils.toActivityWithTag(ProjectDetailActivity.class, mActivity, mProjectId, 006);
            } else {
                ToastUtils.Toast_short(mActivity, "您没有查看项目的权力");
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
        if (requestCode == 123 && resultCode == 123) {
            reloadData();
        }
        if (requestCode == 101 && resultCode == 102) {
            reloadData();
        }
        if (requestCode == 006) {
            reloadData();
        }
        if (requestCode == 555) {
            reloadData();
        }
    }

    private void reloadData() {
        loadingDialog.show();
        detailPresenter.getCalendarDetail(userId, scheduleId);

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
        if (dataBean.getError().equals(Const.success)) {
            String name = dataBean.getData().getName();
            if (name != null) {
                project_relay.setVisibility(View.VISIBLE);
                line_project.setVisibility(View.VISIBLE);
                tempData.setProjectTitle(name);
                project_tv.setText(name);
            } else {
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

    @Override
    public void showEditData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            mLoadingDialog.dismiss();
            CommonAction.refreshCal();
            ToastUtils.showToast(mActivity, "修改成功", R.mipmap.checkmark);
            reloadData();
        }
    }


    @Override
    public void showApplyFrendData(ApplyBeFrendBean dataBean) {

    }

    @Override
    public void showCIData(CalInviteBean dataBean) {

    }

    @Override
    public void showFrendsApply(CommonBean dataBean) {

    }

    /**
     * 答复日程邀请
     *
     * @param dataBean
     */
    @Override
    public void showReplySchedule(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            if (status.equals("1")) {
                ToastUtils.showToast(mActivity, "成功接受邀请", R.mipmap.checkmark);
                CommonAction.refreshCal();
                setResult(101);
                finish();
            } else if (status.equals("2")) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                ToastUtils.showToast1(mActivity, "已拒绝并通知了邀请人");
                setResult(101);
                finish();
            }
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }
    //与该日程冲突的日程！
    @Override
    public void showValidateResult(ConflictCalBean databean) {
        if (databean.getError().equals(Const.success)){
            ArrayList<ConflictCalBean.DataBean> data = databean.getData();
            if (data!=null&&data.size()>0){
                conflictCalList.clear();
                conflictCalList.addAll(data);
                conflictAdapter.notifyDataSetChanged();
            }else {
                warn_lay.setVisibility(View.GONE);
            }
        }else {
            ToastUtils.Toast_short(databean.getMessage());
        }
    }

    private class PicAdapter extends CommonAdapter<CalendarDetailBean.DataBean.PicBean> {

        public PicAdapter(Context context, List<CalendarDetailBean.DataBean.PicBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final CalendarDetailBean.DataBean.PicBean item, final int position) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.dip2px(60), DensityUtil.dip2px(60));
            final ImageView only_iv = helper.getView(R.id.only_iv);
            ImageView close_iv = helper.getView(R.id.close_iv);
            only_iv.setLayoutParams(params);
            if (item.isDelete()) {
                close_iv.setVisibility(View.VISIBLE);
            } else {
                close_iv.setVisibility(View.GONE);
            }
            if (item.getImgUrl() != null) {
                Glide.with(mActivity).load(item.getImgUrl()).transform(new GlideRoundTransform(mContext, 5)).into(only_iv);
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
                    new PhotoPagerConfig.Builder(mActivity)
                            .setBigImageUrls(pictures)                //大图片url,可以是sd卡res，asset，网络图片.
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

    /* //自定义时间选择框
     new OptionsPickerView.Builder(this,new OptionsPickerView.OnOptionsSelectListener(){

     });*/
    private TimePickerView pvCustomTime;
    private TextView start_tv1;
    private TextView start_tv2;
    private TextView end_tv1;
    private TextView end_tv2;
    private View startselect_v;
    private View endselect_v;

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
                        final CheckBox end_checkbox = (CheckBox) v.findViewById(R.id.end_checkbox);
                        final CheckBox start_checkbox = (CheckBox) v.findViewById(R.id.start_checkbox);
                        end_checkbox.setClickable(false);
                        start_checkbox.setClickable(false);
                        checkBoxStatus(end_checkbox, start_checkbox, status);
                        testatus = status;
                        String beginTime = tempData.getBeginTime();
                        final Date starT1 = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
                        start_tv1.setText(DateFormatUtil.getTime(starT1, Const.Y_M_D));
                        start_tv2.setText(DateFormatUtil.getTime(starT1, Const.HM));
                        String endTime = tempData.getEndTime();
                        final Date enD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
                        end_tv1.setText(DateFormatUtil.getTime(enD, Const.Y_M_D));
                        end_tv2.setText(DateFormatUtil.getTime(enD, Const.HM));
                        start_lay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                testatus = "1";
                                checkBoxStatus(end_checkbox, start_checkbox, testatus);
                                String start_1=start_tv1.getText().toString()+"  "+start_tv2.getText().toString();
                                Date starD_1=DateFormatUtil.getTime(start_1,Const.YMD_HM);
                                selectedDate.setTimeInMillis(starD_1.getTime());
                                pvCustomTime.setDate(selectedDate);
                            }
                        });
                        end_lay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String start_1=start_tv1.getText().toString()+"  "+start_tv2.getText().toString();
                                Date starD_1=DateFormatUtil.getTime(start_1,Const.YMD_HM);
                                String end_1=end_tv1.getText().toString()+"  "+end_tv2.getText().toString();
                                Date enD_1=DateFormatUtil.getTime(end_1,Const.YMD_HM);
                                if (starD_1.getTime()<enD_1.getTime()){
                                    testatus = "2";
                                    checkBoxStatus(end_checkbox, start_checkbox, testatus);
                                    selectedDate.setTimeInMillis(enD_1.getTime());
                                    pvCustomTime.setDate(selectedDate);
                                }else {
                                    testatus = "2";
                                    checkBoxStatus(end_checkbox, start_checkbox, testatus);
                                    selectedDate.setTimeInMillis(starD_1.getTime()+3600000);
                                    end_tv1.setText(DateFormatUtil.getTime(starD_1.getTime()+3600000, Const.Y_M_D));
                                    end_tv2.setText(DateFormatUtil.getTime(starD_1.getTime()+3600000, Const.HM));
                                    pvCustomTime.setDate(selectedDate);
                                }
                            }
                        });
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String beginTime = start_tv1.getText().toString() + "  " + start_tv2.getText().toString();
                                String endTime = end_tv1.getText().toString() + "  " + end_tv2.getText().toString();
                                Date staD = DateFormatUtil.getTime(beginTime, Const.YMD_HM);
                                Date enD = DateFormatUtil.getTime(endTime, Const.YMD_HM);
                                if (staD.getTime() > enD.getTime()) {
                                    ToastUtils.Toast_short(mActivity, "开始时间不能大于结束时间");
                                } else {
                                    CalendarDetailBean.DataBean dataBean = new CalendarDetailBean.DataBean();
                                    dataBean.setBeginTime(beginTime);
                                    dataBean.setEndTime(endTime);
                                    dataBean.setStatus("3");
                                    dataBean.setScheduleId(scheduleId);
                                    dataBean.setUserId(CommonAction.getUserId());
                                    mLoadingDialog.show();
                                    editScheduleInfoPresenter.editScheduleInfo(new Gson().toJson(dataBean));
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
                        } else {
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
        if (status.equals("1")) {
            start_checkbox.setChecked(true);
            end_checkbox.setChecked(false);
            startselect_v.setBackgroundColor(getResources().getColor(R.color.blue_title));
            endselect_v.setBackgroundColor(getResources().getColor(R.color.white));

        } else {
            start_checkbox.setChecked(false);
            end_checkbox.setChecked(true);
            endselect_v.setBackgroundColor(getResources().getColor(R.color.blue_title));
            startselect_v.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    //填写拒绝弹框
    private void showInputReason(View v, final String id) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_inputenote, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.note_edit);
        TextView title_tv = (TextView) contentView.findViewById(R.id.title_tv);
        title_tv.setText("拒绝理由");
        editText.setHint("请填写拒绝理由(不得超过20个字)");
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel_tv:
                        mCustomPopWindow.dissmiss();
                        break;
                    case R.id.sure_tv:
                        String edit = editText.getText().toString().trim();
                        if (!TextUtils.isEmpty(edit)) {
                            if (edit.length() < 20) {
                                status = "2";
                                messagePresenter.replySchedule(id, "2", CommonAction.getUserId(), edit);
                            } else {
                                ToastUtils.Toast_short(mActivity, "字数不能超过20个字！");
                            }
                        } else {
                            ToastUtils.Toast_short(mActivity, "理由不能为空");
                        }
                        break;
                }
            }
        };

        contentView.findViewById(R.id.cancel_tv).setOnClickListener(listener);
        contentView.findViewById(R.id.sure_tv).setOnClickListener(listener);
        // 获取编辑框焦点
        editText.setFocusable(true);
        //打开软键盘
        InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .setAnimationStyle(R.anim.pickerview_slide_in_bottom)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(v, Gravity.BOTTOM, 0, 0);

    }
    //有时间冲突的日程列表
    public class ConflictAdapter extends CommonAdapter<ConflictCalBean.DataBean> {

        public ConflictAdapter(Context context, List<ConflictCalBean.DataBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, ConflictCalBean.DataBean item, int position) {
            helper.setText(R.id.title_tv,item.getTitle());
            String beginTime = item.getBeginTime();
            String endTime = item.getEndTime();
            Date endDate = DateFormatUtil.getTime(endTime+":00", Const.YMD_HMS);
            Date beginDate = DateFormatUtil.getTime(beginTime+":00", Const.YMD_HMS);
            Log.i("myTag",endDate.getTime()+","+beginDate);
            int dayExpend = CalculateTimeUtil.getDayExpend(beginDate.getTime(), endDate.getTime());
            if (dayExpend>=1){
                helper.setText(R.id.time_tv, beginTime.substring(5,beginTime.length())+"  —  "+ endTime.substring(5,endTime.length()));
            }else {
                helper.setText(R.id.time_tv, beginTime.substring(11,beginTime.length())+"  —  "+ endTime.substring(11,endTime.length()));
            }
        }
    }
}
