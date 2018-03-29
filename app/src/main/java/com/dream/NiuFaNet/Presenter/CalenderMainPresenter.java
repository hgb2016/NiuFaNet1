package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Contract.CalenderMainContract;
import com.dream.NiuFaNet.Contract.CodeContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class CalenderMainPresenter extends RxPresenter<CalenderMainContract.View> implements CalenderMainContract.Presenter<CalenderMainContract.View>{

    private NFApi itApi;

    private static final String TAG = "CodePresenter";

    @Inject
    public CalenderMainPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }


    @Override
    public void getCalenders(String userId, String beginTime, String endTime) {
        Subscription rxSubscription = itApi.getCalenders(userId,beginTime,endTime).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CalenderedBean>() {
                    @Override
                    public void onNext(CalenderedBean dataBean) {
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
}
