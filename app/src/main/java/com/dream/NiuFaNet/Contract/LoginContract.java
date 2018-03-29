package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.LoginBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface LoginContract {

    interface View extends BaseContract.BaseView {

        void showData(LoginBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void toLogin(String mobilePhone, String password);
        void toLogin(String openId, String unionId, String nickName,String loginType,String headUrl);
    }
}
