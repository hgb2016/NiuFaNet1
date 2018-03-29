package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Contract.CodeContract;
import com.dream.NiuFaNet.Contract.NewCalenderContract;

import java.util.List;

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
public class NewCalenderPresenter extends RxPresenter<NewCalenderContract.View> implements NewCalenderContract.Presenter<NewCalenderContract.View>{

    private NFApi itApi;

    private static final String TAG = "NewCalenderPresenter";

    @Inject
    public NewCalenderPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }


    @Override
    public void addCalender(RequestBody data, MultipartBody.Part[] mFilee) {
        Subscription rxSubscription = itApi.addCalender(data,mFilee).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<NewCalResultBean>() {
                    @Override
                    public void onNext(NewCalResultBean dataBean) {
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
    public void getInputWord(String text) {
        Subscription rxSubscription = itApi.getInputWord(text).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<List<InputGetBean>>() {
                    @Override
                    public void onNext(List<InputGetBean> dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showWordData(dataBean);
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
    public void deleteParticipant(String scheduleId, String userId, final int position) {
        Subscription rxSubscription = itApi.deleteParticipant(scheduleId,userId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showDleteParcipant(dataBean,position);
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
