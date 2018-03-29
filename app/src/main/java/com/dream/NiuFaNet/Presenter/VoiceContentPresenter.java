package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.VoiceRvBean;
import com.dream.NiuFaNet.Contract.CodeContract;
import com.dream.NiuFaNet.Contract.VoiceContentContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class VoiceContentPresenter extends RxPresenter<VoiceContentContract.View> implements VoiceContentContract.Presenter<VoiceContentContract.View>{

    private NFApi itApi;

    @Inject
    public VoiceContentPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void voicePrompt() {
        Subscription rxSubscription = itApi.voicePrompt().subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<VoiceRvBean>() {
                    @Override
                    public void onNext(VoiceRvBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showVoiceContentData(dataBean);
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getClass().getName(), "onError: " + e);
                        mView.showError();
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
