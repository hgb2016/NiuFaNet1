package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Bean.TimeTipBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalendarDetailContract;
import com.dream.NiuFaNet.Contract.ScheduleRemindContract;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalendarDetailPresenter;
import com.dream.NiuFaNet.Presenter.ScheduleRemindPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 设置提醒时间
 * Created by hou on 2018/3/29.
 */

public class SetRemindActivity1 extends CommonActivity implements ScheduleRemindContract.View {
    @Bind(R.id.remindtime_rv)
    MyListView remindtime_rv;
    @Bind(R.id.notime_lay)
    LinearLayout notime_lay;
    @Bind(R.id.back)
    ImageView iv_back;
    @Bind(R.id.remindtip_tv)
    TextView remindtip_tv;
    @Bind(R.id.remind_iv1)
    ImageView remind_iv1;
    private RemindRvAdapter remindRvAdapter;
    private ArrayList<TimeTipBean> lists = new ArrayList();
    private String remind = "";
    private boolean isSelect;
    public static String[] tipStrs = new String[]{"准点提醒", "15分钟前", "30分钟前", "1小时前", "1天前","两天前"};
    public static int[] tipMinutes = new int[]{0, 15, 30, 60, 1440,2880};
    private CalendarDetailBean.DataBean origData;
    private List<CalendarDetailBean.DataBean.PicBean> picList;
    @Inject
    ScheduleRemindPresenter scheduleRemindPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setremind;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        scheduleRemindPresenter.attachView(this);
        Intent intent = getIntent();
        origData = (CalendarDetailBean.DataBean) getIntent().getExtras().getSerializable("data");
        if (origData != null) {
            List<CalendarDetailBean.DataBean.RemindBean> remind = origData.getRemind();
            for (int i = 0; i < tipStrs.length; i++) {
                TimeTipBean bean = new TimeTipBean();
                bean.setTimeStr(tipStrs[i]);
                bean.setMinute(tipMinutes[i]);
                lists.add(bean);
            }
            if (remind.get(0).getDescription().equals("不提醒")) {
                isSelect = true;
                remind_iv1.setImageResource(R.mipmap.check_green);
            } else {
                for (int i = 0; i < remind.size(); i++) {
                    for (int j = 0; j < lists.size(); j++) {
                        if (remind.get(i).getDescription().trim().equals(lists.get(j).getTimeStr().trim())) {
                            lists.get(j).setSelect(true);
                        }
                    }
                }
            }
        }
        remindRvAdapter = new RemindRvAdapter(mActivity, lists, R.layout.rvitem_remind);
        remindtime_rv.setAdapter(remindRvAdapter);

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }

    @OnClick({R.id.back, R.id.sure_tv, R.id.notime_lay})
    public void OnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure_tv:
                int count = 0;
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).isSelect()) {
                        count++;
                    }
                }
                if (count > 3) {
                    ToastUtils.Toast_short(mActivity, "最多只能选择3个时间段");
                } else {
                    StringBuffer buff = new StringBuffer();
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).isSelect()) {
                            buff.append(lists.get(i).getTimeStr());
                            buff.append("、");
                        }
                    }
                    Collections.sort(lists, new Comparator<TimeTipBean>() {
                        @Override
                        public int compare(TimeTipBean lhs, TimeTipBean rhs) {
                            if (lhs.getMinute() < rhs.getMinute()) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    if (buff.toString().length() != 0) {
                        List<CalendarDetailBean.DataBean.RemindBean> remindBeanList = new ArrayList<>();
                        for (int i = 0; i < lists.size(); i++) {
                            CalendarDetailBean.DataBean.RemindBean remindBean = new CalendarDetailBean.DataBean.RemindBean();
                            remindBean.setDescription(lists.get(i).getTimeStr());
                            if (lists.get(i).isSelect() && !lists.get(i).getTimeStr().equals("不提醒")) {
                                Calendar cal = Calendar.getInstance();
                                Date tempbd = DateFormatUtil.getTime(origData.getBeginTime(), Const.YMD_HMS);
                                cal.setTime(new Date(tempbd.getTime()));
                                cal.add(Calendar.MINUTE, -lists.get(i).getMinute());
                                long beforeTime = cal.getTime().getTime();
                                String time = DateFormatUtil.getTime(beforeTime, "yyyy-MM-dd HH:mm:ss");
                                remindBean.setRemindTime(time);
                                remindBeanList.add(remindBean);
                            }
                        }
                        Log.i("remindtime",new Gson().toJson(remindBeanList));
                        mLoadingDialog.show();
                        CalendarDetailBean.DataBean dataBean = new CalendarDetailBean.DataBean();
                        dataBean.setScheduleId(origData.getScheduleId());
                        dataBean.setUserId(origData.getUserId());
                        dataBean.setRemind(remindBeanList);
                        scheduleRemindPresenter.editScheduleRemind(new Gson().toJson(dataBean));
                        //finish();
                    } else {
                        List<CalendarDetailBean.DataBean.RemindBean> remindBeanList = new ArrayList<>();
                        CalendarDetailBean.DataBean.RemindBean remindBean = new CalendarDetailBean.DataBean.RemindBean();
                        remindBean.setDescription("不提醒");
                        remindBean.setRemindTime("1970-01-01 00:00:00");
                        remindBeanList.add(remindBean);
                        mLoadingDialog.show();
                        CalendarDetailBean.DataBean dataBean = new CalendarDetailBean.DataBean();
                        dataBean.setScheduleId(origData.getScheduleId());
                        dataBean.setUserId(origData.getUserId());
                        dataBean.setRemind(remindBeanList);
                        scheduleRemindPresenter.editScheduleRemind(new Gson().toJson(dataBean));
                    }
                }

                break;
            case R.id.notime_lay:
                if (!isSelect) {
                    isSelect = true;
                    remind_iv1.setImageResource(R.mipmap.check_green);
                    for (TimeTipBean r : lists) {
                        r.setSelect(false);
                    }
                    remindRvAdapter.notifyDataSetChanged();

                } else {
                    isSelect = false;
                    remind_iv1.setImageResource(R.mipmap.emptycheck_2);
                }
                break;
        }
    }

    @Override
    public void showError() {
        mLoadingDialog.dismiss();
        ToastUtils.Toast_short(mActivity, getResources().getString(R.string.failconnect));
    }

    private List<File> getFiles() {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < picList.size() - 1; i++) {
            File file = picList.get(i).getFile();
            if (file != null) {
                files.add(file);
            }
        }
        Log.e("tag", "files.size()=" + files.size());
        return files;
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showData(CalendarDetailBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            mLoadingDialog.dismiss();
            finish();
        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    public class RemindRvAdapter extends CommonAdapter<TimeTipBean> {

        public RemindRvAdapter(Context context, List<TimeTipBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(final BaseViewHolder holder, final TimeTipBean remind, final int position) {
            TextView remind_tv = holder.getView(R.id.remind_tv);
            final ImageView remind_iv = holder.getView(R.id.remind_iv);
            holder.setText(R.id.remind_tv, remind.getTimeStr());
            if (remind.isSelect()) {
                remind_tv.setTextColor(ResourcesUtils.getColor(R.color.blue_title));
                remind_iv.setImageResource(R.mipmap.check_green);
            } else {
                remind_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
                remind_iv.setImageResource(R.mipmap.emptycheck_2);
            }

                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isSelect) {

                        }else {
                            isSelect=false;
                            remind_iv1.setImageResource(R.mipmap.emptycheck_2);
                        }
                        remind.setSelect(!remind.isSelect());
                        notifyDataSetChanged();
                    }
                });

        }
    }

}
