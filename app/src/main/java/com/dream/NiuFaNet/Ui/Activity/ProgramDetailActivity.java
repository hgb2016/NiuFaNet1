package com.dream.NiuFaNet.Ui.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ProgramDetailContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ProgramDetailPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.DateUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.PopWindowUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpannableStringUtil;
import com.dream.NiuFaNet.Utils.StringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/4 0004.
 */
public class ProgramDetailActivity extends CommonActivity implements ProgramDetailContract.View {

    @Inject
    ProgramDetailPresenter detailPresenter;

    @Bind(R.id.common_rv)
    RecyclerView common_rv;
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.duration_tv)
    TextView duration_tv;
    @Bind(R.id.beizu_tv)
    TextView beizu_tv;
    @Bind(R.id.titletv)
    TextView titletv;
    @Bind(R.id.beizu_edt)
    EditText beizu_edt;
    @Bind(R.id.duration_edt)
    EditText duration_edt;
    @Bind(R.id.title_edt)
    EditText title_edt;
    @Bind(R.id.title_relay)
    RelativeLayout title_relay;
    @Bind(R.id.beizu_relay)
    LinearLayout beizu_relay;
    @Bind(R.id.annum_relay)
    LinearLayout annum_relay;
    @Bind(R.id.line_beizu)
    View line_beizu;
    @Bind(R.id.line_annum)
    View line_annum;
    @Bind(R.id.particepant_gv)
    GridView particepant_gv;
    @Bind(R.id.doing_iv)
    ImageView doing_iv;
    @Bind(R.id.status_relay)
    RelativeLayout status_relay;
    private InputMethodManager imm;
    private Dialog projectDialog;
    private String projectId;
    private String projectTitle;

    private List<ProgramDetailBean.DataBean.participantBean> participantList = new ArrayList<>();
    private ParticipantAdapter participantAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_programdetail;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        detailPresenter.attachView(this);
        initTopPopwindow();
        imm = ImmUtils.getImm(mActivity);

        participantAdapter = new ParticipantAdapter(this, participantList, R.layout.gvitem_timg_btext);
        particepant_gv.setAdapter(participantAdapter);
    }

    @Override
    public void initDatas() {
        projectId = getIntent().getStringExtra(Const.intentTag);
        Log.e("tag", "projectId=" + projectId);
        if (projectId != null) {
            mLoadingDialog.show();
            detailPresenter.getProjectProgramDetail(projectId);
        } else {
            ToastUtils.Toast_short("数据异常");
            finish();
        }
    }

    @Override
    public void eventListener() {

    }

    private ProgramDetailBean.DataBean tempData;

    @Override
    public void showData(ProgramDetailBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            ProgramDetailBean.DataBean data = dataBean.getData();
            Log.e("tag", "data=" + new Gson().toJson(data));
            if (data != null) {
                tempData = data;
                String status = data.getStatus();
                if (status != null) {
                    if (status.equals("3")) {
                        ToastUtils.Toast_short("该项目已被删除");
                        CommonAction.refreshLogined();
                        finish();
                    } else {
                        tempData.setProjectId(projectId);
                        title_tv.setText(data.getName());
                        projectTitle = data.getName();
                        String caseNo = data.getCaseNo();
                        if (caseNo != null && !caseNo.isEmpty()) {
                            duration_tv.setText(caseNo);
                            duration_edt.setText(caseNo);
                            annum_relay.setVisibility(View.VISIBLE);
                            line_annum.setVisibility(View.VISIBLE);
                        } else {
                            annum_relay.setVisibility(View.GONE);
                            line_annum.setVisibility(View.GONE);
                        }

                        String description = data.getDescription();
                        if (description != null && !description.isEmpty()) {
                            SpannableStringUtil.setTelUrl(mActivity, description, beizu_tv);
//                    beizu_tv.setText(description);
                            beizu_edt.setText(description);
                            beizu_relay.setVisibility(View.VISIBLE);
                            line_beizu.setVisibility(View.VISIBLE);
                        } else {
                            beizu_relay.setVisibility(View.GONE);
                            line_beizu.setVisibility(View.GONE);
                        }
                        title_edt.setText(data.getName());
                        if (status != null && !status.isEmpty()) {
                            status_relay.setVisibility(View.VISIBLE);
                            if (status.equals("1")) {
                                doing_iv.setBackgroundResource(R.mipmap.end);
                            } else if (status.equals("0")) {
                                doing_iv.setBackgroundResource(R.mipmap.ing);
                            } else {
                                status_relay.setVisibility(View.GONE);
                            }
                        } else {
                            status_relay.setVisibility(View.GONE);
                        }

                        List<ProgramDetailBean.DataBean.scheduleBean> schedule = data.getSchedule();
                        if (schedule != null) {
                            RvUtils.setOptionnoLine(common_rv, mActivity);
                            common_rv.setAdapter(new ScheduleAdapter(this, schedule, R.layout.rvitem_calwork_pro));
                        }

                        participantList.clear();
                        ProgramDetailBean.DataBean.participantBean creatBean = new ProgramDetailBean.DataBean.participantBean();
                        creatBean.setUserId(data.getUserId());
                        creatBean.setUserName(data.getUserName());
                        creatBean.setHeadUrl(data.getHeadUrl());
                        participantList.add(creatBean);

                        List<ProgramDetailBean.DataBean.participantBean> participant = data.getParticipant();
                        if (participant != null && participant.size() > 0) {
                            participantList.addAll(participant);
                        }
                        participantAdapter.notifyDataSetChanged();
                    }
                }
            }

        }
    }


    private class ScheduleAdapter extends RVBaseAdapter<ProgramDetailBean.DataBean.scheduleBean> {

        public ScheduleAdapter(Context context, List<ProgramDetailBean.DataBean.scheduleBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder helper, final ProgramDetailBean.DataBean.scheduleBean dataBean, int position) {

            String fileCount = dataBean.getFileCount();
            if (StringUtil.NoNullOrEmpty(fileCount)&&!fileCount.equals("0")){
//                helper.getView(R.id.att_iv).setVisibility(View.VISIBLE);
            }else {
                helper.getView(R.id.att_iv).setVisibility(View.GONE);
            }

            ImageView dot_iv = helper.getView(R.id.dot_iv);
            TextView work_title = helper.getView(R.id.work_title);

            String beginTime = dataBean.getBeginTime();
            String endTime = dataBean.getEndTime();
            Date beginD = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
            Date endD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);

            String durationTime = DateUtil.getTimeDimen_onlyHour(beginD.getTime(), endD.getTime());

            String beginS = DateFormatUtil.getTime(beginD, Const.Y_M_D);
            helper.setText(R.id.time_duration, beginS + "   " + durationTime);
            work_title.setText(dataBean.getTitle());
            Calendar calendar = Calendar.getInstance();
            if (calendar.getTimeInMillis() > endD.getTime()) {
                dot_iv.setImageResource(R.drawable.shape_circle_dot);
                work_title.setTextColor(ResourcesUtils.getColor(R.color.color6c));
            } else {
                work_title.setTextColor(ResourcesUtils.getColor(R.color.color2a));
                dot_iv.setImageResource(R.drawable.shape_circle_voice);
            }
            helper.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    String scheduleId = dataBean.getScheduleId();
                    if (scheduleId != null && !scheduleId.isEmpty()) {
                        Intent intent = new Intent(mActivity, CalenderDetailActivity.class);
                        intent.putExtra(Const.scheduleId, scheduleId);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public void showEdtData(CommonBean dataBean) {
        ToastUtils.Toast_short(dataBean.getMessage());
        if (dataBean.getError().equals(Const.success)) {
            CommonAction.refreshPro();
            String title = title_edt.getText().toString();
            title_edt.setText(title);
            title_tv.setText(title);
            title_edt.setVisibility(View.GONE);
            title_tv.setVisibility(View.VISIBLE);
            String beizu = beizu_edt.getText().toString();
            if (beizu.isEmpty()) {
                beizu_relay.setVisibility(View.GONE);
                line_beizu.setVisibility(View.GONE);
            } else {
                beizu_edt.setText(beizu);
                beizu_edt.setVisibility(View.GONE);
                beizu_tv.setText(beizu);
                beizu_tv.setVisibility(View.VISIBLE);
            }
            String annum = duration_edt.getText().toString();
            if (annum.isEmpty()) {
                annum_relay.setVisibility(View.GONE);
                line_annum.setVisibility(View.GONE);
            } else {
                duration_edt.setText(annum);
                duration_edt.setVisibility(View.GONE);
                duration_tv.setText(annum);
                duration_tv.setVisibility(View.VISIBLE);

            }

        }
    }

    @Override
    public void showDeleteData(CommonBean dataBean) {
        ToastUtils.Toast_short(dataBean.getMessage());
        if (dataBean.getError().equals(Const.success)) {
            CommonAction.refreshPro();
            finish();
        }
    }

    @Override
    public void showDownload(CommonBean dataBean) {

    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @OnClick({R.id.back_relay, R.id.add_schedule, R.id.more_iv,R.id.downSchedule_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.add_schedule:
                if (CommonAction.getIsLogin()) {
                    if (projectId != null) {
                        Calendar calendar = Calendar.getInstance();
                        Intent intent = new Intent(mContext, NewCalenderActivity.class);
                        intent.putExtra("date", String.valueOf(calendar.getTimeInMillis()));
                        intent.putExtra("projectId", projectId);
                        intent.putExtra("projectTitle", projectTitle);
                        startActivityForResult(intent, 123);
                    }
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.more_iv:
                int screenWidth = DensityUtil.getScreenWidth(mActivity);
                PopWindowUtil.backgroundAlpaha(mActivity, 0.5f);
                morepop.showAsDropDown(title_relay, screenWidth - DensityUtil.dip2px(15), 0);
                break;
            case R.id.downSchedule_iv:
                if (StringUtil.NoNullOrEmpty(projectId)){
                    Intent intent = new Intent(mActivity,DownScheduleActivity.class);
                    intent.putExtra(Const.intentTag,projectId);
                    intent.putExtra("proName",projectTitle);
                    startActivity(intent);
                }
                break;
        }

    }

    private PopupWindow morepop,downPop;

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
                bundle.putSerializable("data", tempData);
                Intent intent = new Intent(mActivity, NewProgramActivity.class);
                intent.putExtra(Const.intentTag, "edit");
                intent.putExtras(bundle);
                startActivityForResult(intent, 123);
            }
        });
        //复制
        copy_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morepop.dismiss();
                Intent intent = new Intent(mContext, NewProgramActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) tempData);
                intent.putExtras(bundle);
                intent.putExtra(Const.intentTag, "copy");
                startActivity(intent);
                finish();
            }
        });
        //删除
        delete_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {

                DialogUtils.showDeleteDialog(mContext, new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        morepop.dismiss();
                        detailPresenter.deleteProject(projectId);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            detailPresenter.getProjectProgramDetail(projectId);
        }
    }

    private class ParticipantAdapter extends CommonAdapter<ProgramDetailBean.DataBean.participantBean> {

        public ParticipantAdapter(Context context, List<ProgramDetailBean.DataBean.participantBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, ProgramDetailBean.DataBean.participantBean item, int position) {
            ImageView only_iv = helper.getView(R.id.only_iv);
            TextView only_tv = helper.getView(R.id.only_tv);
            only_tv.setMaxLines(1);
            only_tv.setEllipsize(TextUtils.TruncateAt.END);
            only_tv.setText(item.getUserName());
            Log.e("tag", "userName=" + item.getUserName());
            String headUrl = item.getHeadUrl();
            if (!item.isEmpty()) {
                if (headUrl != null && !headUrl.isEmpty()) {
                    helper.setImageByUrl(R.id.only_iv, headUrl, true);
                } else {
                    only_iv.setImageResource(R.mipmap.niu);
                }
            }
        }
    }

    private TextView start_timetv;
    private TextView endtime_tv;
    private EditText email_edt;
}
