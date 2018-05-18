package com.dream.NiuFaNet.Other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.dream.NiuFaNet.Bean.BusBean.LoginBus;
import com.dream.NiuFaNet.Bean.BusBean.RefreshCalBean;
import com.dream.NiuFaNet.Bean.BusBean.RefreshContactBean;
import com.dream.NiuFaNet.Bean.BusBean.RefreshMyToolsBean;
import com.dream.NiuFaNet.Bean.BusBean.RefreshProBean;
import com.dream.NiuFaNet.Bean.LoginBean;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.XuniKeyWord;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/8/8 0008.
 */
public class CommonAction {
    public static String getThem(){
        return (String) SpUtils.getParam(Const.Them,Const.Black);
    }
    public static String getVoiceType(){
        return (String) SpUtils.getParam(Const.voiceType,Const.vTyep_Off);
    }
    public static String getID(){
        return (String) SpUtils.getParam(Const.ID,"");
    }
    public static String getUserName(){
        return (String) SpUtils.getParam(Const.userName,"");
    }
    public static String getUserEmail(){
        return (String) SpUtils.getParam(Const.userEmail,"");
    }
    public static String getUserId(){
        return (String) SpUtils.getParam(Const.userId,"");
    }
    public static boolean getIsFirst(){
        return (boolean) SpUtils.getParam(Const.is_first,false);
    }
    public static boolean getIsLogin(){
        return (boolean) SpUtils.getParam(Const.isLogin,false);
    }
    @SuppressLint("NewApi")
    public static void setThem(final Activity activity, final View view){
        final String them = CommonAction.getThem();
        if (!them.isEmpty()){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View stateView = XuniKeyWord.initStateView(activity);
                    if (them.equals(Const.Candy)){
                        view.setBackground(ResourcesUtils.getDrable(R.drawable.them_bg2));
                        stateView.setBackgroundColor(ResourcesUtils.getColor(R.color.them_color2));
                    }else if (them.equals(Const.Black)){
                        view.setBackground(ResourcesUtils.getDrable(R.drawable.them_bg6));
                        stateView.setBackgroundColor(ResourcesUtils.getColor(R.color.them_color7));
                    }
                }
            });

        }
    }
    @SuppressLint("NewApi")
    public static void setThem2(final Activity activity, final View view){
        final String them = CommonAction.getThem();
        if (!them.isEmpty()){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View stateView = XuniKeyWord.initStateView(activity);
                    if (them.equals(Const.Candy)){
                        view.setBackground(ResourcesUtils.getDrable(R.drawable.them_bg1));
                        stateView.setBackgroundColor(ResourcesUtils.getColor(R.color.them_color1));
                    }else {
                        view.setBackground(ResourcesUtils.getDrable(R.drawable.them_bg6));
                        stateView.setBackgroundColor(ResourcesUtils.getColor(R.color.them_color7));
                    }
                }
            });

        }
    }

    public static void saveUserData(LoginBean.BodyBean resultBean) {
        if (resultBean != null) {
            SpUtils.savaUserInfo(Const.userPhone, resultBean.getMobilePhone());
            SpUtils.savaUserInfo(Const.userId, resultBean.getUserId());
            SpUtils.savaUserInfo(Const.userName, resultBean.getUserName());
            SpUtils.savaUserInfo(Const.userHeadUrl, resultBean.getHeadUrl());
            SpUtils.savaUserInfo(Const.userEmail, resultBean.getEmail());
            SpUtils.savaUserInfo(Const.userSex,resultBean.getSex());
            SpUtils.savaUserInfo(Const.company,resultBean.getCompany());
            SpUtils.savaUserInfo(Const.userDuty,resultBean.getDuty());
            SpUtils.savaUserInfo(Const.userAddress, resultBean.getAddress());
            SpUtils.savaUserInfo(Const.isLogin, true);
        }
    }
    public static void clearUserData() {
        SpUtils.savaUserInfo(Const.userPhone, "");
        SpUtils.savaUserInfo(Const.userId, "");
        SpUtils.savaUserInfo(Const.userName, "");
        SpUtils.savaUserInfo(Const.userHeadUrl, "");
        SpUtils.savaUserInfo(Const.userEmail, "");
        SpUtils.savaUserInfo(Const.userAddress, "");
        SpUtils.savaUserInfo(Const.isLogin,false);
        SpUtils.savaUserInfo(Const.isBd,false);
        SpUtils.savaUserInfo(Const.thirdType,Const.bdUser);
        SpUtils.savaUserInfo(Const.dataCache,"");
    }

    public static void refreshPro(){
        RefreshProBean busBean = new RefreshProBean();
        busBean.setEventStr(Const.refresh);
        EventBus.getDefault().post(busBean);
    }
    public static void refreshCal(){
        RefreshCalBean busBean = new RefreshCalBean();
        busBean.setEventStr(Const.refresh);
        EventBus.getDefault().post(busBean);
    }
    public static void refreshLogined(){
        LoginBus loginBus = new LoginBus();
        loginBus.setEventStr(Const.refresh);
        EventBus.getDefault().post(loginBus);
    }
    public static void refreshContact(){
        RefreshContactBean busBean=new RefreshContactBean();
        busBean.setEventStr(Const.refresh);
        EventBus.getDefault().post(busBean);
    }
    public static void refreshMyTools(){
        RefreshMyToolsBean busBean=new RefreshMyToolsBean();
        busBean.setEventStr(Const.refresh);
        EventBus.getDefault().post(busBean);
    }
}
