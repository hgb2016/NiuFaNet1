package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Contract.CodeContract;
import com.dream.NiuFaNet.Contract.MyFrendsContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class MyFrendsPresenter extends RxPresenter<MyFrendsContract.View> implements MyFrendsContract.Presenter<MyFrendsContract.View>{

    private NFApi itApi;

    private static final String TAG = "MyFrendsPresenter";

    @Inject
    public MyFrendsPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }



    @Override
    public void getMyFrends(String userId) {
        Subscription rxSubscription = itApi.getMyFrends(userId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<MyFrendBean>() {
                    @Override
                    public void onNext(MyFrendBean dataBean) {
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
