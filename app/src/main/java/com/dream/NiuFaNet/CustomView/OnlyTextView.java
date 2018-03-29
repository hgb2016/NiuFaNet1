package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewLay;
import com.dream.NiuFaNet.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/8 0008.
 */
public class OnlyTextView extends BaseViewLay {
    private TextView only_tv;
    public OnlyTextView(Context context) {
        super(context);
        only_tv = (TextView) findViewById(R.id.only_tv);
    }

    public OnlyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        only_tv = (TextView) findViewById(R.id.only_tv);
    }

    @Override
    public int getLayoutId() {
        return R.layout.rvitem_onlytext;
    }

    public void setOnly_tv(String tvcontent){
        only_tv.setText(tvcontent);
    }

}
