package com.dream.NiuFaNet.Ui.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.HeadPortraitBean;
import com.dream.NiuFaNet.Bean.UserBean;
import com.dream.NiuFaNet.Bean.UserInfoBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.GetUserInfoContract;
import com.dream.NiuFaNet.Contract.UserInfoContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.GetUserInfoPresenter;
import com.dream.NiuFaNet.Presenter.UserInfoPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CustomHelper;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.GlideCircleTransform;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SexPopwindowUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.TakePhotoUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.dream.NiuFaNet.Utils.XuniKeyWord;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.MultipartBody;

/**
 * 个人信息处理
 * Created by Administrator on 2018/4/3.
 */

public class UserInfoActivity1 extends CommonActivity implements UserInfoContract.View,TakePhoto.TakeResultListener, InvokeListener ,GetUserInfoContract.View{
    @Bind(R.id.head_iv)
    ImageView head_iv;
    @Bind(R.id.name_tv)
    TextView name_tv;
    @Bind(R.id.sex_tv)
    TextView sex_tv;
    @Bind(R.id.company_tv)
    TextView company_tv;
    @Bind(R.id.work_tv)
    TextView duty_tv;
    @Bind(R.id.phone_tv)
    TextView phone_tv;
    @Bind(R.id.email_tv)
    TextView email_tv;
    @Bind(R.id.fate_tv)
    TextView fate_tv;
    @Bind(R.id.address_tv)
    TextView address_tv;
    @Inject
    UserInfoPresenter userInfoPresenter;
    @Inject
    GetUserInfoPresenter getUserInfoPresenter;
    private Dialog loadingDialog;
    private String hintStr = "输入";
    private String hintStr1 = "未填写";
    private String hintStr2 = "未绑定";
    private String hintStr3 = "未选择";
    @Override
    public int getLayoutId() {
        return R.layout.activity_myinfo;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        userInfoPresenter.attachView(this);
        getUserInfoPresenter.attachView(this);
        XuniKeyWord.initStateView(this).setBackgroundColor(getResources().getColor(R.color.white));
        loadingDialog = DialogUtils.initLoadingDialog(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
    }
    //获取用户信息
    private void getUserData(){
        UserBean userBean=new UserBean();
        userBean.setUserId(CommonAction.getUserId());
        mLoadingDialog.show();
        getUserInfoPresenter.GetUserInfo(new Gson().toJson(userBean));
    }

    //初始化用户信息
    private void initUserData() {
        String userPhone = (String) SpUtils.getParam(Const.userPhone, "");
        String userHeadUrl = (String) SpUtils.getParam(Const.userHeadUrl, "");
        String tdrHeadUrl = (String) SpUtils.getParam(Const.userHeadUrl, "");
        String userSex = (String) SpUtils.getParam(Const.userSex, "");
        String company = (String) SpUtils.getParam(Const.company, "");
        String userDuty = (String) SpUtils.getParam(Const.userDuty, "");
        String userEmail = (String) SpUtils.getParam(Const.userEmail, "");
        String userName = (String) SpUtils.getParam(Const.userName, "");
        String tdName = (String) SpUtils.getParam(Const.userName, "");
        String type = (String) SpUtils.getParam(Const.thirdType, Const.bdUser);
        if (type.equals(Const.bdUser)){
            if (!userName.isEmpty()){
                name_tv.setText(userName);
                name_tv.setTextColor(getResources().getColor(R.color.black));
            }else {
                name_tv.setText(hintStr1);
                name_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
            }
            if (!userHeadUrl.isEmpty()) {
                Glide.with(mContext).load(userHeadUrl).transform(new GlideCircleTransform(mContext)).into(head_iv);
            }
        }else {
            if (!tdName.isEmpty()){
                name_tv.setText(tdName);
                name_tv.setTextColor(getResources().getColor(R.color.black));
            }else {
                name_tv.setText(hintStr1);
                name_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
            }
            if (!tdrHeadUrl.isEmpty()) {
                Glide.with(mContext).load(tdrHeadUrl).transform(new GlideCircleTransform(mContext)).into(head_iv);
            }
        }
        if (!userPhone.isEmpty()) {
            phone_tv.setText(userPhone);
            phone_tv.setTextColor(getResources().getColor(R.color.black));
        }else {
            phone_tv.setText(hintStr2);
            phone_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
        }

        if (!userSex.isEmpty()) {
            sex_tv.setText(userSex);
            sex_tv.setTextColor(getResources().getColor(R.color.black));
        }else {
            sex_tv.setHint(hintStr3);
            sex_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
        }
        if (!company.isEmpty()) {
            company_tv.setText(company);
            company_tv.setTextColor(getResources().getColor(R.color.black));
        }else {
            company_tv.setText(hintStr1);
            company_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
        }
        if (!userDuty.isEmpty()) {
            duty_tv.setText(userDuty);
            duty_tv.setTextColor(getResources().getColor(R.color.black));
        }else {
            duty_tv.setText(hintStr1);
            duty_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
        }
        if (!userEmail.isEmpty()){
            email_tv.setText(userEmail);
            email_tv.setTextColor(getResources().getColor(R.color.black));
        }else {
            email_tv.setText(hintStr1);
            email_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
        }

    }
    @Override
    public void eventListener() {

    }
    @OnClick({R.id.back_relay,R.id.name_relay,R.id.sex_relay,R.id.company_relay,R.id.work_relay,R.id.phone_relay,R.id.email_relay,R.id.fate_relay,R.id.address_relay,R.id.head_iv})
    public void OnClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.back_relay:
                finish();
                break;
            //名字
            case R.id.name_relay:
                intent.putExtra("input","name");
                String name = name_tv.getText().toString().trim();
                if (name.equals(hintStr1)) {
                }else {
                    intent.putExtra("result", name);
                }
                intent.setClass(getApplicationContext(),InputActivity.class);
                startActivity(intent);
                break;
            //性别
            case R.id.sex_relay:
                new SexPopwindowUtils(v) {
                    @Override
                    public void selectNanListener() {
                        sex_tv.setText("男");
                        mLoadingDialog.show();
                        UserBean userBean=new UserBean();
                        userBean.setUserId(CommonAction.getUserId());
                        userBean.setSex("男");
                        userInfoPresenter.changeUserInfo(new Gson().toJson(userBean));
                    }

                    @Override
                    public void selectNvListener() {
                        sex_tv.setText("女");
                        mLoadingDialog.show();
                        UserBean userBean=new UserBean();
                        userBean.setUserId(CommonAction.getUserId());
                        userBean.setSex("女");
                        userInfoPresenter.changeUserInfo(new Gson().toJson(userBean));
                    }
                };
                break;
            //公司
            case R.id.company_relay:
                intent.putExtra("input","company");
                String company=company_tv.getText().toString().trim();
                if (company.equals(hintStr1)){

                }else {
                    intent.putExtra("result",company);
                }
                intent.setClass(getApplicationContext(),InputActivity.class);
                startActivity(intent);
                break;
            //职务
            case R.id.work_relay:
                intent.putExtra("input","company");
                String duty=duty_tv.getText().toString().trim();
                if (duty.equals(hintStr1)){

                }else {
                    intent.putExtra("result",duty);
                }
                intent.putExtra("input","work");
                intent.setClass(getApplicationContext(),InputActivity.class);
                startActivity(intent);
                break;
            //电话号码
            case R.id.phone_relay:
                intent.setClass(mContext,FindPwdOrBindUserActivity.class);
                startActivityForResult(intent,911);
                break;
            //邮箱
            case R.id.email_relay:
                intent.putExtra("input","email");
                String email=email_tv.getText().toString().trim();
                if (email.equals(hintStr1)){

                }else {
                    intent.putExtra("result",email);
                }
                intent.setClass(getApplicationContext(),InputActivity.class);
                startActivity(intent);
                break;
            //地址
            case R.id.address_relay:
                intent.putExtra("input","userAddress");
                String address=address_tv.getText().toString().trim();
                if (address.equals(hintStr1)){

                }else {
                    intent.putExtra("result",address);
                }
                intent.setClass(getApplicationContext(),InputActivity.class);
                startActivity(intent);
                break;
            //头像
            case R.id.head_iv:
                final CustomHelper customHelper = CustomHelper.INSTANCE(getTakePhoto(), "600", "600", false, "200", "200");
                new TakePhotoUtils(mActivity){
                    @Override
                    protected void onPhoto() {
                        customHelper.onClick(false);
                    }

                    @Override
                    protected void onCamara() {
                        customHelper.onClick(true);
                    }
                };
                break;
            //小时费率
            case R.id.fate_relay:
                intent.putExtra("input","fate");
                intent.putExtra("result",fate_tv.getText().toString().trim());
                intent.setClass(getApplicationContext(),InputActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void showError() {
        mLoadingDialog.dismiss();
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            ToastUtils.Toast_short("修改成功");
        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
        mLoadingDialog.dismiss();
    }

    @Override
    public void showHeadData(HeadPortraitBean dataBean) {
        String imageUrl = dataBean.getImageUrl();
        Log.e("tag","headurl="+ imageUrl);
        if (dataBean.getError().equals(Const.success)&&imageUrl!=null){
            if (!imageUrl.isEmpty()){
                Glide.with(mActivity).load(imageUrl).transform(new GlideCircleTransform(mActivity)).into(head_iv);
                ToastUtils.Toast_short("修改成功");
                SpUtils.savaUserInfo(Const.userHeadUrl,imageUrl);
                SpUtils.savaUserInfo(Const.thirdType,Const.bdUser);
                CommonAction.refreshLogined();
            }

        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==911&&resultCode==911){
            getUserData();
        }
    }

    //选择图片后的回调
    @Override
    public void takeSuccess(TResult result) {
        ArrayList<TImage> images = result.getImages();
        String compressPath = images.get(images.size() - 1).getCompressPath();
        File mFile = new File(compressPath);
        Log.e("tag","mFile.lenth="+mFile.length());
        if (mFile != null) {
            MultipartBody.Part bodyPart = HttpUtils.getRequestBodyPart("file", mFile);
            mLoadingDialog.show();
            userInfoPresenter.updateHead(bodyPart, HttpUtils.getBody(CommonAction.getUserId()));
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        ToastUtils.Toast_short("获取图片失败，请重试");
    }

    @Override
    public void takeCancel() {

    }

    private InvokeParam invokeParam;

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    private TakePhoto takePhoto;
    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    //获取用户信息
    @Override
    public void showUserInfoData(UserInfoBean dataBean) {
        //Log.i("myTag",new Gson().toJson(dataBean)+"获取用户信息");
        mLoadingDialog.dismiss();
        if (dataBean.getError().equals(Const.success)){
            UserInfoBean.UserBean userBean=dataBean.getBody();
            if (!dataBean.getBody().getHeadUrl().isEmpty()) {
                Glide.with(mContext).load(dataBean.getBody().getHeadUrl()).transform(new GlideCircleTransform(mContext)).into(head_iv);
            }
            String name=userBean.getUserName();
            String sex=userBean.getSex();
            String company=userBean.getCompany();
            String duty=userBean.getDuty();
            String email=userBean.getEmail();
            String hourRate=userBean.getHourRate();
            String phone=userBean.getMobilePhone();
            String address=userBean.getAddress();
            if (!name.isEmpty()){
                name_tv.setText(name);
                setUserInfo(name,Const.userName);

                name_tv.setTextColor(getResources().getColor(R.color.black));
            }else {
                name_tv.setText(hintStr1);
                name_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
            }
            if (!phone.isEmpty()) {
                setUserInfo(phone,Const.userPhone);
                phone_tv.setText(phone);
                phone_tv.setTextColor(getResources().getColor(R.color.blue_title));
            }else {
                phone_tv.setText(hintStr2);
                phone_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
            }

            if (!sex.isEmpty()) {
                setUserInfo(sex,Const.userSex);
                sex_tv.setText(sex);
                sex_tv.setTextColor(getResources().getColor(R.color.black));
            }else {
                sex_tv.setHint(hintStr3);
                sex_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
            }
            if (!company.isEmpty()) {
                setUserInfo(company,Const.company);
                company_tv.setText(company);
                company_tv.setTextColor(getResources().getColor(R.color.black));
            }else {
                company_tv.setText(hintStr1);
                company_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
            }
            if (!duty.isEmpty()) {
                setUserInfo(duty,Const.userDuty);
                duty_tv.setText(duty);
                duty_tv.setTextColor(getResources().getColor(R.color.black));
            }else {
                duty_tv.setText(hintStr1);
                duty_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
            }
            if (!email.isEmpty()){
                setUserInfo(email,Const.userEmail);
                email_tv.setText(email);
                email_tv.setTextColor(getResources().getColor(R.color.black));
            }else {
                email_tv.setText(hintStr1);
                email_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
            }
            if (!address.isEmpty()){
                setUserInfo(address,Const.userAddress);
                address_tv.setText(address);
                address_tv.setTextColor(getResources().getColor(R.color.black));
            }else {
                address_tv.setText(hintStr1);
                address_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
            }
            if (!hourRate.isEmpty()){
                fate_tv.setText(hourRate);
                fate_tv.setTextColor(getResources().getColor(R.color.black));
            }else {
                fate_tv.setText(hintStr1);
                fate_tv.setTextColor(getResources().getColor(R.color.textcolor_cc));
            }

        }else {
            ToastUtils.Toast_short("获取用户信息失败，请检查网络重试！");
        }
    }

    private void setUserInfo(String userName,String consName) {
        SpUtils.setParam(consName,userName);

    }
}
