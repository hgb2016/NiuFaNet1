package com.dream.NiuFaNet.Ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dream.NiuFaNet.Base.BaseFragmentV4;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.BusBean.LoginBus;
import com.dream.NiuFaNet.Bean.BusBean.RefreshProBean;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.ProgramListBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ProgramContract;
import com.dream.NiuFaNet.CustomView.Emptyview_Pro;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ProgramPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.CalenderDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.FriendCalenderActivity;
import com.dream.NiuFaNet.Ui.Activity.NewProgramActivity;
import com.dream.NiuFaNet.Ui.Activity.ProgramDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.ProjectDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.SearchProjectActivity;
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.DateUtil;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.PopWindowUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.StringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.dream.NiuFaNet.group.GroupItemDecoration;
import com.dream.NiuFaNet.group.GroupRecyclerAdapter;
import com.dream.NiuFaNet.group.GroupRecyclerView;
import com.kevin.wraprecyclerview.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hou on 2018/4/16.
 */

public class ProjectFragment extends BaseFragmentV4 implements ProgramContract.View {
    @Bind(R.id.project_rv)
    WrapRecyclerView  project_rv;
    private ProjectAdapter projectadapter;
    @Inject
    ProgramPresenter programPresenter;
    @Bind(R.id.smart_refreshlay)
    SmartRefreshLayout smart_refreshlay;
    @Bind(R.id.sort_tv)
    TextView sort_tv;
    private List<ProgramListBean.DataBean> dataList = new ArrayList<>();
    private PopupWindow morepop;
    private ProgramAdapter programAdapter;

