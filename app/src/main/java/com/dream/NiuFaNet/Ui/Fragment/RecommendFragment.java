package com.dream.NiuFaNet.Ui.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Adapter.ChatMainRvAdapter;
import com.dream.NiuFaNet.Base.BaseFragmentV4;
import com.dream.NiuFaNet.Bean.BannerBean;
import com.dream.NiuFaNet.Bean.RecomendBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.MainContract;
import com.dream.NiuFaNet.Contract.PermissionListener;
import com.dream.NiuFaNet.CustomView.AudioAnimView;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.MainPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.ChatActivity;
import com.dream.NiuFaNet.Ui.Activity.TestActivity;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kevin.wraprecyclerview.WrapRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/12 0012.
 */
public class RecommendFragment extends BaseFragmentV4  implements MainContract.View{

    @Bind(R.id.chat_rv)
    WrapRecyclerView chat_rv;
    @Bind(R.id.cmd_tv)
    TextView cmd_tv;
    @Bind(R.id.audiores_tv)
    TextView audiores_tv;
    @Bind(R.id.audio_lay)
    LinearLayout audio_lay;
    @Bind(R.id.audioview_left)
    AudioAnimView audioview_left;
    @Bind(R.id.audioview_right)
    AudioAnimView audioview_right;

    @Inject
    MainPresenter mainPresenter;

    private ChatMainRvAdapter chatAdapter;
    private List<RecomendBean.BodyBean> dataList = new ArrayList<>();
    private List<RecomendBean.BodyBean> tempList = new ArrayList<>();


    @Override
    public void initView() {

        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        mainPresenter.attachView(this);

        RvUtils.setOptionnoLine(chat_rv, getActivity());
        chatAdapter = new ChatMainRvAdapter(getContext(),dataList,R.layout.rvitem_onlytext);
        chat_rv.setAdapter(chatAdapter);
    }

    @SuppressLint("NewApi")
    @Override
    public void initResume() {

        String them = CommonAction.getThem();
        if (them.equals(Const.Candy)){
            rootView.setBackground(ResourcesUtils.getDrable(R.drawable.them_bg2));
        }else if (them.equals(Const.Black)){
            rootView.setBackground(ResourcesUtils.getDrable(R.drawable.them_bg6));
        }
    }

    @Override
    public void initEvents() {

    }

    @Override
    public void initDta() {

        String cacheData = (String) SpUtils.getParam(Const.mainCache, "");
        if (!cacheData.isEmpty()){
            List<RecomendBean.BodyBean> beanList = new Gson().fromJson(cacheData, new TypeToken<List<RecomendBean.BodyBean>>(){}.getType());
            if (beanList!=null){
                dataList.addAll(beanList);
                chatAdapter.notifyDataSetChanged();
            }
        }else {
            String userId = CommonAction.getUserId();
            Log.e("tag","userId="+userId);
            Log.e("tag","szImei="+ MyApplication.getDeviceId());
            if(userId.isEmpty()){
                mainPresenter.getRecomendData(MyApplication.getDeviceId());
            }else {
                mainPresenter.getRecomendData(userId);
            }
        }

        Typeface tf=Typeface.createFromAsset(getActivity().getAssets(), "fonts/fzlth.ttf");
        cmd_tv.setTypeface(tf);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_recommend,null);
    }

    @Override
    public void showData(RecomendBean dataBean) {

        if (dataBean.getError().equals(Const.success)){
            List<RecomendBean.BodyBean> bodyBeanList = dataBean.getBody();
            if (bodyBeanList!=null){
                dataList.clear();
                dataList.addAll(bodyBeanList);
                tempList.addAll(bodyBeanList);
               /* RecomendBean.BodyBean bodyBean = new RecomendBean.BodyBean();
                bodyBean.setQuestion("或");
                bodyBean.setUrl(Const.questionUrl);
                dataList.add(bodyBean);
                RecomendBean.BodyBean bodyBean1 = new RecomendBean.BodyBean();
                bodyBean1.setQuestion("随便撩我呀......");
                bodyBean1.setUrl(Const.questionUrl);
                dataList.add(bodyBean1);*/
                chatAdapter.notifyDataSetChanged();
            }
        }else {
            ToastUtils.Toast_short("服务器异常");
        }
    }

    @Override
    public void showBannerData(BannerBean dataBean) {

    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @OnClick({R.id.userinfo_iv,R.id.liaowo_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.userinfo_iv:
//                permission();
               DialogUtils.showShareDialog(getActivity());
                break;
            case R.id.liaowo_tv:
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void permission(){
        requestRunPermisssion(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS}, new PermissionListener() {
            @Override
            public void onGranted() {
                //表示所有权限都授权了
                Dialog dialog = DialogUtils.shareDialog(getActivity());
                dialog.show();
                DialogUtils.setBespreadWindow(dialog, getActivity());
            }

            @Override
            public void onDenied(List<String> deniedPermission) {

                StringBuffer buffer = new StringBuffer();
                for(String permission : deniedPermission){
//                    Toast.makeText(MainActivity.this, "被拒绝的权限：" + permission, Toast.LENGTH_SHORT).show();
                    //引导用户到设置中去进行设置
                    buffer.append(permission+"\r\n");
                }

                Intent intent = new Intent(getContext(),TestActivity.class);
                intent.putExtra("permission",buffer.toString());
                startActivity(intent);

               /* Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);*/
            }
        });
    }
}
