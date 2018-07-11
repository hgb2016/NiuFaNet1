package com.dream.NiuFaNet.Ui.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.ActivityCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.FunctionBean;
import com.dream.NiuFaNet.Bean.MyToolsBean;
import com.dream.NiuFaNet.Bean.ToolBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.EditMyToolsContract;
import com.dream.NiuFaNet.Contract.FunctionContract;
import com.dream.NiuFaNet.Contract.MyToolsContract;
import com.dream.NiuFaNet.CustomView.DragGridView;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.EditMyToolsPresenter;
import com.dream.NiuFaNet.Presenter.FunctionPresenter;
import com.dream.NiuFaNet.Presenter.MyToolsPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huxq17.handygridview.HandyGridView;
import com.huxq17.handygridview.listener.IDrawer;
import com.huxq17.handygridview.listener.OnItemCapturedListener;
import com.huxq17.handygridview.scrollrunner.OnItemMovedListener;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ljq on 2018/3/24.
 */

public class FunctionActivity extends CommonActivity implements FunctionContract.View, View.OnClickListener, EditMyToolsContract.View, MyToolsContract.View {
    @Bind(R.id.function_gv)
    DragGridView function_gv;
    @Bind(R.id.find_gv)
    MyGridView find_gv;
    @Bind(R.id.caculate_gv)
    MyGridView caculate_gv;
    @Bind(R.id.labor_gv)
    MyGridView labor_gv;
    @Bind(R.id.edit_iv)
    ImageView edit_iv;
    @Bind(R.id.root_lay)
    ViewGroup outLayout;
    @Inject
    FunctionPresenter functionPresenter;
    @Inject
    MyToolsPresenter myToolsPresenter;
    @Inject
    EditMyToolsPresenter editMyToolsPresenter;

    private FindAdapter findAdapter;
    private CaculateAdapter caculateAdapter;
    private LaborAdapter laborAdapter;
    List<FunctionBean.BodyBean.FindBean> findList = new ArrayList<>();
    List<FunctionBean.BodyBean.CalculateBean> calculateList = new ArrayList<>();
    List<FunctionBean.BodyBean.LaborBean> laborList = new ArrayList<>();
    List<MyToolsBean.DataBean> recommandlist = new ArrayList<>();
    private RecommandAdapter recommandAdapter;
    List<HashMap<String, Object>> dataSourceList = new ArrayList<>();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    public int getLayoutId() {
        return R.layout.activity_function;
    }


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        functionPresenter.attachView(this);
        myToolsPresenter.attachView(this);
        editMyToolsPresenter.attachView(this);

        recommandAdapter = new RecommandAdapter(getApplicationContext(), recommandlist, R.layout.view_img);
        findAdapter = new FindAdapter(getApplicationContext(), findList, R.layout.view_imgtext);
        caculateAdapter = new CaculateAdapter(getApplicationContext(), calculateList, R.layout.view_imgtext);
        laborAdapter = new LaborAdapter(getApplicationContext(), laborList, R.layout.view_imgtext);
        function_gv.setAdapter(recommandAdapter);
        find_gv.setAdapter(findAdapter);
        caculate_gv.setAdapter(caculateAdapter);
        labor_gv.setAdapter(laborAdapter);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void initDatas() {
        // Log.i("myTag","UserId"+CommonAction.getUserId());
        mLoadingDialog.show();
        //获取缓存常用工具数据
        String cacheData = (String) SpUtils.getParam(Const.MyTools, "");
        Type listType = new TypeToken<List<MyToolsBean.DataBean>>() {
        }.getType();
        Gson gson = new Gson();

        if (cacheData!=null&&!cacheData.isEmpty()){
            List<MyToolsBean.DataBean> mytools = gson.fromJson(cacheData,listType);
            if (mytools!= null){
                recommandlist.clear();
                recommandlist.addAll(mytools);
                MyToolsBean.DataBean tool=new MyToolsBean.DataBean();
                recommandlist.add(tool);
                recommandAdapter.notifyDataSetChanged();
            }

        }
        //获取缓存所有工具数据
        String funtionData = (String) SpUtils.getParam("function", "");
        Type type = new TypeToken<FunctionBean>() {
        }.getType();
        if (funtionData!=null&&!funtionData.isEmpty()){
            FunctionBean functionBean = gson.fromJson(funtionData,type);
            if (functionBean!= null){
                List<FunctionBean.BodyBean.FindBean> findBeanList = functionBean.getBody().getFind();
                List<FunctionBean.BodyBean.CalculateBean> calculateBeanList = functionBean.getBody().getCalculate();
                List<FunctionBean.BodyBean.LaborBean> laborBeanList = functionBean.getBody().getLabor();

                if (findBeanList != null) {
                    findList.clear();
                    findList.addAll(findBeanList);
                    findAdapter.notifyDataSetChanged();
                }
                if (calculateBeanList != null) {
                    calculateList.clear();
                    calculateList.addAll(calculateBeanList);
                    caculateAdapter.notifyDataSetChanged();
                }
                if (laborBeanList != null) {
                    laborList.clear();
                    laborList.addAll(laborBeanList);
                    laborAdapter.notifyDataSetChanged();
                }
            }

        }
        functionPresenter.getFunctionData("type");
        if (CommonAction.getIsLogin()) {
            myToolsPresenter.getMyTools(CommonAction.getUserId());
        } else {
            myToolsPresenter.getMyTools("");
        }

    }

