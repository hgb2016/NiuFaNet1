package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.VersionBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface VersionUpdateContract {

    interface View extends BaseContract.BaseView {

        void showData(VersionBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void requestVersion(String type, String versionName);
    }
}
