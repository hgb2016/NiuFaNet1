package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.HeadPortraitBean;

import java.io.File;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface UserInfoContract {

    interface View extends BaseContract.BaseView {

        void showData(CommonBean dataBean);
        void showHeadData(HeadPortraitBean dataBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void changeUserInfo(String user);
        void updateHead(MultipartBody.Part fdImg_file, RequestBody userId);
        void updateHead(File fdImg_file, String userId);
    }
}
