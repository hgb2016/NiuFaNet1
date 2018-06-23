package com.dream.NiuFaNet.Ui.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dream.NiuFaNet.Base.BaseActivityRelay;
import com.dream.NiuFaNet.Base.BaseActivityWG;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.BannerBean;
import com.dream.NiuFaNet.Bean.MyToolsBean;
import com.dream.NiuFaNet.Bean.RecomendBean;
import com.dream.NiuFaNet.Bean.ShareBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.MainContract;
import com.dream.NiuFaNet.Contract.PermissionListener;
import com.dream.NiuFaNet.Contract.ShareContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.MainPresenter;
import com.dream.NiuFaNet.Presenter.SharePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;


public class WelcomeActivity extends BaseActivityWG implements ShareContract.View, MainContract.View{


    @Inject
    SharePresenter sharePresenter;
    @Inject
    MainPresenter mainPresenter;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Const.GO_GUIDE:
                    mIntent.setClass(WelcomeActivity.this,GuideActivity.class);
                    startActivity(mIntent);
                    finish();
                    break;
                case Const.GO_MAIN:
                    mIntent.setClass(WelcomeActivity.this,MainActivity.class);
                    startActivity(mIntent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    private ImageView mImageView;
    public Intent mIntent=new Intent();



    @Override
    protected void onDestroy() {
        if(null != mHandler) {
            mHandler.removeMessages(Const.GO_GUIDE);
            mHandler.removeMessages(Const.GO_MAIN);
            mHandler = null;
            Log.d("tag", "release Handler success");
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        sharePresenter.attachView(this);
        mainPresenter.attachView(this);
        mImageView= (ImageView) findViewById(R.id.welcome_pic);
        mImageView.startAnimation(AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.alpha));
        toNextActivity();
    }

    @Override
    public void loadResum() {

    }


    @Override
    public void initDatas() {
        sharePresenter.getShareData("");
        String userId = CommonAction.getUserId();
        Log.e("tag","userId="+userId);
        Log.e("tag","szImei="+MyApplication.getDeviceId());
        if(userId.isEmpty()){
            mainPresenter.getRecomendData(MyApplication.getDeviceId());
        }else {
            mainPresenter.getRecomendData(userId);
        }

    }

    @Override
    public void eventListener() {

    }

    public void toNextActivity(){
        boolean is_first =  (Boolean) SpUtils.getParam(Const.is_first,false);
        if (!is_first) {
            //第一次启动进入引导页
            mHandler.sendEmptyMessageDelayed(Const.GO_GUIDE, Const.SPLASH_DELAY_TIME);

        }  else {
            //不是第一次启动直接进入首页
            mHandler.sendEmptyMessageDelayed(Const.GO_MAIN, Const.SPLASH_DELAY_TIME);
        }
    }

    @Override
    public void showData(ShareBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            permission(dataBean);
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
    public void showData(RecomendBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            List<RecomendBean.BodyBean> beanList = dataBean.getBody();
            String cache = new Gson().toJson(beanList);
            SpUtils.savaUserInfo(Const.mainCache,cache);
        }
    }

    @Override
    public void showBannerData(BannerBean dataBean) {

    }

    @Override
    public void showMyTools(MyToolsBean dataBean) {

    }


    private void permission(final ShareBean dataBean){
        requestRunPermisssion(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS}, new PermissionListener() {
            @Override
            public void onGranted() {
                //表示所有权限都授权了
                SpUtils.savaUserInfo(Const.picUrl,dataBean.getPicUrl());
                SpUtils.savaUserInfo(Const.title,dataBean.getTitle());
                SpUtils.savaUserInfo(Const.content,dataBean.getContent());
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

                StringBuffer buffer = new StringBuffer();
                for(String permission : deniedPermission){
//                    Toast.makeText(MainActivity.this, "被拒绝的权限：" + permission, Toast.LENGTH_SHORT).show();
                    //引导用户到设置中去进行设置
                    buffer.append(permission+"\r\n");
                }

                Intent intent = new Intent(mContext,TestActivity.class);
                intent.putExtra("permission",buffer.toString());
                startActivity(intent);

               /* Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);*/

            }
        });
    }
}
