package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.VersionBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.VersionUpdateContract;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.VersionUpdatePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.FileUtil;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.NetUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;



import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class VersionDescActivity extends CommonActivity implements VersionUpdateContract.View{


    @Inject
    VersionUpdatePresenter versionUpdatePresenter;

    @Bind(R.id.versiondesc_tv)
    TextView versiondesc_tv;
    @Bind(R.id.versioncode_tv)
    TextView versioncode_tv;
    @Bind(R.id.version_newcode_tv)
    TextView version_newcode_tv;
    @Bind(R.id.update_tv)
    TextView update_tv;
    private String apkUrl;
    @Bind(R.id.new_tv)
    TextView new_tv;
    @Override
    public int getLayoutId() {
        return R.layout.activity_versiondesc;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        versionUpdatePresenter.attachView(this);

    }

    @Override
    public void initDatas() {
        versionUpdatePresenter.requestVersion("1", "");
    }

    @Override
    public void eventListener() {

    }
    @OnClick({R.id.update_tv})
    public void updateApk(){
        String netTypeName = NetUtil.getNetTypeName(this);
        Log.e("tag", "netTypeName=" + netTypeName);

        if (netTypeName.equals("无网络")) {
            ToastUtils.Toast_short("您的网络异常");
            return;
        }
        if (!netTypeName.equals("WIFI")) {
            DialogUtils.downloadTip(this,apkUrl);
        } else {
            //下载安装包、
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    HttpUtils.downloadFile(apkUrl, FileUtil.DIR_APK, "法律机器人.apk",VersionDescActivity.this);

                }
            };
            new Thread(runnable).start();
        }
    }
    @OnClick({R.id.back_relay})
    public void back(){
        finish();
    }
    @Override
    public void showData(VersionBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            String url = dataBean.getUrl();
            String version = dataBean.getVersion();
            String versionRemark=dataBean.getVersionRemark();
            Log.e("tag", "apkUrl=" + url);
            Log.e("tag", "version=" + version);
            version_newcode_tv.setText("V "+version+"主要更新:");
            versiondesc_tv.setText(versionRemark.replace("\\n","\n"));
            if (url != null && !url.isEmpty() && version != null && !version.isEmpty()) {//对url和version进行判断
                PackageInfo packageInfo = null;
                try {
                    packageInfo = VersionDescActivity.this.getPackageManager().getPackageInfo(VersionDescActivity.this.getPackageName(), 0);
                    final String localVersionName = packageInfo.versionName;
                    Log.e("tag", "localVersionName=" + localVersionName);
                    int localVersionCode = packageInfo.versionCode;
                    versioncode_tv.setText("V "+localVersionName);
                    String[] currentNames = localVersionName.split("\\.");
                    String[] webNames = version.split("\\.");
                    if (Integer.parseInt(webNames[0]) > Integer.parseInt(currentNames[0])) {//有更高版本的apk
                        update_tv.setVisibility(View.VISIBLE);
                        new_tv.setVisibility(View.GONE);
                        apkUrl=url;
                    } else if (Integer.parseInt(webNames[1]) > Integer.parseInt(currentNames[1])) {//有更高版本的apk
                        update_tv.setVisibility(View.VISIBLE);
                        new_tv.setVisibility(View.GONE);
                        apkUrl=url;
                    } else if (Integer.parseInt(webNames[2]) > Integer.parseInt(currentNames[2])) {//有更高版本的apk
                        update_tv.setVisibility(View.VISIBLE);
                        new_tv.setVisibility(View.GONE);
                        apkUrl=url;
                    } else {

                        update_tv.setVisibility(View.GONE);
                        new_tv.setVisibility(View.VISIBLE);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {

    }

}
