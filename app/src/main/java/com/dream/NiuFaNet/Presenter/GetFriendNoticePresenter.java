package com.dream.NiuFaNet.Presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.FriendNoticeBean;
import com.dream.NiuFaNet.Bean.UserInfoBean;
import com.dream.NiuFaNet.Contract.GetFriendNoticeContract;
import com.dream.NiuFaNet.Contract.GetUserInfoContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2018/4/18.
 */

public class GetFriendNoticePresenter extends RxPresenter<GetFriendNoticeContract.View> implements GetFriendNoticeContract.Presenter<GetFriendNoticeContract.View> {

    private NFApi itApi;

    private static final String TAG = "GetFriendNoticePresenter";
    @Inject
    public GetFriendNoticePresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void GetFriendNotice(String userid) {
        Subscription rxSubscription = itApi.searchFriendNotice(userid).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<FriendNoticeBean>() {
                    @Override
                    public void onNext(FriendNoticeBean dataBean) {
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
    public void receiveFriend(String userId, String friendId) {
        Subscription rxSubscription = itApi.receiveFriend(userId,friendId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showrecevieData(dataBean);
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
}
