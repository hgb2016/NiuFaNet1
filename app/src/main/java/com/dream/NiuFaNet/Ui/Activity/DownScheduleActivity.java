package com.dream.NiuFaNet.Ui.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.DownScheduleContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.DownSchedulePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CheckForAllUtils;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.ImgUtil;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.StringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/2 0002.
 */
public class DownScheduleActivity extends CommonActivity implements DownScheduleContract.View {

    @Inject
    DownSchedulePresenter mDownSchedulePresenter;

    @Bind(R.id.back_relay)
    RelativeLayout mBackRelay;
    @Bind(R.id.titletv)
    TextView mTitletv;
    @Bind(R.id.title_relay)
    RelativeLayout mTitleRelay;
    @Bind(R.id.tip_title)
    TextView mTipTitle;
    @Bind(R.id.foot_view)
    RelativeLayout mFootView;
    @Bind(R.id.start_timetv)
    TextView mStartTimetv;
    @Bind(R.id.endtime_tv)
    TextView mEndtimeTv;
    @Bind(R.id.email_edt)
    EditText mEmailEdt;
    @Bind(R.id.tip_positive)
    TextView mTipPositive;

    private TimePickerView dateDialog;
    private int tempTag;

    @Override
    public int getLayoutId() {
        return R.layout.activity_downschedule;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        mDownSchedulePresenter.attachView(this);

        dateDialog = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                long time = date.getTime();
                String dateStr = DateFormatUtil.getTime(time, Const.Y_M_D);
                Log.e("tag", "dateStr=" + dateStr);
                if (dateStr != null) {
                    if (tempTag == 0) {
                        mStartTimetv.setText(dateStr);
                    } else {
                        mEndtimeTv.setText(dateStr);
                    }
                }
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setContentSize(16)//滚轮文字大小
                .setTitleSize(13)//标题文字大小
                .setCancelText("取消")//取消按钮文字
                .setLabel(" 年", "月", "日", "时", "分", "秒")
                .isCyclic(true)//是否循环滚动
                .setLineSpacingMultiplier(2.0f)
                .build();

    }

    @Override
    public void initDatas() {
        String proName = getIntent().getStringExtra("proName");
        if (StringUtil.NoNullOrEmpty(proName)){
            mTitletv.setText(proName);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String startTime = DateFormatUtil.getTime(calendar.getTimeInMillis(), Const.Y_M_D);
        mStartTimetv.setText(startTime);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endTime = DateFormatUtil.getTime(calendar.getTimeInMillis(), Const.Y_M_D);
        mEndtimeTv.setText(endTime);

        String userEmail = CommonAction.getUserEmail();
        mEmailEdt.setText(userEmail);


    }

    @Override
    public void eventListener() {

    }


    @Override
    public void showDownload(CommonBean dataBean) {
        ToastUtils.Toast_short("导出成功");
        finish();
    }

    @Override
    public void showError() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }


    @OnClick({R.id.start_timetv, R.id.endtime_tv, R.id.tip_positive,R.id.back_relay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.start_timetv:
                ImmUtils.hideImm(mActivity);
                dateDialog.show();

                tempTag = 0;
                break;
            case R.id.endtime_tv:
                ImmUtils.hideImm(mActivity);
                dateDialog.show();
                tempTag = 1;
                break;
            case R.id.tip_positive:
                String email = mEmailEdt.getText().toString();
                if (email.isEmpty()) {
                    ToastUtils.Toast_short("请输入邮箱地址");
                } else if (!CheckForAllUtils.isEmailAdd(email)){
                    ToastUtils.Toast_short("输入的邮箱格式不正确");
                } else {
                    Map<String, String> map = MapUtils.getMapInstance();
                    String projectId = getIntent().getStringExtra(Const.intentTag);
                    map.put("projectId", projectId);
                    map.put("emailUrl", email);
                    map.put("userId", CommonAction.getUserId());
                    String startTime = mStartTimetv.getText().toString();
                    String endTime = mEndtimeTv.getText().toString();
                    map.put("beginTime", startTime);
                    map.put("endTime", endTime);
                    mLoadingDialog.show();
                    mDownSchedulePresenter.exportProjectSchedule(map);

                }
                break;
        }
    }
}
