package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.CaseDetailBean;
import com.dream.NiuFaNet.Bean.CaseListBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.CommonBean1;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CaseContract;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CasePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hou on 2018/4/18.
 */

public class SearchCaseActivity extends CommonActivity implements CaseContract.View{
    @Bind(R.id.search_edt)
    EditText search_edt;
    @Bind(R.id.search_relay)
    RelativeLayout search_relay;
    @Bind(R.id.smart_refreshlay)
    SmartRefreshLayout smart_refreshlay;
    @Bind(R.id.case_lv)
    MyListView case_lv;
    private String keyWord="";
    private InputMethodManager imm;
    private CaseAdapter caseAdapter;
    private String searchValue="";
    private List<CaseListBean.DataBean> caseList=new ArrayList<>();
    @Inject
    CasePresenter casePresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_searchcase;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        casePresenter.attachView(this);
        caseAdapter=new CaseAdapter(mContext,caseList,R.layout.view_case);
        case_lv.setAdapter(caseAdapter);
        //打开软键盘
        imm = ImmUtils.getImm(this);
    }
    @Override
    public void initDatas() {


    }

    @Override
    public void showCaseList(CaseListBean dataBean) {
        smart_refreshlay.finishRefresh();
        smart_refreshlay.finishLoadmore();
        mLoadingDialog.dismiss();
        if (dataBean.getError().equals(Const.success)){
            List<CaseListBean.DataBean> data = dataBean.getData();
            if (data!=null&&data.size()>0){
                caseList.clear();
                caseList.addAll(data);
                caseAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showCaseDetail(CaseDetailBean dataBean) {

    }

    @Override
    public void showAddCaseResult(CommonBean dataBean) {

    }

    @Override
    public void showValidateCaseResult(CommonBean1 dataBean) {

    }

    @Override
    public void showError() {
        mLoadingDialog.dismiss();
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
        smart_refreshlay.finishLoadmore();
        smart_refreshlay.finishRefresh();
    }

    //案件列表
    public class CaseAdapter extends CommonAdapter<CaseListBean.DataBean>{

        public CaseAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);

        }

        @Override
        public void convert(com.dream.NiuFaNet.Base.BaseViewHolder helper, final CaseListBean.DataBean item, int position) {
            helper.setText(R.id.casename_tv,item.getCaseName());
            helper.setText(R.id.price_tv,"¥"+item.getPrice());
            helper.setText(R.id.address_tv,item.getProvince()+"-"+item.getCity());

            if (item.getIsBid().equals("1")){
                helper.setText(R.id.status_tv,"招募中");
            }else if (item.getIsBid().equals("2")){
                helper.setText(R.id.status_tv,"已完成");
            }
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonAction.getIsLogin()) {
                        Intent intent = new Intent();
                        intent.putExtra("caseId", item.getCaseId());
                        intent.setClass(mContext, CaseDetailActivity.class);
                        startActivity(intent);
                    }else {
                        DialogUtils.getLoginTip(mActivity);
                    }
                }
            });
        }
    }
    private int pageNum = 1;
    private boolean isLoadMore;
    @Override
    public void eventListener() {
        smart_refreshlay.setEnableRefresh(true);
        smart_refreshlay.setEnableLoadmore(true);
        smart_refreshlay.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadMore = false;
                pageNum = 1;
                Map<String, String> parmas = MapUtils.getMap();
                parmas.put("searchValues",searchValue);
                parmas.put("city","");
                parmas.put("pageNow",String.valueOf(pageNum));
                casePresenter.selectCaseInfoList(parmas);


            }
        });
        smart_refreshlay.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                pageNum++;
                Map<String, String> parmas = MapUtils.getMap();
                parmas.put("searchValues",searchValue);
                parmas.put("city","");
                parmas.put("pageNow",String.valueOf(pageNum));
                casePresenter.selectCaseInfoList(parmas);

            }
        });
    }

    @OnClick({R.id.back_relay, R.id.search_relay,R.id.clear_iv})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back_relay:
                ImmUtils.hideImm(mActivity,imm);
                finish();
                break;
            case R.id.search_relay:
                 keyWord = search_edt.getText().toString();
                if (keyWord.isEmpty()){
                    ToastUtils.Toast_short("请输入搜索关键字");
                }else {
                    Map<String, String> parmas = MapUtils.getMapInstance();
                    parmas.put("searchValues",keyWord);
                    parmas.put("city","");
                    parmas.put("pageNow",String.valueOf(pageNum));
                    casePresenter.selectCaseInfoList(parmas);
                    mLoadingDialog.show();
                }
                break;
            case R.id.clear_iv:
                search_edt.setText("");
                break;

        }
    }



}
