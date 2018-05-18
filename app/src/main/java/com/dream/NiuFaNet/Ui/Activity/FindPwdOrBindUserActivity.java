package com.dream.NiuFaNet.Ui.Activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.LoginBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.BindPhoneContract;
import com.dream.NiuFaNet.Contract.CodeContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.BindPhonePresenter;
import com.dream.NiuFaNet.Presenter.CodePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CodeUtils;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/8 0008.
 */
public class FindPwdOrBindUserActivity extends CommonActivity implements CodeContract.View,BindPhoneContract.View{
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.sure_tv)
    TextView sure_tv;
    @Bind(R.id.yzcode_tv)
    TextView yzcode_tv;
    @Bind(R.id.phone_edt)
    EditText phone_edt;
    @Bind(R.id.code_edt)
    EditText code_edt;
    private String intentTag;

    private boolean isCode;
    @Inject
    CodePresenter codePresenter;
    @Inject
    BindPhonePresenter bindPhonePresenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_findbackpwd;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        codePresenter.attachView(this);
        bindPhonePresenter.attachView(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initDatas() {
        intentTag = getIntent().getStringExtra(Const.intentTag);
        if (intentTag!=null&&intentTag.equals("loginView")){

        }else {
            title_tv.setText("绑定手机");
            sure_tv.setText("确认绑定");
        }

    }


    @Override
    public void eventListener() {

    }

    @OnClick({R.id.back_relay,R.id.sure_tv,R.id.yzcode_tv})
    public void onClick(View view){
        String phone = phone_edt.getText().toString();
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
            case R.id.sure_tv:
                String code = code_edt.getText().toString();
                if (intentTag!=null&&intentTag.equals("loginView")){
                    if (isCode){
                        if (code.isEmpty()){
                            ToastUtils.Toast_short("请输入验证码");
                        }else if (phone.isEmpty()){
                            ToastUtils.Toast_short("请输入手机号");
                        } else {

                            Intent intent = new Intent(mContext,ResetPwdActivity.class);
                            intent.putExtra("phone",phone);
                            intent.putExtra("code",code);
                            startActivity(intent);
                        }
                    }else {
                        ToastUtils.Toast_short("请先获取验证码");
                    }
                }else {

                    if (phone.isEmpty()){
                        ToastUtils.Toast_short("请输入手机号");
                    }else if (code.isEmpty()){
                        ToastUtils.Toast_short("请输入验证码");
                    }else {
                        bindPhonePresenter.bindPhone(phone,code, CommonAction.getUserId());
                    }
                }
                break;
            case R.id.yzcode_tv:
                if (!phone.isEmpty()){
                    codePresenter.getYzCode(phone,"findPassword");
                    new CodeUtils(60000, 1000) {
                        @Override
                        protected void onTicked(long l) {
                            if (yzcode_tv!=null){
                                yzcode_tv.setClickable(false);
                                yzcode_tv.setEnabled(false);
                                yzcode_tv.setText(l / 1000 + "S");
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
                }else {
                    ToastUtils.Toast_short("请输入手机号");
                }
                isCode = true;
                break;
        }

    }

    @Override
    public void showData(CommonBean dataBean) {
        Log.i("phonebind",new Gson().toJson(dataBean));
        if (dataBean.getError().equals(Const.success)){
            ToastUtils.Toast_short("验证码已发送");
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
            setResult(911);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showData(LoginBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            DialogUtils.actionResultDialog(mActivity,"绑定成功!").show();
            CommonAction.saveUserData(dataBean.getBody());
        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    /**
     * 发送验证码计时器
     *
     * @author wdp
     *
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发

            if (yzcode_tv!=null){
                yzcode_tv.setText("重新发送");
                yzcode_tv.setClickable(true);
                yzcode_tv.setEnabled(true);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示

            if (yzcode_tv!=null){
                yzcode_tv.setClickable(false);
                yzcode_tv.setEnabled(false);
                yzcode_tv.setText(millisUntilFinished / 1000 + "S");
            }
        }
    }
}
