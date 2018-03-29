package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.SearchUserBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.SearchUserContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.SearchUserPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2017/12/6 0006.
 */
public class AddFrendsActivity extends CommonActivity implements SearchUserContract.View {

    @Bind(R.id.search_iv)
    ImageView search_iv;
    @Bind(R.id.search_edt)
    EditText search_edt;
    @Bind(R.id.users_rv)
    RecyclerView users_rv;

    @Inject
    SearchUserPresenter searchUserPresenter;

    @Bind(R.id.back_relay)
    RelativeLayout mBackRelay;
    @Bind(R.id.title_relay)
    RelativeLayout mTitleRelay;
    @Bind(R.id.addfrend_lay)
    LinearLayout mAddfrendLay;

    private List<SearchUserBean.DataBean> dataList = new ArrayList<>();
    private UsersAdapter usersAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_addfrends;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        searchUserPresenter.attachView(this);
        RvUtils.setOptionnoLine(users_rv);
        usersAdapter = new UsersAdapter(this, dataList, R.layout.rvitem_search_users);
        users_rv.setAdapter(usersAdapter);

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {
        search_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==0){
                    users_rv.setVisibility(View.GONE);
                    mAddfrendLay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.back_relay, R.id.search_iv,R.id.addfrends_simlay,R.id.wechat_lay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.search_iv:
                String keyWord = search_edt.getText().toString();
                if (keyWord.isEmpty()) {
                    ToastUtils.Toast_short("请输入搜索关键字");
                } else {
                    searchUserPresenter.searchUser(keyWord);
                }
                break;
            case R.id.addfrends_simlay:
                IntentUtils.toActivity(MySimFrendsActivity.class,mActivity);
                break;
            case R.id.wechat_lay:
                if (CommonAction.getIsLogin()){
//                    DialogUtils.showShareWX(mActivity, Wechat.NAME);
                    String title = CommonAction.getUserName() + Const.wechatFrend_ShareTitle;
                    String content = Const.wechatFrend_ShareContent;
                    String headUrl = (String) SpUtils.getParam(Const.userHeadUrl, "");
                    String clickUrl = Const.wechatFrend_ShareClickUrl + CommonAction.getUserId();
                    DialogUtils.showShareWX(mActivity, Wechat.NAME, title, content,headUrl, clickUrl);
                }else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
        }

    }

    @Override
    public void showData(SearchUserBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<SearchUserBean.DataBean> data = dataBean.getData();
            if (data != null) {
                if (data.size()>0){
                    mAddfrendLay.setVisibility(View.GONE);
                    users_rv.setVisibility(View.VISIBLE);
                    dataList.clear();
                    dataList.addAll(data);
                    usersAdapter.notifyDataSetChanged();
                }
            }
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showAddFrendData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            ToastUtils.Toast_short("添加好友请求已发送");
            mAddfrendLay.setVisibility(View.VISIBLE);
            users_rv.setVisibility(View.GONE);
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    private class UsersAdapter extends RVBaseAdapter<SearchUserBean.DataBean> {

        public UsersAdapter(Context context, List<SearchUserBean.DataBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, final SearchUserBean.DataBean dataBean, int position) {
            holder.setText(R.id.username_tv, dataBean.getUserName());
            holder.setText(R.id.phone_tv, dataBean.getMobilePhone());
            holder.setOnClickListener(R.id.addfrends_tv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (dataBean.getUserId() != null) {
                        if (dataBean.getUserId().equals(CommonAction.getUserId())) {
                            ToastUtils.Toast_short("自己不可以添加自己哦~");
                        } else {
                            if (CommonAction.getIsLogin()){
                                searchUserPresenter.addRends(CommonAction.getUserId(), dataBean.getUserId());
                            }else {
                                DialogUtils.getLoginTip(mContext).show();
                            }
                        }
                    }
                }
            });
        }
    }
}
