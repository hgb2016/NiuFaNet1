package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.Contact;
import com.dream.NiuFaNet.Bean.ProgramListBean;
import com.dream.NiuFaNet.Bean.ProjectClientListBean;
import com.dream.NiuFaNet.Bean.SearchUserBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ProgramContract;
import com.dream.NiuFaNet.Contract.SearchUserContract;
import com.dream.NiuFaNet.Contract.SimFrendsContract;
import com.dream.NiuFaNet.CustomView.Emptyview_Pro;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ProgramPresenter;
import com.dream.NiuFaNet.Presenter.SearchUserPresenter;
import com.dream.NiuFaNet.Presenter.SimFrendsPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Fragment.ProjectFragment;
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.FrendsUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.LocalGroupSearch;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.kevin.wraprecyclerview.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hou on 2018/4/18.
 */

public class SearchProjectActivity extends CommonActivity  implements ProgramContract.View{
    @Bind(R.id.search_edt)
    EditText search_edt;
    @Bind(R.id.search_relay)
    RelativeLayout search_relay;
    @Inject
    ProgramPresenter programPresenter;
    @Bind(R.id.smart_refreshlay)
    SmartRefreshLayout smart_refreshlay;
    @Bind(R.id.project_rv)
    WrapRecyclerView project_rv;
    private ProgramAdapter programAdapter;
    private List<ProgramListBean.DataBean> dataList = new ArrayList<>();
    private String keyWord="";
    private InputMethodManager imm;

