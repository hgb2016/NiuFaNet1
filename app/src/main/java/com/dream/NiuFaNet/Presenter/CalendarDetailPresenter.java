package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Bean.CommonBean1;
import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Contract.CalendarDetailContract;

import java.util.Map;

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
public class CalendarDetailPresenter extends RxPresenter<CalendarDetailContract.View> implements CalendarDetailContract.Presenter<CalendarDetailContract.View>{

    private NFApi itApi;

    private static final String TAG = "CalendarDetailPresenter";

    @Inject
    public CalendarDetailPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }


    @Override
    public void getCalendarDetail(String userId,String scheduleId) {
        Subscription rxSubscription = itApi.getCalendarDetail(userId,scheduleId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CalendarDetailBean>() {
                    @Override
                    public void onNext(CalendarDetailBean dataBean) {
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
    public void deletePic(String picId, final int position) {
        Subscription rxSubscription = itApi.deletePic(picId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.deletePicResult(dataBean,position);
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
    public void edtCalender(RequestBody data, MultipartBody.Part[] mFile) {
        Subscription rxSubscription = itApi.edtCalender(data,mFile).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<NewCalResultBean>() {
                    @Override
                    public void onNext(NewCalResultBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.edtCalendar(dataBean);
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
    public void deleteCalendar(String scheduleId,String inviteUserId) {
        Subscription rxSubscription = itApi.deleteCalendar(scheduleId,inviteUserId).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean>() {
                    @Override
                    public void onNext(CommonBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.deleteCalResult(dataBean);
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
    public void validateProjectShow(Map<String, String> map) {
        Subscription rxSubscription = itApi.validateProjectShow(map).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<CommonBean1>() {
                    @Override
                    public void onNext(CommonBean1 dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.validateProjectShowResult(dataBean);
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
