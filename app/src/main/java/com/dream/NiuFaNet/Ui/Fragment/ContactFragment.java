package com.dream.NiuFaNet.Ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseFragmentV4;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BusBean.RefreshContactBean;
import com.dream.NiuFaNet.Bean.BusBean.RefreshProBean;
import com.dream.NiuFaNet.Bean.Contact;
import com.dream.NiuFaNet.Bean.EditCount;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.ShowCountBean;
import com.dream.NiuFaNet.Bean.SimFrendsBean;
import com.dream.NiuFaNet.Bean.SimFrendsBean1;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.MyFrendsContract;
import com.dream.NiuFaNet.Contract.ShowNoticeCountContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.MyFrendsPresenter;
import com.dream.NiuFaNet.Presenter.ShowNoticeCountPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.FriendCalenderActivity;
import com.dream.NiuFaNet.Ui.Activity.FriendDetailActivity;
import com.dream.NiuFaNet.Ui.Activity.MySimFrendsActivity;
import com.dream.NiuFaNet.Ui.Activity.NewFriendsActivity;
import com.dream.NiuFaNet.Ui.Activity.SearchFriendsActivity;
import com.dream.NiuFaNet.Ui.Activity.SearchNewFriendsActivity;
import com.dream.NiuFaNet.Utils.CheckForAllUtils;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.FrendsUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by ljq on 2018/3/23.
 */

public class ContactFragment extends BaseFragmentV4 implements MyFrendsContract.View ,ShowNoticeCountContract.View{
    @Inject
    MyFrendsPresenter myFrendsPresenter;
    @Inject
    ShowNoticeCountPresenter showNoticeCountPresenter;

