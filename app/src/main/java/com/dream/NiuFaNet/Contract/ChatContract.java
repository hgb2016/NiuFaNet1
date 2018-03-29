package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.RecomendBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface ChatContract {

    interface View extends BaseContract.BaseView {

        void showData(ChatBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getChatAnswer(String id,String text,String type,String token);
    }
}
