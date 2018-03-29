package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Contract.CalendarDetail2Contract;
import com.dream.NiuFaNet.Contract.CalendarDetailContract;

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
public class CalendarDetail2Presenter extends RxPresenter<CalendarDetail2Contract.View> implements CalendarDetail2Contract.Presenter<CalendarDetail2Contract.View>{

    private NFApi itApi;

    private static final String TAG = "CD2Presenter";

    @Inject
    public CalendarDetail2Presenter(NFApi bookApi) {
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
