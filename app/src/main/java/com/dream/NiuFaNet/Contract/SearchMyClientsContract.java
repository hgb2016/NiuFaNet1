package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.ClientDataBean;
import com.dream.NiuFaNet.Bean.CommonBean;

import java.util.Map;

/**
 * @author hou.
 * @date 2018/5/22.
 */
public interface SearchMyClientsContract {

    interface View extends BaseContract.BaseView {

        void showData(ClientDataBean dataBean);
        void showDeleteResult(CommonBean commonBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void searchMyClients(String userId, Map<String,String> map);
        void deleteMyClient(String userId,String clientId);

    }
}
