package com.dream.NiuFaNet.Ui.Activity;

import android.view.View;
import android.widget.EditText;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.BusBean.FinishBus;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ChangePWDContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ChangePWDPresenter;
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
 * Created by Administrator on 2017/8/7 0007.
 */
public class ChangePwdActivity extends CommonActivity implements ChangePWDContract.View{

    @Bind(R.id.oldpwd_edt)
    EditText oldpwd_edt;
    @Bind(R.id.newpwd_edt)
    EditText newpwd_edt;
    @Bind(R.id.againpwd_edt)
    EditText againpwd_edt;
    @Inject
    ChangePWDPresenter changePWDPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_changepwd;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        changePWDPresenter.attachView(this);
        EventBus.getDefault().register(this);
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
                String oldPwd = oldpwd_edt.getText().toString();
                String newPwd = newpwd_edt.getText().toString();
                String againPwd = againpwd_edt.getText().toString();
                /*if (oldPwd.isEmpty()){
                    ToastUtils.Toast_short("请输入原密码");
                }else*/ if (newPwd.isEmpty()||againPwd.isEmpty()){
                    ToastUtils.Toast_short("请输入新密码");
                }else if (!newPwd.equals(againPwd)){
                    ToastUtils.Toast_short("两次输入的密码不一致");
                }else {
                    changePWDPresenter.changePWD(CommonAction.getUserId(),oldPwd,newPwd);
                }
                break;
        }

    }

    @Override
    public void showData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            DialogUtils.actionResultDialog(mActivity,"修改密码成功!").show();
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
    public void mainEvent(FinishBus eventStr){
        if (eventStr.getEventStr().equals("finish")){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
