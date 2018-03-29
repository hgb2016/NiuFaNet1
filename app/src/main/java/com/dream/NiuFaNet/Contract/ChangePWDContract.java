package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface ChangePWDContract {

    interface View extends BaseContract.BaseView {

        void showData(CommonBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void changePWD(String userId,String oldPassword, String newPassword);
    }
}
