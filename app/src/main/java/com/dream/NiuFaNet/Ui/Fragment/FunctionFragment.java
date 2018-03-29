package com.dream.NiuFaNet.Ui.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.dream.NiuFaNet.Base.BaseFragmentV4;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.BusBean.ReturnBean;
import com.dream.NiuFaNet.Bean.FunctionBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.FunctionContract;
import com.dream.NiuFaNet.Contract.PermissionListener;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.FunctionPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.MineActivity;
import com.dream.NiuFaNet.Ui.Activity.TestActivity;
import com.dream.NiuFaNet.Ui.Activity.WebActivity;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ReturnUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/12 0012.
 */
public class FunctionFragment extends BaseFragmentV4 implements FunctionContract.View{

    @Bind(R.id.find_gv)
    MyGridView find_gv;
    @Bind(R.id.caculate_gv)
    MyGridView caculate_gv;
    @Bind(R.id.labor_gv)
    MyGridView labor_gv;
    @Bind(R.id.title_relay)
    RelativeLayout title_relay;

    private FindAdapter findAdapter;
    private CaculateAdapter caculateAdapter;
    private LaborAdapter laborAdapter;
    @Inject
    FunctionPresenter functionPresenter;

    List<FunctionBean.BodyBean.FindBean> findList = new ArrayList<>();
    List<FunctionBean.BodyBean.CalculateBean> calculateList = new ArrayList<>();
    List<FunctionBean.BodyBean.LaborBean> laborList = new ArrayList<>();


    @Override
    public void initView() {

        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        functionPresenter.attachView(this);

        findAdapter = new FindAdapter(getContext(),findList,R.layout.view_imgtext);
        caculateAdapter = new CaculateAdapter(getContext(),calculateList,R.layout.view_imgtext);
        laborAdapter = new LaborAdapter(getContext(),laborList,R.layout.view_imgtext);
        find_gv.setAdapter(findAdapter);
        caculate_gv.setAdapter(caculateAdapter);
        labor_gv.setAdapter(laborAdapter);

    }

    @Override
    public void initResume() {
       /* String them = CommonAction.getThem();
        if (them.equals(Const.Candy)){
//            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_candy));
            title_relay.setBackgroundColor(getResources().getColor(R.color.main_botcandy));
        }else if (them.equals(Const.Black)){
//            audio_lay.setBackgroundColor(getResources().getColor(R.color.anm_black));
            title_relay.setBackgroundColor(getResources().getColor(R.color.main_botblack));
        }*/
    }

    @Override
    public void initEvents() {

    }

    @Override
    public void initDta() {
        functionPresenter.getFunctionData(CommonAction.getUserId());
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_fuction,null);
    }

    @Override
    public void showData(FunctionBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            List<FunctionBean.BodyBean.FindBean> findBeanList = dataBean.getBody().getFind();
            List<FunctionBean.BodyBean.CalculateBean> calculateBeanList = dataBean.getBody().getCalculate();
            List<FunctionBean.BodyBean.LaborBean> laborBeanList = dataBean.getBody().getLabor();

            if (findBeanList!=null){
                findList.clear();
                findList.addAll(findBeanList);
                int num = findBeanList.size() % 4;
                if (num !=0){
                    int addnum = 4 - num;
                    for (int i = 0; i <addnum ; i++) {
                        FunctionBean.BodyBean.FindBean bean = new FunctionBean.BodyBean.FindBean();
                        findList.add(bean);
                    }
                }
                findAdapter.notifyDataSetChanged();
            }
            if (calculateBeanList!=null){
                calculateList.clear();
                calculateList.addAll(calculateBeanList);
                int num = calculateBeanList.size() % 4;
                if (num !=0){
                    int addnum = 4 - num;
                    for (int i = 0; i <addnum ; i++) {
                        FunctionBean.BodyBean.CalculateBean bean = new FunctionBean.BodyBean.CalculateBean();
                        calculateList.add(bean);
                    }
                }
                caculateAdapter.notifyDataSetChanged();
            }
            if (laborBeanList!=null){
                laborList.clear();
                laborList.addAll(laborBeanList);
                int num = laborBeanList.size() % 4;
                if (num !=0){
                    int addnum = 4 - num;
                    for (int i = 0; i <addnum ; i++) {
                        FunctionBean.BodyBean.LaborBean bean = new FunctionBean.BodyBean.LaborBean();
                        laborList.add(bean);
                    }
                }
                laborAdapter.notifyDataSetChanged();
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

    private class FindAdapter extends CommonAdapter<FunctionBean.BodyBean.FindBean>{

        public FindAdapter(Context context, List<FunctionBean.BodyBean.FindBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, FunctionBean.BodyBean.FindBean dataBean, int position) {

            holder.setImageByUrl(R.id.img_iv,dataBean.getActionPic(),false);
            holder.setText(R.id.content_tv,dataBean.getActionName());
            final String url = dataBean.getUrl();
            holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (url != null && !url.isEmpty()) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra(Const.webUrl, url);
                        startActivity(intent);
                    }
                }
            });
        }
    }
    private class CaculateAdapter extends CommonAdapter<FunctionBean.BodyBean.CalculateBean>{

        public CaculateAdapter(Context context, List<FunctionBean.BodyBean.CalculateBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, FunctionBean.BodyBean.CalculateBean dataBean, int position) {

            holder.setImageByUrl(R.id.img_iv,dataBean.getActionPic(),false);
            holder.setText(R.id.content_tv,dataBean.getActionName());
            final String url = dataBean.getUrl();
            holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (url != null && !url.isEmpty()) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra(Const.webUrl, url);
                        startActivity(intent);
                    }
                }
            });
        }
    }
    private class LaborAdapter extends CommonAdapter<FunctionBean.BodyBean.LaborBean>{

        public LaborAdapter(Context context, List<FunctionBean.BodyBean.LaborBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, FunctionBean.BodyBean.LaborBean dataBean, int position) {

            holder.setImageByUrl(R.id.img_iv,dataBean.getActionPic(),false);
            holder.setText(R.id.content_tv,dataBean.getActionName());
            final String url = dataBean.getUrl();
            holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (url != null && !url.isEmpty()) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra(Const.webUrl, url);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @OnClick({R.id.share_relay})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share_relay:
//                permission();
                DialogUtils.showShareDialog(getActivity());
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
