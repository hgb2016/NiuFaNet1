package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.ThirdInfoBeanWX;
import com.dream.NiuFaNet.Contract.ThirdLoginContract;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class ThirdLoginPresenter extends RxPresenter<ThirdLoginContract.View> implements ThirdLoginContract.Presenter<ThirdLoginContract.View> , PlatformActionListener {

    private NFApi itApi;

    private static final String TAG = "ThirdLoginPresenter";

    @Inject
    public ThirdLoginPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.e(TAG, "第三方授权回调");
        PlatformDb platDB = platform.getDb();//获取平台数据DB
        String userId = platDB.getUserId();//用户id
        String userIcon = platDB.getUserIcon();
        String userName = platDB.getUserName();
        String exportData = platDB.exportData();
        String name = platform.getName();
        Log.e("dsf", "tdname=" + userName);//用户昵称
        Log.e("dsf", "userId=" + userId);//用户id
        Log.e("dsf", "tdUserIcon=" + userIcon);//用户头像
        Log.e("dsf", "tdSecret=" + platDB.getTokenSecret());//用户密码
        Log.e("dsf", "exportData=" + exportData);//用户密码
        String unionId = "qq";
       if (name.equals(Wechat.NAME)){
            if (exportData!=null){
                Gson gson = new Gson();
                ThirdInfoBeanWX thirdInfoBean = gson.fromJson(exportData, ThirdInfoBeanWX.class);
                if (thirdInfoBean!=null){
                    unionId = thirdInfoBean.getUnionid();
                }
            }
        }
//        SpUtils.savaUserInfo(Const.tdHeadUrl, userIcon);
//        SpUtils.savaUserInfo(Const.tdUserName, userName);
        SpUtils.setParam(Const.isBd,false);
        mView.authorizeComplete(userId,userIcon,userName,unionId);

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        mView.showThirdLoginErro(platform,i,throwable);
        Log.e(TAG, "onError");

    }

    @Override
    public void onCancel(Platform platform, int i) {
        Log.e(TAG, "取消授权");
        mView.showThirdLoginCancle(platform,i);
    }

    public void toAuthorize(String platName) {
        Platform platform = ShareSDK.getPlatform(platName);
        if (platform == null) {
            return;
        }
        platform.setPlatformActionListener(this);
//        platform.authorize();
        platform.SSOSetting(false);//false:跳转客户端，true:跳转web页面
        platform.showUser(null);//只获取用户信息，不需要功能
        //移除授权，防止手机微信切换账号后登录的是上次的微信号
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
        }
    }
}
