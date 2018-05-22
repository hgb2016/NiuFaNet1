package com.dream.NiuFaNet.Ui.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.VersionBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.VersionUpdateContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.VersionUpdatePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.GlideCircleTransform;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.SpUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MineActivity1 extends CommonActivity implements VersionUpdateContract.View{
    @Bind(R.id.head_icon)
    ImageView head_icon;
    @Bind(R.id.username_tv)
    TextView mUsernameTv;
    //暂时展示电话号
    @Bind(R.id.desc_tv)
    TextView phone_tv;
    @Bind(R.id.nologin_lay)
    LinearLayout nologin_lay;
    @Bind(R.id.login_tv)
    TextView login_tv;
    @Bind(R.id.register_tv)
    TextView register_tv;
    @Inject
    VersionUpdatePresenter versionUpdatePresenter;
    private Dialog loginDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityMcodule(this))
                .build()
                .inject(this);
        versionUpdatePresenter.attachView(this);
        loginDialog = DialogUtils.getLoginTip(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLogin();
    }

    @Override
    public void initDatas() {
        refreshLogin();
    }
    private void refreshLogin() {
        boolean isLogin = (boolean) SpUtils.getParam(Const.isLogin, false);
        if (isLogin) {
            toLogin();
        } else {
            noLogin();
        }
    }
    private void toLogin() {
        nologin_lay.setVisibility(View.GONE);
        mUsernameTv.setVisibility(View.VISIBLE);
        phone_tv.setVisibility(View.VISIBLE);
        String thirdType = (String) SpUtils.getParam(Const.thirdType, Const.bdUser);
        String itUserIcon = (String) SpUtils.getParam(Const.userHeadUrl, "");
        String phone = (String) SpUtils.getParam(Const.userPhone, "");
        String userName = (String) SpUtils.getParam(Const.userName, "");
        loginSet(itUserIcon, phone, userName);
       /* if (thirdType.equals("bduser")) {

        } else {
            String tdUserIcon = (String) SpUtils.getParam(Const.userHeadUrl, "");
            String tdUserName = (String) SpUtils.getParam(Const.userName, "");
            loginSet(tdUserIcon, "", tdUserName);
        }*/

    }
    private void noLogin() {
        head_icon.setImageResource(R.mipmap.niu);
        nologin_lay.setVisibility(View.VISIBLE);
        mUsernameTv.setVisibility(View.GONE);
        phone_tv.setVisibility(View.GONE);
        phone_tv.setText("");
        mUsernameTv.setText("");
        SpUtils.savaUserInfo(Const.isLogin, false);
    }
    private void loginSet(String headUrl, String phone, String userName) {
        if (phone.isEmpty()) {
            phone_tv.setVisibility(View.GONE);
        } else {
            phone_tv.setVisibility(View.VISIBLE);
            phone_tv.setText(phone);
        }
        phone_tv.setText(phone);
        if (userName.isEmpty()) {
            mUsernameTv.setVisibility(View.GONE);
        } else {
            mUsernameTv.setVisibility(View.VISIBLE);
            mUsernameTv.setText(userName);
        }

        if (!headUrl.isEmpty()) {
            Glide.with(this).load(headUrl).transform(new GlideCircleTransform(mActivity)).into(head_icon);
        }
        SpUtils.savaUserInfo(Const.isLogin, true);
    }
    @Override
    public void eventListener() {

    }
    @OnClick({R.id.back_relay,R.id.client_lay,R.id.whocansee_lay,R.id.share_lay,R.id.feedback_lay,R.id.aboutus_lay,R.id.contactus_lay,R.id.setting_relay,R.id.myinfo_relay,R.id.register_tv,R.id.login_tv})
    public void OnClick(View v){
        switch (v.getId()){
            //我的个人信息
            case R.id.myinfo_relay:
                if (CommonAction.getIsLogin()) {
                    IntentUtils.toActivity_result(UserInfoActivity1.class,mActivity,28);
                } else {
                    IntentUtils.toActivity_result(LoginActivity1.class,mActivity,21);
                }
                //startActivity(new Intent(getApplicationContext(),UserInfoActivity1.class));
                break;
            //返回
            case R.id.back_relay:
                finish();
                break;
            //我的客户
            case R.id.client_lay:
                IntentUtils.toActivityWithTag(ClientsActivity.class,mActivity,"client");
               // startActivity(new Intent(mContext, ClientsActivity.class));
                break;
            //谁可以看我的工作
            case R.id.whocansee_lay:
                if (CommonAction.getIsLogin()){
                    IntentUtils.toActivity(WorkVisibleFrendsActivity.class, mActivity);
                }else {
                    loginDialog.show();
                }
                break;
            //分享
            case R.id.share_lay:
                DialogUtils.showShareDialog(mActivity);
                break;
            //意见反馈
            case R.id.feedback_lay:
                startActivity(new Intent(getApplicationContext(),FeedBackActivity.class));
                break;
            //关于我们
            case R.id.aboutus_lay:
                startActivity(new Intent(getApplicationContext(),AboutusActivity.class));
                break;
            //联系我们
            case R.id.contactus_lay:
                startActivity(new Intent(mActivity,ContactusActivity.class));
                break;
            //设置
            case R.id.setting_relay:
                startActivityForResult(new Intent(mActivity, SettingActivity.class), 103);
                break;
            //去登陆
            case R.id.login_tv:
                startActivity(new Intent(getApplicationContext(),LoginActivity1.class));
                break;
            //去注册
            case R.id.register_tv:
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                break;
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showData(VersionBean dataBean) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 103) {
            noLogin();
        }
    }
}
