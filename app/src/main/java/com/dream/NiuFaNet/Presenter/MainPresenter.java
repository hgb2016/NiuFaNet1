package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.BannerBean;
import com.dream.NiuFaNet.Bean.MyToolsBean;
import com.dream.NiuFaNet.Bean.RecomendBean;
import com.dream.NiuFaNet.Contract.MainContract;
import com.dream.NiuFaNet.Contract.ThirdLoginContract;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Utils.SpUtils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter<MainContract.View>{

    private NFApi itApi;

    private static final String TAG = "MainPresenter";

    @Inject
    public MainPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }



    @Override
    public void getRecomendData(String topN) {
        Subscription rxSubscription = itApi.getRecomendData(topN).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<RecomendBean>() {
                    @Override
                    public void onNext(RecomendBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showData(dataBean);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "onError: " + e);
                        mView.showError();
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getBannerDat(String type) {
        Subscription rxSubscription = itApi.getBannerData(type).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<BannerBean>() {
                    @Override
                    public void onNext(BannerBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showBannerData(dataBean);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "onError: " + e);
                        mView.showError();
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void getMyTools(String userId) {
        Subscription rxSubscription = itApi.selectMyTools(userId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<MyToolsBean>() {
                    @Override
                    public void onNext(MyToolsBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showMyTools(dataBean);
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "onError: " + e);
                        mView.showError();
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
