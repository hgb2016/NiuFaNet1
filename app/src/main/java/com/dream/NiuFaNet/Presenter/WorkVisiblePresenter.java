package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.WorkVisibleBean;
import com.dream.NiuFaNet.Contract.CodeContract;
import com.dream.NiuFaNet.Contract.WorkVisibleContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class WorkVisiblePresenter extends RxPresenter<WorkVisibleContract.View> implements WorkVisibleContract.Presenter<WorkVisibleContract.View>{

    private NFApi itApi;

    private static final String TAG = "WorkVisiblePresenter";

    @Inject
    public WorkVisiblePresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void addWorkVisible(String data) {
        Subscription rxSubscription = itApi.addWorkVisible(data).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showAddData(dataBean);
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
    public void deleteWorkVisible(String userId, String user_id) {
        Subscription rxSubscription = itApi.deleteWorkVisible(userId,user_id).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showDeleteData(dataBean);
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
    public void getWorkVisible(String userId) {
        Subscription rxSubscription = itApi.getWorkVisible(userId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<WorkVisibleBean>() {
                    @Override
                    public void onNext(WorkVisibleBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showVisiblesData(dataBean);
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
