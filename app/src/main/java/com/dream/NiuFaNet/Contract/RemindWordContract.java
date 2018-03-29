package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.RemindWordBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface RemindWordContract {

    interface View extends BaseContract.BaseView {

        void showData(RemindWordBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getRemindWord(String id);
    }
}
