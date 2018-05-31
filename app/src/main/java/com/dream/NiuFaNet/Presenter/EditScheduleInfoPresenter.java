package com.dream.NiuFaNet.Presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Contract.EditScheduleInfoContract;
import com.dream.NiuFaNet.Contract.ScheduleRemindContract;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2018/4/18.
 */

public class EditScheduleInfoPresenter extends RxPresenter<EditScheduleInfoContract.View> implements EditScheduleInfoContract.Presenter<EditScheduleInfoContract.View> {

    private NFApi itApi;

    private static final String TAG = "EditScheduleInfoPresenter";
    @Inject
    public EditScheduleInfoPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }

    @Override
    public void editScheduleInfo(String data) {
        Subscription rxSubscription = itApi.editScheduleInfo(data).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showEditData(dataBean);
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
