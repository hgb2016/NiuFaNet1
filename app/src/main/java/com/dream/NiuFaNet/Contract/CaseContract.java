package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CaseDetailBean;
import com.dream.NiuFaNet.Bean.CaseListBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.CommonBean1;
import com.dream.NiuFaNet.Bean.UserInfoBean;

import java.util.Map;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface CaseContract {

    interface View extends BaseContract.BaseView {
        void showCaseList(CaseListBean dataBean);
        void showCaseDetail(CaseDetailBean dataBean);
        void showAddCaseResult(CommonBean dataBean);
        void showValidateCaseResult(CommonBean1 dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void selectCaseInfoList(Map<String,String> map);
        void selectCaseInfo(String userId,String caseId);
        void addCaseOrder(Map<String,String> map);
        void validateCaseOrder(String userId,String caseId);
    }
}
