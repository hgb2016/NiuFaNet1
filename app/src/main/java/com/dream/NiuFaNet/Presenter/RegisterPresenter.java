package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.RegisterBean;
import com.dream.NiuFaNet.Contract.FeedBackContract;
import com.dream.NiuFaNet.Contract.RegisterContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class RegisterPresenter extends RxPresenter<RegisterContract.View> implements RegisterContract.Presenter<RegisterContract.View>{

    private NFApi itApi;

    private static final String TAG = "RegisterPresenter";

    @Inject
    public RegisterPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }


    @Override
    public void toRegister(String mobilePhone, String captcha, String password) {
        Subscription rxSubscription = itApi.toRegister(mobilePhone,captcha,password).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<RegisterBean>() {
                    @Override
                    public void onNext(RegisterBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showRegisterData(dataBean);
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
