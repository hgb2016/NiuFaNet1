package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.MyToolsBean;
import com.dream.NiuFaNet.Contract.MyToolsContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class MyToolsPresenter extends RxPresenter<MyToolsContract.View> implements MyToolsContract.Presenter<MyToolsContract.View>{

    private NFApi itApi;

    private static final String TAG = "MyToolsPresenter";

    @Inject
    public MyToolsPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }



    @Override
    public void getMyTools(String userId) {
        Subscription rxSubscription = itApi.selectMyTools(userId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<MyToolsBean>() {
                    @Override
                    public void onNext(MyToolsBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showMyToolsData(dataBean);
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
