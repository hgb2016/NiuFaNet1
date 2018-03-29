package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.CommonBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface CalenderMainContract {

    interface View extends BaseContract.BaseView {

        void showData(CalenderedBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getCalenders(String userId, String beginTime,String endTime);
    }
}
