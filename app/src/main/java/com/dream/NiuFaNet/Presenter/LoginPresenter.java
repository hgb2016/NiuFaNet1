package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.LoginBean;
import com.dream.NiuFaNet.Contract.FeedBackContract;
import com.dream.NiuFaNet.Contract.LoginContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter<LoginContract.View>{

    private NFApi itApi;

    private static final String TAG = "LoginPresenter";

    @Inject
    public LoginPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void toLogin(final String mobilePhone, String password) {
        Subscription rxSubscription = itApi.toLogin(mobilePhone,password).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onNext(LoginBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showData(dataBean);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        mView.complete();
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
    public void toLogin(String openId, String unionId, String nickName, String loginType,String headUrl) {
        Subscription rxSubscription = itApi.toLogin(openId,unionId,nickName,loginType,headUrl).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<LoginBean>() {
                    @Override
                    public void onNext(LoginBean dataBean) {
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
}
