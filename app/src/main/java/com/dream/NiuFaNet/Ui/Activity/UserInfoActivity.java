package com.dream.NiuFaNet.Ui.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Base.BaseActivity;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.BusBean.LoginBus;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.HeadPortraitBean;
import com.dream.NiuFaNet.Bean.UserBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.UserInfoContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.UserInfoPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.BitmapUtils;
import com.dream.NiuFaNet.Utils.CustomHelper;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.GlideCircleTransform;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.MacheUtils;
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

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.MultipartBody;

/**
 * Created by Administrator on 2017/8/4 0004.
 */
public class UserInfoActivity extends CommonActivity implements UserInfoContract.View,TakePhoto.TakeResultListener, InvokeListener {

    @Bind(R.id.right_editiv)
    ImageView right_editiv;
    @Bind(R.id.head_icon)
    ImageView head_icon;
    @Bind(R.id.info_lay)
    LinearLayout info_lay;
    @Bind(R.id.edit_lay)
    LinearLayout edit_lay;
    @Bind(R.id.userphone_tv)
    TextView userphone_tv;
    @Bind(R.id.sex_tv)
    TextView sex_tv;
    @Bind(R.id.company_tv)
    TextView company_tv;
    @Bind(R.id.duty_tv)
    TextView duty_tv;
    @Bind(R.id.username_edt)
    EditText username_edt;
    @Bind(R.id.username_tv)
    TextView username_tv;
    @Bind(R.id.email_tv)
    TextView email_tv;
    @Bind(R.id.sex_edt)
    TextView sex_edt;
    @Bind(R.id.company_edt)
    EditText company_edt;
    @Bind(R.id.duty_edt)
    EditText duty_edt;
    @Bind(R.id.wechat_edt)
    EditText wechat_edt;
    @Bind(R.id.email_edt)
    EditText email_edt;
    @Bind(R.id.userphone_tv2)
    TextView userphone_tv2;
    private boolean isEditor;
    private Dialog loadingDialog;
    private String hintStr = "输入";
    private String hintStr1 = "未填写";
    private String hintStr2 = "未绑定";

    @Inject
    UserInfoPresenter userInfoPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initView() {

        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        userInfoPresenter.attachView(this);
        XuniKeyWord.initStateView(this).setBackgroundColor(getResources().getColor(R.color.white));
        loadingDialog = DialogUtils.initLoadingDialog(this);

    }

