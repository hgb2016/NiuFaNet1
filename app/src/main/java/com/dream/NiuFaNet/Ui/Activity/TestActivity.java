package com.dream.NiuFaNet.Ui.Activity;

import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseActivityRelay;
import com.dream.NiuFaNet.R;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/9/18 0018.
 */
public class TestActivity extends BaseActivityRelay {

    @Bind(R.id.test_tv)
    TextView test_tv;
    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView() {

    }

    @Override
    public void refreshView() {

    }

    @Override
    public void initDatas() {

        String permission = getIntent().getStringExtra("permission");
        test_tv.setText(permission);

    }

    @Override
    public void eventListener() {

    }
}
