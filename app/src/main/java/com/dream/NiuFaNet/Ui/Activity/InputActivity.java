package com.dream.NiuFaNet.Ui.Activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.HeadPortraitBean;
import com.dream.NiuFaNet.Bean.UserBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.EditScheduleInfoContract;
import com.dream.NiuFaNet.Contract.UserInfoContract;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.EditFriendRemarkPresenter;
import com.dream.NiuFaNet.Presenter.EditScheduleInfoPresenter;
import com.dream.NiuFaNet.Presenter.UserInfoPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CheckForAllUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 事件,地址和备注的输入界面
 * Created by hou on 2018/3/29.
 */

public class InputActivity extends CommonActivity implements UserInfoContract.View,EditScheduleInfoContract.View{
    @Bind(R.id.title1_tv)
    TextView title1_tv;
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.input_edt)
    EditText input_edt;
    private String input;
    private int temp;
    @Inject
    UserInfoPresenter userInfoPresenter;
    @Inject
    EditScheduleInfoPresenter editScheduleInfoPresenter;
    private String hintStr1 = "未填写";
    private String scheduleId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_input;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        userInfoPresenter.attachView(this);
        editScheduleInfoPresenter.attachView(this);

        //获取数据
        Intent intent = getIntent();
        input=intent.getStringExtra("input");
        scheduleId=intent.getStringExtra("scheduleId");
        String result=intent.getStringExtra("result");
        if (!TextUtils.isEmpty(result)){
            input_edt.setText(result);
        }
        input_edt.setSelection(input_edt.getText().length());
        if (!TextUtils.isEmpty(input)){
           switch (input){
                   case "event":
                   title_tv.setText("标题");
                   title1_tv.setText("修改标题");
                   temp= Const.EVENT;
                   break;
               case "address":
                   title_tv.setText("地址");
                   title1_tv.setText("修改地址");
                   temp= Const.ADDRESS;
                   break;
               case "note":
                   title_tv.setText("备注");
                   title1_tv.setText("备注");
                   temp= Const.NOTE;
                   break;
               case "name":
                   title_tv.setText("姓名");
                   title1_tv.setText("修改姓名");
                   temp= Const.NAME;
                   break;
               case "company":
                   title_tv.setText("公司名称");
                   title1_tv.setText("修改公司名称");
                   temp= Const.COMPANY;
                   break;
               case "work":
                   title_tv.setText("职务");
                   title1_tv.setText("修改职务");
                   temp= Const.WORK;
                   break;
               case "email":
                   title_tv.setText("邮箱");
                   title1_tv.setText("修改邮箱");
                   temp= Const.EMAIL;
                   break;
               case "userAddress":
                   title_tv.setText("地址");
                   title1_tv.setText("编辑地址");
                   temp= Const.USERADDRESS;
                   break;
               case "fate":
                   input_edt.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                   input_edt.setSelection(input_edt.getText().length());
                   title_tv.setText("小时费率(元/小时)");
                   title1_tv.setText("小时费率");
                   temp= Const.FATE;
                   break;
           }
        }
    }

    @Override
    public void initDatas() {

    }
    @OnClick({R.id.sure_tv,R.id.back_relay})
    public void OnClick(View v){
        String result=input_edt.getText().toString().trim();
        UserBean userBean = new UserBean();
        userBean.setUserId(CommonAction.getUserId());
        switch (v.getId()){

            case R.id.sure_tv:
                switch (temp) {
                    case Const.EVENT:
                        if (!TextUtils.isEmpty(result)) {
                            CalendarDetailBean.DataBean dataBean=new CalendarDetailBean.DataBean();
                            dataBean.setTitle(result);
                            dataBean.setScheduleId(scheduleId);
                            dataBean.setUserId(CommonAction.getUserId());
                            mLoadingDialog.show();
                            editScheduleInfoPresenter.editScheduleInfo(new Gson().toJson(dataBean));
                        }else {
                            ToastUtils.Toast_short("标题不能为空");
                        }
                        break;
                    case Const.ADDRESS:
                        if (!TextUtils.isEmpty(result)) {
                            CalendarDetailBean.DataBean dataBean=new CalendarDetailBean.DataBean();
                            dataBean.setAddress(result);
                            dataBean.setScheduleId(scheduleId);
                            dataBean.setUserId(CommonAction.getUserId());
                            mLoadingDialog.show();
                            editScheduleInfoPresenter.editScheduleInfo(new Gson().toJson(dataBean));
                        }else {
                            ToastUtils.Toast_short(mActivity,"地址不能为空");
                        }
                        break;
                    case Const.NOTE:
                        if (TextUtils.isEmpty(result)) {
                            ToastUtils.Toast_short("备注不能为空");
                        }else {
                            CalendarDetailBean.DataBean dataBean=new CalendarDetailBean.DataBean();
                            dataBean.setRemark(result);
                            dataBean.setScheduleId(scheduleId);
                            dataBean.setUserId(CommonAction.getUserId());
                            mLoadingDialog.show();
                            editScheduleInfoPresenter.editScheduleInfo(new Gson().toJson(dataBean));
                        }
                        break;
                    case Const.NAME:
                        if (TextUtils.isEmpty(result)){
                            ToastUtils.Toast_short("名字不能为空");
                        } else if (!TextUtils.isEmpty(result)&&result.length()<10) {
                            userBean.setUserName(result);
                            mLoadingDialog.show();
                            userInfoPresenter.changeUserInfo(new Gson().toJson(userBean));
                        }else {
                            ToastUtils.Toast_short("名字的长度不能超过10个字符");
                        }
                        break;
                    case Const.COMPANY:
                        if (!TextUtils.isEmpty(result)) {
                            userBean.setCompany(result);
                            mLoadingDialog.show();
                            userInfoPresenter.changeUserInfo(new Gson().toJson(userBean));
                        }else {
                            ToastUtils.Toast_short("公司名字不能为空");
                        }
                        break;
                    case Const.WORK:
                        if (!TextUtils.isEmpty(result)) {
                            userBean.setDuty(result);
                            mLoadingDialog.show();
                            userInfoPresenter.changeUserInfo(new Gson().toJson(userBean));
                        }else {
                            ToastUtils.Toast_short("职务不能为空");
                        }
                        break;
                    case Const.EMAIL:
                        if ( TextUtils.isEmpty(result)) {
                            ToastUtils.Toast_short("邮箱不能为空");
                        } else if (!CheckForAllUtils.isEmailAdd(result)){
                            ToastUtils.Toast_short("输入的邮箱格式不正确");
                        }else {
                            userBean.setEmail(result);
                            mLoadingDialog.show();
                            userInfoPresenter.changeUserInfo(new Gson().toJson(userBean));

                        }
                        break;
                    case Const.USERADDRESS:
                        if ( TextUtils.isEmpty(result)) {
                            ToastUtils.Toast_short("地址不能为空");
                        }else {
                            userBean.setAddress(result);
                            mLoadingDialog.show();
                            userInfoPresenter.changeUserInfo(new Gson().toJson(userBean));
                        }
                        break;
                    case Const.FATE:
                        if (TextUtils.isEmpty(result)) {
                            ToastUtils.Toast_short(mActivity,"费率不能为空");
                        }else if (result.matches("^[1-9]\\d*$")){
                            userBean.setHourRate(result);
                            mLoadingDialog.show();
                            userInfoPresenter.changeUserInfo(new Gson().toJson(userBean));
                        }else {
                            ToastUtils.Toast_short(mActivity,"输入格式有误");
                        }
                        break;

                }
                break;
            case R.id.back_relay:
                finish();
                break;
        }
    }
    @Override
    public void eventListener() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            ToastUtils.Toast_short("修改成功");
            mLoadingDialog.dismiss();
            finish();
        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }
    private void setUserInfo(String userName,String consName) {

            SpUtils.setParam(consName,userName);

    }
    @Override
    public void showHeadData(HeadPortraitBean dataBean) {

    }

    @Override
    public void showEditData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            mLoadingDialog.dismiss();
            ToastUtils.showToast(mActivity,"修改成功",R.mipmap.checkmark);
            CommonAction.refreshCal();
            finish();
        }
    }
}
