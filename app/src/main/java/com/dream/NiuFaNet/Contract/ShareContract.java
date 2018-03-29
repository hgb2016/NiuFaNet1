package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.LoginBean;
import com.dream.NiuFaNet.Bean.ShareBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface ShareContract {

    interface View extends BaseContract.BaseView {

        void showData(ShareBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getShareData(String id);
    }
}
