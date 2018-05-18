package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface EditFriendRemarkContract {

    interface View extends BaseContract.BaseView {

        void showEditResult(CommonBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void editFriendRemark(String userId, String friendId,String friendRemark);
    }
}
