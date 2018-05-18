package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.ApplyBeFrendBean;
import com.dream.NiuFaNet.Bean.CalInviteBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.MessageLayBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.MessageContract;
import com.dream.NiuFaNet.CustomView.MessageChildView1;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.MessagePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.example.zhouwei.library.CustomPopWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hou on 2018/4/24.
 */

public class NewMessageActivity extends CommonActivity implements MessageContract.View{

    @Bind(R.id.message_lv)
    MyListView  message_lv;
    @Bind(R.id.back_relay)
    RelativeLayout back_relay;
    @Inject
    MessagePresenter messagePresenter;
    private MessageAdapter messageAdapter;
    private ArrayList<CalInviteBean.DataBean> calInvitelist=new ArrayList<>();
    private String status;
    private CustomPopWindow mCustomPopWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_newmessage;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        messagePresenter.attachView(this);
        messageAdapter=new MessageAdapter(mActivity,calInvitelist,R.layout.view_calinvite);
        message_lv.setAdapter(messageAdapter);

    }

    @Override
    public void initDatas() {
        messagePresenter.getCalInviteList(CommonAction.getUserId());
    }

    @Override
    public void eventListener() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showApplyFrendData(ApplyBeFrendBean dataBean) {

    }
    @OnClick({R.id.back_relay})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
        }
    }
    @Override
    public void showCIData(CalInviteBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<CalInviteBean.DataBean> data = dataBean.getData();
            if (data != null) {
                if (data.size()>0) {
                    calInvitelist.clear();
                    calInvitelist.addAll(dataBean.getData());
                    messageAdapter.notifyDataSetChanged();
                }else {
                    finish();
                }
            }else {
                finish();
            }
        }
    }

    @Override
    public void showFrendsApply(CommonBean dataBean) {

    }

    @Override
    public void showReplySchedule(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            if (status.equals("1")) {
                ToastUtils.showToast(mActivity, "成功接受邀请", R.mipmap.checkmark);
                messagePresenter.getCalInviteList(CommonAction.getUserId());
                CommonAction.refreshCal();
            }else if (status.equals("2")){
                if (mCustomPopWindow!=null) {
                    mCustomPopWindow.dissmiss();
                }
                ToastUtils.showToast1(mActivity, "已拒绝并通知了邀请人");
                messagePresenter.getCalInviteList(CommonAction.getUserId());
            }
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }


    public class MessageAdapter extends CommonAdapter<CalInviteBean.DataBean> {

        public MessageAdapter(Context context, List<CalInviteBean.DataBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final CalInviteBean.DataBean item, int position) {
            helper.setText(R.id.invitename_tv,item.getUserName()+" 邀请你加入新日程");
            helper.setText(R.id.invitecontent_tv,item.getTitle());
            //起始时间
            Date tempbd = DateFormatUtil.getTime(item.getBeginTime(), Const.YMD_HMS);
            String startTime = DateFormatUtil.getTime(tempbd, Const.MD);
            String startTime1 = DateFormatUtil.getTime(tempbd, Const.HM);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempbd);
            String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

            //结束时间
            Date tempnd = DateFormatUtil.getTime(item.getEndTime(), Const.YMD_HMS);
            String endDateStr = DateFormatUtil.getTime(tempnd, Const.MD);
            String endDateStr1 = DateFormatUtil.getTime(tempnd, Const.HM);
            calendar.setTime(tempnd);
            String weekDay1 = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            if (startTime.equals(endDateStr)){
                helper.setText(R.id.duringtime_tv,"时间："+startTime+" (周"+weekDay+") "+" "+startTime1+"-"+endDateStr1);
            }else {
                helper.setText(R.id.duringtime_tv,"时间："+startTime+" (周"+weekDay+") "+" "+startTime1+" - "+endDateStr+" (周"+weekDay1+") "+" "+endDateStr1);
            }
            helper.setText(R.id.invitetime_tv,item.getBeginTime());
            helper.setOnClickListener(R.id.reject_tv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    showInputReason(view,item.getId());
                }
            });
            helper.setOnClickListener(R.id.accept_tv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    status="1";
                    messagePresenter.replySchedule(item.getId(), "1", CommonAction.getUserId(),"");
                }
            });


        }
    }
    //填写备注弹框
    private void showInputReason(View v, final String id) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_inputenote,null);
        final EditText editText= (EditText) contentView.findViewById(R.id.note_edit);
        TextView title_tv= (TextView) contentView.findViewById(R.id.title_tv);
        title_tv.setText("拒绝理由");
        editText.setHint("请填写拒绝理由(不得超过20个字)");
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.cancel_tv:
                        mCustomPopWindow.dissmiss();
                        break;
                    case R.id.sure_tv:
                        String edit=editText.getText().toString().trim();
                        if (!TextUtils.isEmpty(edit)) {
                            if (edit.length()<20) {
                                status = "2";
                                messagePresenter.replySchedule(id, "2", CommonAction.getUserId(), edit);
                            }else {
                                ToastUtils.Toast_short(mActivity,"字数不能超过20个字！");
                            }
                        }else {
                            ToastUtils.Toast_short(mActivity,"理由不能为空");
                        }
                        break;
                }
                //Toast.makeText(HomeActivity.this,showContent,Toast.LENGTH_SHORT).show();
            }
        };

        contentView.findViewById(R.id.cancel_tv).setOnClickListener(listener);
        contentView.findViewById(R.id.sure_tv).setOnClickListener(listener);
        // 获取编辑框焦点
        editText.setFocusable(true);
        //打开软键盘
        InputMethodManager imm = (InputMethodManager)getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .setAnimationStyle(R.anim.pickerview_slide_in_bottom)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(v, Gravity.BOTTOM,0,0);
    }

}
