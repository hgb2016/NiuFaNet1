package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BusBean.RefreshBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.MyFrendsContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.MyFrendsPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2017/12/6 0006.
 */
public class MyFrendsActivity extends CommonActivity implements MyFrendsContract.View {

    @Bind(R.id.search_iv)
    ImageView search_iv;
    @Bind(R.id.search_edt)
    EditText search_edt;
    @Bind(R.id.users_rv)
    RecyclerView users_rv;
    @Bind(R.id.fixgrid_lay)
    GridView fixgrid_lay;
    @Bind(R.id.addfrends_iv)
    ImageView addfrends_iv;
    @Bind(R.id.invite_relay)
    RelativeLayout invite_relay;

    @Inject
    MyFrendsPresenter myFrendsPresenter;
    private List<MyFrendBean.DataBean> dataList = new ArrayList<>();
    private List<MyFrendBean.DataBean> selectList = new ArrayList<>();
    private UsersAdapter usersAdapter;
    private SelectedAdapter selectedAdapter;

    private boolean isSelect;
    private int rightTag;
    @Override
    public int getLayoutId() {
        return R.layout.activity_myfrends;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        myFrendsPresenter.attachView(this);
        RvUtils.setOptionnoLine(users_rv);
        usersAdapter = new UsersAdapter(this, dataList, R.layout.rvitem_myfrends);
        users_rv.setAdapter(usersAdapter);

        selectedAdapter = new SelectedAdapter(this,selectList,R.layout.gvitem_timg_btext);
        fixgrid_lay.setAdapter(selectedAdapter);

        String tag = getIntent().getStringExtra(Const.intentTag);
        if (tag!=null){
            isSelect = true;
            addfrends_iv.setImageResource(R.mipmap.finish);
            rightTag = 1;
            invite_relay.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void initDatas() {
        myFrendsPresenter.getMyFrends(CommonAction.getUserId());
    }

    @Override
    public void eventListener() {

    }

    @OnClick({R.id.back_relay, R.id.search_iv,R.id.addfrends_iv,R.id.invite_relay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.addfrends_iv:
                if (rightTag==1){
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectdata", (Serializable) selectList);
                    intent.putExtras(bundle);
                    setResult(102,intent);
                    finish();
                }else {
                    IntentUtils.toActivity(AddFrendsActivity.class,mActivity);
                }
                break;
            case R.id.search_iv:
                String keyWord = search_edt.getText().toString();
                if (keyWord.isEmpty()) {
                    ToastUtils.Toast_short("请输入搜索关键字");
                } else {

                }
                break;
            case R.id.invite_relay:
                String title = CommonAction.getUserName() + Const.wechatFrend_ShareTitle;
                String content = Const.wechatFrend_ShareContent;
                String headUrl = (String) SpUtils.getParam(Const.userHeadUrl, "");
                String clickUrl = Const.wechatFrend_ShareClickUrl + CommonAction.getUserId();
                DialogUtils.showShareWX(mActivity, Wechat.NAME, title, content,headUrl, clickUrl);
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
                dataList.clear();
                dataList.addAll(data);
                usersAdapter.notifyDataSetChanged();
            }
            Log.e("tag","data="+new Gson().toJson(data));
        }else {
            ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
        }
    }

    private class UsersAdapter extends RVBaseAdapter<MyFrendBean.DataBean> {

        public UsersAdapter(Context context, List<MyFrendBean.DataBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, final MyFrendBean.DataBean dataBean, int position) {
            if (dataBean.getFriendName().isEmpty()){
                String s = "用户" + position;
                holder.setText(R.id.username_tv, "用户"+s.hashCode());
            }else {
                holder.setText(R.id.username_tv, dataBean.getFriendName());
            }
            final ImageView select_iv = holder.getView(R.id.select_iv);
            final ImageView head_icon = holder.getView(R.id.head_icon);
            head_icon.setVisibility(View.VISIBLE);
            if (isSelect){
                select_iv.setVisibility(View.VISIBLE);
                if (dataBean.isSelect()){
                    select_iv.setImageResource(R.mipmap.choose);
                }else {
                    select_iv.setImageResource(R.drawable.shape_circle_noselect);
                }

            }else {
                select_iv.setVisibility(View.GONE);
            }

            if (dataBean.getHeadUrl().isEmpty()){
                head_icon.setImageResource(R.mipmap.niu);
            }else {
                holder.setImageByUrl(R.id.head_icon,dataBean.getHeadUrl(),true);
            }

            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {

                    if (isSelect){
                        if (dataBean.isSelect()){
                            select_iv.setImageResource(R.drawable.shape_circle_noselect);
                            dataBean.setSelect(false);
                            for (int i = 0; i < selectList.size(); i++) {
                                if (selectList.get(i).getId().equals(dataBean.getId())){
                                    selectList.remove(i);
                                }
                            }
                        }else {
                            select_iv.setImageResource(R.mipmap.choose);
                            dataBean.setSelect(true);
                            selectList.add(dataBean);
                        }
                        notifyDataSetChanged();
                        selectedAdapter.notifyDataSetChanged();
                    }

                }
            });

            holder.setOnClickListener(R.id.next_info, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    Intent intent = new Intent(mActivity,FrendScheduleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data",dataBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }
    }

    private class SelectedAdapter extends CommonAdapter<MyFrendBean.DataBean>{

        public SelectedAdapter(Context context, List<MyFrendBean.DataBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final MyFrendBean.DataBean item, final int position) {
            ImageView close_iv = helper.getView(R.id.close_iv);
            close_iv.setVisibility(View.VISIBLE);
            TextView only_tv = helper.getView(R.id.only_tv);
            only_tv.setVisibility(View.VISIBLE);

            if (item.getHeadUrl().isEmpty()){
                helper.setImageResource(R.id.only_iv,R.mipmap.niu);
            }else {
                helper.setImageByUrl(R.id.only_iv,item.getHeadUrl(),true);
            }
            if (item.getFriendName().isEmpty()){
                only_tv.setText( "用户"+position);
            }else {
                only_tv.setText(item.getFriendName());
            }
            helper.setOnClickListener(R.id.close_iv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    selectList.remove(position);
                    selectedAdapter.notifyDataSetChanged();
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).getId().equals(item.getId())){
                            dataList.get(i).setSelect(false);
                        }
                    }
                    usersAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
