package com.dream.NiuFaNet.Presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Contract.DeleteUserFriendsContract;
import com.dream.NiuFaNet.Contract.EditFriendRemarkContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class EditFriendRemarkPresenter extends RxPresenter<EditFriendRemarkContract.View> implements EditFriendRemarkContract.Presenter<EditFriendRemarkContract.View>{

    private NFApi itApi;

    private static final String TAG = "EditFriendRemarkPresenter";

    @Inject
    public EditFriendRemarkPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void editFriendRemark(String userId, String friendId,String friendRemark) {
        Subscription rxSubscription = itApi.editFriendRemark(userId,friendId,friendRemark).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showEditResult(dataBean);
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
