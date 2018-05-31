package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.ClientDataBean;
import com.dream.NiuFaNet.Bean.ClientDescBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Other.CommonAction;

/**
 * @author hou.
 * @date 2018/5/22.
 */
public interface SearchClientDescContract {

    interface View extends BaseContract.BaseView {

        void showData(ClientDescBean dataBean);
        void showDeleteResult(CommonBean commonBean);
        void showAddContactResult(CommonBean commonBean);
        void showDeleMyClientContactResult(CommonBean commonBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void searchClientDesc(String userId, String clientId);
        void deleteMyClient(String userId,String clientId);
        void addContact(String data);
        void deleMyClientContact(String userId,String clientId,String id);
    }
}
