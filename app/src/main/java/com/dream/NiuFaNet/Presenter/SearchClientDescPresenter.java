package com.dream.NiuFaNet.Presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.ClientDataBean;
import com.dream.NiuFaNet.Bean.ClientDescBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Contract.SearchClientDescContract;
import com.dream.NiuFaNet.Contract.SearchMyClientsContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class SearchClientDescPresenter extends RxPresenter<SearchClientDescContract.View> implements SearchClientDescContract.Presenter<SearchClientDescContract.View> {

    private NFApi itApi;

    private static final String TAG = "SearchClientDescPresenter";

    @Inject
    public SearchClientDescPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void searchClientDesc(String userId, String clientId) {
        Subscription rxSubscription = itApi.searchClientDesc(userId,clientId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<ClientDescBean>() {
                    @Override
                    public void onNext(ClientDescBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showData(dataBean);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        mView.complete();

                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "onError: " + e);
                        mView.showError();
                    }
                });
        addSubscrebe(rxSubscription);
    }
    @Override
    public void deleteMyClient(String userId, String clientId) {
        Subscription rxSubscription = itApi.deleMyClient(userId,clientId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showDeleteResult(dataBean);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        mView.complete();

                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "onError: " + e);
                        mView.showError();
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void addContact(String data) {
        Subscription rxSubscription = itApi.addClientContact(data).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showAddContactResult(dataBean);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        mView.complete();

                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "onError: " + e);
                        mView.showError();
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void deleMyClientContact(String userId, String clientId, String id) {
        Subscription rxSubscription = itApi.deleMyClientContact(userId,clientId,id).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showDeleMyClientContactResult(dataBean);
                        }
                    }

                    @Override
                    public void onCompleted() {
                        mView.complete();

                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(Throwable e) {

                        Log.e(TAG, "onError: " + e);
                        mView.showError();
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
