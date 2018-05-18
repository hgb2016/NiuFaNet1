package com.dream.NiuFaNet.Contract;


import com.dream.NiuFaNet.Base.BaseContract;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;

import java.util.Map;

/**
 * @author lfh.
 * @date 2016/8/6.
 */
public interface ProgramDetailContract {

    interface View extends BaseContract.BaseView {

        void showData(ProgramDetailBean dataBean);
        void showEdtData(CommonBean dataBean);
        void showDeleteData(CommonBean dataBean);
        void showDownload(CommonBean dataBean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void getProjectProgramDetail(String projectId,String page,String userId);
        void edtProject(String data);
        void deleteProject(String projectId);

    }
}
