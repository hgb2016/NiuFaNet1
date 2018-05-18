package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.ShowCountBean;
import com.dream.NiuFaNet.Bean.UserInfoBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface ShowNoticeCountContract {

    interface View extends BaseContract.BaseView {

        void showData(ShowCountBean dataBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void showNoticeCount(String userId);
    }
}
