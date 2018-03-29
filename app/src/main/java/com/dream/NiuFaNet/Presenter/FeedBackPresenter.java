package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Contract.ChatContract;
import com.dream.NiuFaNet.Contract.FeedBackContract;

import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class FeedBackPresenter extends RxPresenter<FeedBackContract.View> implements FeedBackContract.Presenter<FeedBackContract.View>{

    private NFApi itApi;

    private static final String TAG = "FeedBackPresenter";

    @Inject
    public FeedBackPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void toFeedBack(Map<String,String> map) {
        Subscription rxSubscription = itApi.toFeedBack(map).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
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
