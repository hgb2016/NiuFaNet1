package com.dream.NiuFaNet.Ui.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.AddWorkVisibleBean;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.Contact;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.SimFrendsBean;
import com.dream.NiuFaNet.Bean.WorkVisibleBean;
import com.dream.NiuFaNet.Bean.WorkVisibleFrendsBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.WorkVisibleContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.WorkVisiblePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.FrendsUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.PinYinUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/17 0017.
 */
public class WorkVisibleFrendsActivity extends CommonActivity implements WorkVisibleContract.View {
    @Bind(R.id.simfrend_rv)
    RecyclerView mSimfrendRv;
    @Bind(R.id.letter_rv)
    RecyclerView mLetterRv;
    @Bind(R.id.frends_refresh)
    SmartRefreshLayout mFrendsRefresh;
    @Bind(R.id.search_edt)
    EditText mSearchEdt;
    @Bind(R.id.search_iv)
    ImageView mSearchIv;
    @Bind(R.id.addwoks_lay)
    LinearLayout mAddwoksLay;
    @Bind(R.id.back_relay)
    RelativeLayout back_relay;
    private Map<String, WorkVisibleBean.DataBean> map = new HashMap<>();

    private String[] lArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private FrendsRvAdapter mFrendsRvAdapter;
    private LetterAdapter letterAdapter;

    private List<String> letterList = new ArrayList<>();
    private Dialog mLoadingDialog;

    private List<WorkVisibleFrendsBean> mDataBeanList = new ArrayList<>();
    private List<WorkVisibleBean.DataBean> origList = new ArrayList<>();
    private List<WorkVisibleBean.DataBean> particpantList = new ArrayList<>();
    @Inject
    WorkVisiblePresenter mWorkVisiblePresenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_workvisible_frends;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        mWorkVisiblePresenter.attachView(this);
        RvUtils.setOptionnoLine(mSimfrendRv, this);
        mFrendsRvAdapter = new FrendsRvAdapter(this, mDataBeanList, R.layout.rvitem_workvisible);
        mSimfrendRv.setAdapter(mFrendsRvAdapter);

