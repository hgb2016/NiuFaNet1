package com.dream.NiuFaNet.Base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/21 0021.
 */
public abstract class BaseViewRelay extends RelativeLayout {
    private View view;
    public BaseViewRelay(Context context) {
        super(context);
        initView(context);
    }

    public BaseViewRelay(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        view = inflate(context, getLayoutId(), this);
        ButterKnife.bind(this,view);
    }

    public abstract int getLayoutId();

}
