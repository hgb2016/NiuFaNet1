package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.Contact;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.MyFrendsContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.MyFrendsPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.LocalGroupSearch;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hou on 2018/4/18.
 */

public class SearchFriendsActivity extends CommonActivity implements MyFrendsContract.View{
    @Bind(R.id.search_edt)
    EditText search_edt;
    @Bind(R.id.users_rv)
    RecyclerView users_rv;
    @Inject
    MyFrendsPresenter myFrendsPresenter;
    private List<MyFrendBean.DataBean> dataList = new ArrayList<>();
    private List<MyFrendBean.DataBean> AllData = new ArrayList<>();
    private UsersAdapter usersAdapter;
    private InputMethodManager imm;

    @Override
    public int getLayoutId() {
        return R.layout.activity_searchfriends;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        myFrendsPresenter.attachView(this);
        RvUtils.setOptionnoLine(users_rv);
        // 获取编辑框焦点
        search_edt.setFocusable(true);
        imm=ImmUtils.getImm(mActivity);
    }

    @Override
    public void initDatas() {
        myFrendsPresenter.getMyFrends(CommonAction.getUserId());
    }

    @Override
    public void eventListener() {
        search_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataList.clear();
                if (s != null && s.length() > 0) {
                    dataList = LocalGroupSearch.searchGroup2(s, (ArrayList<MyFrendBean.DataBean>) AllData);
                    if (dataList.size() > 0 && dataList != null) {
                    } else {
                        //ToastUtils.Toast_short(mActivity,"没有搜索到好友");
                    }
                } else {

                }
                usersAdapter = new  UsersAdapter(mActivity, dataList, R.layout.rvitem_myfrends);
                users_rv.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                dataList.clear();
                String s=search_edt.getText().toString().trim();
                if (s != null && s.length() > 0) {
                    dataList = LocalGroupSearch.searchGroup2(s, (ArrayList<MyFrendBean.DataBean>) AllData);
                    if (dataList.size() > 0 && dataList != null) {
                    } else {
                        ToastUtils.Toast_short("没有搜索到好友");
                    }
                } else {
                    ToastUtils.Toast_short("请输入搜索关键字");
                }
                usersAdapter = new  UsersAdapter(mActivity, dataList, R.layout.rvitem_myfrends);
                users_rv.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();
                break;
            case R.id.clear_iv:
                search_edt.setText("");
                break;
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showData(MyFrendBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            List<MyFrendBean.DataBean> data = dataBean.getData();
            if (data!=null){
                AllData.clear();
                AllData.addAll(data);
            }
            Log.e("tag","data="+new Gson().toJson(data));
        }else {
            ToastUtils.Toast_short(mActivity,ResourcesUtils.getString(R.string.failconnect));
        }
    }
    private class UsersAdapter extends RVBaseAdapter<MyFrendBean.DataBean> {

        public UsersAdapter(Context context, List<MyFrendBean.DataBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, final MyFrendBean.DataBean dataBean, int position) {
            if (dataBean.getFriendName().isEmpty()) {
                String s = "用户" + position;
                holder.setText(R.id.username_tv, "用户" + s.hashCode());
            } else {
                holder.setText(R.id.username_tv, dataBean.getFriendName());
            }
            final ImageView head_icon = holder.getView(R.id.head_icon);
            head_icon.setVisibility(View.VISIBLE);
            if (dataBean.getHeadUrl().isEmpty()) {
                head_icon.setImageResource(R.mipmap.niu);
            } else {
                holder.setImageByUrl(R.id.head_icon, dataBean.getHeadUrl(), true);
            }
            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {

                    Intent intent = new Intent(mActivity, FriendCalenderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", dataBean);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });


        }
    }
}
