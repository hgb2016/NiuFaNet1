package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Bean.ApplyBeFrendBean;
import com.dream.NiuFaNet.Bean.CalInviteBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.MessageContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.MessagePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/9 0009.
 */
public class MessageChildView extends LinearLayout {

    @Bind(R.id.username_tv)
    TextView username_tv;
    @Bind(R.id.typetip_tv)
    TextView typetip_tv;
    @Bind(R.id.content_tv)
    TextView content_tv;
    @Bind(R.id.duration_tv)
    TextView duration_tv;
    @Bind(R.id.refuse_tv)
    TextView refuse_tv;
    @Bind(R.id.agree_tv)
    TextView agree_tv;
    @Bind(R.id.reply_lay)
    LinearLayout reply_lay;
    @Bind(R.id.old_relay)
    RelativeLayout old_relay;

    private RemoveListener removeListener;
    interface RemoveListener{
        void onRemove(CalInviteBean.DataBean dataBean,String status);
        void onRemove(ApplyBeFrendBean.DataBean dataBean,String status);
    }
    public void setOnRemoveListener(RemoveListener removeListener){
        this.removeListener = removeListener;
    }
    private CalInviteBean.DataBean tempBean;
    private ApplyBeFrendBean.DataBean tempBeanFrend;
    private String type;
    public MessageChildView(Context context,String type) {
        super(context);
        this.type = type;
        init(context);
        intEvent();
    }

    private void intEvent() {
        refuse_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                replyResult("2");
            }
        });
        agree_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                replyResult("1");
            }
        });
    }

    private void replyResult(String status) {
        if (type.equals("frends")){
            removeListener.onRemove(tempBeanFrend,status);
            tempBeanFrend.setMethod(status);
            EventBus.getDefault().post(tempBeanFrend);
        }else {
            removeListener.onRemove(tempBean,status);
            tempBean.setMethod(status);
            EventBus.getDefault().post(tempBean);
        }
    }

    public void setData(ApplyBeFrendBean.DataBean dataBean) {
        tempBeanFrend = dataBean;
        username_tv.setText(dataBean.getFriendName());
        typetip_tv.setText("申请添加你为好友");
        content_tv.setVisibility(GONE);
        duration_tv.setVisibility(GONE);
    }
    public void setData(CalInviteBean.DataBean dataBean,int tag) {
        tempBean = dataBean;
        username_tv.setText(dataBean.getUserName());
        typetip_tv.setText("邀请你参加");
        content_tv.setText(dataBean.getTitle());
        String beginTime = dataBean.getBeginTime();
        String endTime = dataBean.getEndTime();
        Date endDate = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
        Date beginDate = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
        String beginStr1 = DateFormatUtil.getTime(beginDate, "MM.dd");
        String beginStr2 = DateFormatUtil.getTime(beginDate, " hh:mm");
        String endStr1 = DateFormatUtil.getTime(endDate, "MM.dd");
        String endStr2 = DateFormatUtil.getTime(endDate, " hh:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        String weekDay1 = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        calendar.setTime(endDate);
        String weekDay2 = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        duration_tv.setText(beginStr1+"周("+weekDay1+")"+beginStr2 +"~"+ endStr1+"周("+weekDay2+")"+endStr2);
        if (tag==1){
            reply_lay.setVisibility(GONE);
            old_relay.setVisibility(VISIBLE);
        }else {
            reply_lay.setVisibility(VISIBLE);
            old_relay.setVisibility(GONE);
        }
    }

    public MessageChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context){
        View view = inflate(context, R.layout.rvitem_rightmeesage, this);
        ButterKnife.bind(view,this);
    }

}
