package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.FriendNoticeBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.GetFriendNoticeContract;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.GetFriendNoticePresenter;
import com.dream.NiuFaNet.R;
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

public class NewFriendsActivity extends CommonActivity implements GetFriendNoticeContract.View{
    @Inject
    GetFriendNoticePresenter getFriendNoticePresenter;
    @Bind(R.id.newfriends_lv)
    MyListView newfriends_lv;
    @Bind(R.id.empty_lay)
    LinearLayout empty_lay;
    private  NewFriendsAdapter newFriendsAdapter;
    private ArrayList<FriendNoticeBean.NoticeBean> friendlist=new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_newfriend;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        getFriendNoticePresenter.attachView(this);
        newFriendsAdapter=new NewFriendsAdapter(mActivity,friendlist,R.layout.view_newfriend);
        newfriends_lv.setAdapter(newFriendsAdapter);
    }

    @Override
    public void initDatas() {
        getFriendNoticePresenter.GetFriendNotice(CommonAction.getUserId());
    }

    @Override
    public void eventListener() {

    }
    @OnClick({R.id.search_lay,R.id.back_relay})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.search_lay:
                startActivity(new Intent(mActivity,SearchNewFriendsActivity.class));
                break;
            case R.id.back_relay:
                finish();
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
    public void showData(FriendNoticeBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            List<FriendNoticeBean.NoticeBean> data = dataBean.getData();
            if (data!=null) {
                if (data.size()>0) {
                    empty_lay.setVisibility(View.GONE);
                    friendlist.clear();
                    friendlist.addAll(data);
                    Log.i("friendid", new Gson().toJson(friendlist));
                    newFriendsAdapter.notifyDataSetChanged();
                }else {
                    empty_lay.setVisibility(View.VISIBLE);
                }
            }else {
                empty_lay.setVisibility(View.VISIBLE);
            }
        }else {
            ToastUtils.Toast_short(dataBean.getError());
        }
    }

    @Override
    public void showrecevieData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            ToastUtils.showToast(mActivity,"成功接受对方的请求!",R.mipmap.checkmark);
            getFriendNoticePresenter.GetFriendNotice(CommonAction.getUserId());
            CommonAction.refreshContact();
        }
    }

    public class NewFriendsAdapter extends CommonAdapter<FriendNoticeBean.NoticeBean>{
        public NewFriendsAdapter(Context context, List<FriendNoticeBean.NoticeBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final FriendNoticeBean.NoticeBean item, int position) {
            helper.setText(R.id.username_tv,item.getFriendName());
            helper.setText(R.id.requestinfo,"请求添加你为好友");
            TextView agree_tv=helper.getView(R.id.agree_tv);
            helper.setImageByUrl(R.id.head_iv,item.getHeadUrl(),true);
            if (item.getAcceptStatus().equals("1")){
                agree_tv.setText("接受");
                agree_tv.setTextColor(getResources().getColor(R.color.textcolor_blue2));
                agree_tv.setBackgroundResource(R.drawable.shape_blue);
            }else {
                agree_tv.setText("已同意");
                agree_tv.setTextColor(getResources().getColor(R.color.textcolor_bd));
                agree_tv.setBackgroundResource(R.drawable.shape_gray);
            }

            helper.setOnClickListener(R.id.agree_tv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {

                    if (item.getAcceptStatus().equals("1")) {
                        getFriendNoticePresenter.receiveFriend(CommonAction.getUserId(), item.getFriendId());
                    }
                }
            });
        }
    }

    @Override
    public void finish() {
        super.finish();
        CommonAction.refreshContact();
    }
}
