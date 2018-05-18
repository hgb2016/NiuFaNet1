package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Bean.ApplyBeFrendBean;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.R;

import org.greenrobot.eventbus.EventBus;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/6 0006.
 */
public class ApplyFrendView extends LinearLayout {
    @Bind(R.id.username_tv)
    TextView username_tv;
    @Bind(R.id.agree_tv)
    TextView agree_tv;
    @Bind(R.id.refuse_tv)
    TextView refuse_tv;

    private ApplyBeFrendBean.DataBean tempData;
    public ApplyFrendView(Context context) {
        super(context);
        init(context);
        agree_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                tempData.setMethod("1");
                EventBus.getDefault().post(tempData);
            }
        });
        refuse_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                tempData.setMethod("2");
                EventBus.getDefault().post(tempData);
            }
        });
    }

    public ApplyFrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context){
        View messagelay = inflate(context, R.layout.view_messagelay, this);
        ButterKnife.bind(messagelay,this);
    }

    public void setData(ApplyBeFrendBean.DataBean data){
        tempData = data;
        username_tv.setText(data.getFriendName());
    }

}
