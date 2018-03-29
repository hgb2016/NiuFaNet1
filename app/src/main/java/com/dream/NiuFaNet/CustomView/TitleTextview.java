package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dream.NiuFaNet.Other.MyApplication;

/**
 * Created by Administrator on 2017/9/11 0011.
 */
public class TitleTextview extends TextView {

    public TitleTextview(Context context) {
        super(context);
        initView(context);
    }

    public TitleTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context){
        setTypeface(MyApplication.getTypeface_fzlth());

    }
}
