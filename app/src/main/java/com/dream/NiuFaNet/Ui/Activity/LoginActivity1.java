package com.dream.NiuFaNet.Ui.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.LoginBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.LoginContract;
import com.dream.NiuFaNet.Contract.ThirdLoginContract;
import com.dream.NiuFaNet.CustomView.CircularAnim;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.LoginPresenter;
import com.dream.NiuFaNet.Presenter.ThirdLoginPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2018/4/3.
 */

public class LoginActivity1 extends CommonActivity implements ThirdLoginContract.View, LoginContract.View {
    @Bind(R.id.phone_edt)
    EditText phone_edt;
    @Bind(R.id.pwd_edt)
    EditText pwd_edt;
    @Inject
    ThirdLoginPresenter thirdLoginPresenter;
    @Bind(R.id.progressBar2)
    ProgressBar progressBar;
    @Bind(R.id.login_btn)
    Button login_btn;
    @Inject
    LoginPresenter loginPresenter;
    private int reSultCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login1;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        thirdLoginPresenter.attachView(this);
        loginPresenter.attachView(this);

    }

    @Override
    public void initDatas() {
        String phone = getIntent().getStringExtra(Const.intentTag);
        if (phone != null) {
            phone_edt.setText(phone);
        }
    }

    @OnClick({R.id.login_btn, R.id.findpwd_tv, R.id.register_tv, R.id.wechat_iv, R.id.weibo_iv, R.id.qq_iv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                SpUtils.setParam(Const.isBd, true);
                String phone = phone_edt.getText().toString();
                String password = pwd_edt.getText().toString();
                if (phone.isEmpty()) {
                    ToastUtils.Toast_short("请输入手机号");
                } else if (password.isEmpty()) {
                    ToastUtils.Toast_short("请输入密码");
                } else {
                    loginPresenter.toLogin(phone, password);
                    SpUtils.savaUserInfo(Const.thirdType, Const.bdUser);
                }
                break;
            case R.id.findpwd_tv:
                Intent intent1 = new Intent(mContext, FindPwdOrBindUserActivity.class);
                intent1.putExtra(Const.intentTag, "loginView");
                startActivity(intent1);
                break;
            case R.id.register_tv:
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.wechat_iv:
                thirdLoginPresenter.toAuthorize(Wechat.NAME);
                Log.e("ThirdLoginPresenter","toLogin");
                mLoadingDialog.show();
                SpUtils.savaUserInfo(Const.thirdType, "WeChat");
                reSultCode = 22;
                break;
            case R.id.weibo_iv:

                break;
            case R.id.qq_iv:
                thirdLoginPresenter.toAuthorize(QQ.NAME);
                DialogUtils.show(mActivity, mLoadingDialog);
                SpUtils.savaUserInfo(Const.thirdType, "QQ");
                reSultCode = 21;
                break;

        }
    }

    @Override
    public void eventListener() {

    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void authorizeComplete(String openID, String userIcon, String nickName, String unionId) {
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        if (reSultCode == 21) {
            loginPresenter.toLogin(openID, unionId, nickName, "3", userIcon);
        } else {
            loginPresenter.toLogin(openID, unionId, nickName, "2", userIcon);
        }
    }

    @Override
    public void showThirdLoginErro(Platform platform, int i, Throwable throwable) {
        if (i == Platform.ACTION_USER_INFOR) {
            //授权失败,重新授权
            Log.e("tag", "erro=" + throwable.toString());
            mLoadingDialog.dismiss();
            ToastUtils.Toast_short(mActivity, "授权失败,如微信登录，需要微信客户端");
           /* if (reSultCode==21){
                thirdLoginPresenter.toAuthorize(QQ.NAME);
            }else if (reSultCode==22){
                thirdLoginPresenter.toAuthorize(Wechat.NAME);
            }*/
        }
    }

    @Override
    public void showThirdLoginCancle(Platform platform, int i) {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showData(LoginBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            ToastUtils.Toast_short("登录成功");
            CommonAction.saveUserData(dataBean.getBody());
            JPushInterface.setAlias(mContext, 0, CommonAction.getUserId());
            CommonAction.refreshLogined();
            CommonAction.refreshContact();
            CommonAction.refreshCal();
          /*  CircularAnim.hide(login_btn)
                    .endRadius(progressBar.getHeight() / 2)
                    .go(new CircularAnim.OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd() {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    CircularAnim.fullActivity(LoginActivity1.this, progressBar)
                                            .go(new CircularAnim.OnAnimationEndListener() {
                                                @Override
                                                public void onAnimationEnd() {

                                                    finish();
                                                }
                                            });
                                }
                            }, 1000);
                        }
                    });*/
          finish();
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && reSultCode == 100 && data != null) {
            String phone = data.getStringExtra(Const.intentTag);
            if (phone != null) {
                phone_edt.setText(phone);
            }
        }

    }
}