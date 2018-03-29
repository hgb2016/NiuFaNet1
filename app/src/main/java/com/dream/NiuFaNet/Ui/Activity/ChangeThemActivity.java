package com.dream.NiuFaNet.Ui.Activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseActivityRelay;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.XuniKeyWord;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
public class ChangeThemActivity extends BaseActivityRelay {
    @Bind(R.id.style_tv)
    TextView style_tv;
    @Bind(R.id.candy_rb)
    RadioButton candy_rb;
    @Bind(R.id.black_rb)
    RadioButton black_rb;
    @Override
    public int getLayoutId() {
        return R.layout.activity_changethem;
    }

    @Override
    public void initView() {
        CommonAction.setThem(this,root_lay);
        String them = CommonAction.getThem();
        if (them.equals(Const.Candy)){
            style_tv.setText("您正在使用糖果粉");
            candy_rb.setChecked(true);
        }else if (them.equals(Const.Black)){
            style_tv.setText("您正在使用钢琴黑");
            black_rb.setChecked(true);
        }
    }

    @Override
    public void refreshView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }
    @SuppressLint("NewApi")
    @OnClick({R.id.back_relay,R.id.candy_rb,R.id.black_rb})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
            case R.id.candy_rb:
                root_lay.setBackground(ResourcesUtils.getDrable(R.drawable.them_bg2));
                XuniKeyWord.initStateView(mActivity).setBackgroundColor(ResourcesUtils.getColor(R.color.them_color2));
                style_tv.setText("您正在使用糖果粉");
                SpUtils.setParam(Const.Them,Const.Candy);
                break;
            case R.id.black_rb:
                root_lay.setBackground(ResourcesUtils.getDrable(R.drawable.them_bg5));
                XuniKeyWord.initStateView(mActivity).setBackgroundColor(ResourcesUtils.getColor(R.color.them_color8));
                style_tv.setText("您正在使用钢琴黑");
                SpUtils.setParam(Const.Them,Const.Black);

                break;
        }

    }
}
