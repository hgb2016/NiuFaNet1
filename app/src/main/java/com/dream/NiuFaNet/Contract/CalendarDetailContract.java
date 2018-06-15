package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Bean.CommonBean1;
import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface CalendarDetailContract {

    interface View extends BaseContract.BaseView {

        void showData(CalendarDetailBean dataBean);
        void deletePicResult(CommonBean dataBean,int position);
        void edtCalendar(NewCalResultBean dataBean);
        void deleteCalResult(CommonBean dataBean);
        void validateProjectShowResult(CommonBean1 databean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getCalendarDetail(String userId,String scheduleId);
        void deletePic(String picId,int position);
        void edtCalender(RequestBody data, MultipartBody.Part[] mFile);
        void deleteCalendar(String scheduleId,String inviteUserId);
        void validateProjectShow(Map<String,String> map);
    }
}
