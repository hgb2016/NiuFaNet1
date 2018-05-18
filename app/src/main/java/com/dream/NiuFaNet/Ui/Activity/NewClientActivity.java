package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CustomHelper;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.example.zhouwei.library.CustomPopWindow;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hou on 2018/4/16.
 */

public class NewClientActivity extends CommonActivity {
    @Bind(R.id.back_relay)
    RelativeLayout back_relay;
    @Bind(R.id.clientname_edt)
    EditText clientname_edt;
    @Bind(R.id.web_edt)
    EditText web_edt;
    @Bind(R.id.address_edt)
    EditText address_edt;
    @Bind(R.id.phone_edt)
    EditText phone_edt;
    @Bind(R.id.fax_edt)
    EditText fax_edt;
    @Bind(R.id.iv_more)
    ImageView iv_more;
    private CustomPopWindow mCustomPopWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_newclient;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);

    }
    @OnClick({R.id.back_relay,R.id.more_lay,R.id.addcontact_iv,R.id.addvisible_iv,R.id.submit_relay})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.more_lay:

                break;
            case R.id.addcontact_iv:
                showcontact(v);
                break;
            case R.id.addvisible_iv:
                //跳转到通讯录
                IntentUtils.toActivityWithTag(MyFrendsActivity.class, mActivity, "newclient", 155);
                break;
            case R.id.submit_relay:

                break;
        }
    }
    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }

    //填写联系人弹框
    private void showcontact(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_contactinfo,null);
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCustomPopWindow!=null){
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()){
                    case R.id.save_relay:
                        mCustomPopWindow.dissmiss();

                        break;

                }
                //Toast.makeText(HomeActivity.this,showContent,Toast.LENGTH_SHORT).show();
            }
        };
        contentView.findViewById(R.id.save_relay).setOnClickListener(listener);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .setAnimationStyle(R.anim.pickerview_slide_in_bottom)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(v, Gravity.BOTTOM,0,0);
    }
    private class PeoPleAdapter extends CommonAdapter<ProgramDetailBean.DataBean.participantBean> {

        public PeoPleAdapter(Context context, List<ProgramDetailBean.DataBean.participantBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final ProgramDetailBean.DataBean.participantBean item, final int position) {
            ImageView only_iv = helper.getView(R.id.only_iv);
            ImageView close_iv = helper.getView(R.id.close_iv);
            TextView only_tv = helper.getView(R.id.only_tv);
            only_tv.setMaxLines(1);
            only_tv.setEllipsize(TextUtils.TruncateAt.END);
            only_tv.setText(item.getUserName());
            if (item.isDelete()) {
                close_iv.setVisibility(View.VISIBLE);
            } else {
                close_iv.setVisibility(View.GONE);
            }

            String headUrl = item.getHeadUrl();
            if (!item.isEmpty()){
                if (headUrl!=null&&!headUrl.isEmpty()){
                    helper.setImageByUrl(R.id.only_iv,headUrl,true);
                }else {
                    only_iv.setImageResource(R.mipmap.niu);
                }
            }

            close_iv.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                  //  participantBeanList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==155&&resultCode==102&&data!=null) {
            List<MyFrendBean.DataBean> selectdata = (List<MyFrendBean.DataBean>) data.getExtras().getSerializable("selectdata");

        }
    }
}
