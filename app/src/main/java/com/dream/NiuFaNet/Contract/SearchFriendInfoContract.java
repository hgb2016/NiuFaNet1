package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.UserInfoBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface SearchFriendInfoContract {

    interface View extends BaseContract.BaseView {

        void showData(UserInfoBean dataBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void SearchFriendInfoInfo(String userId, String friendId);
    }
}