    @Override
    public void initDatas() {
        initUserData();
    }

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
                username_tv.setText(userName);
                username_edt.setHint(userName);
            }else {
                username_tv.setHint(hintStr1);
            }
            if (!userHeadUrl.isEmpty()) {
                Glide.with(mContext).load(userHeadUrl).transform(new GlideCircleTransform(mContext)).into(head_icon);
            }
        }else {
            if (!tdName.isEmpty()){
                username_tv.setText(tdName);
                username_edt.setHint(tdName);
            }else {
                username_tv.setHint(hintStr1);
            }
            if (!tdrHeadUrl.isEmpty()) {
                Glide.with(mContext).load(tdrHeadUrl).transform(new GlideCircleTransform(mContext)).into(head_icon);
            }
        }
        if (!userPhone.isEmpty()) {
            userphone_tv.setText(userPhone);
            userphone_tv2.setText(userPhone);
        }else {
            userphone_tv.setHint(hintStr2);
        }

        if (!userSex.isEmpty()) {
            sex_tv.setText(userSex);
            sex_edt.setHint(userSex);
        }else {
            sex_tv.setHint("未选择");
        }
        if (!company.isEmpty()) {
            company_tv.setText(company);
            company_edt.setHint(company);
        }else {
            company_tv.setHint(hintStr1);
        }
        if (!userDuty.isEmpty()) {
            duty_tv.setText(userDuty);
            duty_edt.setHint(userDuty);
        }else {
            duty_tv.setHint(hintStr1);
        }
        if (!userEmail.isEmpty()){
            email_tv.setText(userEmail);
            email_edt.setHint(userEmail);
        }else {
            email_tv.setHint(hintStr1);
        }

    }

    @Override
    public void eventListener() {

    }

    @OnClick({R.id.back_relay, R.id.edit_relay,R.id.phone_relay,R.id.head_icon,R.id.sex_edt,R.id.phone_bdrelay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_relay:

                finish();
                break;
            case R.id.edit_relay:
                if (!isEditor) {
                    edit_lay.setVisibility(View.VISIBLE);
                    username_edt.setVisibility(View.VISIBLE);
                    username_tv.setVisibility(View.GONE);
                    info_lay.setVisibility(View.GONE);

                    right_editiv.setImageResource(R.mipmap.icon_achieve);
                    isEditor = true;

                } else {
                    DialogUtils.show(mActivity,loadingDialog);
                    UserBean userBean = new UserBean();
                    String userName = username_edt.getText().toString();
                    String userNameh = username_edt.getHint().toString();
                    String sex = sex_edt.getText().toString();
                    String sexh = sex_edt.getHint().toString();
                    String company = company_edt.getText().toString();
                    String companyh = company_edt.getHint().toString();
                    String duty = duty_edt.getText().toString();
                    String dutyh = duty_edt.getHint().toString();
                    String email = email_edt.getText().toString();
                    String emailh = email_edt.getHint().toString();
                    String phone = (String) SpUtils.getParam(Const.userPhone,"");
                    String headUrl = (String) SpUtils.getParam(Const.userHeadUrl,"");

                    if (!userName.isEmpty()){
                        userBean.setUserName(userName);
                    }else {
                        if (userNameh.contains(hintStr)){
                            userBean.setUserName("");
                        }else {
                            userBean.setUserName(userNameh);
                        }
                    }
                    if (!sex.isEmpty()){
                        userBean.setSex(sex);
                    }else {
                        if (sexh.contains(hintStr)){
                            userBean.setSex("");
                        }else {
                            userBean.setSex(sexh);
                        }
                    }
                    if (!company.isEmpty()){
                        userBean.setCompany(company);
                    }else {
                        if (companyh.contains(hintStr)){
                            userBean.setCompany("");
                        }else {
                            userBean.setCompany(companyh);
                        }
                    }
                    if (!duty.isEmpty()){
                        userBean.setDuty(duty);
                    }else {
                        if (dutyh.contains(hintStr)){
                            userBean.setDuty("");
                        }else {
                            userBean.setDuty(dutyh);
                        }
                    }
                    if (!email.isEmpty()){
                        if (MacheUtils.isEmail(email.trim())){
                            userBean.setEmail(email.trim());
                        }else {
                            ToastUtils.Toast_short("邮箱格式不正确");
                            if (loadingDialog.isShowing()){
                                loadingDialog.dismiss();
                            }
                            break;
                        }
                    }else {
                        if (emailh.contains(hintStr)){
                            userBean.setEmail("");
                        }else {
                            userBean.setEmail(emailh);
                        }
                    }

                    userBean.setUserId(CommonAction.getUserId());
                    userBean.setMobilePhone(phone);
                    userBean.setHeadUrl(headUrl);
                    String json = new Gson().toJson(userBean);
                    Log.e("tag","json="+json);
                    userInfoPresenter.changeUserInfo(json);

                }
                break;
            case R.id.phone_relay:
                    Intent intent = new Intent(mContext,FindPwdOrBindUserActivity.class);
                    startActivityForResult(intent,911);
                break;
            case R.id.phone_bdrelay:
                Intent intent1 = new Intent(mContext,FindPwdOrBindUserActivity.class);
                startActivityForResult(intent1,911);
                break;
            case R.id.head_icon:
                if (isEditor){
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

                }
                break;
            case R.id.sex_edt:
                new SexPopwindowUtils(sex_edt) {
                    @Override
                    public void selectNanListener() {
                        sex_edt.setText("男");
                        sex_tv.setText("男");
                    }

                    @Override
                    public void selectNvListener() {
                        sex_edt.setText("女");
                        sex_tv.setText("女");
                    }
                };
                break;
        }

    }

    @Override
    public void showData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            DialogUtils.dismiss(mActivity,loadingDialog);
            String userName = username_edt.getText().toString();
            String userNameh = username_edt.getHint().toString();
            String sex = sex_edt.getText().toString();
            String sexh = sex_edt.getHint().toString();
            String company = company_edt.getText().toString();
            String companyh = company_edt.getHint().toString();
            String duty = duty_edt.getText().toString();
            String dutyh = duty_edt.getHint().toString();
            String email = email_edt.getText().toString();
            String emailh = email_edt.getHint().toString();

            setUserInfo(userName, userNameh.trim(),Const.userName);
            setUserInfo(sex, sexh.trim(),Const.userSex);
            setUserInfo(company, companyh.trim(),Const.company);
            setUserInfo(duty, dutyh.trim(),Const.userDuty);
            setUserInfo(email, emailh.trim(),Const.userEmail);
            initUserData();
            edit_lay.setVisibility(View.GONE);
            username_edt.setVisibility(View.GONE);
            username_tv.setVisibility(View.VISIBLE);
            info_lay.setVisibility(View.VISIBLE);
            right_editiv.setImageResource(R.mipmap.icon_editor);
            isEditor = false;

        }else {


        }
        loadingDialog.dismiss();
    }

    private void setUserInfo(String userName, String userNameh,String consName) {
        if (userName.isEmpty()){
            if (userNameh.contains(hintStr)){
                SpUtils.setParam(consName,"");
            }else {
                SpUtils.setParam(consName,userNameh);
            }
        }else {
            SpUtils.setParam(consName,userName);
        }
    }

    @Override
    public void showHeadData(HeadPortraitBean dataBean) {
        String imageUrl = dataBean.getImageUrl();
        Log.e("tag","headurl="+ imageUrl);
        if (dataBean.getError().equals(Const.success)&&imageUrl!=null){
            if (!imageUrl.isEmpty()){
                Glide.with(mActivity).load(imageUrl).transform(new GlideCircleTransform(mActivity)).into(head_icon);
                ToastUtils.Toast_short("修改成功");
                SpUtils.savaUserInfo(Const.userHeadUrl,imageUrl);
                SpUtils.savaUserInfo(Const.thirdType,Const.bdUser);
                CommonAction.refreshLogined();
            }

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
        mLoadingDialog.dismiss();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

      /*  if (requestCode == Const.CAMERA && data != null) {
            updateImg(data,Const.CAMERA);
        }

        if (requestCode == Const.PICTURE && data != null) {
            updateImg(data,Const.PICTURE);
        }*/

        if (requestCode==911&&resultCode==911){
            initUserData();
        }


    }

    private void updateImg(Intent data,int methode) {
        File mFile = BitmapUtils.parseData2File(data, methode, mActivity);
        Log.e("tag","userid="+CommonAction.getUserId());
        if (mFile != null) {
            MultipartBody.Part bodyPart = HttpUtils.getRequestBodyPart("file", mFile);
            userInfoPresenter.updateHead(bodyPart, HttpUtils.getBody(CommonAction.getUserId()));
//            userInfoPresenter.updateHead(mFile, CommonAction.getUserId());
        }
    }




    //takePhoto

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

}
