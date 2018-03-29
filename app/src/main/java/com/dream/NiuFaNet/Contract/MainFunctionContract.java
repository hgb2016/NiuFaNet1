package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.MainFunctionBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface MainFunctionContract {

    interface View extends BaseContract.BaseView {

        void showData(MainFunctionBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getMianFunction(String type);
    }
}
