package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.ApplyBeFrendBean;
import com.dream.NiuFaNet.Bean.CalInviteBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.ConflictCalBean;
import com.dream.NiuFaNet.Contract.CodeContract;
import com.dream.NiuFaNet.Contract.MessageContract;

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
public class MessagePresenter extends RxPresenter<MessageContract.View> implements MessageContract.Presenter<MessageContract.View>{

    private NFApi itApi;

    private static final String TAG = "MessagePresenter";

    @Inject
    public MessagePresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }


    @Override
    public void applyBeFrend(String userId) {
        Subscription rxSubscription = itApi.applyBeFrend(userId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<ApplyBeFrendBean>() {
                    @Override
                    public void onNext(ApplyBeFrendBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showApplyFrendData(dataBean);
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
    public void getCalInviteList(String userId) {
        Subscription rxSubscription = itApi.getCalInviteList(userId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CalInviteBean>() {
                    @Override
                    public void onNext(CalInviteBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showCIData(dataBean);
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
    public void replyFrendsApply(String id, String status) {
        Subscription rxSubscription = itApi.replyFrend(id,status).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showFrendsApply(dataBean);
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
    public void replySchedule(String id, String status,String userId,String rejectRemark) {
        Subscription rxSubscription = itApi.replySchedule(id,status,userId,rejectRemark).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showReplySchedule(dataBean);
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
    public void validateSchedule(Map<String, String> map) {
        Subscription rxSubscription = itApi.validateScheduleData(map).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<ConflictCalBean>() {
                    @Override
                    public void onNext(ConflictCalBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showValidateResult(dataBean);
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
