package com.dream.NiuFaNet.Ui.Activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseActivity;
import com.dream.NiuFaNet.Base.BaseActivityRelay;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.RegisterBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CodeContract;
import com.dream.NiuFaNet.Contract.RegisterContract;
import com.dream.NiuFaNet.CustomView.UView;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CodePresenter;
import com.dream.NiuFaNet.Presenter.RegisterPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CodeUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.dream.NiuFaNet.Utils.XuniKeyWord;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/4 0004.
 */
public class RegisterActivity extends CommonActivity implements RegisterContract.View,CodeContract.View{

    @Bind(R.id.yzcode_tv)
    TextView yzcode_tv;
    @Bind(R.id.phone_edt)
    EditText phone_edt;
    @Bind(R.id.yzcode_edt)
    EditText yzcode_edt;
    @Bind(R.id.pwd_edt1)
    EditText pwd_edt1;
    @Bind(R.id.pwd_edt2)
    EditText pwd_edt2;

    @Inject
    RegisterPresenter registerPresenter;
    @Inject
    CodePresenter codePresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register1;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        registerPresenter.attachView(this);
        codePresenter.attachView(this);

       // XuniKeyWord.initStateView(this).setBackgroundColor(ResourcesUtils.getColor(R.color.them_color1));

    }



    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }
    @OnClick({R.id.login_tv,R.id.yzcode_tv,R.id.register_tv,R.id.back_relay})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
            case R.id.login_tv:
                finish();
                break;
            case R.id.yzcode_tv:
                String phoneNum = phone_edt.getText().toString();
                if (phoneNum.isEmpty()){
                    ToastUtils.Toast_short("请输入手机号");
                }else {
                    codePresenter.getYzCode(phoneNum,"register");
                    //计时开始
                    new CodeUtils(60000, 1000) {
                        @Override
                        protected void onTicked(long millisUntilFinished) {
                            if (yzcode_tv!=null){
                                yzcode_tv.setClickable(false);
                                yzcode_tv.setEnabled(false);
                                yzcode_tv.setText(millisUntilFinished / 1000 + "S");
                            }
                        }

                        @Override
                        protected void onFinished() {
                            if (yzcode_tv!=null){
                                yzcode_tv.setText("重新发送");
                                yzcode_tv.setClickable(true);
                                yzcode_tv.setEnabled(true);
                            }
                        }
                    }.start();
                }
                break;
            case R.id.register_tv:
                String code = yzcode_edt.getText().toString();
                String phone = phone_edt.getText().toString();
                String pwd1 = pwd_edt1.getText().toString();
                String pwd2 = pwd_edt2.getText().toString();
                if (code.isEmpty()){
                    ToastUtils.Toast_short("请输入验证码");
                }else if (phone.isEmpty()){
                    ToastUtils.Toast_short("请输入手机号");
                }else if (pwd1.isEmpty()||pwd2.isEmpty()){
                    ToastUtils.Toast_short("请输入密码");
                }else if (!pwd1.equals(pwd2)){
                    ToastUtils.Toast_short("两次输入的密码不一致");
                }else {
                    registerPresenter.toRegister(phone,code,pwd2);
                }
                break;
        }

    }

    @Override
    public void showData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            ToastUtils.Toast_short("验证码已发送");

        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showRegisterData(RegisterBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            ToastUtils.Toast_short("注册成功");
            Intent intent = getIntent();
            intent.putExtra(Const.intentTag,dataBean.getBody().getMobilePhone());
            setResult(100,intent);
            finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