    @Bind(R.id.simfrend_rv)
    RecyclerView  mSimfrendRv;
    @Bind(R.id.letter_rv)
    RecyclerView  mLetterRv;
    @Bind(R.id.frends_refresh)
    SmartRefreshLayout mFrendsRefresh;
    @Bind(R.id.noticecount_tv)
    TextView noticecount_tv;
    @Bind(R.id.empty_lay)
    LinearLayout empty_lay;
    private List<SimFrendsBean1> frendsList = new ArrayList<>();
    private List<String> letterList = new ArrayList<>();
    private String[] lArray = new String[]{"#","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private FrendsRvAdapter mFrendsRvAdapter;
    private LetterAdapter letterAdapter;

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        myFrendsPresenter.attachView(this);
        showNoticeCountPresenter.attachView(this);
        RvUtils.setOptionnoLine(mSimfrendRv, getActivity());
        EventBus.getDefault().register(this);
        mFrendsRvAdapter = new FrendsRvAdapter(getActivity(), frendsList, R.layout.rvitem_simfrends);
        mSimfrendRv.setAdapter(mFrendsRvAdapter);

        RvUtils.setOptionnoLine(mLetterRv, getActivity());
        letterAdapter = new LetterAdapter(getActivity(), letterList, R.layout.rvitem_onlytext);
        mLetterRv.setAdapter(letterAdapter);

    }
    @Override
    protected View loadViewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_contact, null);
    }
    @Override
    public void initResume() {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(RefreshContactBean proBean) {
        if (proBean.getEventStr().equals(Const.refresh)) {
            String userId=CommonAction.getUserId();
            if (userId!=null&&!userId.isEmpty()) {
                myFrendsPresenter.getMyFrends(CommonAction.getUserId());
                showNoticeCountPresenter.showNoticeCount(CommonAction.getUserId());
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initEvents() {
        mFrendsRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                String userId=CommonAction.getUserId();
                if (userId!=null&&!userId.isEmpty()) {
                    myFrendsPresenter.getMyFrends(CommonAction.getUserId());
                }
            }
        });
        mFrendsRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });
    }

    @Override
    public void initDta() {
        String userId=CommonAction.getUserId();
        if (userId!=null&&!userId.isEmpty()) {
            myFrendsPresenter.getMyFrends(CommonAction.getUserId());
            showNoticeCountPresenter.showNoticeCount(CommonAction.getUserId());
        }
    }

    @OnClick({R.id.contact_lay, R.id.wechat_lay,R.id.search_relay,R.id.newfriend_lay})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.contact_lay:
                startActivity(new Intent(getActivity(), MySimFrendsActivity.class));
                break;
            case R.id.wechat_lay:
                if (CommonAction.getIsLogin()) {
//                    DialogUtils.showShareWX(mActivity, Wechat.NAME);
                    String title = CommonAction.getUserName() + Const.wechatFrend_ShareTitle;
                    String content = Const.wechatFrend_ShareContent;
                    String headUrl = (String) SpUtils.getParam(Const.userHeadUrl, "");
                    String clickUrl = Const.wechatFrend_ShareClickUrl + CommonAction.getUserId();
                    DialogUtils.showShareWX(getActivity(), Wechat.NAME, title, content, headUrl, clickUrl);
                } else {
                    DialogUtils.getLoginTip(getActivity()).show();
                }
                break;
            case R.id.search_relay:
                startActivity(new Intent(getActivity(),SearchFriendsActivity.class));
                break;
            case R.id.newfriend_lay:
                startActivity(new Intent(getActivity(), NewFriendsActivity.class));
                break;
        }
    }

    @Override
    public void showData(ShowCountBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            if (!dataBean.getShowCount().equals("0")){
                noticecount_tv.setText(dataBean.getShowCount());
                noticecount_tv.setVisibility(View.VISIBLE);
            }else {
                noticecount_tv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showEditCount(EditCount editCount) {

    }


    private class LetterAdapter extends RVBaseAdapter<String> {

        public LetterAdapter(Context context, List<String> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, final String cityBean, int position) {
            final TextView only_tv = holder.getView(R.id.only_tv);
            only_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            only_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
            only_tv.setText(cityBean);
            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    for (int j = 0; j < letterList.size(); j++) {
                        if (cityBean.equals(letterList.get(j))) {
//                          cars_rv.smoothScrollToPosition(j);
                            if (frendsList.size()>6) {
                                RvUtils.MoveToPosition((LinearLayoutManager) mSimfrendRv.getLayoutManager(), j);
                            }
                        }
                    }
                }
            });
        }
    }

    //以字母排序

    public class FrendsRvAdapter extends RVBaseAdapter<SimFrendsBean1> {
        public FrendsRvAdapter(Context context, List<SimFrendsBean1> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder,SimFrendsBean1 contactBean, int position) {
            holder.setText(R.id.headchar_tv, contactBean.getLetter());
            ListView contacts_lv = holder.getView(R.id.contacts_lv);
            contacts_lv.setAdapter(new FriendsLvAdapter(getContext(),contactBean.getContacts() , R.layout.rvitem_myfriends1));
        }
    }
    //好友列表
    private class FriendsLvAdapter extends CommonAdapter<MyFrendBean.DataBean> {

        public FriendsLvAdapter(Context context, List<MyFrendBean.DataBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final MyFrendBean.DataBean dataBean, int position) {
                    helper.setImageByUrl(R.id.head_icon,dataBean.getHeadUrl(),true);
                    helper.setText(R.id.username_tv,dataBean.getShowName());
                    helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View view) {
                            Intent intent1=new Intent(getContext(),FriendDetailActivity.class);
                            intent1.putExtra("friendid",dataBean.getFriendId());
                            startActivity(intent1);
                        }
                    });
        }

    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {

    }

    @Override
    public void showData(MyFrendBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            List<MyFrendBean.DataBean> data = dataBean.getData();
            List<SimFrendsBean1> frends = new ArrayList<>();
            if (data!=null) {
                if (data.size() > 0) {
                    empty_lay.setVisibility(View.GONE);
                    letterList.clear();
                    for (int i = 0; i < lArray.length; i++) {
                        SimFrendsBean1 simFrend = new SimFrendsBean1();
                        List<MyFrendBean.DataBean> contactList = new ArrayList<>();
                        for (int j = 0; j < data.size(); j++) {
                            String name = data.get(j).getShowName();
                            if (name != null && !name.isEmpty()) {
                                String pinYinHeadChar = PinYinUtil.getInstance().getPinYinHeadChar(name.substring(0, 1));
                                if (!pinYinHeadChar.matches("^[a-zA-Z]")) {
                                    pinYinHeadChar = "#";
                                }
                                if (lArray[i].equals(pinYinHeadChar)) {
                                    contactList.add(data.get(j));
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
                    frendsList.clear();
                    frendsList.addAll(frends);
                    letterAdapter.notifyDataSetChanged();
                    mFrendsRvAdapter.notifyDataSetChanged();
                    mFrendsRefresh.finishRefresh();
                    //usersAdapter.notifyDataSetChanged();
                } else {
                    mFrendsRefresh.finishRefresh();
                }
            }else {
                empty_lay.setVisibility(View.VISIBLE);
            }

        }else {
            ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
        }
    }
}
