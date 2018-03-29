package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.WorkVisibleBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface WorkVisibleContract {

    interface View extends BaseContract.BaseView {

        void showAddData(CommonBean dataBean);
        void showDeleteData(CommonBean dataBean);
        void showVisiblesData(WorkVisibleBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void addWorkVisible(String data);
        void deleteWorkVisible(String userId,String user_id);
        void getWorkVisible(String userId);
    }
}
