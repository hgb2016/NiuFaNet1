package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.FunctionBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface FunctionContract {

    interface View extends BaseContract.BaseView {

        void showData(FunctionBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getFunctionData(String id);
    }
}
