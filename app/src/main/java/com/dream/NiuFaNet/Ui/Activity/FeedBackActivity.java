package com.dream.NiuFaNet.Ui.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.dream.NiuFaNet.Base.BaseActivity;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.FeedBackContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.FeedBackPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.dream.NiuFaNet.Utils.XuniKeyWord;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
public class FeedBackActivity extends CommonActivity implements FeedBackContract.View{

    @Bind(R.id.content_edt)
    EditText content_edt;
    @Inject
    FeedBackPresenter feedBackPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        feedBackPresenter.attachView(this);
        XuniKeyWord.initStateView(this).setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }
    @OnClick({R.id.back_relay,R.id.submit_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
            case R.id.submit_tv:
                String s = content_edt.getText().toString();
                if (s.isEmpty()){
                    ToastUtils.Toast_short("请输入要反馈的的意见");
                }else {
                    Map<String,String> map = new HashMap<>();
                    String userId = CommonAction.getUserId();
                    map.put("feedback",s);
                    if (!userId.isEmpty()){
                        map.put("userId", userId);
                    }
                    feedBackPresenter.toFeedBack(map);
                }
                break;
        }

    }

    @Override
    public void showData(CommonBean dataBean) {
        String error = dataBean.getError();
        if (error.equals(Const.success)){
            ToastUtils.Toast_short("您的意见已提交");
            content_edt.setText("");
        }
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {

    }
}
