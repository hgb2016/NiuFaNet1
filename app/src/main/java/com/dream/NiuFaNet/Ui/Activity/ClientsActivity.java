package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.BusBean.RefreshClientsBean;
import com.dream.NiuFaNet.Bean.BusBean.RefreshContactBean;
import com.dream.NiuFaNet.Bean.ClientDataBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.SearchMyClientsContract;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.SearchMyClientsPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hou on 2018/4/17.
 */

public class ClientsActivity extends CommonActivity implements SearchMyClientsContract.View{

    @Bind(R.id.client_gv)
    MyGridView client_gv;
    @Bind(R.id.client_lv)
    MyListView client_lv;
    @Bind(R.id.search_edt)
    EditText search_edt;
    @Bind(R.id.commonused_relay)
    RelativeLayout commonused_relay;
    @Bind(R.id.allclient_relay)
    RelativeLayout allclient_relay;
    @Bind(R.id.sure_relay)
    RelativeLayout sure_relay;
    @Bind(R.id.refresh_lay)
    SmartRefreshLayout smart_refreshlay;
    @Bind(R.id.empty_lay)
    LinearLayout empty_lay;
    private ClientTipAdaper clientTipAdaper;
    private AllClientAdaper allClientAdaper;
    private ArrayList<ClientDataBean.DataBean> clients=new ArrayList<>();
    private int page=1;
    @Inject
    SearchMyClientsPresenter searchMyClientsPresenter;
    private boolean isLoadMore;
    private String searchValue="";
    private String tag;

    @Override
    public int getLayoutId() {
        return R.layout.activity_client;
    }
    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        searchMyClientsPresenter.attachView(this);
        EventBus.getDefault().register(this);
        ArrayList list=new ArrayList();
        list.add('1');
        list.add('1');
        list.add('1');
        clientTipAdaper=new ClientTipAdaper(mActivity,list,R.layout.view_clienttip);
        allClientAdaper=new AllClientAdaper(mActivity,clients,R.layout.view_clienttext);
        client_gv.setAdapter(clientTipAdaper);
        client_lv.setAdapter(allClientAdaper);
    }

    @Override
    public void initDatas() {
        mLoadingDialog.show();
        Map<String, String> map = MapUtils.getMapInstance();
        map.put("pageNow",page+"");
        searchMyClientsPresenter.searchMyClients(CommonAction.getUserId(),map);
        tag = getIntent().getStringExtra(Const.intentTag);
        if (tag!=null){
            if (tag.equals("client")) {
                commonused_relay.setVisibility(View.GONE);
                client_gv.setVisibility(View.GONE);
                allclient_relay.setVisibility(View.GONE);
                sure_relay.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //事件监听
    @Override
    public void eventListener() {
        search_edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String keyword=search_edt.getText().toString().trim();
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    clients.clear();
                    Map<String, String> map = MapUtils.getMapInstance();
                    page=1;
                    map.put("pageNow",page+"");
                    map.put("searchValues",keyword);
                    mLoadingDialog.show();
                    searchMyClientsPresenter.searchMyClients(CommonAction.getUserId(),map);
                    return true;
                }
                return false;
            }
        });

        smart_refreshlay.setEnableLoadmore(true);
        smart_refreshlay.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                Map<String,String> map = new HashMap<String, String>();
