package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface NewCalenderContract {

    interface View extends BaseContract.BaseView {

        void showData(NewCalResultBean dataBean);
        void showWordData(List<InputGetBean> dataBean);
        void showDleteParcipant(CommonBean dataBean,int position);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void addCalender(RequestBody data, MultipartBody.Part[] mFilee);
        void getInputWord(String text);
        void deleteParticipant(String scheduleId,String userId,int position,String inviteUserId);
    }
}
