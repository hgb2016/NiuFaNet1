package com.dream.NiuFaNet.Ui.Activity;

import android.view.View;
import android.webkit.WebView;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
public class AboutusActivity extends CommonActivity {

    @Bind(R.id.aboutus_web)
    WebView aboutus_web;

    @Override
    public int getLayoutId() {
        return R.layout.activity_aboutus;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initDatas() {
        aboutus_web.loadUrl(Const.aboutmeUrl);
    }

    @Override
    public void eventListener() {

    }
    @OnClick({R.id.back_relay})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
        }

    }
}
