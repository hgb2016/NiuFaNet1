package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.ClientBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.InputGetBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author hou.
 * @date 2018/5/22.
 */
public interface NewClientContract {

    interface View extends BaseContract.BaseView {

        void showData(CommonBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void addMyClient(String data);
    }
}
