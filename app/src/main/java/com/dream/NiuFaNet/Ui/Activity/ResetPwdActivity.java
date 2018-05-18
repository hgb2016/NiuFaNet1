package com.dream.NiuFaNet.Ui.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.FindBackPWDContract;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.FindbackPwdPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/10 0010.
 */
public class ResetPwdActivity extends CommonActivity implements FindBackPWDContract.View{
    @Bind(R.id.pwd_edt1)
    EditText pwd_edt1;
    @Bind(R.id.pwd_edt2)
    EditText pwd_edt2;
    private String phone,code;

    @Inject
    FindbackPwdPresenter findbackPwdPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_resetpwd;
    }

    @Override
    public void initView() {

        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        findbackPwdPresenter.attachView(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initDatas() {
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
    }

    @Override
    public void eventListener() {

    }

    @OnClick({R.id.back_relay,R.id.sure_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
            case R.id.sure_tv:
                String pwd1 = pwd_edt1.getText().toString();
                String pwd2 = pwd_edt2.getText().toString();
                if (!pwd1.equals(pwd2)){
                    ToastUtils.Toast_short("两次输入的密码不一致");
                }else {
                    findbackPwdPresenter.findbackPwd(phone,code,pwd2);
                }
                break;
        }

    }

    @Override
    public void showData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            DialogUtils.actionResultDialog(mActivity,"重置密码成功!").show();
//            ToastUtils.Toast_short("密码重置成功");
            /*setResult(123);
            finish();*/
        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mainEvent(String eventStr){
        if (eventStr.equals("finish")){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
