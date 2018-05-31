package com.dream.NiuFaNet.Ui.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dream.NiuFaNet.Base.BaseFragmentV4;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.BusBean.LoginBus;
import com.dream.NiuFaNet.Bean.BusBean.RefreshProBean;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.ProgramListBean;
import com.dream.NiuFaNet.Bean.ProjectClientListBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ProCalContract;
import com.dream.NiuFaNet.Contract.ProgramContract;
import com.dream.NiuFaNet.CustomView.Emptyview_Pro;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ProCalPresenter;
import com.dream.NiuFaNet.Presenter.ProgramPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.CalenderDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.MineActivity;
import com.dream.NiuFaNet.Ui.Activity.NewCalenderActivity;
import com.dream.NiuFaNet.Ui.Activity.NewProgramActivity;
import com.dream.NiuFaNet.Ui.Activity.ProgramDetailActivity;
import com.dream.NiuFaNet.Utils.AnimaCommonUtil;
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
import com.kevin.wraprecyclerview.WrapRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
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
 * Created by Administrator on 2017/12/4 0004.
 */
public class ProgramFragment extends BaseFragmentV4 implements ProgramContract.View {

    @Bind(R.id.program_rv)
    WrapRecyclerView program_rv;
    @Bind(R.id.swich_lay)
    RelativeLayout swich_lay;
    @Bind(R.id.keyword_edt)
    EditText keyword_edt;
    @Bind(R.id.sort_tv)
    TextView sort_tv;
    @Bind(R.id.smart_refreshlay)
    SmartRefreshLayout smart_refreshlay;
    private Dialog mLoadingDialog;

    @Inject
    ProgramPresenter programPresenter;

    private PopupWindow morepop;
    private ProgramAdapter programAdapter;
    private List<ProgramListBean.DataBean> dataList = new ArrayList<>();

    @Override
    public void initView() {

        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        programPresenter.attachView(this);
        RvUtils.setOptionnoLine(program_rv, getActivity());
        View view = new View(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(10));
        view.setLayoutParams(params);
        program_rv.addHeaderView(view);
        programAdapter = new ProgramAdapter(R.layout.rvitem_program,dataList);
        programAdapter.setEmptyView(new Emptyview_Pro(getContext()));
        program_rv.setAdapter(programAdapter);
        initTopPopwindow();
        EventBus.getDefault().register(this);
        mLoadingDialog = DialogUtils.initLoadingDialog(getActivity());

    }

    @Override
    public void initResume() {

    }

    private int pageNum = 2;
    private boolean isLoadMore;
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

    private String sortField = "createTime";
    @Override
    public void initDta() {
        Map<String, String> map = MapUtils.getMapInstance();
        map.put("sortField",sortField);
//        mLoadingDialog.show();
        loadData(map);
    }

    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_program, null);
    }

    @OnClick({R.id.add_program, R.id.swich_lay,R.id.search_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_program:
                IntentUtils.toActivity(NewProgramActivity.class, getActivity());
                break;
            case R.id.swich_lay:
                morepop.showAsDropDown(swich_lay);
                break;
            case R.id.search_iv:
                String keyWord = keyword_edt.getText().toString();
                if (keyWord.isEmpty()){
                    ToastUtils.Toast_short("请输入搜索关键字");
                }else {
                    Map<String, String> mapInstance = MapUtils.getMapInstance();
                    mapInstance.put("keyWord",keyWord);
                    mLoadingDialog.show();
                    loadData(mapInstance);
                }
                break;
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

    private void getProgramList_paixu(String type) {
        morepop.dismiss();
        mLoadingDialog.show();
        Map<String, String> mapInstance = MapUtils.getMapInstance();
        mapInstance.put("sortField",type);
        sortField = type;
        loadData(mapInstance);
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

    @Override
    public void showProjectClientList(ProjectClientListBean dataBean) {

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
    public void showError() {
        smart_refreshlay.finishRefresh();
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        smart_refreshlay.finishRefresh();
        mLoadingDialog.dismiss();
    }

    public class ProgramAdapter extends BaseQuickAdapter<ProgramListBean.DataBean, BaseViewHolder> {


        public ProgramAdapter(@LayoutRes int layoutResId, @Nullable List<ProgramListBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final ProgramListBean.DataBean baseBean) {
            TextView proname_tv = holder.getView(R.id.proname_tv);
            proname_tv.setText(baseBean.getName());
            ListView procal_rv = holder.getView(R.id.pro_cal_rv);
            List<ProgramListBean.DataBean.ScheduleBean> schedule = baseBean.getSchedule();
            ImageView prostatu_iv = holder.getView(R.id.prostatu_iv);
            final ImageView spand_iv = holder.getView(R.id.spand_iv);
            LinearLayout right_lay = holder.getView(R.id.right_lay);
            if (baseBean.getStatus().equals("1")) {
                prostatu_iv.setImageResource(R.mipmap.end);
                proname_tv.setTextColor(ResourcesUtils.getColor(R.color.color6c));
            }else {
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

            ProCalAdapter proCalAdapter = new ProCalAdapter(getContext(), schedule, R.layout.rvitem_calwork_pro);
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
                        IntentUtils.toActivityWithTag(ProgramDetailActivity.class, getActivity(), baseBean.getId(), 006);
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
        public void convert(com.dream.NiuFaNet.Base.BaseViewHolder helper, final ProgramListBean.DataBean.ScheduleBean dataBean, int position) {

            String fileCount = dataBean.getFileCount();
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
            work_tv.setText(dataBean.getTitle());

            helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    String scheduleId = dataBean.getScheduleId();
                    if (scheduleId != null && !scheduleId.isEmpty()) {
                        Intent intent = new Intent(getContext(), CalenderDetailActivity.class);
                        intent.putExtra(Const.scheduleId, scheduleId);
                        getActivity().startActivity(intent);
                    }
                }
            });
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMain1(LoginBus refreshCalBean) {
        if (refreshCalBean.getEventStr().equals(Const.refresh)) {
            Map<String, String> map = MapUtils.getMapInstance();
            loadData(map);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
