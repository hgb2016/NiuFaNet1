package com.dream.NiuFaNet.Ui.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aitsuki.swipe.SwipeMenuRecyclerView;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalendarDetailContract;
import com.dream.NiuFaNet.Contract.NewCalenderContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalendarDetailPresenter;
import com.dream.NiuFaNet.Presenter.NewCalenderPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.AppManager;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/19 0019.
 */
public class ParticipantsActivity extends CommonActivity implements CalendarDetailContract.View,NewCalenderContract.View {
    @Bind(R.id.back_relay)
    RelativeLayout mBackRelay;
    @Bind(R.id.sure_iv)
    ImageView mSureIv;
    @Bind(R.id.participant_numtv)
    TextView mParticipantNumtv;
    @Bind(R.id.participant_rv)
    SwipeMenuRecyclerView mParticipantRv;
    @Bind(R.id.jia_iv)
    ImageView mJiaIv;
    @Bind(R.id.bot_tv)
    TextView mBotTv;
    @Bind(R.id.invite_relay)
    RelativeLayout mInviteRelay;

    @Inject
    CalendarDetailPresenter calendarDetailPresenter;

    @Inject
    NewCalenderPresenter newCalenderPresenter;

    private List<CalendarDetailBean.DataBean.participantBean> participantList = new ArrayList<>();
    private List<CalendarDetailBean.DataBean.participantBean> tempPart = new ArrayList<>();
    private List<CalendarDetailBean.DataBean.participantBean> origParts = new ArrayList<>();
    private Map<String, CalendarDetailBean.DataBean.participantBean> map = new HashMap<>();