    @Override
    public void eventListener() {

        function_gv.setOnChangeListener(new DragGridView.OnChanageListener() {

            @Override
            public void onChange(int from, int to) {
                    MyToolsBean.DataBean temp = recommandlist.get(from);
                    //直接交互item
//				dataSourceList.set(from, dataSourceList.get(to));
//				dataSourceList.set(to, temp);


                    //这里的处理需要注意下
                    if (from < to) {
                        for (int i = from; i < to; i++) {
                            Collections.swap(recommandlist, i, i + 1);
                        }
                    } else if (from > to) {
                        for (int i = from; i > to; i--) {
                            Collections.swap(recommandlist, i, i - 1);
                        }
                    }

                    recommandlist.set(to, temp);

                    recommandAdapter.notifyDataSetChanged();
                }


        });

    }

    @Override
    public void showError() {
        mLoadingDialog.dismiss();
        ToastUtils.Toast_short(mActivity,ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showData(FunctionBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            Log.i("myTag",new Gson().toJson(dataBean));
            SpUtils.setParam("function",new Gson().toJson(dataBean));
            mLoadingDialog.dismiss();
            List<FunctionBean.BodyBean.FindBean> findBeanList = dataBean.getBody().getFind();
            List<FunctionBean.BodyBean.CalculateBean> calculateBeanList = dataBean.getBody().getCalculate();
            List<FunctionBean.BodyBean.LaborBean> laborBeanList = dataBean.getBody().getLabor();

            if (findBeanList != null) {
                findList.clear();
                findList.addAll(findBeanList);
                findAdapter.notifyDataSetChanged();
            }
            if (calculateBeanList != null) {
                calculateList.clear();
                calculateList.addAll(calculateBeanList);
                caculateAdapter.notifyDataSetChanged();
            }
            if (laborBeanList != null) {
                laborList.clear();
                laborList.addAll(laborBeanList);
                laborAdapter.notifyDataSetChanged();
            }
        }
    }

    private boolean isedit = false;

    @OnClick({R.id.edit_iv, R.id.back_tv})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_iv:
                if (CommonAction.getIsLogin()) {
                    if (isedit) {
                        if (recommandlist.size() > 3 || recommandlist.size() < 3) {
                            ToastUtils.Toast_short(mActivity,"常用工具必须为3个");
                        } else {
                            isedit = false;
                            edit_iv.setImageResource(R.mipmap.icon_edit);
                            changeStatus(isedit);
                            ToolBean toolBean = new ToolBean();
                            toolBean.setUserId(CommonAction.getUserId());
                            List<ToolBean.DataBean> tools = new ArrayList<>();
                            for (int i = 0; i < recommandlist.size(); i++) {
                                ToolBean.DataBean dataBean = new ToolBean.DataBean();
                                dataBean.setToolId(recommandlist.get(i).getToolId());
                                tools.add(dataBean);

                            }
                            toolBean.setTool(tools);
                            mLoadingDialog.show();
                            editMyToolsPresenter.editMyTools(new Gson().toJson(toolBean));
                        }
                    } else {

                        isedit = true;
                        edit_iv.setImageResource(R.mipmap.icon_yes);
                        changeStatus(isedit);
                    }
                }else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.back_tv:
                finish();
                break;
        }
    }

    private void changeStatus(boolean isedit) {
        for (int i=0;i<findList.size();i++){
            findList.get(i).setSelected(false);
        }
        for (int i=0;i<calculateList.size();i++){
            calculateList.get(i).setSelected(false);
        }
        for (int i=0;i<laborList.size();i++){
            laborList.get(i).setSelected(false);
        }
        for (int i = 0; i < findList.size(); i++) {
            for (int j = 0; j < recommandlist.size(); j++) {
                if (recommandlist.get(j).getActionName().equals(findList.get(i).getActionName())) {
                    findList.get(i).setSelected(true);
                }
            }
            findList.get(i).setEdited(isedit);
        }
        findAdapter.notifyDataSetChanged();

        for (int i = 0; i < calculateList.size(); i++) {
            for (int j = 0; j < recommandlist.size(); j++) {
                if (recommandlist.get(j).getActionName().equals(calculateList.get(i).getActionName())) {
                    calculateList.get(i).setSelected(true);
                }
            }
            calculateList.get(i).setEdited(isedit);
        }
        caculateAdapter.notifyDataSetChanged();
        for (int i = 0; i < laborList.size(); i++) {
            for (int j = 0; j < recommandlist.size(); j++) {
                if (recommandlist.get(j).getActionName().equals(laborList.get(i).getActionName())) {
                    laborList.get(i).setSelected(true);
                }
            }
            laborList.get(i).setEdited(isedit);
        }
        laborAdapter.notifyDataSetChanged();
        for (int i = 0; i < recommandlist.size(); i++) {
            recommandlist.get(i).setEdited(isedit);
        }
        recommandAdapter.notifyDataSetChanged();
       // Log.i("function", new Gson().toJson(findList) + "," + new Gson().toJson(calculateList) + "," + new Gson().toJson(laborList));
    }


    @Override
    public void showEditResult(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            mLoadingDialog.dismiss();
            ToastUtils.Toast_short(mActivity,"修改成功");
            CommonAction.refreshMyTools();
        }
    }

    @Override
    public void showMyToolsData(MyToolsBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<MyToolsBean.DataBean> tools = dataBean.getDataBean();
            if (tools != null) {
                recommandlist.clear();
                recommandlist.addAll(tools);
                recommandAdapter.notifyDataSetChanged();
            }
        }
    }

    private class FindAdapter extends CommonAdapter<FunctionBean.BodyBean.FindBean> {

        public FindAdapter(Context context, List<FunctionBean.BodyBean.FindBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, final FunctionBean.BodyBean.FindBean dataBean, int position) {

            holder.setImageByUrl(R.id.img_iv, dataBean.getActionPic(), false);
            holder.setText(R.id.content_tv, dataBean.getActionName());
            final ImageView check_iv = holder.getView(R.id.checkbox);
            if (dataBean.isEdited()) {
                if (dataBean.isSelected()) {
                    check_iv.setImageResource(R.mipmap.check_green);
                } else {
                    check_iv.setImageResource(R.mipmap.icon_checkempty);
                }
                holder.getView(R.id.checkbox).setVisibility(View.VISIBLE);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recommandlist.size() <=3) {
                            dataBean.setSelected(!dataBean.isSelected());
                            notifyDataSetChanged();
                            if (dataBean.isSelected()&&recommandlist.size()!=3) {
                                MyToolsBean.DataBean bean = new MyToolsBean.DataBean();
                                bean.setActionName(dataBean.getActionName());
                                bean.setToolId(dataBean.getToolId());
                                bean.setImgUrl(dataBean.getActionPic());
                                bean.setLink(dataBean.getUrl());
                                bean.setEdited(true);
                                recommandlist.add(bean);
                                recommandAdapter.notifyDataSetChanged();
                            } else if (dataBean.isSelected()&&recommandlist.size()==3){
                                dataBean.setSelected(!dataBean.isSelected());
                                notifyDataSetChanged();
                                ToastUtils.Toast_short(mActivity,"只能选3个常用工具");
                            } else {
                                for (int i = 0; i < recommandlist.size(); i++) {
                                    if (recommandlist.get(i).getActionName().equals(dataBean.getActionName())) {
                                        recommandlist.remove(i);
                                    }
                                }
                                recommandAdapter.notifyDataSetChanged();
                            }
                        } else {
                            ToastUtils.Toast_short(mActivity,"只能选3个常用工具");
                        }
                    }
                });

            } else {
                holder.getView(R.id.checkbox).setVisibility(View.GONE);
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
    }

    private class CaculateAdapter extends CommonAdapter<FunctionBean.BodyBean.CalculateBean> {

        public CaculateAdapter(Context context, List<FunctionBean.BodyBean.CalculateBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, final FunctionBean.BodyBean.CalculateBean dataBean, int position) {

            holder.setImageByUrl(R.id.img_iv, dataBean.getActionPic(), false);
            holder.setText(R.id.content_tv, dataBean.getActionName());
            ImageView check_iv = holder.getView(R.id.checkbox);
            if (dataBean.isEdited()) {
                holder.getView(R.id.checkbox).setVisibility(View.VISIBLE);
                if (dataBean.isSelected()) {
                    check_iv.setImageResource(R.mipmap.check_green);
                } else {
                    check_iv.setImageResource(R.mipmap.icon_checkempty);
                }
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recommandlist.size() <= 3) {
                            dataBean.setSelected(!dataBean.isSelected());
                            notifyDataSetChanged();
                            if (dataBean.isSelected()&&recommandlist.size()!=3) {
                                MyToolsBean.DataBean bean = new MyToolsBean.DataBean();
                                bean.setActionName(dataBean.getActionName());
                                bean.setToolId(dataBean.getToolId());
                                bean.setImgUrl(dataBean.getActionPic());
                                bean.setLink(dataBean.getUrl());
                                bean.setEdited(true);
                                recommandlist.add(bean);
                                recommandAdapter.notifyDataSetChanged();
                            }  else if (dataBean.isSelected()&&recommandlist.size()==3){
                                dataBean.setSelected(!dataBean.isSelected());
                                notifyDataSetChanged();
                                ToastUtils.Toast_short(mActivity,"只能选3个常用工具");
                            } else {

                                for (int i = 0; i < recommandlist.size(); i++) {
                                    if (recommandlist.get(i).getActionName().equals(dataBean.getActionName())) {
                                        recommandlist.remove(i);
                                    }
                                }
                                recommandAdapter.notifyDataSetChanged();
                            }
                        } else {
                            ToastUtils.Toast_short(mActivity,"只能选3个常用工具");
                        }
                    }
                });
            } else {
                holder.getView(R.id.checkbox).setVisibility(View.GONE);
                final String url = dataBean.getUrl();
                holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        if (url != null && !url.isEmpty()) {
                            Intent intent = new Intent(mContext, DateCalculateActivity.class);
                            intent.putExtra(Const.webUrl, url);
                            startActivity(intent);
                        }
                    }
                });
            }

        }
    }

    private class LaborAdapter extends CommonAdapter<FunctionBean.BodyBean.LaborBean> {

        public LaborAdapter(Context context, List<FunctionBean.BodyBean.LaborBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, final FunctionBean.BodyBean.LaborBean dataBean, int position) {
            ImageView check_iv = holder.getView(R.id.checkbox);
            holder.setImageByUrl(R.id.img_iv, dataBean.getActionPic(), false);
            holder.setText(R.id.content_tv, dataBean.getActionName());
            if (dataBean.isEdited()) {
                if (dataBean.isSelected()) {
                    check_iv.setImageResource(R.mipmap.check_green);
                } else {
                    check_iv.setImageResource(R.mipmap.icon_checkempty);
                }
                holder.getView(R.id.checkbox).setVisibility(View.VISIBLE);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recommandlist.size() <=3) {
                            dataBean.setSelected(!dataBean.isSelected());
                            notifyDataSetChanged();
                            if (dataBean.isSelected()&&recommandlist.size()!=3) {
                                MyToolsBean.DataBean bean = new MyToolsBean.DataBean();
                                bean.setActionName(dataBean.getActionName());
                                bean.setToolId(dataBean.getToolId());
                                bean.setImgUrl(dataBean.getActionPic());
                                bean.setLink(dataBean.getUrl());
                                bean.setEdited(true);
                                recommandlist.add(bean);
                                recommandAdapter.notifyDataSetChanged();
                            } else if (dataBean.isSelected()&&recommandlist.size()==3){
                                dataBean.setSelected(!dataBean.isSelected());
                                notifyDataSetChanged();
                                ToastUtils.Toast_short(mActivity,"只能选3个常用工具");
                            } else {
                                for (int i = 0; i < recommandlist.size(); i++) {
                                    if (recommandlist.get(i).getActionName().equals(dataBean.getActionName())) {
                                        recommandlist.remove(i);
                                    }
                                }
                                recommandAdapter.notifyDataSetChanged();
                            }
                        } else {
                            ToastUtils.Toast_short(mActivity,"只能选3个常用工具");
                        }
                    }
                });
            } else {
                holder.getView(R.id.checkbox).setVisibility(View.GONE);
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
    }

    public class RecommandAdapter extends CommonAdapter<MyToolsBean.DataBean>{
        private int mHidePosition = -1;

        public RecommandAdapter(Context context, List<MyToolsBean.DataBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, MyToolsBean.DataBean dataBean, final int position) {

            holder.setImageByUrl(R.id.img_iv, dataBean.getImgUrl(), false);
            holder.setImageResource(R.id.icon_dele, R.mipmap.icon_dele);
            holder.setText(R.id.content_tv, dataBean.getActionName());
            if (dataBean.isEdited()) {
                holder.setOnClickListener(R.id.icon_dele, new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        recommandlist.remove(position);
                        changeStatus(true);
                    }
                });
                holder.getView(R.id.icon_dele).setVisibility(View.VISIBLE);
                holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {

                    }
                });
            } else {
                holder.getView(R.id.icon_dele).setVisibility(View.GONE);
                final String url = dataBean.getLink();
                holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        if (url != null && !url.isEmpty()) {
                            Intent intent = new Intent(mContext, WebActivity.class);
                            intent.putExtra(Const.webUrl, url);
                            mContext.startActivity(intent);
                        }
                    }
                });
            }

        }
    }

}
