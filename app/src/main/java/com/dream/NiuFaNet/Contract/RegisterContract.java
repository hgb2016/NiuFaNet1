package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.RegisterBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface RegisterContract {

    interface View extends BaseContract.BaseView {

        void showRegisterData(RegisterBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void toRegister(String mobilePhone, String captcha,String password);
    }
}
