package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;

/**
 * @author hou.
 * @date 2018/5/22.
 */
public interface WhoCanSeeContract {

    interface View extends BaseContract.BaseView {

        void showAddResult(CommonBean dataBean);
        void showDeleteResult(CommonBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void addClientShowUser(String data);
        void deleMyClientUser(String userId,String clientId,String id);
    }
}
