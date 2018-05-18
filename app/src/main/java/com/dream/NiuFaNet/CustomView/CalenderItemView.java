package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Bean.CalendarBean;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/11 0011.
 */
public class CalenderItemView extends LinearLayout {

    @Bind(R.id.gday_tv)
    TextView gday_tv;
    @Bind(R.id.nday_tv)
    TextView nday_tv;
    @Bind(R.id.week_tv)
    TextView week_tv;
    public CalenderItemView(Context context) {
        super(context);
        initView(context);
    }

    public CalenderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context){
        View inflate = inflate(context, R.layout.rvitem_maincalender, this);
        ButterKnife.bind(this,inflate);

    }
    public void setData(CalendarBean calendarBean){
        String s = calendarBean.getgDay();
        gday_tv.setText(s);
        week_tv.setText(calendarBean.getWeekDay());
        nday_tv.setText(calendarBean.getnDay());
    }
}