    @Override
    public void initView() {
        //
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        programPresenter.attachView(this);
        initTopPopwindow();
        /*EventBus.getDefault().register(this);
        mLoadingDialog = DialogUtils.initLoadingDialog(getActivity());
*/
    /*          //模拟数据
        project_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        GroupItemDecoration<String, List> itemDecoration = new GroupItemDecoration<>();
        project_rv.addItemDecoration(itemDecoration);*/
        RvUtils.setOptionnoLine(project_rv, getActivity());
        View view = new View(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(1));
        view.setLayoutParams(params);
        project_rv.addHeaderView(view);
        programAdapter = new ProgramAdapter(R.layout.rvitem_program,dataList);
        programAdapter.setEmptyView(new Emptyview_Pro(getContext()));
        project_rv.setAdapter(programAdapter);
        initTopPopwindow();
        EventBus.getDefault().register(this);
        mLoadingDialog = DialogUtils.initLoadingDialog(getActivity());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initResume() {

    }

    private int pageNum = 2;
    private boolean isLoadMore;
    private String sortField = "createTime";
    @Override
    public void initEvents() {
        smart_refreshlay.setEnableRefresh(true);
        smart_refreshlay.setEnableLoadmore(true);
        smart_refreshlay.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                Map<String,String> map = new HashMap<String, String>();
//                map.put("page",String.valueOf(1));
                isLoadMore = false;
                Map<String, String> map = MapUtils.getMapInstance();
                map.put("sortField",sortField);
                programPresenter.getProgramList(CommonAction.getUserId(),map);
                pageNum = 2;

            }
        });
        smart_refreshlay.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                Map<String,String> map = new HashMap<String, String>();
                map.put("page",String.valueOf(pageNum));
                map.put("sortField",sortField);
                programPresenter.getProgramList(CommonAction.getUserId(),map);
                pageNum++;

            }
        });
    }
    //
    @OnClick({R.id.add_program, R.id.sort_tv,R.id.search_relay})
    public void onClick(View view) {
        switch (view.getId()) {
            //添加项目
            case R.id.add_program:
                IntentUtils.toActivity(NewProgramActivity.class, getActivity());
                getActivity().overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                break;
            //分类排序
            case R.id.sort_tv:
                morepop.showAsDropDown(view);
                break;
            //搜索项目
            case R.id.search_relay:
                startActivity(new Intent(getContext(), SearchProjectActivity.class));
                break;
        }
    }
    @Override
    public void initDta() {
        Map<String, String> map = MapUtils.getMapInstance();
        map.put("sortField",sortField);
        //加载数据
        loadData(map);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(RefreshProBean proBean) {
        if (proBean.getEventStr().equals(Const.refresh)) {
            Map<String, String> map = MapUtils.getMapInstance();
            loadData(map);
        }
    }

    private void loadData(Map<String, String> map) {
        isLoadMore = false;
        programPresenter.getProgramList(CommonAction.getUserId(),map);
        pageNum = 2;
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_project, null);
    }

    @Override
    public void showError() {
        smart_refreshlay.finishRefresh();
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        smart_refreshlay.finishRefresh();
        mLoadingDialog.dismiss();
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
            }
            programAdapter.notifyDataSetChanged();
        }
    }
    private void initTopPopwindow() {
        View moreview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_more1, null);
        LinearLayout change_lay = (LinearLayout) moreview.findViewById(R.id.share_lay);
        LinearLayout copy_lay = (LinearLayout) moreview.findViewById(R.id.function_lay);
        LinearLayout delete_lay = (LinearLayout) moreview.findViewById(R.id.mine_lay);
        morepop = PopWindowUtil.getPopupWindow(getActivity(), moreview, R.style.top2botAnimation, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //名称
        change_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProgramList_paixu("name");
                sort_tv.setText("名称");
            }
        });
        //更新时间
        copy_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                getProgramList_paixu("updateTime");
                sort_tv.setText("更新时间");


            }
        });
        //最新待办
        delete_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                getProgramList_paixu("createTime");
                sort_tv.setText("最新待办");
            }
        });
    }
    //排序方式
    private void getProgramList_paixu(String type) {
        morepop.dismiss();
        mLoadingDialog.show();
        Map<String, String> mapInstance = MapUtils.getMapInstance();
        mapInstance.put("sortField",type);
        sortField = type;
        loadData(mapInstance);
    }
    //展开列表
    public class ProgramAdapter extends BaseQuickAdapter<ProgramListBean.DataBean, com.chad.library.adapter.base.BaseViewHolder> {


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
            if (baseBean.getStatus().equals("1")) {
                prostatu_tv.setText("已完成");
                prostatu_tv.setBackgroundResource(R.drawable.shape_projectstatus1);
                prostatu_iv.setImageResource(R.mipmap.end);
                proname_tv.setTextColor(ResourcesUtils.getColor(R.color.color6c));
            }else {
                prostatu_tv.setText("进行中");
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

            ProCalAdapter proCalAdapter = new ProCalAdapter(getContext(), schedule, R.layout.view_projectdetail);
            procal_rv.setAdapter(proCalAdapter);
            final LinearLayout spand_lay = holder.getView(R.id.spand_lay);

            if (baseBean.isExpand()){
                spand_lay.setVisibility(View.VISIBLE);
//                spand_iv.startAnimation(AnimaCommonUtil.getNiRotate());
                spand_iv.setImageResource(R.mipmap.up_black);
            }else {
                spand_lay.setVisibility(View.GONE);
//                spand_iv.startAnimation(AnimaCommonUtil.getShunRotate());
                spand_iv.setImageResource(R.mipmap.down_black);
            }
            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (baseBean.getId() != null) {
                        IntentUtils.toActivityWithTag(ProjectDetailActivity.class, getActivity(), baseBean.getId(), 006);
                    }
                }
            });

            right_lay.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (!baseBean.isExpand()){
                        spand_lay.setVisibility(View.VISIBLE);
//                        spand_iv.startAnimation(AnimaCommonUtil.getNiRotate());
                        baseBean.setExpand(true);
                    }else {
                        spand_lay.setVisibility(View.GONE);
//                        spand_iv.startAnimation(AnimaCommonUtil.getShunRotate());
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
    //分组列表
    public class ProjectAdapter extends GroupRecyclerAdapter<String,List<ProgramListBean.DataBean>> {


        public ProjectAdapter(Context context, String s , List<ProgramListBean.DataBean> dataList) {
            super(context);
            LinkedHashMap map = new LinkedHashMap<>();
            List<String> titles = new ArrayList<>();
            map.put(s,dataList);
            titles.add(s);
            resetGroups(map,titles);
        }

        @Override
        protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
           return new ProjectAdapter.ProjectViewHolder(mInflater.inflate(R.layout.view_project, parent, false));
        }

        @Override
        protected void onBindViewHolder(RecyclerView.ViewHolder holder, Object item, int position) {
            final ProgramListBean.DataBean dataBean= (ProgramListBean.DataBean) item;
            ProjectViewHolder h= (ProjectViewHolder) holder;
            h.project_name.setText(dataBean.getName());
            if (dataBean.getStatus().equals("1")){
                h.project_status.setText("已完成");
                h.project_status.setBackgroundResource(R.drawable.shape_projectstatus1);
            }else {
                h.project_status.setText("进行中");
                h.project_status.setBackgroundResource(R.drawable.shape_projectstatus);
            }
            h.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (dataBean.getId() != null) {
                        IntentUtils.toActivityWithTag(ProjectDetailActivity.class, getActivity(), dataBean.getId(), 006);
                    }
                }
            });
        }
        private class ProjectViewHolder extends RecyclerView.ViewHolder {
            private TextView project_name,project_status;
            private ProjectViewHolder(View itemView) {
                super(itemView);
                project_name= (TextView) itemView.findViewById(R.id.project_name);
                project_status= (TextView) itemView.findViewById(R.id.project_status);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMain1(LoginBus refreshCalBean) {
        if (refreshCalBean.getEventStr().equals(Const.refresh)) {
            Map<String, String> map = MapUtils.getMapInstance();
            loadData(map);
        }
    }
}
