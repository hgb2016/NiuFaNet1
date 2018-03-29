package com.dream.NiuFaNet.Ui.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseActivity;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.BusBean.RefreshProBean;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.NewProResultBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.NewProgramContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.NewProgramPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ImgUtil;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/4 0004.
 */
public class NewProgramActivity extends CommonActivity implements NewProgramContract.View {

    @Bind(R.id.submit_iv)
    ImageView submit_iv;
    @Bind(R.id.close_iv)
    ImageView close_iv;
    @Bind(R.id.voice_iv)
    ImageView voice_iv;
    @Bind(R.id.title_tv)
    EditText title_tv;
    @Bind(R.id.annum_tv)
    EditText annum_tv;
    @Bind(R.id.beizu_edt)
    EditText beizu_edt;
    @Bind(R.id.particepant_gv)
    GridView particepant_gv;
    @Bind(R.id.doing_tv)
    TextView doing_tv;
    @Bind(R.id.end_tv)
    TextView end_tv;
    @Bind(R.id.statu_lay)
    LinearLayout statu_lay;
    @Bind(R.id.statu_relay)
    RelativeLayout statu_relay;
    @Bind(R.id.addpeople_iv)
    ImageView addpeople_iv;

    private PeoPleAdapter peoPlesAdapter;
    private List<ProgramDetailBean.DataBean.participantBean> participantBeanList = new ArrayList<>();
    private List<ProgramDetailBean.DataBean.participantBean> dataParcipant = new ArrayList<>();
    private Map<String,ProgramDetailBean.DataBean.participantBean> mMap = new HashMap<>();
    private Dialog loadingDialog;
    private String tempStatus = "0";
    @Inject
    NewProgramPresenter programPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_addprogram;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        programPresenter.attachView(this);

