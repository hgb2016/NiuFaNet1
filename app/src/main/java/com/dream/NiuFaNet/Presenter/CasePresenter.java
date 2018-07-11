package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CaseDetailBean;
import com.dream.NiuFaNet.Bean.CaseListBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.CommonBean1;
import com.dream.NiuFaNet.Bean.UserInfoBean;
import com.dream.NiuFaNet.Contract.CaseContract;
import com.dream.NiuFaNet.Contract.GetUserInfoContract;

import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2018/4/18.
 */

public class CasePresenter extends RxPresenter<CaseContract.View> implements CaseContract.Presenter<CaseContract.View> {

    private NFApi itApi;

    private static final String TAG = "CasePresenter";
    @Inject
    public CasePresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void selectCaseInfoList(Map<String, String> map) {
        Subscription rxSubscription = itApi.selectCaseInfoList(map).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CaseListBean>() {
                    @Override
                    public void onNext(CaseListBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showCaseList(dataBean);
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

    @Override
    public void selectCaseInfo(String userId, String caseId) {
        Subscription rxSubscription = itApi.selectCaseInfo(userId,caseId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CaseDetailBean>() {
                    @Override
                    public void onNext(CaseDetailBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showCaseDetail(dataBean);
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

    @Override
    public void addCaseOrder(Map<String, String> map) {
        Subscription rxSubscription = itApi.addCaseOrder(map).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showAddCaseResult(dataBean);
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

    @Override
    public void validateCaseOrder(String userId, String caseId) {
        Subscription rxSubscription = itApi.validateCaseOrder(userId,caseId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean1>() {
                    @Override
                    public void onNext(CommonBean1 dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showValidateCaseResult(dataBean);
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