        RvUtils.setOptionnoLine(mLetterRv, this);
        letterAdapter = new LetterAdapter(this, letterList, R.layout.rvitem_onlytext);
        mLetterRv.setAdapter(letterAdapter);
        mLoadingDialog = DialogUtils.initLoadingDialog(this);

    }

    @Override
    public void initDatas() {
        loadData();
    }

    @Override
    public void eventListener() {
        mFrendsRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData();
            }
        });
        mFrendsRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
            }
        });

        back_relay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showAddData(CommonBean dataBean) {
        ToastUtils.Toast_short(dataBean.getMessage());
        loadData();
    }

    private void loadData() {
        mLoadingDialog.show();
        mWorkVisiblePresenter.getWorkVisible(CommonAction.getUserId());
    }

    @Override
    public void showDeleteData(CommonBean dataBean) {

    }

    private int origListSize;
    @Override
    public void showVisiblesData(WorkVisibleBean dataBean) {
        mFrendsRefresh.finishRefresh();
        mFrendsRefresh.finishLoadmore();
        if (dataBean.getError().equals(Const.success)){
            List<WorkVisibleBean.DataBean> data = dataBean.getData();
            if (data!=null&&data.size()>0){
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSource(true);
                }
                mDataBeanList.clear();
                origList.clear();
                letterList.clear();
                origList.addAll(data);
                parseLetterData(data);
                origListSize = data.size();
                mFrendsRefresh.finishRefresh();
            }
        }
    }


    @OnClick(R.id.addwoks_lay)
    public void onViewClicked() {
        IntentUtils.toActivityWithTag(MyFrendsActivity.class, mActivity, "workVisible", 101);
    }

    public class FrendsRvAdapter extends RVBaseAdapter<WorkVisibleFrendsBean> {
        public FrendsRvAdapter(Context context, List<WorkVisibleFrendsBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, WorkVisibleFrendsBean contactBean, int position) {
            holder.setText(R.id.headchar_tv, contactBean.getLetter());
            RecyclerView contacts_lv = holder.getView(R.id.contacts_lv);
            RvUtils.setOptionnoLine(contacts_lv);
            contacts_lv.setAdapter(new ContacsLvAdapter(mContext, contactBean.getContacts(), R.layout.rvitem_participants_deleteble));
        }
    }

    private class ContacsLvAdapter extends RVBaseAdapter<WorkVisibleBean.DataBean> {

        public ContacsLvAdapter(Context context, List<WorkVisibleBean.DataBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, final WorkVisibleBean.DataBean contact, final int position) {
            TextView status_tv = holder.getView(R.id.status_tv);
            status_tv.setVisibility(View.GONE);
            holder.setText(R.id.username_tv,contact.getUserName());
            String headUrl = contact.getHeadUrl();
            if (headUrl !=null&&!headUrl.isEmpty()){
                holder.setImageByUrl(R.id.particepant_headiv, headUrl,true);
            }else {
                holder.setImageResource(R.id.particepant_headiv,R.mipmap.niu);
            }

            if (position==data.size()-1){
                holder.getView(R.id.fegexian_line).setVisibility(View.GONE);
            }
            holder.setOnClickListener(R.id.delete_tv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    DialogUtils.showDeleteDialog(mContext, new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View view) {
                            mLoadingDialog.show();
                            mWorkVisiblePresenter.deleteWorkVisible(CommonAction.getUserId(),contact.getUserId());
                            data.remove(position);
                            origListSize--;
                            notifyDataSetChanged();
                        }
                    });
                }
            });
        }

    }

    private class LetterAdapter extends RVBaseAdapter<String> {

        public LetterAdapter(Context context, List<String> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, final String cityBean, int position) {
            TextView only_tv = holder.getView(R.id.only_tv);
            only_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            only_tv.setTextColor(ResourcesUtils.getColor(R.color.letterTvColor));
            only_tv.setText(cityBean);
            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    for (int j = 0; j < letterList.size(); j++) {
                        if (cityBean.equals(letterList.get(j))) {
//                            cars_rv.smoothScrollToPosition(j);
                            RvUtils.MoveToPosition((LinearLayoutManager) mSimfrendRv.getLayoutManager(), j);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 102 && data != null) {
            List<MyFrendBean.DataBean> selectdata = (List<MyFrendBean.DataBean>) data.getExtras().getSerializable("selectdata");
            if (selectdata != null && selectdata.size() > 0) {
                mLoadingDialog.show();
                particpantList.clear();
                for (Map.Entry<String, WorkVisibleBean.DataBean> mm:map.entrySet()){
                    if (!mm.getValue().isSource()){
                        map.remove(mm);
                    }
                }
                //合并去重
                for (int i = 0; i < selectdata.size(); i++) {
                    MyFrendBean.DataBean dataBean = selectdata.get(i);
                    WorkVisibleBean.DataBean bean = new WorkVisibleBean.DataBean();
                    bean.setUserId(dataBean.getFriendId());
                    Log.e("tag", "userName=" + dataBean.getFriendName());
                    bean.setUserName(dataBean.getFriendName());
                    bean.setHeadUrl(dataBean.getHeadUrl());
                    bean.setSource(false);
                    map.put(dataBean.getFriendId(), bean);
                }


                //把原来的参与人重新加上
                for (int i = 0; i < origList.size(); i++) {
                    map.put(origList.get(i).getUserId(), origList.get(i));
                }

                for (Map.Entry<String, WorkVisibleBean.DataBean> item : map.entrySet()) {
                    particpantList.add(item.getValue());
                }

                if (particpantList.size()>0){
                    AddWorkVisibleBean bean = new AddWorkVisibleBean();
                    bean.setUserId(CommonAction.getUserId());
                    List<AddWorkVisibleBean.VisibleBean> visibles = new ArrayList<>();
                    for (int i = 0; i <particpantList.size() ; i++) {
                        AddWorkVisibleBean.VisibleBean visiBle = new AddWorkVisibleBean.VisibleBean();
                        visiBle.setUser_id(particpantList.get(i).getUserId());
                        visibles.add(visiBle);
                    }
                    bean.setVisible(visibles);
                    String dataStr = new Gson().toJson(bean);
                    Log.e("tag","dataStr="+dataStr);
                    mWorkVisiblePresenter.addWorkVisible(dataStr);
                }
            }
        }
    }

    private void parseLetterData(List<WorkVisibleBean.DataBean> particpantList) {
        for (int i = 0; i < lArray.length; i++) {
            WorkVisibleFrendsBean simFrend = new WorkVisibleFrendsBean();
            List<WorkVisibleBean.DataBean> contactList = new ArrayList<>();
            for (int j = 0; j < particpantList.size(); j++) {
                String name = particpantList.get(j).getUserName();
                if (name != null && !name.isEmpty()) {
                    String pinYinHeadChar = PinYinUtil.getInstance().getPinYinHeadChar(name.substring(0, 1));
                    if (lArray[i].equals(pinYinHeadChar)) {
                        contactList.add(particpantList.get(j));
                    }
                }
            }
            if (contactList.size() != 0) {
                simFrend.setLetter(lArray[i]);
                simFrend.setContacts(contactList);
                mDataBeanList.add(simFrend);
                letterList.add(lArray[i]);
            }
        }
        List<WorkVisibleBean.DataBean> otherList = new ArrayList<>();
        for (int i = 0; i < particpantList.size(); i++) {
            String userName = particpantList.get(i).getUserName();
            if (userName.length()>0&&userName.substring(0,1).equals("1")){
                otherList.add(particpantList.get(i));
            }
        }
        if (otherList.size() != 0) {
            WorkVisibleFrendsBean simFrend = new WorkVisibleFrendsBean();
            simFrend.setLetter("#");
            simFrend.setContacts(otherList);
            mDataBeanList.add(simFrend);
            letterList.add("#");
        }

        mFrendsRvAdapter.notifyDataSetChanged();
        letterAdapter.notifyDataSetChanged();
    }

}
