package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.ClientDescBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.ProgramDetailContract;
import com.dream.NiuFaNet.Contract.SearchClientDescContract;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.CustomView.MyScrollView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.ProgramDetailPresenter;
import com.dream.NiuFaNet.Presenter.SearchClientDescPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CalculateTimeUtil;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.PopUtils;
import com.dream.NiuFaNet.Utils.PopWindowUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.StringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/4 0004.
 */
public class ProjectDetailActivity extends CommonActivity implements ProgramDetailContract.View, MyScrollView.OnScrollListener ,SearchClientDescContract.View{

    @Inject
    ProgramDetailPresenter detailPresenter;
    private InputMethodManager imm;

    private List<ProgramDetailBean.DataBean.participantBean> participantList = new ArrayList<>();
    private String projectId;
    private String projectTitle;


    @Bind(R.id.calendar_lv)
    MyListView calendar_lv;
    @Bind(R.id.myscrollview)
    MyScrollView myScrollView;
    @Bind(R.id.refresh_lay)
    SmartRefreshLayout smart_refreshlay;
    @Bind(R.id.tv_topView)
    LinearLayout mTopView;
    /**
     * 要悬浮在顶部的View的子View
     */
    @Bind(R.id.ll_tabTopView)
    LinearLayout mTopTabViewLayout;
    /**
     * 跟随ScrollView的TabviewLayout
     */
    @Bind(R.id.ll_tabView)
    RelativeLayout mTabViewLayout;

