package com.dream.NiuFaNet.Contract;

import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.MarkDateBean;


/**
 * Created by hou on 2018/4/18.
 */

public interface MarkDateContract {
    interface View extends BaseContract.BaseView {

        void showData(MarkDateBean dataBean);
    }
    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void toGetDateMark(String userId, String beginTime,String endTime);
    }
}