        peoPlesAdapter = new PeoPleAdapter(this, participantBeanList, R.layout.gvitem_timg_btext);
        particepant_gv.setAdapter(peoPlesAdapter);
        loadingDialog = DialogUtils.initLoadingDialog(this);
    }

    @Override
    public void initDatas() {

        peoPlesAdapter.notifyDataSetChanged();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            origData = (ProgramDetailBean.DataBean) extras.getSerializable("data");
            if (origData!=null){
                initDataView();
            }
        }
        intentTag = getIntent().getStringExtra(Const.intentTag);

    }

    private void initDataView() {
        title_tv.setText(origData.getName());
        annum_tv.setText(origData.getCaseNo());
        beizu_edt.setText(origData.getDescription());
        //初始化参与者
        List<ProgramDetailBean.DataBean.participantBean> participant = origData.getParticipant();
        if (participant!=null){
            participantBeanList.clear();
            dataParcipant.clear();
            participantBeanList.addAll(participant);
            dataParcipant.addAll(participant);
            peoPlesAdapter.notifyDataSetChanged();
        }

        String status = origData.getStatus();
        if (status!=null&&!status.isEmpty()){
            statu_relay.setVisibility(View.VISIBLE);
            tempStatus = status;
            if (status.equals("0")){
                statu_lay.setBackgroundResource(R.mipmap.icon_underway);
                doing_tv.setTextColor(ResourcesUtils.getColor(R.color.white));
                end_tv.setTextColor(ResourcesUtils.getColor(R.color.color87));
            }else if (status.equals("1")){
                statu_lay.setBackgroundResource(R.mipmap.icon_finish);
                doing_tv.setTextColor(ResourcesUtils.getColor(R.color.color87));
                end_tv.setTextColor(ResourcesUtils.getColor(R.color.white));
            }else {
                statu_relay.setVisibility(View.GONE);
            }
        }else {
            statu_relay.setVisibility(View.GONE);
        }

        for (int i = 0; i < participantBeanList.size(); i++) {
            participantBeanList.get(i).setDelete(true);
            participantBeanList.get(i).setSource(true);
            mMap.put(participantBeanList.get(i).getUserId(),participantBeanList.get(i));
        }
    }

    private ProgramDetailBean.DataBean origData;
    private String intentTag;
    @Override
    public void eventListener() {

    }

    @OnClick({R.id.close_iv, R.id.submit_iv,R.id.addpeople_iv, R.id.doing_tv, R.id.end_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                finish();
                break;
            case R.id.submit_iv://创建项目
                String title = title_tv.getText().toString();
                String anNumber = annum_tv.getText().toString();
                String remark = beizu_edt.getText().toString();
                if (origData==null){
                    origData = new ProgramDetailBean.DataBean();
                }
                origData.setName(title);
                origData.setCaseNo(anNumber);
                origData.setParticipant(participantBeanList);
                origData.setDescription(remark);
                origData.setUserId(CommonAction.getUserId());
                origData.setStatus(tempStatus);
                if (title.isEmpty()) {
                    ToastUtils.Toast_short("请输入标题");
                } else {
                    String dataStr = new Gson().toJson(origData);
                    Log.e("pro","newproData="+dataStr);
                    loadingDialog.show();
                    if (intentTag!=null){
                        if (intentTag.equals("edit")){
                            programPresenter.edtProject(dataStr);
                        }else {
                            programPresenter.newProgram(dataStr);
                        }
                    }else {
                        programPresenter.newProgram(dataStr);
                    }
                }
                break;
            case R.id.doing_tv:
                statu_lay.setBackgroundResource(R.mipmap.icon_underway);
                doing_tv.setTextColor(ResourcesUtils.getColor(R.color.white));
                end_tv.setTextColor(ResourcesUtils.getColor(R.color.color87));
                tempStatus = "0";
                break;
            case R.id.end_tv:
                statu_lay.setBackgroundResource(R.mipmap.icon_finish);
                doing_tv.setTextColor(ResourcesUtils.getColor(R.color.color87));
                end_tv.setTextColor(ResourcesUtils.getColor(R.color.white));
                tempStatus = "1";
                break;
            case R.id.addpeople_iv:
                //跳转到通讯录
                IntentUtils.toActivityWithTag(MyFrendsActivity.class, mActivity, "newpro", 101);
                break;
        }

    }

    @Override
    public void showData(NewProResultBean dataBean) {
        Log.e("tag", "dataBean=" + dataBean.toString());
        ToastUtils.Toast_short(dataBean.getMessage());
        if (dataBean.getError().equals(Const.success)) {
            CommonAction.refreshPro();
            String projectId = dataBean.getProjectId();
            if (projectId==null){
                ToastUtils.Toast_short("数据异常");
                return;
            }
            if (intentTag!=null&&intentTag.equals("newCal")){
                Intent intent = getIntent();
                intent.putExtra("projectId", projectId);
                intent.putExtra("title",title_tv.getText().toString());
                setResult(111,intent);
            }
            finish();
        }
    }

    @Override
    public void showEdtData(CommonBean dataBean) {
        ToastUtils.Toast_short(dataBean.getMessage());
        if (dataBean.getError().equals(Const.success)){
            finish();
        }
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {
        loadingDialog.dismiss();
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
                    participantBeanList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101&&resultCode==102&&data!=null){
            List<MyFrendBean.DataBean> selectdata = (List<MyFrendBean.DataBean>) data.getExtras().getSerializable("selectdata");
            if (selectdata!=null&&selectdata.size()>0){
                participantBeanList.clear();
                for (int i = 0; i <selectdata.size() ; i++) {
                    MyFrendBean.DataBean dataBean = selectdata.get(i);
                    ProgramDetailBean.DataBean.participantBean partsBean = new ProgramDetailBean.DataBean.participantBean();
                    partsBean.setUserId(dataBean.getFriendId());
                    partsBean.setUserName(dataBean.getFriendName());
                    partsBean.setHeadUrl(dataBean.getHeadUrl());
                    partsBean.setAdd(false);
                    partsBean.setDelete(true);
                    mMap.put(dataBean.getFriendId(),partsBean);
                }

                for (Map.Entry<String, ProgramDetailBean.DataBean.participantBean> mm:mMap.entrySet()){
                    participantBeanList.add(mm.getValue());
                }
                peoPlesAdapter.notifyDataSetChanged();
            }
        }
    }
}
