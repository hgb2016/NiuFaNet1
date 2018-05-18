package com.dream.NiuFaNet.Presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Contract.DeleteUserFriendsContract;
import com.dream.NiuFaNet.Contract.InviteFriendsContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class InviteFridendsPresenter extends RxPresenter<InviteFriendsContract.View> implements InviteFriendsContract.Presenter<InviteFriendsContract.View>{

    private NFApi itApi;

    private static final String TAG = "InviteFridendsPresenter";

    @Inject
    public InviteFridendsPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void inviteFriends(String userId, String friendId) {
        Subscription rxSubscription = itApi.deleteUserFriends(userId,friendId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showInviteResult(dataBean);
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
