package com.dream.NiuFaNet.Presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.EditCount;
import com.dream.NiuFaNet.Bean.ShowCountBean;
import com.dream.NiuFaNet.Bean.UserInfoBean;
import com.dream.NiuFaNet.Contract.SearchFriendInfoContract;
import com.dream.NiuFaNet.Contract.ShowNoticeCountContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2018/4/18.
 */

public class ShowNoticeCountPresenter extends RxPresenter<ShowNoticeCountContract.View> implements ShowNoticeCountContract.Presenter<ShowNoticeCountContract.View> {

    private NFApi itApi;

    private static final String TAG = "ShowNoticeCountPresenter";
    @Inject
    public ShowNoticeCountPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void showNoticeCount(String useId) {
        Subscription rxSubscription = itApi.searchFriendNoticeShowCount(useId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<ShowCountBean>() {
                    @Override
                    public void onNext(ShowCountBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showData(dataBean);
                        }
                    }

                    @Override
                    public void onCompleted() {

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
    public void getEditCount(String userId) {
        Subscription rxSubscription = itApi.searchProjectIsEditCount(userId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<EditCount>() {
                    @Override
                    public void onNext(EditCount dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showEditCount(dataBean);
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
