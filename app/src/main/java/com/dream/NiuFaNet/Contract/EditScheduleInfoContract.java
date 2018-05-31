package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface EditScheduleInfoContract {

    interface View extends BaseContract.BaseView {

        void showEditData(CommonBean dataBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void editScheduleInfo(String user);
    }
}
