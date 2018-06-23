package com.dream.NiuFaNet.Ui.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Base.BaseActivity;
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
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/12/4 0004.
 */
public class MineActivity extends BaseActivity implements VersionUpdateContract.View {

    @Bind(R.id.head_icon)
    ImageView head_icon;
    @Bind(R.id.phone_tv)
    TextView phone_tv;
    @Bind(R.id.username_tv)
    TextView mUsernameTv;

    @Inject
    VersionUpdatePresenter versionUpdatePresenter;
    @Bind(R.id.back_relay)
    RelativeLayout mBackRelay;
    @Bind(R.id.userinfo_lay)
    LinearLayout mUserinfoLay;
    @Bind(R.id.myfriends_lay)
    LinearLayout mMyfriendsLay;
    @Bind(R.id.addfriend_lay)
    LinearLayout mAddfriendLay;
    @Bind(R.id.jobvi_lay)
    LinearLayout mJobviLay;
    @Bind(R.id.sharefriend_lay)
    LinearLayout mSharefriendLay;
    @Bind(R.id.feedback_lay)
    LinearLayout mFeedbackLay;
    @Bind(R.id.aboutus_lay)
    LinearLayout mAboutusLay;
    @Bind(R.id.update_lay)
    LinearLayout mUpdateLay;
    @Bind(R.id.set_lay)
    LinearLayout mSetLay;

    private Dialog loginDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        versionUpdatePresenter.attachView(this);
        loginDialog = DialogUtils.getLoginTip(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void loadResum() {
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

    @Override
    public void eventListener() {

    }

    private void noLogin() {
        head_icon.setImageResource(R.mipmap.niu);
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

    @OnClick({R.id.head_icon, R.id.userinfo_lay, R.id.feedback_lay, R.id.aboutus_lay, R.id.set_lay, R.id.back_relay, R.id.myfriends_lay, R.id.addfriend_lay, R.id.jobvi_lay, R.id.sharefriend_lay, R.id.update_lay})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_relay:
//                ReturnUtil.returnMain();
                finish();
                break;
            case R.id.head_icon:
                if (CommonAction.getIsLogin()) {
                    IntentUtils.toActivity_result(UserInfoActivity.class,mActivity,28);
                } else {
                    IntentUtils.toActivity_result(LoginActivity1.class,mActivity,21);
                }
                break;
            case R.id.userinfo_lay:
                if (CommonAction.getIsLogin()) {
                    IntentUtils.toActivity_result(UserInfoActivity.class,mActivity,28);
                } else {
                    IntentUtils.toActivity_result(LoginActivity1.class,mActivity,21);
                }
                break;
            case R.id.feedback_lay:
                intent = new Intent(mActivity, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutus_lay:
                intent = new Intent(mActivity, AboutusActivity.class);
                startActivity(intent);
                break;
            case R.id.set_lay:
                intent = new Intent(mActivity, SetActivity.class);
                startActivityForResult(intent, 103);
                break;
            case R.id.myfriends_lay:
                if (CommonAction.getIsLogin()){
                    IntentUtils.toActivity(MyFrendsActivity.class, mActivity);
                }else {
                    loginDialog.show();
                }
                break;
            case R.id.addfriend_lay:
                IntentUtils.toActivity(AddFrendsActivity.class, mActivity);
                break;
            case R.id.jobvi_lay:
                if (CommonAction.getIsLogin()){
                    IntentUtils.toActivity(WorkVisibleFrendsActivity.class, mActivity);
                }else {
                    loginDialog.show();
                }
                break;
            case R.id.sharefriend_lay:
                DialogUtils.showShareDialog(mActivity);
                break;
            case R.id.update_lay:
                versionUpdatePresenter.requestVersion("1", "");
                break;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 103) {
            noLogin();
        }
    }

    private void toLogin() {
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

    @Override
    public void showData(VersionBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            String url = dataBean.getUrl();
            String version = dataBean.getVersion();
            String versionRemark=dataBean.getVersionRemark();
            Log.e("tag", "apkUrl=" + url);
            Log.e("tag", "version=" + version);
            if (url != null && !url.isEmpty() && version != null && !version.isEmpty()) {//对url和version进行判断
                PackageInfo packageInfo = null;
                try {
                    packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
                    final String localVersionName = packageInfo.versionName;
                    Log.e("tag", "localVersionName=" + localVersionName);
                    int localVersionCode = packageInfo.versionCode;
                    String[] currentNames = localVersionName.split("\\.");
                    String[] webNames = version.split("\\.");
                    if (currentNames.length == webNames.length) {
                        boolean isNew = false;
                        for (int j = 0; j < webNames.length; j++) {
                            if (Integer.parseInt(webNames[j]) > Integer.parseInt(currentNames[j])) {//有更高版本的apk
                                DialogUtils.getVersionDialog(version, url,versionRemark, this);
                                isNew = true;
                                break;
                            }
                        }
                        if (!isNew){
                            ToastUtils.Toast_short("当前已是最新版本");
                        }
                    } else {
                        ToastUtils.Toast_short("服务器异常，暂不能检测更新！");
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }
}
