package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;

import cn.sharesdk.framework.Platform;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface ThirdLoginContract {

    interface View extends BaseContract.BaseView {

        void authorizeComplete(String openID, String userIcon, String nickName,String unionId);
        void showThirdLoginErro(Platform platform, int i, Throwable throwable);
        void showThirdLoginCancle(Platform platform, int i);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void toAuthorize(String platName);
    }
}
