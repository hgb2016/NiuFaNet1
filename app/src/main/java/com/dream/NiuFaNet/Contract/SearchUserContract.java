package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.SearchUserBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface SearchUserContract {

    interface View extends BaseContract.BaseView {

        void showData(SearchUserBean dataBean);
        void showAddFrendData(CommonBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void searchUser(String keyWord);
        void addRends(String userId,String frendId);
    }
}
