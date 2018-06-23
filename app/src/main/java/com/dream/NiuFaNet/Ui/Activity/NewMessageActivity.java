package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.ApplyBeFrendBean;
import com.dream.NiuFaNet.Bean.CalInviteBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.ConflictCalBean;
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
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.NetUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.example.zhouwei.library.CustomPopWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public void showValidateResult(ConflictCalBean databean) {

    }


    public class MessageAdapter extends CommonAdapter<CalInviteBean.DataBean> {

        public MessageAdapter(Context context, List<CalInviteBean.DataBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final CalInviteBean.DataBean item, int position) {
            helper.setText(R.id.invitename_tv,item.getUserName()+" 邀请你加入日程");
            helper.setText(R.id.invitecontent_tv,item.getTitle());
            helper.setImageByUrl(R.id.head_iv,item.getHeadUrl(),true);
            TextView starttime_tv1=helper.getView(R.id.starttime_tv1);
            TextView starttime_tv2=helper.getView(R.id.starttime_tv2);
            TextView endtime_tv1=helper.getView(R.id.endtime_tv1);
            TextView endtime_tv2=helper.getView(R.id.endtime_tv2);
            TextView address_tv=helper.getView(R.id.address_tv);
            TextView outofdate_tv=helper.getView(R.id.outofdate_tv);
            LinearLayout address_lay=helper.getView(R.id.address_lay);
            LinearLayout warn_lay=helper.getView(R.id.warn_lay);
            //起始时间
            final Date tempbd = DateFormatUtil.getTime(item.getBeginTime(), Const.YMD_HMS);
            String startTime = DateFormatUtil.getTime(tempbd, Const.MR);
            String startTime1 = DateFormatUtil.getTime(tempbd, Const.HM);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tempbd);
            String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            starttime_tv1.setText(startTime1);
            starttime_tv2.setText(startTime+" (周"+weekDay+")");
            //结束时间
            Date tempnd = DateFormatUtil.getTime(item.getEndTime(), Const.YMD_HMS);
            String endDateStr = DateFormatUtil.getTime(tempnd, Const.MR);
            String endDateStr1 = DateFormatUtil.getTime(tempnd, Const.HM);
            calendar.setTime(tempnd);
            String weekDay1 = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            endtime_tv1.setText(endDateStr1);
            endtime_tv2.setText(endDateStr+" (周"+weekDay1+")");
            String address=item.getAddress();
            if (address!= null&&!address.isEmpty()){
                address_lay.setVisibility(View.VISIBLE);
                address_tv.setText("地址："+item.getAddress());
            }else {
                address_lay.setVisibility(View.GONE);
            }
            Calendar instance = Calendar.getInstance();
            if (instance.getTimeInMillis()>tempbd.getTime()){
                outofdate_tv.setVisibility(View.VISIBLE);
            }else {
                outofdate_tv.setVisibility(View.GONE);
            }
            if (item.isConflict()){
                warn_lay.setVisibility(View.VISIBLE);
            }else {
                warn_lay.setVisibility(View.GONE);
            }
            warn_lay.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    String scheduleId = item.getId();
                    if (scheduleId!=null&&!scheduleId.isEmpty()){
                        Intent intent = new Intent(mContext, CalenderDetailActivity.class);
                        intent.putExtra(Const.scheduleId, scheduleId);
                        intent.putExtra("inviteCal","inviteCal");
                        intent.putExtra(Const.userId, CommonAction.getUserId());
                        startActivityForResult(intent,100);
                    }
                }
            });
            helper.setText(R.id.duringtime_tv,CalculateTimeUtil.getTimeExpend(tempbd.getTime(),tempnd.getTime()));
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
            helper.setOnClickListener(R.id.invitecontent_tv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    String scheduleId = item.getId();
                    if (scheduleId!=null&&!scheduleId.isEmpty()){
                        Intent intent = new Intent(mContext, CalenderDetailActivity.class);
                        intent.putExtra(Const.scheduleId, scheduleId);
                        intent.putExtra("inviteCal","inviteCal");
                        intent.putExtra(Const.userId, CommonAction.getUserId());
                       startActivityForResult(intent,100);
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==101){
            messagePresenter.getCalInviteList(CommonAction.getUserId());
        }
    }

    //填写拒绝弹框
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
