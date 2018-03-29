package com.dream.NiuFaNet.Base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/21 0021.
 */
public abstract class BaseViewLay extends LinearLayout {
    private View view;
    public BaseViewLay(Context context) {
        super(context);
        initView(context);
    }

    public BaseViewLay(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        view = inflate(context, getLayoutId(), this);
        ButterKnife.bind(this,view);
    }

    public abstract int getLayoutId();

}