//                map.put("page",String.valueOf(1));
                clients.clear();
                page=1;
                isLoadMore = false;
                Map<String, String> map = MapUtils.getMapInstance();
                map.put("pageNow",page+"");
                searchMyClientsPresenter.searchMyClients(CommonAction.getUserId(),map);


            }
        });
        smart_refreshlay.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                isLoadMore = true;
                Map<String, String> map = MapUtils.getMapInstance();
                map.put("pageNow",page+"");
                searchMyClientsPresenter.searchMyClients(CommonAction.getUserId(),map);

            }
        });

    }

    //点击事件
    @OnClick({R.id.back_relay,R.id.addclient_iv,R.id.sure_relay})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.addclient_iv:
                IntentUtils.toActivityWithTag(NewClientActivity.class, mActivity, "newclient",100);
                break;
            case R.id.sure_relay:
                Intent intent = new Intent();
                for (int i=0;i<clients.size();i++) {
                    if (clients.get(i).isSelect()) {
                        intent.putExtra("clientName",clients.get(i).getClientName());
                        intent.putExtra("clientId",clients.get(i).getClientId());
                    }
                }
                setResult(201,intent);
                finish();
                break;
        }
    }

    @Override
    public void showData(ClientDataBean dataBean) {
        mLoadingDialog.dismiss();
        smart_refreshlay.finishRefresh();
        smart_refreshlay.finishLoadmore();
        if (dataBean.getError().equals(Const.success)){
            ArrayList<ClientDataBean.DataBean> data = dataBean.getData();
            if (data!=null&&data.size()>0){
                clients.addAll(data);
                String tag = getIntent().getStringExtra(Const.intentTag);
                if (tag!=null) {
                    for (int i = 0; i < clients.size(); i++) {
                        if (tag.equals(clients.get(i).getClientName())) {
                            clients.get(i).setSelect(true);
                        }
                    }
                }
                if (clients.size()>0){
                    empty_lay.setVisibility(View.GONE);
                    client_lv.setVisibility(View.VISIBLE);
                }else {
                    empty_lay.setVisibility(View.VISIBLE);
                    client_lv.setVisibility(View.GONE);
                }
                allClientAdaper.notifyDataSetChanged();
            }else {
                if (clients.size()>0){
                    empty_lay.setVisibility(View.GONE);
                    client_lv.setVisibility(View.VISIBLE);
                }else {
                    client_lv.setVisibility(View.GONE);
                    empty_lay.setVisibility(View.VISIBLE);
                }
            }
        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showDeleteResult(CommonBean commonBean) {
        if (commonBean.getError().equals(Const.success)){
            mLoadingDialog.dismiss();
            ToastUtils.showToast1(mContext,"删除成功");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            clients.clear();
            Map<String, String> map = MapUtils.getMapInstance();
            page=1;
            map.put("pageNow",page+"");
            searchMyClientsPresenter.searchMyClients(CommonAction.getUserId(),map);
        }
    }

    @Override
    public void showError() {
        smart_refreshlay.finishRefresh();
        ToastUtils.Toast_short(getResources().getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        smart_refreshlay.finishRefresh();
        mLoadingDialog.dismiss();
    }

    //常用tip列表
    public  class  ClientTipAdaper extends CommonAdapter{

        public ClientTipAdaper(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }

    //全部客户列表
    public  class  AllClientAdaper extends CommonAdapter<ClientDataBean.DataBean>{

        public AllClientAdaper(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final ClientDataBean.DataBean item, int position) {
            LinearLayout address_lay=helper.getView(R.id.address_lay);
            helper.setText(R.id.clientname_tv,item.getClientName());
            helper.setText(R.id.address_tv,item.getClientAddress());
            ImageView check_iv=helper.getView(R.id.check_iv);
            if (tag!=null&&tag.equals("client")){
                check_iv.setVisibility(View.GONE);
            }
            if (item.isSelect()){
                check_iv.setImageResource(R.mipmap.check_green);
            }else {
                check_iv.setImageResource(R.mipmap.emptycheck_2);
            }
            if (item.getClientAddress().isEmpty()){
                address_lay.setVisibility(View.GONE);
            }else {
                address_lay.setVisibility(View.VISIBLE);
            }
            helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    Intent intent=new Intent();
                    intent.putExtra("clientId",item.getClientId());
                    intent.setClass(mContext,ClientDetailActivity.class);
                    startActivity(intent);
                }
            });
            if (tag!=null&&tag.equals("client")){
                check_iv.setVisibility(View.GONE);
            }else {
               /* helper.setOnClickListener(R.id.empty_view, new NoDoubleClickListener() {
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
                    }
                });*/
            }
            helper.setOnClickListener(R.id.check_lay, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (item.isSelect()){
                        item.setSelect(false);
                    }else {
                        int count=0;
                        for (int i=0;i<mDatas.size();i++){
                            if (mDatas.get(i).isSelect()){
                                count++;
                            }
                        }
                        if (count>1){
                            item.setSelect(false);
                        }else {
                            for (int i=0;i<mDatas.size();i++){
                                mDatas.get(i).setSelect(false);
                            }
                            item.setSelect(true);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(RefreshClientsBean clientsBean) {
        if (clientsBean.getEventStr().equals(Const.refresh)) {
            String userId=CommonAction.getUserId();
            if (userId!=null&&!userId.isEmpty()) {
                clients.clear();
                Map<String, String> map = MapUtils.getMapInstance();
                page=1;
                map.put("pageNow",page+"");
                searchMyClientsPresenter.searchMyClients(CommonAction.getUserId(),map);
            }
        }
    }
}
