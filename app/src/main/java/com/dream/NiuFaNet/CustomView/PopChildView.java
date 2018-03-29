package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.dream.NiuFaNet.Bean.ApplyBeFrendBean;
import com.dream.NiuFaNet.Bean.CalInviteBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.MessageLayBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.MessageContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.MessagePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/11 0011.
 */
public class PopChildView extends LinearLayout implements MessageContract.View{
    @Bind(R.id.message_lay)
    LinearLayout message_lay;
    @Inject
    MessagePresenter messagePresenter;

    private int tag;
    public PopChildView(Context context,int tag) {
        super(context);
        init(context);
        initData();
        this.tag = tag;
    }

    private void initData() {
        messagePresenter.applyBeFrend(CommonAction.getUserId());
        messagePresenter.getCalInviteList(CommonAction.getUserId());
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.fragmentmessagereceive, this);
        ButterKnife.bind(rootView,this);
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        messagePresenter.attachView(this);
    }

    public PopChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private String itemId;
/*    //答复日程邀请
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent2(CalInviteBean.DataBean busBean) {
        if (busBean.getEventStr().equals("reply")){
            itemId = busBean.getId();
            Log.e("tag","itemId="+itemId);
            messagePresenter.replySchedule(busBean.getId(), busBean.getMethod(),CommonAction.getUserId());
        }
    }
    //答复好友邀请
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent2(ApplyBeFrendBean.DataBean busBean) {
        if (busBean.getEventStr().equals("reply")){
            itemId = busBean.getId();
            Log.e("tag","itemId="+itemId);
            messagePresenter.replyFrendsApply(busBean.getId(),busBean.getMethod());
        }
    }*/
    @Override
    public void showApplyFrendData(ApplyBeFrendBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            List<ApplyBeFrendBean.DataBean> data = dataBean.getData();
            if (data!=null){
                for (int i = 0; i < data.size(); i++) {
                    MessageChildView childView = new MessageChildView(getContext(),"frends");
                    childView.setData(data.get(i));
                    MessageLayBean messageLayBean = new MessageLayBean();
                    messageLayBean.setId(data.get(i).getId());
                    messageLayBean.setType("frends");
                    messageLays.add(messageLayBean);
                    childView.setOnRemoveListener(new MessageChildView.RemoveListener() {
                        @Override
                        public void onRemove(CalInviteBean.DataBean dataBean, String status) {
//                            itemId = dataBean.getId();
//                            messagePresenter.replySchedule(dataBean.getId(), status,CommonAction.getUserId());
                        }

                        @Override
                        public void onRemove(ApplyBeFrendBean.DataBean dataBean, String status) {
                            itemId = dataBean.getId();
                            messagePresenter.replyFrendsApply(dataBean.getId(),status);
                        }
                    });
                    if (tag==0){
                        message_lay.addView(childView);
                    }
                }
            }
        }
    }

    private List<CalInviteBean.DataBean> tempdataCal1 = new ArrayList<>();
    private List<CalInviteBean.DataBean> tempdataCal = new ArrayList<>();
    private List<MessageLayBean> messageLays = new ArrayList<>();

    @Override
    public void showCIData(CalInviteBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            List<CalInviteBean.DataBean> data = dataBean.getData();
            if (data!=null){
                for (int i = 0; i < data.size(); i++) {
                    String beginTime = data.get(i).getBeginTime();
                    Date beginDate = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
                    Calendar calendar = Calendar.getInstance();
                    if (beginDate.getTime()>=calendar.getTimeInMillis()){
                        tempdataCal.add(data.get(i));
                    }else {
                        tempdataCal1.add(data.get(i));
                    }
                }
                if (tag==0){
                    for (int i = 0; i <tempdataCal.size() ; i++) {
                        MessageChildView childView = new MessageChildView(getContext(),"schedule");
                        childView.setData(data.get(i),tag);
                        message_lay.addView(childView);
                        MessageLayBean messageLayBean = new MessageLayBean();
                        messageLayBean.setId(data.get(i).getId());
                        messageLayBean.setType("schedule");
                        messageLays.add(messageLayBean);
                        childView.setOnRemoveListener(new MessageChildView.RemoveListener() {
                            @Override
                            public void onRemove(CalInviteBean.DataBean dataBean, String status) {
                                itemId = dataBean.getId();
                                messagePresenter.replySchedule(dataBean.getId(), status,CommonAction.getUserId());
                            }

                            @Override
                            public void onRemove(ApplyBeFrendBean.DataBean dataBean, String status) {
                                itemId = dataBean.getId();
                                messagePresenter.replyFrendsApply(dataBean.getId(),status);
                            }
                        });
                    }
                }else {
                    for (int i = 0; i <tempdataCal1.size() ; i++) {
                        MessageChildView childView = new MessageChildView(getContext(),"schedule");
                        childView.setData(data.get(i),tag);
                        message_lay.addView(childView);
                        MessageLayBean messageLayBean = new MessageLayBean();
                        messageLayBean.setId(data.get(i).getId());
                        messageLayBean.setType("schedule");
                        messageLays.add(messageLayBean);
                        childView.setOnRemoveListener(new MessageChildView.RemoveListener() {
                            @Override
                            public void onRemove(CalInviteBean.DataBean dataBean, String status) {
                                itemId = dataBean.getId();
                                messagePresenter.replySchedule(dataBean.getId(), status,CommonAction.getUserId());
                            }

                            @Override
                            public void onRemove(ApplyBeFrendBean.DataBean dataBean, String status) {
                                itemId = dataBean.getId();
                                messagePresenter.replyFrendsApply(dataBean.getId(),status);
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void showFrendsApply(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            if (!itemId.isEmpty()){
                for (int i = 0; i <messageLays.size() ; i++) {
                    if (messageLays.get(i).getType().equals("frends")){
                        if (messageLays.get(i).getId().equals(itemId)){
                            message_lay.removeViewAt(i);
                            messageLays.remove(i);
                        }
                    }
                }
            }
        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showReplySchedule(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            if (!itemId.isEmpty()){
                for (int i = 0; i <messageLays.size() ; i++) {
                    if (messageLays.get(i).getType().equals("schedule")){
                        if (messageLays.get(i).getId().equals(itemId)){
                            message_lay.removeViewAt(i);
                            messageLays.remove(i);
                        }
                    }
                }
            }
        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }
}
