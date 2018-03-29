package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.NewProResultBean;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface NewProgramContract {

    interface View extends BaseContract.BaseView {

        void showData(NewProResultBean dataBean);
        void showEdtData(CommonBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void newProgram(String data);
        void edtProject(String data);
    }
}
