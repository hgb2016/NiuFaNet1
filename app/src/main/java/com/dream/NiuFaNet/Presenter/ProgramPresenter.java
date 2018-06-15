package com.dream.NiuFaNet.Presenter;

import android.util.Log;

import com.dream.NiuFaNet.Api.NFApi;
import com.dream.NiuFaNet.Base.RxPresenter;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.EditCount;
import com.dream.NiuFaNet.Bean.ProgramListBean;
import com.dream.NiuFaNet.Bean.ProjectClientListBean;
import com.dream.NiuFaNet.Contract.CodeContract;
import com.dream.NiuFaNet.Contract.ProgramContract;

import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public class ProgramPresenter extends RxPresenter<ProgramContract.View> implements ProgramContract.Presenter<ProgramContract.View>{

    private NFApi itApi;

    private static final String TAG = "ProgramPresenter";

    @Inject
    public ProgramPresenter(NFApi bookApi) {
        this.itApi = bookApi;
    }


    @Override
    public void getProgramList(String userId,Map<String, String> map) {
        Subscription rxSubscription = itApi.getProgramlist(userId,map).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<ProgramListBean>() {
                    @Override
                    public void onNext(ProgramListBean dataBean) {
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
    public void getProjectList(Map<String, String> map) {
        Subscription rxSubscription = itApi.searchProjectClientList(map).subscribeOn(Schedulers.io())//放在异步中执行
                .observeOn(AndroidSchedulers.mainThread())//回到主线程
                .subscribe(new Observer<ProjectClientListBean>() {
                    @Override
                    public void onNext(ProjectClientListBean dataBean) {
                        if (dataBean != null && mView != null) {
                            mView.showProjectClientList(dataBean);
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
