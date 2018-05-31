package com.dream.NiuFaNet.Ui.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.dream.NiuFaNet.Utils.ImmUtils;
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
    @Bind(R.id.title_tv)
    EditText title_tv;
    @Bind(R.id.annum_tv)
    EditText annum_tv;
    @Bind(R.id.beizu_edt)
    EditText beizu_edt;
    @Bind(R.id.particepant_gv)
    GridView particepant_gv;
    @Bind(R.id.statu_relay)
    RelativeLayout statu_relay;
    @Bind(R.id.doing_iv)
    ImageView doing_iv;
    @Bind(R.id.end_iv)
    ImageView end_iv;
    @Bind(R.id.addpeople_iv)
    ImageView addpeople_iv;
    @Bind(R.id.chooseclient_relay)
    RelativeLayout chooseclient_relay;
    @Bind(R.id.chooseclient_tv)
    TextView chooseclient_tv;
    @Bind(R.id.talking_lay)
    LinearLayout talking_lay;
    @Bind(R.id.doing_lay)
    LinearLayout doing_lay;
    @Bind(R.id.end_lay)
    LinearLayout end_lay;
    @Bind(R.id.talking_iv)
    ImageView talking_iv;
    private PeoPleAdapter peoPlesAdapter;
    private List<ProgramDetailBean.DataBean.participantBean> participantBeanList = new ArrayList<>();
    private List<ProgramDetailBean.DataBean.participantBean> dataParcipant = new ArrayList<>();
    private Map<String, ProgramDetailBean.DataBean.participantBean> mMap = new HashMap<>();
    private Dialog loadingDialog;
    private String tempStatus = "0";
    @Inject
    NewProgramPresenter programPresenter;
    private InputMethodManager imm;
    private String clientId="0";

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
        imm = ImmUtils.getImm(mActivity);
    }

    @Override
    public void initDatas() {

        peoPlesAdapter.notifyDataSetChanged();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            origData = (ProgramDetailBean.DataBean) extras.getSerializable("data");
            if (origData != null) {
                initDataView();
            }
        }
        intentTag = getIntent().getStringExtra(Const.intentTag);

        String projectName = getIntent().getStringExtra("projectName");
        if (projectName != null) {
            title_tv.setText(projectName);
            title_tv.setSelection(title_tv.getText().length());
        }
    }

    private void initDataView() {
        title_tv.setText(origData.getName());
        title_tv.setSelection(title_tv.getText().length());
        annum_tv.setText(origData.getCaseNo());
        beizu_edt.setText(origData.getDescription());
        beizu_edt.setSelection(beizu_edt.getText().length());
        //初始化参与者
        List<ProgramDetailBean.DataBean.participantBean> participant = origData.getParticipant();
        if (participant != null) {
            participantBeanList.clear();
            dataParcipant.clear();
            participantBeanList.addAll(participant);
            dataParcipant.addAll(participant);
            peoPlesAdapter.notifyDataSetChanged();
        }
        if (!origData.getClientId().equals("0")){
            chooseclient_tv.setText(origData.getClientName());
            clientId=origData.getClientId();
        }else {
            clientId="0";
        }
        String status = origData.getStatus();
        if (status != null && !status.isEmpty()) {
            end_lay.setVisibility(View.VISIBLE);
            tempStatus = status;
            if (status.equals("0")) {
                doing_iv.setImageResource(R.mipmap.check_green);
                talking_iv.setImageResource(R.mipmap.icon_checkempty);
                end_iv.setImageResource(R.mipmap.icon_checkempty);
            } else if (status.equals("1")) {
                doing_iv.setImageResource(R.mipmap.icon_checkempty);
                talking_iv.setImageResource(R.mipmap.check_green);
                end_iv.setImageResource(R.mipmap.icon_checkempty);
            }else if (status.equals("2")){
                doing_iv.setImageResource(R.mipmap.icon_checkempty);
                talking_iv.setImageResource(R.mipmap.icon_checkempty);
                end_iv.setImageResource(R.mipmap.check_green);
            } else {
                statu_relay.setVisibility(View.GONE);
            }
        } else {
           statu_relay.setVisibility(View.VISIBLE);
           doing_lay.setVisibility(View.VISIBLE);
           talking_lay.setVisibility(View.VISIBLE);
           end_lay.setVisibility(View.GONE);
        }

        for (int i = 0; i < participantBeanList.size(); i++) {
            participantBeanList.get(i).setDelete(true);
            participantBeanList.get(i).setSource(true);
            mMap.put(participantBeanList.get(i).getUserId(), participantBeanList.get(i));
        }
    }

    private ProgramDetailBean.DataBean origData;
    private String intentTag;

    @Override
    public void eventListener() {

    }

    @Override
    public void finish() {
        super.finish();

    }

    @OnClick({R.id.talking_lay,R.id.close_iv, R.id.submit_iv, R.id.addpeople_iv, R.id.doing_lay, R.id.end_lay, R.id.chooseclient_relay, R.id.title_relay, R.id.beizu_lay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                ImmUtils.hideImm(mActivity, imm);
                finish();
                break;
            case R.id.submit_iv://创建项目
                ImmUtils.hideImm(mActivity, imm);
                String title = title_tv.getText().toString();
                String anNumber = annum_tv.getText().toString();
                String remark = beizu_edt.getText().toString();
                if (origData == null) {
                    origData = new ProgramDetailBean.DataBean();
                }
                origData.setName(title);
                origData.setCaseNo(anNumber);
                origData.setParticipant(participantBeanList);
                origData.setDescription(remark);
                origData.setUserId(CommonAction.getUserId());
                origData.setStatus(tempStatus);
                if (!clientId.equals("0")){
                    origData.setClientId(clientId);
                }else {
                    origData.setClientId("0");
                }
                if (title.isEmpty()) {
                    ToastUtils.Toast_short(mActivity,"请输入标题");
                } else {
                    String dataStr = new Gson().toJson(origData);
                    Log.e("pro", "newproData=" + dataStr);
                    loadingDialog.show();
                    if (intentTag != null) {
                        if (intentTag.equals("edit")) {
                            programPresenter.edtProject(dataStr);
                        } else {
                            programPresenter.newProgram(dataStr);
                        }
                    } else {
                        programPresenter.newProgram(dataStr);
                    }
                }
                break;
            case R.id.doing_lay:
                doing_iv.setImageResource(R.mipmap.check_green);
                end_iv.setImageResource(R.mipmap.icon_checkempty);
                talking_iv.setImageResource(R.mipmap.icon_checkempty);
                tempStatus = "0";
                break;
            case R.id.end_lay:
                doing_iv.setImageResource(R.mipmap.icon_checkempty);
                end_iv.setImageResource(R.mipmap.check_green);
                talking_iv.setImageResource(R.mipmap.icon_checkempty);
                tempStatus = "2";
                break;
            case R.id.talking_lay:
                doing_iv.setImageResource(R.mipmap.icon_checkempty);
                end_iv.setImageResource(R.mipmap.icon_checkempty);
                talking_iv.setImageResource(R.mipmap.check_green);
                tempStatus = "1";
                break;
            case R.id.addpeople_iv:
                ImmUtils.hideImm(mActivity, imm);
                //跳转到通讯录
                //IntentUtils.toActivityWithTag(MyFrendsActivity.class, mActivity, "newpro", 101);
                Intent intent1 = new Intent(mContext, MyFrendsActivity.class);
                intent1.putExtra(Const.intentTag, "newpro");
                Bundle bundle = new Bundle();
                bundle.putSerializable("peoplelist", (Serializable) participantBeanList);
                intent1.putExtra("people", bundle);
                startActivityForResult(intent1, 101);
                break;

            case R.id.chooseclient_relay://我的客户
                ImmUtils.hideImm(mActivity, imm);
                Intent intent=new Intent(mContext,ClientsActivity.class);
                intent.putExtra("clientname",chooseclient_tv.getText().toString().trim());
                startActivityForResult(intent,200);
                break;
            case R.id.title_relay:
                title_tv.requestFocus();
                title_tv.setFocusable(true);
                title_tv.setFocusableInTouchMode(true);
                ImmUtils.showImm(mActivity, title_tv, imm);
                break;
            case R.id.beizu_lay:
                beizu_edt.requestFocus();
                beizu_edt.setFocusable(true);
                beizu_edt.setFocusableInTouchMode(true);
                ImmUtils.showImm(mActivity, beizu_edt, imm);
                break;

        }

    }

    @Override
    public void showData(NewProResultBean dataBean) {
        Log.e("tag", "dataBean=" + dataBean.toString());
        ToastUtils.Toast_short(mActivity,dataBean.getMessage());
        if (dataBean.getError().equals(Const.success)) {
            CommonAction.refreshPro();
            String projectId = dataBean.getProjectId();
            if (projectId == null) {
                ToastUtils.Toast_short(mActivity,"数据异常");
                return;
            }
            if (intentTag != null && intentTag.equals("newCal")) {
                Intent intent = getIntent();
                intent.putExtra("projectId", projectId);
                intent.putExtra("title", title_tv.getText().toString());
                setResult(111, intent);
            }
            finish();
            ImmUtils.hideImm(mActivity,imm);
        }
    }

    @Override
    public void showEdtData(CommonBean dataBean) {
        ToastUtils.Toast_short(mActivity,dataBean.getMessage());
        if (dataBean.getError().equals(Const.success)) {
            finish();
            ImmUtils.hideImm(mActivity,imm);
        }
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(mActivity,ResourcesUtils.getString(R.string.failconnect));
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
            if (!item.isEmpty()) {
                if (headUrl != null && !headUrl.isEmpty()) {
                    helper.setImageByUrl(R.id.only_iv, headUrl, true);
                } else {
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
        if (requestCode == 101 && resultCode == 102 && data != null) {
            List<MyFrendBean.DataBean> selectdata = (List<MyFrendBean.DataBean>) data.getExtras().getSerializable("selectdata");
            if (selectdata != null && selectdata.size() > 0) {
                participantBeanList.clear();
                for (int i = 0; i < selectdata.size(); i++) {
                    MyFrendBean.DataBean dataBean = selectdata.get(i);
                    ProgramDetailBean.DataBean.participantBean partsBean = new ProgramDetailBean.DataBean.participantBean();
                    partsBean.setUserId(dataBean.getFriendId());
                    partsBean.setUserName(dataBean.getShowName());
                    partsBean.setHeadUrl(dataBean.getHeadUrl());
                    partsBean.setAdd(false);
                    partsBean.setDelete(true);
                    mMap.put(dataBean.getFriendId(), partsBean);
                }

                for (Map.Entry<String, ProgramDetailBean.DataBean.participantBean> mm : mMap.entrySet()) {
                    participantBeanList.add(mm.getValue());
                }
                peoPlesAdapter.notifyDataSetChanged();
            }
        }

        if (requestCode==200 &&resultCode==201&&data!=null){
            String clientName = data.getStringExtra("clientName");
            chooseclient_tv.setText(clientName);
            clientId=data.getStringExtra("clientId");

        }
    }

}
