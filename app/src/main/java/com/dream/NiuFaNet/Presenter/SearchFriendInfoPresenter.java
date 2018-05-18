package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.UserInfoBean;
import com.dream.NiuFaNet.Contract.GetUserInfoContract;
import com.dream.NiuFaNet.Contract.SearchFriendInfoContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2018/4/18.
 */

public class SearchFriendInfoPresenter extends RxPresenter<SearchFriendInfoContract.View> implements SearchFriendInfoContract.Presenter<SearchFriendInfoContract.View> {

    private NFApi itApi;

    private static final String TAG = "SearchFriendInfoPresenter";
    @Inject
    public SearchFriendInfoPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void SearchFriendInfoInfo(String useId,String friendId) {
        Subscription rxSubscription = itApi.searchFriendInfo(useId,friendId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<UserInfoBean>() {
                    @Override
                    public void onNext(UserInfoBean dataBean) {
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
