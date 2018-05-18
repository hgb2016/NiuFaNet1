package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hou on 2018/4/17.
 */

public class ClientsActivity extends CommonActivity {

    @Bind(R.id.client_gv)
    MyGridView client_gv;
    @Bind(R.id.client_lv)
    MyListView client_lv;
    private ClientTipAdaper clientTipAdaper;
    private AllClientAdaper allClientAdaper;
    @Override
    public int getLayoutId() {
        return R.layout.activity_client;
    }
    @Override
    public void initView() {
            ArrayList list=new ArrayList();
            list.add('1');
            list.add('1');
            list.add('1');
            clientTipAdaper=new ClientTipAdaper(mActivity,list,R.layout.view_clienttip);
            allClientAdaper=new AllClientAdaper(mActivity,list,R.layout.view_text);
            client_gv.setAdapter(clientTipAdaper);
            client_lv.setAdapter(allClientAdaper);
    }

    @Override
    public void initDatas() {

    }
    @Override
    public void eventListener() {

    }
    @OnClick({R.id.back_relay,R.id.addclient_iv,R.id.search_relay})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.addclient_iv:
                startActivity(new Intent(mActivity,NewClientActivity.class));
                break;
            case R.id.search_relay:

                break;
        }
    }
    //常用列表
    public  class  ClientTipAdaper extends CommonAdapter{

        public ClientTipAdaper(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }
    //全部客户列表
    public  class  AllClientAdaper extends CommonAdapter{

        public AllClientAdaper(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, Object item, int position) {

        }
    }
}
