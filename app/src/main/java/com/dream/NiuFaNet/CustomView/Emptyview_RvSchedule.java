package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewLay;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DateFormatUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;

/**
 * Created by Administrator on 2018/3/2 0002.
 */
public class Emptyview_RvSchedule extends BaseViewLay {
    @Bind(R.id.begint_tv)
    TextView mBegintTv;
    @Bind(R.id.begint1_tv)
    TextView mBegint1Tv;

    public Emptyview_RvSchedule(final Context context) {
        super(context);
    }

    public Emptyview_RvSchedule(Context context, AttributeSet attrs) {
        super(context, attrs);
        Calendar calendar = Calendar.getInstance();
        String time = DateFormatUtil.getTime(calendar.getTimeInMillis(), Const.YMD_HM);
        mBegintTv.setText(time);
        calendar.add(Calendar.HOUR,2);
        String time1 = DateFormatUtil.getTime(calendar.getTimeInMillis(), Const.YMD_HM);
        mBegint1Tv.setText(time1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_empty_rvschedule;
    }

    public void setData(Date data){
        String time = DateFormatUtil.getTime(data, Const.YMD_HM);
        mBegintTv.setText(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.add(Calendar.HOUR,2);
        String time1 = DateFormatUtil.getTime(calendar.getTimeInMillis(), Const.YMD_HM);
        mBegint1Tv.setText(time1);
    }
}
