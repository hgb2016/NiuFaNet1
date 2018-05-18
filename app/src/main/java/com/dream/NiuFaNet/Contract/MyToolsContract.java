package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.MyToolsBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface MyToolsContract {

    interface View extends BaseContract.BaseView {

        void showMyToolsData(MyToolsBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getMyTools(String userId);
    }
}
