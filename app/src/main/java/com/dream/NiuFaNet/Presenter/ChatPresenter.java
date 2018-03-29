package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.RecomendBean;
import com.dream.NiuFaNet.Contract.ChatContract;
import com.dream.NiuFaNet.Contract.MainContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class ChatPresenter extends RxPresenter<ChatContract.View> implements ChatContract.Presenter<ChatContract.View>{

    private NFApi itApi;

    private static final String TAG = "ChatPresenter";

    @Inject
    public ChatPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }


    @Override
    public void getChatAnswer(String id, String text, String type,String token) {
        Subscription rxSubscription = itApi.getChatAnswer(id,text,type,token).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<ChatBean>() {
                    @Override
                    public void onNext(ChatBean dataBean) {
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
