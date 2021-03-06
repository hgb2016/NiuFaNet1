package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.ApplyBeFrendBean;
import com.dream.NiuFaNet.Bean.CalInviteBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.ConflictCalBean;

import java.util.Map;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface MessageContract {

    interface View extends BaseContract.BaseView {

        void showApplyFrendData(ApplyBeFrendBean dataBean);
        void showCIData(CalInviteBean dataBean);
        void showFrendsApply(CommonBean dataBean);
        void showReplySchedule(CommonBean dataBean);
        void showValidateResult(ConflictCalBean databean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void applyBeFrend(String userId);
        void getCalInviteList(String userId);
        void replyFrendsApply(String id,String status);
        void replySchedule(String id,String status,String userId,String rejectRemark);
        void validateSchedule(Map<String,String> map);
    }
}