    @Override
    public int getLayoutId() {
        return R.layout.activity_searchproject;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        programPresenter.attachView(this);
        RvUtils.setOptionnoLine(project_rv, mActivity);
        View view = new View(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(1));
        view.setLayoutParams(params);
        project_rv.addHeaderView(view);
        programAdapter = new ProgramAdapter(R.layout.rvitem_program,dataList);
       // programAdapter.setEmptyView(new Emptyview_Pro(mActivity));
        project_rv.setAdapter(programAdapter);
        //打开软键盘
        imm = ImmUtils.getImm(this);
    }
    private String sortField = "createTime";
    @Override
    public void initDatas() {
        Map<String, String> map = MapUtils.getMapInstance();
        map.put("sortField",sortField);

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
                Map<String, String> map = MapUtils.getMapInstance();
                map.put("sortField",sortField);
                map.put("keyWord",keyWord);
                map.put("page",String.valueOf(pageNum));
                programPresenter.getProgramList(CommonAction.getUserId(),map);


            }
        });
        smart_refreshlay.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                pageNum++;
                Map<String,String> map = new HashMap<String, String>();
                map.put("page",String.valueOf(pageNum));
                map.put("keyWord",keyWord);
                map.put("sortField",sortField);
                programPresenter.getProgramList(CommonAction.getUserId(),map);


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
                    Map<String, String> mapInstance = MapUtils.getMapInstance();
                    mapInstance.put("keyWord",keyWord);
                    mLoadingDialog.show();
                    loadData(mapInstance);
                }
                break;
            case R.id.clear_iv:
                search_edt.setText("");
                break;

        }
    }
    private void loadData(Map<String, String> map) {
        isLoadMore = false;
        programPresenter.getProgramList(CommonAction.getUserId(),map);
    }
    @Override
    public void showError() {
        smart_refreshlay.finishRefresh();
        ToastUtils.Toast_short(mActivity,ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
        smart_refreshlay.finishRefresh();
    }

    @Override
    public void showData(ProgramListBean dataBean) {
        smart_refreshlay.finishRefresh();
        smart_refreshlay.finishLoadmore();
        if (dataBean.getError().equals(Const.success)) {
            List<ProgramListBean.DataBean> data = dataBean.getData();
            if (!isLoadMore){
                dataList.clear();
            }
            if (data != null&&data.size()>0) {
                dataList.addAll(data);
                dataList.get(0).setExpand(true);
            }else {
                ToastUtils.Toast_short(mActivity," 没有搜索到项目");
            }
            programAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showProjectClientList(ProjectClientListBean dataBean) {

    }

    //展开列表
    public class ProgramAdapter extends BaseQuickAdapter<ProgramListBean.DataBean, BaseViewHolder> {


        public ProgramAdapter(@LayoutRes int layoutResId, @Nullable List<ProgramListBean.DataBean> data) {
            super(layoutResId, data);
        }
        @Override
        protected void convert(com.chad.library.adapter.base.BaseViewHolder holder, final ProgramListBean.DataBean baseBean) {
            TextView proname_tv = holder.getView(R.id.proname_tv);
            proname_tv.setText(baseBean.getName());
            ListView procal_rv = holder.getView(R.id.pro_cal_rv);
            List<ProgramListBean.DataBean.ScheduleBean> schedule = baseBean.getSchedule();
            ImageView prostatu_iv = holder.getView(R.id.prostatu_iv);
            TextView prostatu_tv = holder.getView(R.id.prostatu_tv);
            final ImageView spand_iv = holder.getView(R.id.spand_iv);
            LinearLayout right_lay = holder.getView(R.id.right_lay);
            if (baseBean.getStatus().equals("2")) {
                prostatu_tv.setText("已完成");
                prostatu_tv.setBackgroundResource(R.drawable.shape_projectstatus1);
                prostatu_iv.setImageResource(R.mipmap.end);
                proname_tv.setTextColor(ResourcesUtils.getColor(R.color.color6c));
            }else if (baseBean.getStatus().equals("0")){
                prostatu_tv.setText("进行中");
                prostatu_tv.setBackgroundResource(R.drawable.shape_projectstatus);
                prostatu_iv.setImageResource(R.mipmap.ing);
                proname_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
            }else if (baseBean.getStatus().equals("1")){
                prostatu_tv.setText("洽谈中");
                prostatu_tv.setBackgroundResource(R.drawable.shape_projectstatus);
                prostatu_iv.setImageResource(R.mipmap.ing);
                proname_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
            }
            if (schedule.size() != 0) {
                holder.getView(R.id.head_view).setVisibility(View.VISIBLE);
                holder.getView(R.id.foot_view).setVisibility(View.VISIBLE);
            } else {
                holder.getView(R.id.head_view).setVisibility(View.GONE);
                holder.getView(R.id.foot_view).setVisibility(View.GONE);
            }

            ProCalAdapter proCalAdapter = new ProCalAdapter(mContext, schedule, R.layout.view_projectdetail);
            procal_rv.setAdapter(proCalAdapter);
            final LinearLayout spand_lay = holder.getView(R.id.spand_lay);

            if (baseBean.isExpand()){
                spand_lay.setVisibility(View.VISIBLE);
                spand_iv.setImageResource(R.mipmap.up_black);
            }else {
                spand_lay.setVisibility(View.GONE);
                spand_iv.setImageResource(R.mipmap.down_black);
            }
            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (baseBean.getId() != null) {
                        IntentUtils.toActivityWithTag(ProjectDetailActivity.class, mActivity, baseBean.getId(), 006);
                    }
                }
            });

            right_lay.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (!baseBean.isExpand()){
                        spand_lay.setVisibility(View.VISIBLE);
                        baseBean.setExpand(true);
                    }else {
                        spand_lay.setVisibility(View.GONE);
                        baseBean.setExpand(false);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    public class ProCalAdapter extends CommonAdapter<ProgramListBean.DataBean.ScheduleBean> {

        public ProCalAdapter(Context context, List<ProgramListBean.DataBean.ScheduleBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void convert(com.dream.NiuFaNet.Base.BaseViewHolder holder, final ProgramListBean.DataBean.ScheduleBean dataBean, int position) {
            ImageView status_iv = holder.getView(R.id.status_iv);
            ImageView apendix_iv = holder.getView(R.id.apendix_iv);
            TextView username_tv = holder.getView(R.id.username_tv);
            TextView time_tv = holder.getView(R.id.time_tv);
            TextView title_tv = holder.getView(R.id.title_tv);
            TextView duringtime_tv = holder.getView(R.id.duringtime_tv);
            if (Integer.parseInt(dataBean.getFileCount())<1){
                apendix_iv.setVisibility(View.GONE);
            }else {
                apendix_iv.setVisibility(View.VISIBLE);
            }
            Date endD = DateFormatUtil.getTime(dataBean.getEndTime(), Const.YMD_HMS);
            Calendar calendar = Calendar.getInstance();
            if (calendar.getTimeInMillis() > endD.getTime()) {
                status_iv.setImageResource(R.drawable.shape_circle_dot);
                title_tv.setTextColor(ResourcesUtils.getColor(R.color.outdatecolor));
            } else {
                title_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
                status_iv.setImageResource(R.mipmap.dot1);
            }
            username_tv.setText(dataBean.getCreateUserName());
            holder.setImageByUrl(R.id.head_iv,dataBean.getHeadUrl(),true);
            duringtime_tv.setText(dataBean.getHourNum()+"小时");
            Date beDa = DateFormatUtil.getTime(dataBean.getBeginTime(), Const.YMD_HMS);
            String beStr = DateFormatUtil.getTime(beDa, Const.HM);
            String beStr1 = DateFormatUtil.getTime(beDa, Const.YMD_HM);
            String beYMD = DateFormatUtil.getTime(beDa, Const.Y_M_D);
            Date enDa = DateFormatUtil.getTime(dataBean.getEndTime(), Const.YMD_HMS);
            int dayExpend = CalculateTimeUtil.getDayExpend(beDa.getTime(), enDa.getTime());
            String endStr = DateFormatUtil.getTime(enDa, Const.HM);
            String endStr1 = DateFormatUtil.getTime(enDa, Const.YMD_HM);
            if (dayExpend>=1){
                time_tv.setText(beStr1+" — "+endStr1);
            }else {
                time_tv.setText(beYMD+"  "+beStr+" — "+endStr);
            }
            title_tv.setText(dataBean.getTitle());
            holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    String scheduleId = dataBean.getScheduleId();
                    if (scheduleId != null && !scheduleId.isEmpty()) {
                        Intent intent = new Intent(mContext, CalenderDetailActivity.class);
                        intent.putExtra(Const.scheduleId, scheduleId);
                        startActivity(intent);
                    }
                }
            });
           /* String fileCount = dataBean.getFileCount();
            if (StringUtil.NoNullOrEmpty(fileCount)&&!fileCount.equals("0")){
//                helper.getView(R.id.att_iv).setVisibility(View.VISIBLE);
            }else {
                helper.getView(R.id.att_iv).setVisibility(View.GONE);
            }
            if (position == mDatas.size() - 1) {
                helper.getView(R.id.xuxian_view).setVisibility(View.GONE);
            } else {
                helper.getView(R.id.xuxian_view).setVisibility(View.VISIBLE);
            }
            String beginTime = dataBean.getBeginTime();
            String endTime = dataBean.getEndTime();
            Date beginD = DateFormatUtil.getTime(beginTime, Const.YMD_HMS);
            Date endD = DateFormatUtil.getTime(endTime, Const.YMD_HMS);
            long endLongTime = endD.getTime();
            String durationTime = DateUtil.getTimeDimen_onlyHour(beginD.getTime(), endLongTime);
            String beginS = DateFormatUtil.getTime(beginD, Const.Y_M_D);
            helper.setText(R.id.time_duration, beginS + "   " + durationTime);
            Calendar calendar = Calendar.getInstance();
            TextView work_tv = helper.getView(R.id.work_title);
            ImageView dot_iv = helper.getView(R.id.dot_iv);
            if (calendar.getTimeInMillis()>endLongTime){
                work_tv.setTextColor(ResourcesUtils.getColor(R.color.color6c));
                dot_iv.setImageResource(R.drawable.shape_circle_dot);
            }else {
                dot_iv.setImageResource(R.drawable.shape_circle_voice);
                work_tv.setTextColor(ResourcesUtils.getColor(R.color.color2a));
            }
            work_tv.setText(dataBean.getTitle());*/


        }

    }
}
