package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.UserInfoBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface ScheduleRemindContract {

    interface View extends BaseContract.BaseView {

        void showData(CalendarDetailBean dataBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void editScheduleRemind(String user);
    }
}
