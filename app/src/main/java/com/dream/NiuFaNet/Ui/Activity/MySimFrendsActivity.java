package com.dream.NiuFaNet.Ui.Activity;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.Contact;
import com.dream.NiuFaNet.Bean.SimFrendsBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.SimFrendsContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.SimFrendsPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CheckForAllUtils;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.FrendsUtils;
import com.dream.NiuFaNet.Utils.PinYinUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.StringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/17 0017.
 */
public class MySimFrendsActivity extends CommonActivity implements SimFrendsContract.View {
    @Bind(R.id.simfrend_rv)
    RecyclerView mSimfrendRv;
    @Bind(R.id.letter_rv)
    RecyclerView mLetterRv;
    @Bind(R.id.frends_refresh)
    SmartRefreshLayout mFrendsRefresh;
    @Bind(R.id.back_relay)
    RelativeLayout back_relay;

    private String[] lArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private FrendsRvAdapter mFrendsRvAdapter;
    private LetterAdapter letterAdapter;

    private List<SimFrendsBean> frendsList = new ArrayList<>();
    private List<String> letterList = new ArrayList<>();
    private Dialog mLoadingDialog;

    @Inject
    SimFrendsPresenter mSimFrendsPresenter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_mysimfrends;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        mSimFrendsPresenter.attachView(this);
        RvUtils.setOptionnoLine(mSimfrendRv, this);
        mFrendsRvAdapter = new FrendsRvAdapter(this, frendsList, R.layout.rvitem_simfrends);
        mSimfrendRv.setAdapter(mFrendsRvAdapter);

        RvUtils.setOptionnoLine(mLetterRv, this);
        letterAdapter = new LetterAdapter(this, letterList, R.layout.rvitem_onlytext);
        mLetterRv.setAdapter(letterAdapter);
        mLoadingDialog = DialogUtils.initLoadingDialog(this);
    }

    @Override
    public void initDatas() {
        String contact = (String) SpUtils.getParam(Const.Contact, "");
        String letters = (String) SpUtils.getParam(Const.Letter, "");
        if (contact.isEmpty()) {
            mLoadingDialog.show();
            new DataTask().execute();
        } else {
            List<SimFrendsBean> data = new Gson().fromJson(contact, new TypeToken<List<SimFrendsBean>>() {
            }.getType());
            if (data != null) {
                frendsList.clear();
                frendsList.addAll(data);
                mFrendsRvAdapter.notifyDataSetChanged();
            }
        }

        if (!letters.isEmpty()) {
            List<String> data = new Gson().fromJson(letters, new TypeToken<List<String>>() {
            }.getType());
            if (data != null) {
                letterList.clear();
                letterList.addAll(data);
                letterAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void eventListener() {
        mFrendsRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                new DataTask().execute();
            }
        });
        mFrendsRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
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
    public void showData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            mFrendsRvAdapter.notifyDataSetChanged();
            SpUtils.savaUserInfo(Const.Contact, new Gson().toJson(frendsList));
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    public class FrendsRvAdapter extends RVBaseAdapter<SimFrendsBean> {
        public FrendsRvAdapter(Context context, List<SimFrendsBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, SimFrendsBean contactBean, int position) {
            holder.setText(R.id.headchar_tv, contactBean.getLetter());
            ListView contacts_lv = holder.getView(R.id.contacts_lv);
            contacts_lv.setAdapter(new ContacsLvAdapter(mContext, contactBean.getContacts(), R.layout.rvitem_sendmsg_users));
        }
    }

    private class ContacsLvAdapter extends CommonAdapter<Contact> {

        public ContacsLvAdapter(Context context, List<Contact> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final Contact item, int position) {
            helper.setText(R.id.username_tv, item.getName());
            helper.setText(R.id.phone_tv, item.getPhoneNumber());
            TextView addfrends_tv = helper.getView(R.id.addfrends_tv);
            if (item.isSend()) {
                addfrends_tv.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
                addfrends_tv.setTextColor(ResourcesUtils.getColor(R.color.color6c));
                addfrends_tv.setText("已推荐");
            } else {
                addfrends_tv.setText("推荐给TA");
                addfrends_tv.setBackgroundResource(R.drawable.shape_addfrends);
                addfrends_tv.setTextColor(ResourcesUtils.getColor(R.color.white));
            }

            addfrends_tv.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    mLoadingDialog.show();
                    mSimFrendsPresenter.sendMsg(item.getPhoneNumber(), CommonAction.getUserId(), CommonAction.getUserName());
                    item.setSend(true);
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

    private class DataTask extends AsyncTask<String, String, List<SimFrendsBean>> {


        @Override
        protected List<SimFrendsBean> doInBackground(String... strings) {
            List<SimFrendsBean> frends = new ArrayList<>();
            try {
                List<Contact> contacts = FrendsUtils.loadContacts(mActivity);
                Log.e("tag", "contacs.size()=" + contacts.size());
                if (contacts != null) {
                    letterList.clear();
                    for (int i = 0; i < lArray.length; i++) {
                        SimFrendsBean simFrend = new SimFrendsBean();
                        List<Contact> contactList = new ArrayList<>();
                        for (int j = 0; j < contacts.size(); j++) {
                            String name = contacts.get(j).getName();
                            String phoneNumber = contacts.get(j).getPhoneNumber();
                            if (name != null && !name.isEmpty()) {
                                String pinYinHeadChar = PinYinUtil.getInstance().getPinYinHeadChar(name.substring(0, 1));
                                if (lArray[i].equals(pinYinHeadChar) && phoneNumber != null && !phoneNumber.isEmpty()&& CheckForAllUtils.isMobileNO(phoneNumber)) {
                                    contactList.add(contacts.get(j));
                                }
                            }
                        }
                        if (contactList.size() != 0) {
                            simFrend.setLetter(lArray[i]);
                            simFrend.setContacts(contactList);
                            frends.add(simFrend);
                            letterList.add(lArray[i]);
                        }
                    }
                    Collections.sort(letterList, new Comparator<String>() {
                        @Override
                        public int compare(String lhs, String rhs) {
                            return lhs.compareTo(rhs);
                        }
                    });
                    SpUtils.savaUserInfo(Const.Letter, new Gson().toJson(letterList));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return frends;
        }

        @Override
        protected void onPostExecute(List<SimFrendsBean> simFrendsBeen) {
            super.onPostExecute(simFrendsBeen);
            if (simFrendsBeen.size() > 0) {
                frendsList.clear();
                frendsList.addAll(simFrendsBeen);
                mFrendsRvAdapter.notifyDataSetChanged();
                letterAdapter.notifyDataSetChanged();
                mLoadingDialog.dismiss();
                mFrendsRefresh.finishRefresh();
            }
        }
    }
}
