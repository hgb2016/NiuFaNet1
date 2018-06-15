package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.BannerBean;
import com.dream.NiuFaNet.Bean.EditCount;
import com.dream.NiuFaNet.Bean.MyToolsBean;
import com.dream.NiuFaNet.Bean.RecomendBean;

import cn.sharesdk.framework.Platform;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface MainContract {

    interface View extends BaseContract.BaseView {

        void showData(RecomendBean dataBean);
        void showBannerData(BannerBean dataBean);
        void showMyTools(MyToolsBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getRecomendData(String topN);
        void getBannerDat(String type);
        void getMyTools(String userId);

    }
}
