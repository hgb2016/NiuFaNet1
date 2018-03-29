package com.dream.NiuFaNet.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewLay;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.SpUtils;

/**
 * Created by Administrator on 2017/8/3 0003.
 */
public class HeadService extends BaseViewLay {

    private RelativeLayout voice_relay;
    private TextView chat_contenttv;
    private ImageView right_voice;
    public HeadService(Context context) {
        super(context);
        initView();
    }

    public HeadService(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView(){
        chat_contenttv = (TextView) findViewById(R.id.chat_contenttv);

    }

    @Override
    public int getLayoutId() {
        return R.layout.view_headservice;
    }

}
