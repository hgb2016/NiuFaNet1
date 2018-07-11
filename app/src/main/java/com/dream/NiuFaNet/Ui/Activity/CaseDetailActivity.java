package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.CaseDetailBean;
import com.dream.NiuFaNet.Bean.CaseListBean;
import com.dream.NiuFaNet.Bean.ClientBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.CommonBean1;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CaseContract;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.CustomView.URLImageGetter;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CasePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CheckForAllUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.MapUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

import static com.dream.NiuFaNet.Other.Const.company;
import static com.dream.NiuFaNet.Other.Const.content;

/**
 * 案件竞标情况
 */
public class CaseDetailActivity extends CommonActivity implements CaseContract.View{
    @Bind(R.id.bitperson_lv)
    MyListView bitperson_lv;
    @Bind(R.id.casename_tv)
    TextView casename_tv;
    @Bind(R.id.price_tv)
    TextView price_tv;
    @Bind(R.id.address_tv)
    TextView address_tv;
    @Bind(R.id.status_tv)
    TextView status_tv;
    @Bind(R.id.caseremark_tv)
    TextView caseremark_tv;
    @Bind(R.id.userinfo_lay)
    LinearLayout userinfo_lay;
    @Bind(R.id.usercount_tv)
    TextView usercount_tv;
    @Bind(R.id.submit_btn)
    Button submit_btn;
    private BitPersonAdapter bitPersonAdapter;
    private CustomPopWindow mCustomPopWindow;
    private URLImageGetter mUrlImageGetter;
    private List<CaseDetailBean.DataBean.UserBean> userlist=new ArrayList<>();
    @Inject
    CasePresenter casePresenter;
    private String caseId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_casedetail;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        casePresenter.attachView(this);
        bitPersonAdapter = new BitPersonAdapter(mContext, userlist, R.layout.view_bitperson);
        bitperson_lv.setAdapter(bitPersonAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    mLoadingDialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void initDatas() {
        caseId=getIntent().getStringExtra("caseId");
        if (caseId!=null){
            casePresenter.selectCaseInfo(CommonAction.getUserId(),caseId);
        }
    }
    //点击事件：1.返回，2.提交
    @OnClick({R.id.back_relay, R.id.submit_btn})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.submit_btn:
                casePresenter.validateCaseOrder(CommonAction.getUserId(),caseId);

                break;
        }
    }

    @Override
    public void eventListener() {

    }

    @Override
    public void showCaseList(CaseListBean dataBean) {

    }
    //案件详情
    @Override
    public void showCaseDetail(CaseDetailBean dataBean) {
        Log.i("tag",new Gson().toJson(dataBean.getData().getUserList()));
        if (dataBean.getError().equals(Const.success)){
            casename_tv.setText(dataBean.getData().getCaseName());
            price_tv.setText("¥"+dataBean.getData().getPrice());
            address_tv.setText(dataBean.getData().getProvince()+"-"+dataBean.getData().getCity());
            String isBid = dataBean.getData().getIsBid();
            String caseRemark = dataBean.getData().getCaseRemark();
            if (isBid.equals("1")){
                status_tv.setText("招募中");
            }else if (isBid.equals("2")){
                status_tv.setText("已竞标");
            }
            if (Build.VERSION.SDK_INT >= 24)
                caseremark_tv.setText(Html.fromHtml(caseRemark,Html.FROM_HTML_OPTION_USE_CSS_COLORS));
            else
                caseremark_tv.setText(Html.fromHtml(caseRemark));

            List<CaseDetailBean.DataBean.UserBean> list = dataBean.getData().getUserList();
            if (list!=null&&list.size()>0){
                userinfo_lay.setVisibility(View.VISIBLE);
                usercount_tv.setText("已投标 ("+list.size()+")");
                userlist.clear();
                userlist.addAll(list);
                bitPersonAdapter.notifyDataSetChanged();
            }else {
                userinfo_lay.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showAddCaseResult(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)){
            if (mCustomPopWindow!=null) {
                mCustomPopWindow.dissmiss();
            }
            ToastUtils.showToast(mContext,dataBean.getMessage(),R.mipmap.checkmark);
        }else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }
    //验证是否重复竞标结果
    @Override
    public void showValidateCaseResult(CommonBean1 dataBean) {
        if (dataBean.getError().equals(Const.success)){
            if (dataBean.getStatus().equals("1")){
                showcontact();
            }else {
                ToastUtils.Toast_short(dataBean.getMessage());
            }
        }
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(ResourcesUtils.getString(R.string.failconnect));
    }

    @Override
    public void complete() {

    }

    //投标人列表
    public class BitPersonAdapter extends CommonAdapter<CaseDetailBean.DataBean.UserBean> {

        public BitPersonAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, CaseDetailBean.DataBean.UserBean item, int position) {
            RelativeLayout root_relay = helper.getView(R.id.root_relay);
            if (position % 2 == 0) {
                root_relay.setBackgroundColor(Color.parseColor("#FFEFF6FE"));
            } else {
                root_relay.setBackgroundColor(Color.parseColor("#91DAEDFF"));
            }
            helper.setText(R.id.username_tv,item.getUserName());
            helper.setText(R.id.phone_tv,item.getUserPhone());
            helper.setImageByUrl(R.id.head_icon,item.getHeadUrl(),true);
        }
    }

    //填写竞标人信息·弹框
    private void showcontact() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_popbid, null);
        final EditText name_edt = (EditText) contentView.findViewById(R.id.name_edt);
        final EditText phone_edt = (EditText) contentView.findViewById(R.id.phone_edt);
        final EditText law_edt = (EditText) contentView.findViewById(R.id.law_edt);
        final EditText beizu_edt = (EditText) contentView.findViewById(R.id.beizu_edt);
        //处理popWindow 显示内容和点击事件
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    // mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.sure_btn:
                        //  mCustomPopWindow.dissmiss();
                        String name = name_edt.getText().toString().trim();
                        String phone = phone_edt.getText().toString().trim();
                        String law = law_edt.getText().toString().trim();
                        String remark=beizu_edt.getText().toString().trim();
                        if (!TextUtils.isEmpty(name)) {
                        } else {
                            ToastUtils.Toast_short("姓名不能为空");
                            return;
                        }
                        if (!TextUtils.isEmpty(law)) {

                        } else {
                            ToastUtils.Toast_short("律师事务所不能为空");
                            return;
                        }

                        if (TextUtils.isEmpty(phone)) {
                            ToastUtils.Toast_short("电话不能为空");
                            return;
                        } else if (!CheckForAllUtils.isMobileNO(phone)) {
                            ToastUtils.Toast_short("请输入正确的电话格式");
                            return;
                        } else {

                        }
                        if (name.isEmpty() && phone.isEmpty() && law.isEmpty()) {
                            ToastUtils.Toast_short("请填写竞标者信息");
                            return;
                        } else {
                            Map<String, String> parmas = MapUtils.getMap();
                            parmas.put("caseId",caseId);
                            parmas.put("userPhone",phone);
                            parmas.put("userId",CommonAction.getUserId());
                            parmas.put("userName",name);
                            parmas.put("companName",law);
                            parmas.put("remark",remark);
                            casePresenter.addCaseOrder(parmas);
                        }
                        break;
                    case R.id.cancel_btn:
                        mCustomPopWindow.dissmiss();
                        break;

                }
                //Toast.makeText(HomeActivity.this,showContent,Toast.LENGTH_SHORT).show();
            }
        };
        contentView.findViewById(R.id.sure_btn).setOnClickListener(listener);
        contentView.findViewById(R.id.cancel_btn).setOnClickListener(listener);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .setAnimationStyle(R.anim.actionsheet_dialog_in)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(submit_btn, Gravity.CENTER, 0, 0);
    }
}
