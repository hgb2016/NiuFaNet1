package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.HeadPortraitBean;
import com.dream.NiuFaNet.Bean.LoginBean;
import com.dream.NiuFaNet.Contract.LoginContract;
import com.dream.NiuFaNet.Contract.UserInfoContract;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class UserInfoPresenter extends RxPresenter<UserInfoContract.View> implements UserInfoContract.Presenter<UserInfoContract.View>{

    private NFApi itApi;

    private static final String TAG = "UserInfoPresenter";

    @Inject
    public UserInfoPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void changeUserInfo(String user) {
        Subscription rxSubscription = itApi.editUserInfo(user).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
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

    @Override
    public void updateHead(MultipartBody.Part fdImg_file, RequestBody userId) {
        Subscription rxSubscription = itApi.updateHeadPortrait(fdImg_file,userId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<HeadPortraitBean>() {
                    @Override
                    public void onNext(HeadPortraitBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showHeadData(dataBean);
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
    public void updateHead(File fdImg_file, String userId) {
        Subscription rxSubscription = itApi.updateHeadPortrait(fdImg_file,userId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<HeadPortraitBean>() {
                    @Override
                    public void onNext(HeadPortraitBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showHeadData(dataBean);
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
