package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dream.NiuFaNet.Adapter.FunctionViewAdapter;
import com.dream.NiuFaNet.Adapter.FunctionViewAdapter1;
import com.dream.NiuFaNet.Adapter.RecommandAdapter;
import com.dream.NiuFaNet.Base.BaseActivity;
import com.dream.NiuFaNet.Base.BaseActivityRelay;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.FunctionBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.FunctionContract;
import com.dream.NiuFaNet.CustomView.DragGridView;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.FunctionPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.haibin.calendarview.CalendarView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by ljq on 2018/3/24.
 */

public class FunctionActivity extends BaseActivity implements FunctionContract.View ,View.OnClickListener{
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
    @Inject
    FunctionPresenter functionPresenter;

    private FindAdapter findAdapter;
    private CaculateAdapter caculateAdapter;
    private LaborAdapter laborAdapter;
    List<FunctionBean.BodyBean.FindBean> findList = new ArrayList<>();
    List<FunctionBean.BodyBean.CalculateBean> calculateList = new ArrayList<>();
    List<FunctionBean.BodyBean.LaborBean> laborList = new ArrayList<>();
    List<FunctionBean.BodyBean.FindBean> recommandlist = new ArrayList<>();
    private RecommandAdapter recommandAdapter;
    List<HashMap<String, Object>> dataSourceList = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_function;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
        functionPresenter.attachView(this);
        recommandAdapter=new RecommandAdapter(getApplicationContext(),recommandlist,R.layout.view_img);
        //function_gv.setAdapter(new FunctionViewAdapter1(getApplicationContext(),titles));
        findAdapter = new FindAdapter(getApplicationContext(),findList,R.layout.view_imgtext);
        caculateAdapter = new CaculateAdapter(getApplicationContext(),calculateList,R.layout.view_imgtext);
        laborAdapter = new LaborAdapter(getApplicationContext(),laborList,R.layout.view_imgtext);
        function_gv.setAdapter(recommandAdapter);
        find_gv.setAdapter(findAdapter);
        caculate_gv.setAdapter(caculateAdapter);
        labor_gv.setAdapter(laborAdapter);
    }

    @Override
    public void loadResum() {

    }

    @Override
    public void initDatas() {
       // Log.i("myTag","UserId"+CommonAction.getUserId());
        functionPresenter.getFunctionData(CommonAction.getUserId());
    }

    @Override
    public void eventListener() {

    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {

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
                recommandlist.add(findList.get(0));
                recommandlist.add(findList.get(1));
                recommandlist.add(findList.get(2));
                recommandAdapter.notifyDataSetChanged();
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
    private boolean isedit=false;
    @OnClick({R.id.edit_iv})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_iv:
                if (isedit){
                    isedit=false;
                    edit_iv.setImageResource(R.mipmap.icon_edit);
                    changeStatus(isedit);
                }else {
                    isedit=true;
                    edit_iv.setImageResource(R.mipmap.icon_yes);
                    changeStatus(isedit);
                }
                break;
        }
    }

    private void changeStatus(boolean isedit) {
        for (int i=0;i<findList.size();i++){
            findList.get(i).setEdited(isedit);
        }
        findAdapter.notifyDataSetChanged();
        for (int i=0;i<calculateList.size();i++){
            calculateList.get(i).setEdited(isedit);
        }
        caculateAdapter.notifyDataSetChanged();
        for (int i=0;i<laborList.size();i++){
            laborList.get(i).setEdited(isedit);
        }
        laborAdapter.notifyDataSetChanged();
        for (int i=0;i<recommandlist.size();i++){
            recommandlist.get(i).setEdited(isedit);
        }
        recommandAdapter.notifyDataSetChanged();
    }

    private class FindAdapter extends CommonAdapter<FunctionBean.BodyBean.FindBean> {

        public FindAdapter(Context context, List<FunctionBean.BodyBean.FindBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, FunctionBean.BodyBean.FindBean dataBean, int position) {

            holder.setImageByUrl(R.id.img_iv,dataBean.getActionPic(),false);
            holder.setText(R.id.content_tv,dataBean.getActionName());
            if (dataBean.isEdited()){
                holder.getView(R.id.checkbox).setVisibility(View.VISIBLE);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else {
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
    private class CaculateAdapter extends CommonAdapter<FunctionBean.BodyBean.CalculateBean>{

        public CaculateAdapter(Context context, List<FunctionBean.BodyBean.CalculateBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, FunctionBean.BodyBean.CalculateBean dataBean, int position) {

            holder.setImageByUrl(R.id.img_iv,dataBean.getActionPic(),false);
            holder.setText(R.id.content_tv,dataBean.getActionName());
            if (dataBean.isEdited()){
                holder.getView(R.id.checkbox).setVisibility(View.VISIBLE);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else {
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
    private class LaborAdapter extends CommonAdapter<FunctionBean.BodyBean.LaborBean>{

        public LaborAdapter(Context context, List<FunctionBean.BodyBean.LaborBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, FunctionBean.BodyBean.LaborBean dataBean, int position) {

            holder.setImageByUrl(R.id.img_iv,dataBean.getActionPic(),false);
            holder.setText(R.id.content_tv,dataBean.getActionName());
            if (dataBean.isEdited()){
                holder.getView(R.id.checkbox).setVisibility(View.VISIBLE);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else {
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


}
