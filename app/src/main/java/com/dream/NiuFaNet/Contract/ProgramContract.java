package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.ProgramListBean;

import java.util.Map;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface ProgramContract {

    interface View extends BaseContract.BaseView {

        void showData(ProgramListBean dataBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getProgramList(String userId,Map<String, String> map);
    }
}