    @Bind(R.id.particepant_gv)
    MyGridView particepant_gv;
    private ParticipantAdapter participantAdapter;
    @Bind(R.id.project_name)
    TextView project_name;
    @Bind(R.id.desc_tv)
    TextView desc_tv;
    @Bind(R.id.project_status)
    TextView project_status;
    @Bind(R.id.more_relay)
    RelativeLayout more_relay;
    @Bind(R.id.no_callay)
    LinearLayout nocalay;
    @Bind(R.id.cal_lay)
    LinearLayout cal_lay;
    @Bind(R.id.add_schedule)
    ImageView addschedule_iv;
    @Bind(R.id.downSchedule_iv)
    ImageView downSchedule_iv;
    @Bind(R.id.client_lay)
    LinearLayout client_relay;
    @Bind(R.id.client_tv)
    TextView client_tv;
    @Bind({R.id.case_relay})
    RelativeLayout case_relay;
    @Bind(R.id.case_tv)
    TextView case_tv;
    private int page= 1;
    private boolean isLoadMore;
    private  List<ProgramDetailBean.DataBean.scheduleBean> schedule=new ArrayList<>();
    @Inject
    SearchClientDescPresenter searchClientDescPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_projectdetail;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        detailPresenter.attachView(this);
        searchClientDescPresenter.attachView(this);
        imm = ImmUtils.getImm(mActivity);
        initTopPopwindow();
        participantAdapter = new ParticipantAdapter(this, participantList, R.layout.gvitem_timg_btext);
        particepant_gv.setAdapter(participantAdapter);
        //滑动监听
        myScrollView.setOnScrollListener(this);
    }

    //初始化数据
    @Override
    public void initDatas() {
        projectId = getIntent().getStringExtra(Const.intentTag);
        if (projectId != null) {
            mLoadingDialog.show();
            detailPresenter.getProjectProgramDetail(projectId,CommonAction.getUserId(),String.valueOf(page));
        } else {
            ToastUtils.Toast_short("数据异常");
            finish();
        }
    }

    @Override
    public void eventListener() {
        smart_refreshlay.setEnableRefresh(true);
        smart_refreshlay.setEnableLoadmore(true);
        smart_refreshlay.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                Map<String,String> map = new HashMap<String, String>();
//                map.put("page",String.valueOf(1));

                page=1;
                isLoadMore = false;
                detailPresenter.getProjectProgramDetail(projectId,CommonAction.getUserId(),String.valueOf(page));


            }
        });
        smart_refreshlay.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                isLoadMore = true;
                detailPresenter.getProjectProgramDetail(projectId,CommonAction.getUserId(),String.valueOf(page));

            }
        });
    }

    private ProgramDetailBean.DataBean tempData;

    @Override
    public void showData(ProgramDetailBean dataBean) {
        smart_refreshlay.finishRefresh();
        smart_refreshlay.finishLoadmore();
        if (dataBean.getError().equals(Const.success)) {
            ProgramDetailBean.DataBean data = dataBean.getData();
            Log.e("tag", "data=" + new Gson().toJson(data));
            if (data != null) {
                tempData = data;
                String status = data.getStatus();
                if (status != null) {
                    if (status.equals("3")) {
                        ToastUtils.Toast_short("该项目已被删除");
                        CommonAction.refreshLogined();
                        finish();
                    } else {
                        tempData.setProjectId(projectId);
                        project_name.setText(data.getName());
                        projectTitle = data.getName();
                        String description = data.getDescription();
                        if (description != null && !description.isEmpty()) {
                            //   SpannableStringUtil.setTelUrl(mActivity, description, desc_tv);
                            //   beizu_tv.setText(description);
                            desc_tv.setVisibility(View.VISIBLE);
                            desc_tv.setText(description);
                        } else {
                            desc_tv.setVisibility(View.GONE);
                        }
                        String caseNo = data.getCaseNo();
                        if (caseNo!=null&&!caseNo.isEmpty()){
                            case_relay.setVisibility(View.VISIBLE);
                            case_tv.setText(caseNo);
                        }else {
                            case_relay.setVisibility(View.GONE);
                        }
                        if (!data.getClientId().equals("0")){
                            client_relay.setVisibility(View.VISIBLE);
                            client_tv.setText(data.getClientName());
                        }else {
                            client_relay.setVisibility(View.GONE);
                        }
                        if (status != null && !status.isEmpty()) {
                            if (status.equals("2")) {
                                project_status.setBackgroundResource(R.drawable.shape_projectstatus1);
                                project_status.setText("已完成");
                            } else if (status.equals("0")) {
                                project_status.setBackgroundResource(R.drawable.shape_projectstatus);
                                project_status.setText("进行中");
                            }else if (status.equals("1")){
                                project_status.setBackgroundResource(R.drawable.shape_projectstatus);
                                project_status.setText("洽谈中");
                            }
                        }
                        if (page==1){
                            schedule.clear();
                            schedule.addAll(data.getSchedule());
                        }else {
                            schedule.addAll(data.getSchedule());
                        }
                        if (schedule != null) {
                            if (schedule.size()>0) {
                                nocalay.setVisibility(View.GONE);
                                cal_lay.setVisibility(View.VISIBLE);
                               // addschedule_iv.setVisibility(View.VISIBLE);
                                downSchedule_iv.setVisibility(View.VISIBLE);
                                calendar_lv.setAdapter(new ProjectAdapter(this, schedule, R.layout.view_projectdetail));
                            }else {
                                nocalay.setVisibility(View.VISIBLE);
                                //addschedule_iv.setVisibility(View.GONE);
                                downSchedule_iv.setVisibility(View.GONE);
                                cal_lay.setVisibility(View.GONE);
                            }
                        }else {
                            nocalay.setVisibility(View.VISIBLE);
                          //  addschedule_iv.setVisibility(View.GONE);
                            downSchedule_iv.setVisibility(View.GONE);
                        }

                        participantList.clear();
                        ProgramDetailBean.DataBean.participantBean creatBean = new ProgramDetailBean.DataBean.participantBean();
                        creatBean.setUserId(data.getUserId());
                        creatBean.setUserName(data.getUserName());
                        creatBean.setHeadUrl(data.getHeadUrl());
                        participantList.add(creatBean);

                        List<ProgramDetailBean.DataBean.participantBean> participant = data.getParticipant();
                        if (participant != null && participant.size() > 0) {
                            participantList.addAll(participant);
                        }
                        participantAdapter.notifyDataSetChanged();
                    }
                }
            }

        }
    }

    @Override
    public void showEdtData(CommonBean dataBean) {

    }

    @Override
    public void showDeleteData(CommonBean dataBean) {
        ToastUtils.Toast_short(dataBean.getMessage());
        if (dataBean.getError().equals(Const.success)) {
            CommonAction.refreshPro();
            finish();
        }
    }

    @Override
    public void showDownload(CommonBean dataBean) {

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

    @OnClick({R.id.back_relay, R.id.add_schedule, R.id.add_schedule1, R.id.more_relay, R.id.downSchedule_iv,R.id.desc_tv,R.id.client_lay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.add_schedule:
                if (CommonAction.getIsLogin()) {
                    if (projectId != null) {
                        Calendar calendar = Calendar.getInstance();
                        Intent intent = new Intent(mContext, NewCalenderActivity.class);
                        intent.putExtra("date", String.valueOf(calendar.getTimeInMillis()));
                        intent.putExtra("projectId", projectId);
                        intent.putExtra("projectTitle", projectTitle);
                        startActivityForResult(intent, 123);
                        this.overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                    }
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.add_schedule1:
                if (CommonAction.getIsLogin()) {
                    if (projectId != null) {
                        Calendar calendar = Calendar.getInstance();
                        Intent intent = new Intent(mContext, NewCalenderActivity.class);
                        intent.putExtra("date", String.valueOf(calendar.getTimeInMillis()));
                        intent.putExtra("projectId", projectId);
                        intent.putExtra("projectTitle", projectTitle);
                        startActivityForResult(intent, 123);
                        this.overridePendingTransition(R.anim.activity_open,R.anim.exitanim);
                    }
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.more_relay:
                int screenWidth = DensityUtil.getScreenWidth(mActivity);
                PopWindowUtil.backgroundAlpaha(mActivity, 0.5f);
                morepop.showAsDropDown(more_relay, screenWidth - DensityUtil.dip2px(15), 0);
                break;
            case R.id.downSchedule_iv:
                if (StringUtil.NoNullOrEmpty(projectId)) {
                    Intent intent = new Intent(mActivity, DownScheduleActivity.class);
                    intent.putExtra(Const.intentTag, projectId);
                    intent.putExtra("proName", projectTitle);
                    intent.putExtra("type","1");
                    startActivity(intent);

                }
                break;
            case R.id.desc_tv:
                String desc=desc_tv.getText().toString().trim();
                if (!TextUtils.isEmpty(desc)){
                    PopUtils.showDetailPop(view,desc,mContext);
                }
                break;
            case R.id.client_lay:
                searchClientDescPresenter.searchClientDesc(CommonAction.getUserId(),tempData.getClientId());
                break;
        }

    }

    @Override
    public void onScroll(int scrollY) {
        int mHeight = mTabViewLayout.getTop();
        //判断滑动距离scrollY是否大于0，因为大于0的时候就是可以滑动了，此时mTabViewLayout.getTop()才能取到值。
        if (scrollY > 0 && scrollY >= mHeight) {
           // mTopTabViewLayout.setVisibility(View.VISIBLE);
            if (mTopView.getParent() != mTopTabViewLayout) {
                mTabViewLayout.removeView(mTopView);
                mTopTabViewLayout.addView(mTopView);
            }

        } else {
            //mTopTabViewLayout.setVisibility(View.GONE);
            if (mTopView.getParent() != mTabViewLayout) {
                mTopTabViewLayout.removeView(mTopView);
                mTabViewLayout.addView(mTopView);
            }

        }
    }
    //判断是否有查看客户的权限
    @Override
    public void showData(ClientDescBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            String isshow=dataBean.getIsShow();
            if (isshow.equals("Y")){
                Intent intent=new Intent();
                intent.putExtra("clientId",tempData.getClientId());
                intent.setClass(mContext,ClientDetailActivity.class);
                startActivity(intent);
            }else {
                ToastUtils.Toast_short("您没有查看此客户的权限！");
            }
        }
    }

    @Override
    public void showDeleteResult(CommonBean commonBean) {

    }

    @Override
    public void showAddContactResult(CommonBean commonBean) {

    }

    @Override
    public void showDeleMyClientContactResult(CommonBean commonBean) {

    }

    //项目日程列表
    public class ProjectAdapter extends CommonAdapter<ProgramDetailBean.DataBean.scheduleBean> {

        public ProjectAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder holder, final ProgramDetailBean.DataBean.scheduleBean scheduleBean, int position) {
            ImageView head_iv = holder.getView(R.id.head_iv);
            ImageView status_iv = holder.getView(R.id.status_iv);
            ImageView apendix_iv = holder.getView(R.id.apendix_iv);
            TextView username_tv = holder.getView(R.id.username_tv);
            TextView time_tv = holder.getView(R.id.time_tv);
            TextView title_tv = holder.getView(R.id.title_tv);
            TextView duringtime_tv = holder.getView(R.id.duringtime_tv);
            if (Integer.parseInt(scheduleBean.getFileCount())<1){
                apendix_iv.setVisibility(View.GONE);
            }else {
                apendix_iv.setVisibility(View.VISIBLE);
            }
            Date endD = DateFormatUtil.getTime(scheduleBean.getEndTime(), Const.YMD_HMS);
            Calendar calendar = Calendar.getInstance();
            if (calendar.getTimeInMillis() > endD.getTime()) {
                status_iv.setImageResource(R.drawable.shape_circle_dot);
                title_tv.setTextColor(ResourcesUtils.getColor(R.color.outdatecolor));
            } else {
                title_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
                status_iv.setImageResource(R.mipmap.dot1);
            }
            username_tv.setText(scheduleBean.getCreateUserName());
            holder.setImageByUrl(R.id.head_iv,scheduleBean.getHeadUrl(),true);
            duringtime_tv.setText(scheduleBean.getHourNum()+"小时");

            Date beDa = DateFormatUtil.getTime(scheduleBean.getBeginTime(), Const.YMD_HMS);
            String beStr = DateFormatUtil.getTime(beDa, Const.HM);
            String beStr1 = DateFormatUtil.getTime(beDa, Const.YMD_HM);
            String beYMD = DateFormatUtil.getTime(beDa, Const.Y_M_D);
            Date enDa = DateFormatUtil.getTime(scheduleBean.getEndTime(), Const.YMD_HMS);
            int dayExpend = CalculateTimeUtil.getDayExpend(beDa.getTime(), enDa.getTime());
            String endStr = DateFormatUtil.getTime(enDa, Const.HM);
            String endStr1 = DateFormatUtil.getTime(enDa, Const.YMD_HM);
            if (dayExpend>=1){
                time_tv.setText(beStr1+" — "+endStr1);
            }else {
                time_tv.setText(beYMD+"  "+beStr+" — "+endStr);
            }

            title_tv.setText(scheduleBean.getTitle());
            holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    String scheduleId = scheduleBean.getScheduleId();
                    if (scheduleId != null && !scheduleId.isEmpty()) {
                        Intent intent = new Intent(mActivity, CalenderDetailActivity.class);
                        intent.putExtra(Const.scheduleId, scheduleId);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    //项目成员列表
    private class ParticipantAdapter extends CommonAdapter<ProgramDetailBean.DataBean.participantBean> {

        public ParticipantAdapter(Context context, List<ProgramDetailBean.DataBean.participantBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final ProgramDetailBean.DataBean.participantBean item, int position) {
            ImageView only_iv = helper.getView(R.id.only_iv);
            ImageView lancher_iv = helper.getView(R.id.lancher_iv);
            TextView only_tv = helper.getView(R.id.only_tv);
            only_tv.setMaxLines(1);
            only_tv.setEllipsize(TextUtils.TruncateAt.END);
            Log.e("tag", "userName=" + item.getUserName());
            if (item.getUserId().equals(CommonAction.getUserId())){
                only_tv.setTextColor(mContext.getResources().getColor(R.color.blue_title));
                only_tv.setText("我");
            }else {
                only_tv.setTextColor(mContext.getResources().getColor(R.color.black));
                only_tv.setText(item.getUserName());
                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1=new Intent(mContext,FriendDetailActivity.class);
                        intent1.putExtra("friendid",item.getUserId());
                        intent1.putExtra("type","1");
                        mContext.startActivity(intent1);
                    }
                });
            }

            String headUrl = item.getHeadUrl();
            if (!item.isEmpty()) {
                if (headUrl != null && !headUrl.isEmpty()) {
                    helper.setImageByUrl(R.id.only_iv, headUrl, true);
                } else {
                    only_iv.setImageResource(R.mipmap.niu);
                }
            }
        }
    }


    //更多内容
    private PopupWindow morepop, downPop;

    private void initTopPopwindow() {
        View moreview = LayoutInflater.from(this).inflate(R.layout.dialog_calmore, null);
        LinearLayout change_lay = (LinearLayout) moreview.findViewById(R.id.change_lay);
        LinearLayout copy_lay = (LinearLayout) moreview.findViewById(R.id.copy_lay);
        LinearLayout delete_lay = (LinearLayout) moreview.findViewById(R.id.delete_lay);
        morepop = PopWindowUtil.getPopupWindow(this, moreview, R.style.top2botAnimation, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        morepop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpaha(mActivity, 1f);
            }
        });
        //编辑
        change_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morepop.dismiss();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", tempData);
                Intent intent = new Intent(mActivity, NewProgramActivity.class);
                intent.putExtra(Const.intentTag, "edit");
                intent.putExtras(bundle);
                startActivityForResult(intent, 123);
            }
        });
        //复制
        copy_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morepop.dismiss();
                Intent intent = new Intent(mContext, NewProgramActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) tempData);
                intent.putExtras(bundle);
                intent.putExtra(Const.intentTag, "copy");
                startActivity(intent);
                finish();
            }
        });
        //删除
        delete_lay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                morepop.dismiss();
                DialogUtils.showDeleteDialog(mContext, new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View view) {
                        detailPresenter.deleteProject(projectId);
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            page=1;
            detailPresenter.getProjectProgramDetail(projectId,CommonAction.getUserId(),String.valueOf(page));
        }
    }

}
