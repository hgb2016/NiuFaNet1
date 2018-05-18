package com.dream.NiuFaNet.Ui.Activity;

import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.UploadApkContract;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.UploadApkPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.HttpUtils;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import okhttp3.MultipartBody;

public class UploadApkActivity extends CommonActivity implements UploadApkContract.View{
    @Bind(R.id.upload)
    Button upload_btn;
    @Inject
    UploadApkPresenter uploadApkPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_uploadapk;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
       uploadApkPresenter.attachView(this);

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {
        //上传
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultipartBody.Part file = HttpUtils.getRequestBodyPart("file", new File(Environment.getExternalStorageDirectory().getPath() + "/niufa.apk"));
               // uploadApkPresenter.uploadApk(file,veri);

            }
        });
    }

    @Override
    public void showData(CommonBean dataBean) {

    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }
}
