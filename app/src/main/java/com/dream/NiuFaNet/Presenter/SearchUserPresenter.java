package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.SearchUserBean;
import com.dream.NiuFaNet.Contract.CodeContract;
import com.dream.NiuFaNet.Contract.SearchUserContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class SearchUserPresenter extends RxPresenter<SearchUserContract.View> implements SearchUserContract.Presenter<SearchUserContract.View>{

    private NFApi itApi;

    private static final String TAG = "CodePresenter";

    @Inject
    public SearchUserPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }


    @Override
    public void searchUser(String keyWord) {
        Subscription rxSubscription = itApi.searchUser(keyWord).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<SearchUserBean>() {
                    @Override
                    public void onNext(SearchUserBean dataBean) {
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

    @Override
    public void addRends(String userId, String frendId) {
        Subscription rxSubscription = itApi.addFrend(userId,frendId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showAddFrendData(dataBean);
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
