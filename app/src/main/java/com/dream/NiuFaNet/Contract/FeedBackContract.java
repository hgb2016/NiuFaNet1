package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.ChatBean;
import com.dream.NiuFaNet.Bean.CommonBean;

import java.util.Map;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface FeedBackContract {

    interface View extends BaseContract.BaseView {

        void showData(CommonBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void toFeedBack(Map<String,String> map);
    }
}
