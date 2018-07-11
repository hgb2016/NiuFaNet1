package com.dream.NiuFaNet.Ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dream.NiuFaNet.Base.BaseFragmentV4;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.BusBean.LoginBus;
import com.dream.NiuFaNet.Bean.BusBean.RefreshProBean;
import com.dream.NiuFaNet.Bean.CalenderedBean;
import com.dream.NiuFaNet.Bean.EditCount;
import com.dream.NiuFaNet.Bean.ProgramListBean;
import com.dream.NiuFaNet.Bean.ProjectClientListBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ProgramContract;
import com.dream.NiuFaNet.CustomView.Emptyview_Pro;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ProgramPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.CalenderDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.FriendCalenderActivity;
import com.dream.NiuFaNet.Ui.Activity.MainActivity;
import com.dream.NiuFaNet.Ui.Activity.NewProgramActivity;
import com.dream.NiuFaNet.Ui.Activity.ProgramDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.ProjectDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.SearchProjectActivity;
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.DateUtil;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.HiddenAnimUtils;
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
import com.google.gson.Gson;
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
    WrapRecyclerView project_rv;
    @Bind(R.id.project_grv)
    GroupRecyclerView project_grv;
    private ProjectAdapter projectadapter;
    @Inject
    ProgramPresenter programPresenter;
    @Bind(R.id.smart_refreshlay)
    SmartRefreshLayout smart_refreshlay;
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
    @Bind({R.id.bg_v})
    View bg_v;
    private List<ProgramListBean.DataBean> dataList = new ArrayList<>();
    private PopupWindow morepop;
    private ProgramAdapter programAdapter;
    private List<SortBean> sortList = new ArrayList<>();
    private List<SortBean> sortList1 = new ArrayList<>();
    private List<ProjectClientListBean.DataBean> projectList = new ArrayList<>();
    private SortAdapter sortAdater;
    private SortAdapter1 sortAdater1;
    private boolean isSelectClient;

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        programPresenter.attachView(this);
        //模拟数据
        project_grv.setLayoutManager(new LinearLayoutManager(getContext()));
        GroupItemDecoration<String,List<ProjectClientListBean.DataBean>> itemDecoration = new GroupItemDecoration<>();
        project_grv.addItemDecoration(itemDecoration);
        RvUtils.setOptionnoLine(project_rv, getActivity());
        View view = new View(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(1));
        view.setLayoutParams(params);
        project_rv.addHeaderView(view);
        programAdapter = new ProgramAdapter(R.layout.rvitem_program, dataList);
        programAdapter.setEmptyView(new Emptyview_Pro(getContext()));
        project_rv.setAdapter(programAdapter);
        sortAdater = new SortAdapter(getContext(), sortList, R.layout.view_sort);
        sortAdater1 = new SortAdapter1(getContext(), sortList1, R.layout.view_sort);
        initSortData();
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
    private String sortField = "isEdit";
    String status = "";

    /**
     * 刷新和分页加载监听
     */
    @Override
    public void initEvents() {
        smart_refreshlay.setEnableRefresh(true);
        smart_refreshlay.setEnableLoadmore(true);
        smart_refreshlay.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadMore = false;
                if (isSelectClient) {
                    projectList.clear();
                    pageNum=2;
                    Map<String, String> mapInstance = MapUtils.getMapInstance();
                    mapInstance.put("status", status);
                    mapInstance.put("page", "1");
                    mapInstance.put("userId", CommonAction.getUserId());
                    programPresenter.getProjectList(mapInstance);
                } else {
                    dataList.clear();
                    pageNum=2;
                    Map<String, String> map = MapUtils.getMapInstance();
                    map.put("sortField", sortField);
                    map.put("status", status);
                    map.put("page", "1");
                    programPresenter.getProgramList(CommonAction.getUserId(), map);
                }
            }
        });
        smart_refreshlay.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                if (isSelectClient) {
                    Map<String, String> mapInstance = MapUtils.getMapInstance();
                    mapInstance.put("page", String.valueOf(pageNum));
                    mapInstance.put("status", status);
                    mapInstance.put("userId", CommonAction.getUserId());
                    programPresenter.getProjectList(mapInstance);
                    pageNum++;
                }else {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("page", String.valueOf(pageNum));
                    map.put("sortField", sortField);
                    map.put("status", status);
                    programPresenter.getProgramList(CommonAction.getUserId(), map);
                    pageNum++;
                }

            }
        });
    }

    @OnClick({R.id.add_program, R.id.paixu_lay, R.id.search_relay, R.id.shaixuan_lay, R.id.bg_v})
    public void onClick(View view) {
        switch (view.getId()) {
            //添加项目
            case R.id.add_program:
                IntentUtils.toActivity(NewProgramActivity.class, getActivity());
                getActivity().overridePendingTransition(R.anim.activity_open, R.anim.exitanim);
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
                HiddenAnimUtils.newInstance(getContext(), sort_lay, sort_iv, sortList.size() * 50).toggle();
                break;
            //搜索项目
            case R.id.search_relay:
                startActivity(new Intent(getContext(), SearchProjectActivity.class));
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
                HiddenAnimUtils.newInstance(getContext(), sort_lay, sort1_iv, sortList1.size() * 50).toggle();
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

    @Override
    public void initDta() {
        Map<String, String> map = MapUtils.getMapInstance();
        map.put("sortField", sortField);
        map.put("status", status);
        //加载数据
        loadData(map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(RefreshProBean proBean) {
        if (proBean.getEventStr().equals(Const.refresh)) {
            if (isSelectClient){
                dataList.clear();
                Map<String, String> mapInstance = MapUtils.getMapInstance();
                mapInstance.put("page", "1");
                mapInstance.put("status", status);
                mapInstance.put("userId", CommonAction.getUserId());
                programPresenter.getProjectList(mapInstance);

            }else {

                dataList.clear();
                Map<String, String> map = MapUtils.getMapInstance();
                map.put("sortField", sortField);
                map.put("status", status);
                loadData(map);
            }
        }
    }

    private void loadData(Map<String, String> map) {
        isLoadMore = false;
        programPresenter.getProgramList(CommonAction.getUserId(), map);
        pageNum = 2;
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_project, null);
    }

    @Override
    public void showError() {
        smart_refreshlay.finishRefresh();
        mLoadingDialog.dismiss();
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        smart_refreshlay.finishRefresh();
        mLoadingDialog.dismiss();
    }

    /**
     * 项目列表
     * @param dataBean
     */
    @Override
    public void showData(ProgramListBean dataBean) {
        smart_refreshlay.finishRefresh();
        smart_refreshlay.finishLoadmore();
        project_rv.setVisibility(View.VISIBLE);
        project_grv.setVisibility(View.GONE);
        if (dataBean.getError().equals(Const.success)) {
            List<ProgramListBean.DataBean> data = dataBean.getData();
            if (data != null && data.size() > 0) {
                dataList.addAll(data);
               // Log.i("myTag",new Gson().toJson(dataList));
                dataList.get(0).setExpand(true);
            }
            programAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取按客户排序的项目列表
     *
     * @param dataBean
     */
    @Override
    public void showProjectClientList(ProjectClientListBean dataBean) {
        //Log.i("myTag", new Gson().toJson(dataBean) + "---");
        smart_refreshlay.finishRefresh();
        smart_refreshlay.finishLoadmore();
        mLoadingDialog.dismiss();
        if (dataBean.getError().equals(Const.success)) {
            List<ProjectClientListBean.DataBean> data = dataBean.getData();
            if (data != null && data.size() > 0) {
                project_rv.setVisibility(View.GONE);
                project_grv.setVisibility(View.VISIBLE);
                projectList.addAll(data);
                projectadapter = new ProjectAdapter(getContext(), projectList);
                project_grv.setAdapter(projectadapter);
                project_grv.notifyDataSetChanged();
            }
        }
    }


    /**
     *
     * @param type   按类型排序
     * @param status 按条件筛选
     */
    private void getProgramList_paixu(String type, String status) {
        //  morepop.dismiss();
        mLoadingDialog.show();

        Map<String, String> mapInstance = MapUtils.getMapInstance();
        mapInstance.put("sortField", type);
        mapInstance.put("status", status);
        sortField = type;
        loadData(mapInstance);
    }

    /**
     * 展开列表
     */
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
            ImageView isSee_iv=holder.getView(R.id.isSee_iv);
            Log.e("myTag",baseBean.getIsEdit()+"");
            if(baseBean.getIsEdit()!=null&&!baseBean.getIsEdit().isEmpty()){
                if (baseBean.getIsEdit().equals("1")) {
                    isSee_iv.setVisibility(View.VISIBLE);
                } else {
                    isSee_iv.setVisibility(View.GONE);
                }
            }else {
                isSee_iv.setVisibility(View.GONE);
            }
            if (baseBean.getStatus().equals("2")) {
                prostatu_tv.setText("已完成");
                prostatu_tv.setBackgroundResource(R.drawable.shape_projectstatus1);
                prostatu_iv.setImageResource(R.mipmap.end);
                proname_tv.setTextColor(ResourcesUtils.getColor(R.color.color6c));
            } else if (baseBean.getStatus().equals("0")) {
                prostatu_tv.setText("进行中");
                prostatu_tv.setBackgroundResource(R.drawable.shape_projectstatus);
                prostatu_iv.setImageResource(R.mipmap.ing);
                proname_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
            } else if (baseBean.getStatus().equals("1")) {
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

            ProCalAdapter proCalAdapter = new ProCalAdapter(getContext(), schedule, baseBean.getId(),R.layout.view_projectdetail);
            procal_rv.setAdapter(proCalAdapter);
            final LinearLayout spand_lay = holder.getView(R.id.spand_lay);

            if (baseBean.isExpand()) {
                spand_lay.setVisibility(View.VISIBLE);
//              spand_iv.startAnimation(AnimaCommonUtil.getNiRotate());
                spand_iv.setImageResource(R.mipmap.up_black);
            } else {
                spand_lay.setVisibility(View.GONE);
//              spand_iv.startAnimation(AnimaCommonUtil.getShunRotate());
                spand_iv.setImageResource(R.mipmap.down_black);
            }
            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (baseBean.getId() != null) {
                        baseBean.setIsEdit("2");
                        notifyDataSetChanged();
                        IntentUtils.toActivityWithTag(ProjectDetailActivity.class, getActivity(), baseBean.getId(), 006);
                    }
                }
            });

            right_lay.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (!baseBean.isExpand()) {
                        spand_lay.setVisibility(View.VISIBLE);
//                      spand_iv.startAnimation(AnimaCommonUtil.getNiRotate());
                        baseBean.setExpand(true);
                    } else {
                        spand_lay.setVisibility(View.GONE);
//                      spand_iv.startAnimation(AnimaCommonUtil.getShunRotate());
                        baseBean.setExpand(false);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * 展开的子列表适配器
     */
    public class ProCalAdapter extends CommonAdapter<ProgramListBean.DataBean.ScheduleBean> {
        private String projectId;

        public ProCalAdapter(Context context, List<ProgramListBean.DataBean.ScheduleBean> data, String id, int layoutId) {
            super(context, data, layoutId);
            projectId=id;
        }

        @Override
        public void convert(com.dream.NiuFaNet.Base.BaseViewHolder holder, final ProgramListBean.DataBean.ScheduleBean dataBean, int position) {
            ImageView status_iv = holder.getView(R.id.status_iv);
            ImageView apendix_iv = holder.getView(R.id.apendix_iv);
            TextView username_tv = holder.getView(R.id.username_tv);
            TextView time_tv = holder.getView(R.id.time_tv);
            TextView title_tv = holder.getView(R.id.title_tv);
            RelativeLayout more_relay=holder.getView(R.id.more_relay);
            if (position==2){
                more_relay.setVisibility(View.VISIBLE);
            }else {
                more_relay.setVisibility(View.GONE);
            }
            TextView duringtime_tv = holder.getView(R.id.duringtime_tv);
            if (Integer.parseInt(dataBean.getFileCount()) < 1) {
                apendix_iv.setVisibility(View.GONE);
            } else {
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
            holder.setImageByUrl(R.id.head_iv, dataBean.getHeadUrl(), true);
            duringtime_tv.setText(dataBean.getHourNum() + "小时");

            Date beDa = DateFormatUtil.getTime(dataBean.getBeginTime(), Const.YMD_HMS);
            String beStr = DateFormatUtil.getTime(beDa, Const.HM);
            String beStr1 = DateFormatUtil.getTime(beDa, Const.YMD_HM);
            String beYMD = DateFormatUtil.getTime(beDa, Const.Y_M_D);
            Date enDa = DateFormatUtil.getTime(dataBean.getEndTime(), Const.YMD_HMS);
            int dayExpend = CalculateTimeUtil.getDayExpend(beDa.getTime(), enDa.getTime());
            String endStr = DateFormatUtil.getTime(enDa, Const.HM);
            String endStr1 = DateFormatUtil.getTime(enDa, Const.YMD_HM);
            if (dayExpend >= 1) {
                time_tv.setText(beStr1 + " — " + endStr1);
            } else {
                time_tv.setText(beYMD + "  " + beStr + " — " + endStr);
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
            holder.setOnClickListener(R.id.more_relay, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (projectId != null) {
                        IntentUtils.toActivityWithTag(ProjectDetailActivity.class, getActivity(),projectId, 006);
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

    /**
     * 分组列表 只有当按客户排序时 才会调用此适配器
     */
    public class ProjectAdapter extends GroupRecyclerAdapter<String, List<ProjectClientListBean.DataBean>> {


        public ProjectAdapter(Context context, List<ProjectClientListBean.DataBean> dataList) {
            super(context);
            LinkedHashMap map = new LinkedHashMap<>();
            List<String> titles = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                titles.add(dataList.get(i).getClientName());
                map.put(dataList.get(i).getClientName(), dataList.get(i).getProjectList());
            }
            resetGroups(map, titles);


        }

        @Override
        protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
            return new ProjectAdapter.ProjectViewHolder(mInflater.inflate(R.layout.view_project, parent, false));
        }

        @Override
        protected void onBindViewHolder(RecyclerView.ViewHolder holder, Object item, int position) {
            final ProjectClientListBean.DataBean.ProjectBean dataBean = (ProjectClientListBean.DataBean.ProjectBean) item;
            ProjectViewHolder h = (ProjectViewHolder) holder;
            h.project_name.setText(dataBean.getName());
            if (dataBean.getStatus().equals("2")) {
                h.project_status.setText("已完成");
                h.project_status.setBackgroundResource(R.drawable.shape_projectstatus1);
            } else if (dataBean.getStatus().equals("0")) {
                h.project_status.setText("进行中");
                h.project_status.setBackgroundResource(R.drawable.shape_projectstatus);
            } else if (dataBean.getStatus().equals("1")) {
                h.project_status.setText("洽谈中");
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
            private TextView project_name, project_status;

            private ProjectViewHolder(View itemView) {
                super(itemView);
                project_name = (TextView) itemView.findViewById(R.id.project_name);
                project_status = (TextView) itemView.findViewById(R.id.project_status);
            }
        }
    }

    /**
     * 登录后刷新项目列表数据
     * @param refreshCalBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMain1(LoginBus refreshCalBean) {
        if (refreshCalBean.getEventStr().equals(Const.refresh)) {
            if (isSelectClient){
                dataList.clear();
                Map<String, String> mapInstance = MapUtils.getMapInstance();
                mapInstance.put("page", "1");
                mapInstance.put("status", status);
                mapInstance.put("userId", CommonAction.getUserId());
                programPresenter.getProjectList(mapInstance);

            }else {

                dataList.clear();
                Map<String, String> map = MapUtils.getMapInstance();
                map.put("sortField", sortField);
                map.put("status", status);
                loadData(map);
            }
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
                    for (SortBean sortBean : sortList) {
                        if (sortBean.isSelect()) {
                            sortField = sortBean.getSortField();
                        }
                    }
                    for (SortBean sortBean : sortList1) {
                        if (sortBean.isSelect()) {
                            status = sortBean.getStatus();
                        }
                    }
                    sort_tv.setText(item.getSortName());
                    if (item.getSortName().equals("客户")) {
                        pageNum=2;
                        isSelectClient = true;
                        projectList.clear();
                        mLoadingDialog.show();
                        Map<String, String> mapInstance = MapUtils.getMapInstance();
                        mapInstance.put("status", status);
                        mapInstance.put("userId", CommonAction.getUserId());
                        programPresenter.getProjectList(mapInstance);
                        sort_lay.setVisibility(View.GONE);
                        bg_v.setVisibility(View.GONE);
                    } else {
                        pageNum=2;
                        isSelectClient = false;
                        dataList.clear();
                        getProgramList_paixu(sortField, status);
                        sort_lay.setVisibility(View.GONE);
                        bg_v.setVisibility(View.GONE);
                    }

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
                    for (SortBean sortBean : sortList) {
                        if (sortBean.isSelect()) {
                            sortField = sortBean.getSortField();
                        }
                    }
                    for (SortBean sortBean : sortList1) {
                        if (sortBean.isSelect()) {
                            status = sortBean.getStatus();
                        }
                    }
                    if (isSelectClient) {
                        pageNum=2;
                        projectList.clear();
                        mLoadingDialog.show();
                        Map<String, String> mapInstance = MapUtils.getMapInstance();
                        mapInstance.put("status", status);
                        mapInstance.put("userId", CommonAction.getUserId());
                        programPresenter.getProjectList(mapInstance);
                        sort_lay.setVisibility(View.GONE);
                        bg_v.setVisibility(View.GONE);
                    } else {
                        pageNum=2;
                        dataList.clear();
                        getProgramList_paixu(sortField, status);
                        sort_lay.setVisibility(View.GONE);
                        bg_v.setVisibility(View.GONE);
                    }
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
        sort7.setSortName("所有");
        sort7.setStatus("");
        sortList1.add(sort7);
        SortBean sort1 = new SortBean();
        sort1.setSelect(false);
        sort1.setSortName("进行中");
        sort1.setStatus("0");
        sortList1.add(sort1);
        SortBean sort2 = new SortBean();
        sort2.setSelect(false);
        sort2.setSortName("洽谈中");
        sort2.setStatus("1");
        sortList1.add(sort2);
        SortBean sort3 = new SortBean();
        sort3.setSelect(false);
        sort3.setSortName("已完成");
        sort3.setStatus("2");
        sortList1.add(sort3);

        SortBean sort8 = new SortBean();
        sort8.setSelect(false);
        sort8.setSortName("未读");
        sort8.setSortField("isEdit");
        sortList.add(sort8);
        SortBean sort4 = new SortBean();
        sort4.setSelect(false);
        sort4.setSortName("名称");
        sort4.setSortField("name");
        sortList.add(sort4);
        SortBean sort5 = new SortBean();
        sort5.setSelect(false);
        sort5.setSortName("时间");
        sort5.setSortField("updateTime");
        sortList.add(sort5);
        SortBean sort6 = new SortBean();
        sort6.setSelect(false);
        sort6.setSortName("客户");
        sort6.setSortField("c.clientName");
        sortList.add(sort6);


    }
}
