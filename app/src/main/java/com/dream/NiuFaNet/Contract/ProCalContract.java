package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Ui.Fragment.ProgramFragment;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface ProCalContract {

    interface View extends BaseContract.BaseView {

        void showProCalData(CommonBean dataBean,ProgramFragment.ProCalAdapter adapter);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getProgramCals(String projectId, ProgramFragment.ProCalAdapter adapter);
    }
}
