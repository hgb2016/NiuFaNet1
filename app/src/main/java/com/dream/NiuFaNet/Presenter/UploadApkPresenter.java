package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.HeadPortraitBean;
import com.dream.NiuFaNet.Contract.UploadApkContract;
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
public class UploadApkPresenter extends RxPresenter<UploadApkContract.View> implements UploadApkContract.Presenter<UploadApkContract.View>{

    private NFApi itApi;

    private static final String TAG = "UserInfoPresenter";

    @Inject
    public UploadApkPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }


    @Override
    public void uploadApk(MultipartBody.Part fdImg_file, RequestBody version) {
        Subscription rxSubscription = itApi.uploadApk(fdImg_file,version).subscribeOn(Schedulers.io())//放在异步中执行
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

}
