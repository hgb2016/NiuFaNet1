package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.FriendNoticeBean;
import com.dream.NiuFaNet.Bean.UserInfoBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface GetFriendNoticeContract {

    interface View extends BaseContract.BaseView {

        void showData(FriendNoticeBean dataBean);
        void showrecevieData(CommonBean dataBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void GetFriendNotice(String user);
        void receiveFriend(String userId,String friendId);
    }
}
