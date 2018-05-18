package com.dream.NiuFaNet.Contract;



import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.HeadPortraitBean;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface UploadApkContract {

    interface View extends BaseContract.BaseView {

        void showData(CommonBean dataBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void uploadApk(MultipartBody.Part fdImg_file, RequestBody version);
    }
}
