package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.CaseDetailBean;
import com.dream.NiuFaNet.Bean.CaseListBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.CommonBean1;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CaseContract;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CasePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Fragment.ProjectFragment;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.HiddenAnimUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class CaseCenterActivity extends CommonActivity implements CaseContract.View{
    @Bind(R.id.case_lv)
    MyListView case_lv;
    private CaseAdapter caseAdapter;
    @Bind(R.id.smart_refreshlay)
    SmartRefreshLayout smart_refreshlay;
    @Bind(R.id.paixu_lay)
    LinearLayout paixu_lay;
    @Bind(R.id.shaixuan_lay)
    LinearLayout shaixuanlay;
    @Bind(R.id.sort_tv)
    TextView sort_tv;
    @Bind(R.id.sort1_tv)
    TextView sort1_tv;
    @Bind(R.id.sort_iv)
    ImageView sort_iv;
    @Bind(R.id.sort1_iv)
    ImageView sort1_iv;
    @Bind(R.id.sort_lay)
    LinearLayout sort_lay;
    @Bind(R.id.sort_lv)
    MyListView sort_lv;
    @Bind(R.id.bg_v)
    View bg_v;
    private boolean isLoadMore;
    private List<CaseListBean.DataBean> caseList=new ArrayList<>();
    private int page=1;
    private String searchValue="";
    private List<SortBean> sortList = new ArrayList<>();
    private List<SortBean> sortList1 = new ArrayList<>();
    private SortAdapter sortAdater;
    private SortAdapter1 sortAdater1;
    @Override
    public int getLayoutId() {
        return R.layout.activity_casecenter;
    }
    @Inject
    CasePresenter casePresenter;
    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        casePresenter.attachView(this);

        initSortData();
        caseAdapter=new CaseAdapter(mContext,caseList,R.layout.view_case);
        case_lv.setAdapter(caseAdapter);
        sortAdater = new SortAdapter(mContext, sortList, R.layout.view_sort);
        sortAdater1 = new SortAdapter1(mContext, sortList1, R.layout.view_sort);
        getCaseList();
    }
    //获取案件列表
    private void getCaseList() {
        Map<String, String> parmas = MapUtils.getMap();
        parmas.put("searchValues",searchValue);
        parmas.put("city","");
        parmas.put("pageNow",String.valueOf(page));
        casePresenter.selectCaseInfoList(parmas);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {
        smart_refreshlay.setEnableRefresh(true);
        smart_refreshlay.setEnableLoadmore(true);
        smart_refreshlay.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadMore = false;
                page=1;
                Map<String, String> parmas = MapUtils.getMap();
                parmas.put("searchValues",searchValue);
                parmas.put("city","");
                parmas.put("pageNow",String.valueOf(page));
                casePresenter.selectCaseInfoList(parmas);
            }
        });
        smart_refreshlay.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                page++;
                Map<String, String> parmas = MapUtils.getMap();
                parmas.put("searchValues",searchValue);
                parmas.put("city","");
                parmas.put("pageNow",String.valueOf(page));
                casePresenter.selectCaseInfoList(parmas);
            }
        });
    }

    //点击事件
    @OnClick({R.id.back_relay,R.id.search_relay,R.id.paixu_lay,R.id.shaixuan_lay,R.id.bg_v})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.back_relay:
                finish();
                break;
            //排序
            case R.id.paixu_lay:
                //morepop.showAsDropDown(view);
                bgIsVisible();
                sort_tv.setTextColor(getResources().getColor(R.color.blue_title));
                sort1_tv.setTextColor(getResources().getColor(R.color.black));
                sort_iv.setImageResource(R.mipmap.down_blue);
                sort1_iv.setImageResource(R.mipmap.down);
                sort_lv.setAdapter(sortAdater);
                sortAdater.notifyDataSetChanged();
                HiddenAnimUtils.newInstance(mContext, sort_lay, sort_iv, sortList.size() * 50).toggle();
                break;
            //搜索项目
            case R.id.search_relay:
                startActivity(new Intent(mContext, SearchCaseActivity.class));
                break;
            //筛选
            case R.id.shaixuan_lay:
                bgIsVisible();
                sort1_iv.setImageResource(R.mipmap.down_blue);
                sort_iv.setImageResource(R.mipmap.down);
                sort1_tv.setTextColor(getResources().getColor(R.color.blue_title));
                sort_tv.setTextColor(getResources().getColor(R.color.black));
                sort_lv.setAdapter(sortAdater1);
                sortAdater1.notifyDataSetChanged();
                HiddenAnimUtils.newInstance(mContext, sort_lay, sort1_iv, sortList1.size() * 50).toggle();
                break;
            case R.id.bg_v:
                sort_lay.setVisibility(View.GONE);
                bg_v.setVisibility(View.GONE);
                break;
        }
    }
    private void bgIsVisible() {
        if (View.VISIBLE == sort_lay.getVisibility()) {
            //布局半透明
            bg_v.setVisibility(View.GONE);
        } else {
            bg_v.setVisibility(View.VISIBLE);
        }
    }
    //案件列表返回信息
    @Override
    public void showCaseList(CaseListBean dataBean) {
        smart_refreshlay.finishRefresh();
        smart_refreshlay.finishLoadmore();
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
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        smart_refreshlay.finishLoadmore();
        smart_refreshlay.finishRefresh();
    }

    //案件列表
    public class CaseAdapter extends CommonAdapter<CaseListBean.DataBean>{

        public CaseAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);

        }

        @Override
        public void convert(BaseViewHolder helper, final CaseListBean.DataBean item, int position) {
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
    /**
     * 排序适配器
     */
    public class SortAdapter extends CommonAdapter<SortBean> {

        public SortAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final SortBean item, int position) {
            ImageView check_iv = helper.getView(R.id.check_iv);
            final TextView sortTv = helper.getView(R.id.sort_tv);
            sortTv.setText(item.getSortName());
            if (item.isSelect()) {
                check_iv.setVisibility(View.VISIBLE);
                sortTv.setTextColor(getResources().getColor(R.color.blue_title));
            } else {
                check_iv.setVisibility(View.GONE);
                sortTv.setTextColor(getResources().getColor(R.color.black));
            }
            helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (item.isSelect()) {
                        item.setSelect(false);
                    } else {
                        int count = 0;
                        for (int i = 0; i < mDatas.size(); i++) {
                            if (mDatas.get(i).isSelect()) {
                                count++;
                            }
                        }
                        if (count > 1) {
                            item.setSelect(false);

                        } else {
                            for (int i = 0; i < mDatas.size(); i++) {
                                mDatas.get(i).setSelect(false);

                            }
                            item.setSelect(true);
                        }
                    }
                    notifyDataSetChanged();
                    sort_tv.setText(item.getSortName());
                }
            });

        }
    }

    /**
     * 筛选适配器
     */
    public class SortAdapter1 extends CommonAdapter<SortBean> {

        public SortAdapter1(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final SortBean item, int position) {
            ImageView check_iv = helper.getView(R.id.check_iv);
            final TextView sort_tv = helper.getView(R.id.sort_tv);
            sort_tv.setText(item.getSortName());
            if (item.isSelect()) {
                check_iv.setVisibility(View.VISIBLE);
                sort_tv.setTextColor(getResources().getColor(R.color.blue_title));

            } else {
                check_iv.setVisibility(View.GONE);
                sort_tv.setTextColor(getResources().getColor(R.color.black));
            }

            helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (item.isSelect()) {
                        item.setSelect(false);
                    } else {
                        int count = 0;
                        for (int i = 0; i < mDatas.size(); i++) {
                            if (mDatas.get(i).isSelect()) {
                                count++;
                            }
                        }
                        if (count > 1) {
                            item.setSelect(false);

                        } else {
                            for (int i = 0; i < mDatas.size(); i++) {
                                mDatas.get(i).setSelect(false);

                            }
                            item.setSelect(true);
                        }
                    }
                    notifyDataSetChanged();
                    sort1_tv.setText(item.getSortName());
                }
            });

        }
    }

    /**
     * 筛选和排序的Bean类
     */
    public class SortBean {
        private String sortName;
        private boolean select;
        private String sortField;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSortName() {
            return sortName;
        }

        public void setSortName(String sortName) {
            this.sortName = sortName;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public String getSortField() {
            return sortField;
        }

        public void setSortField(String sortField) {
            this.sortField = sortField;
        }
    }
    /**
     * 初始化排序和筛选内容数据
     */
    private void initSortData() {
        SortBean sort7 = new SortBean();
        sort7.setSelect(false);
        sort7.setSortName("深圳");
        sort7.setStatus("city");
        sortList1.add(sort7);
        SortBean sort1 = new SortBean();
        sort1.setSelect(false);
        sort1.setSortName("上海");
        sort1.setStatus("0");
        sortList1.add(sort1);
        SortBean sort2 = new SortBean();
        sort2.setSelect(false);
        sort2.setSortName("北京");
        sort2.setStatus("1");
        sortList1.add(sort2);
        SortBean sort3 = new SortBean();
        sort3.setSelect(false);
        sort3.setSortName("广州");
        sort3.setStatus("2");
        sortList1.add(sort3);

        SortBean sort4 = new SortBean();
        sort4.setSelect(false);
        sort4.setSortName("招募中");
        sort4.setSortField("name");
        sortList.add(sort4);
        SortBean sort5 = new SortBean();
        sort5.setSelect(false);
        sort5.setSortName("已完成");
        sortList.add(sort5);

    }
}