    private PartsAdapter mParserAdapter;
    private String scheduleId;
    private CalendarDetailBean.DataBean tempData;
    private Dialog mLoadingDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_participants;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        calendarDetailPresenter.attachView(this);
        newCalenderPresenter.attachView(this);
        mLoadingDialog = DialogUtils.initLoadingDialog(this);
    }

    @Override
    public void initDatas() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tempData = (CalendarDetailBean.DataBean) extras.getSerializable("data");
            scheduleId = extras.getString("scheduleId");
            if (tempData != null) {
                participantList.clear();
                List<CalendarDetailBean.DataBean.participantBean> participant = tempData.getParticipant();
                if (participant != null && participant.size() > 0) {
                    participantList.addAll(participant);
                }
                for (int i = 0; i < participantList.size(); i++) {
                    participantList.get(i).setSouce(true);
                }
                mParticipantNumtv.setText("共" + participantList.size() + "人");
                origParts.addAll(participantList);
                mParserAdapter = new PartsAdapter(this, participantList, R.layout.rvitem_participants_deleteble);
                RvUtils.setOptionnoLine(mParticipantRv, this);
                mParticipantRv.setAdapter(mParserAdapter);
            }
        }
    }

    @Override
    public void eventListener() {

    }

    @OnClick({R.id.back_relay, R.id.sure_iv, R.id.invite_relay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_relay:
                setResult(102);
                finish();
                break;
            case R.id.sure_iv:
                if (tempData != null && scheduleId != null) {
                    tempData.setScheduleId(scheduleId);
                    tempPart.clear();
                    for (int i = 0; i < participantList.size(); i++) {
                        if (!participantList.get(i).isSouce()) {
                            tempPart.add(participantList.get(i));
                        }
                    }
                    if (tempPart.size()>0){
                        tempData.setParticipant(tempPart);
                        String data = new Gson().toJson(tempData);
                        Log.e("tag", "data=" + data);
                        mLoadingDialog.show();
                        calendarDetailPresenter.edtCalender(HttpUtils.getBody(data), HttpUtils.getRequestBodyParts("file", new ArrayList<File>()));
                    }
                }
                break;
            case R.id.invite_relay:
                IntentUtils.toActivityWithTag(MyFrendsActivity.class, mActivity, "newcal", 101);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 102 && data != null) {
            List<MyFrendBean.DataBean> selectdata = (List<MyFrendBean.DataBean>) data.getExtras().getSerializable("selectdata");
            if (selectdata != null && selectdata.size() > 0) {
                mSureIv.setVisibility(View.VISIBLE);
                participantList.clear();
                for (Map.Entry<String, CalendarDetailBean.DataBean.participantBean> mm:map.entrySet()){
                    if (!mm.getValue().isSouce()){
                        map.remove(mm);
                    }
                }
                //合并去重
                for (int i = 0; i < selectdata.size(); i++) {
                    MyFrendBean.DataBean dataBean = selectdata.get(i);
                    CalendarDetailBean.DataBean.participantBean bean = new CalendarDetailBean.DataBean.participantBean();
                    bean.setUserId(dataBean.getFriendId());
                    Log.e("tag", "userName=" + dataBean.getFriendName());
                    bean.setUserName(dataBean.getFriendName());
                    bean.setHeadUrl(dataBean.getHeadUrl());
                    bean.setSouce(false);
                    map.put(dataBean.getFriendId(), bean);
                }

                //把原来的参与人重新加上
                for (int i = 0; i < origParts.size(); i++) {
                    map.put(origParts.get(i).getUserId(), origParts.get(i));
                }

                for (Map.Entry<String, CalendarDetailBean.DataBean.participantBean> item : map.entrySet()) {
                    participantList.add(item.getValue());
                }
                mParticipantNumtv.setText("共" + participantList.size() + "人");
                mParserAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showData(CalendarDetailBean dataBean) {

    }

    @Override
    public void deletePicResult(CommonBean dataBean, int position) {

    }

    @Override
    public void edtCalendar(NewCalResultBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            setResult(102);
            finish();
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void deleteCalResult(CommonBean dataBean) {

    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showData(NewCalResultBean dataBean) {

    }

    @Override
    public void showWordData(List<InputGetBean> dataBean) {

    }

    @Override
    public void showDleteParcipant(CommonBean dataBean, int position) {
        if (dataBean.getError().equals(Const.success)){
            for (int i = 0; i <origParts.size() ; i++) {
                if (origParts.get(i).getUserId().equals(participantList.get(position).getUserId())){
                    origParts.remove(i);
                }
            }
            participantList.remove(position);
            mParserAdapter.notifyDataSetChanged();

        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }


    private class PartsAdapter extends RVBaseAdapter<CalendarDetailBean.DataBean.participantBean> {

        public PartsAdapter(Context context, List<CalendarDetailBean.DataBean.participantBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, final CalendarDetailBean.DataBean.participantBean baseBean, final int position) {
            ImageView only_iv = holder.getView(R.id.particepant_headiv);
            final TextView status_tv = holder.getView(R.id.status_tv);
            String headUrl = baseBean.getHeadUrl();
            if (headUrl != null && !headUrl.isEmpty()) {
                holder.setImageByUrl(R.id.particepant_headiv, headUrl, true);
            } else {
                only_iv.setImageResource(R.mipmap.niu);
            }
            holder.setOnClickListener(R.id.delete_tv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    String rightStr = status_tv.getText().toString();
                    if (!rightStr.equals("发起人")){
                        if (baseBean.isSouce()){
                            DialogUtils.showDeleteDialog(mContext, new NoDoubleClickListener() {
                                @Override
                                public void onNoDoubleClick(View view) {
                                    newCalenderPresenter.deleteParticipant(scheduleId,baseBean.getUserId(),position);
                                }
                            });
                        }else {
                            participantList.remove(position);
                            notifyDataSetChanged();
                        }
                    }else {
                        ToastUtils.Toast_short("不能删除发起人");
                    }
                }
            });
            holder.setText(R.id.username_tv, baseBean.getUserName());
            String status = baseBean.getStatus();
            if (tempData.getCreateUserId().equals(baseBean.getUserId())) {
                status_tv.setText("发起人");
            }
            if (status != null) {
                switch (status) {
                    case "0"://默认
                        status_tv.setText("待答复");
                        break;
                    case "1"://同意
                        status_tv.setText("参与");
                        break;
                    case "2"://拒绝
                        status_tv.setText("拒绝");
                        break;
                    default:
                        status_tv.setText("待邀请");
                        break;
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            setResult(102);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
