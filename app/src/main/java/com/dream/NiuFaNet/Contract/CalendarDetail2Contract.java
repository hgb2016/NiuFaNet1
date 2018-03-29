package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface CalendarDetail2Contract {

    interface View extends BaseContract.BaseView {

        void showData(CalendarDetailBean dataBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getCalendarDetail(String userId, String scheduleId);
    }
}
