package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.ClientBean;
import com.dream.NiuFaNet.Bean.ClientDescBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.NewClientContract;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.NewClientPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CheckForAllUtils;
import com.dream.NiuFaNet.Utils.CustomHelper;
import com.dream.NiuFaNet.Utils.HiddenAnimUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 新建和编辑客户
 * Created by hou on 2018/4/16.
 */

public class NewClientActivity extends CommonActivity implements NewClientContract.View {
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
    @Bind(R.id.clientremark_edt)
    EditText clientremark_edt;
    @Bind(R.id.iv_more)
    ImageView iv_more;
    @Bind(R.id.contact_lv)
    MyListView contact_lv;
    @Bind(R.id.particepant_gv)
    MyGridView particepant_gv;
    @Bind(R.id.clientinfo_lay)
    LinearLayout clientinfo_lay;
    @Bind(R.id.more_lay)
    LinearLayout more_lay;
    private boolean isupordown = false;
    private ContactAdapter contactAdapter;
    private CustomPopWindow mCustomPopWindow;
    private PeoPleAdapter particepantAdapter;
    private ArrayList<ClientBean.ContactBean> contacts = new ArrayList<>();
    private InputMethodManager imm;
    private List<MyFrendBean.DataBean> selectdata = new ArrayList<>();

    @Inject
    NewClientPresenter newClientPresenter;
    private String clientid;

    @Override
    public int getLayoutId() {
        return R.layout.activity_newclient;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        newClientPresenter.attachView(this);
        contactAdapter = new ContactAdapter(mContext, contacts, R.layout.view_clientcontact);
        particepantAdapter = new PeoPleAdapter(mContext, selectdata, R.layout.gvitem_timg_btext);
        particepant_gv.setAdapter(particepantAdapter);
        contact_lv.setAdapter(contactAdapter);
        imm = ImmUtils.getImm(mActivity);
    }


    @OnClick({R.id.back_relay, R.id.more_lay, R.id.addcontact_iv, R.id.addvisible_iv, R.id.submit_relay})
    public void OnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back_relay:
                ImmUtils.hideImm(mActivity,imm);
                finish();
                break;
            case R.id.more_lay:
                ImmUtils.hideImm(mActivity,imm);
                HiddenAnimUtils.newInstance(mContext,clientinfo_lay,iv_more,250).toggle();
                break;
            case R.id.addcontact_iv:
                ImmUtils.hideImm(mActivity,imm);
                ClientBean.ContactBean contactBean = new ClientBean.ContactBean();
                showcontact(v, contactBean, "new");
                break;
            case R.id.addvisible_iv:
                ImmUtils.hideImm(mActivity,imm);
                intent.setClass(mContext, MyFrendsActivity.class);
                intent.putExtra(Const.intentTag, "newclient");
                Bundle bundle = new Bundle();
                bundle.putSerializable("peoplelist", (Serializable) selectdata);
                intent.putExtra("people", bundle);
                startActivityForResult(intent, 155);
                break;
            case R.id.submit_relay:
                ImmUtils.hideImm(mActivity,imm);
                String clientname = clientname_edt.getText().toString().trim();
                String web = web_edt.getText().toString().trim();
                String address = address_edt.getText().toString().trim();
                String phone = phone_edt.getText().toString().trim();
                String fax = fax_edt.getText().toString().trim();
                String clientremark = clientremark_edt.getText().toString().trim();
                if (TextUtils.isEmpty(clientname)) {
                    ToastUtils.Toast_short("客户名称不能为空！");
                } else {
                    ClientBean clientBean = new ClientBean();
                    clientBean.setClientId(clientid);
                    clientBean.setClientName(clientname);
                    clientBean.setClientWebsite(web);
                    clientBean.setClientAddress(address);
                    clientBean.setClientPhone(phone);
                    clientBean.setClientFax(fax);
                    clientBean.setClientRemark(clientremark);
                    clientBean.setContactData(contacts);
                    clientBean.setUserId(CommonAction.getUserId());
                    ArrayList<ClientBean.UserBean> userlist = new ArrayList<>();
                    for (int i = 0; i < selectdata.size(); i++) {
                        ClientBean.UserBean userBean = new ClientBean.UserBean();
                        userBean.setUserId(selectdata.get(i).getFriendId());
                        userlist.add(userBean);
                    }
                    clientBean.setUserData(userlist);
                    newClientPresenter.addMyClient(new Gson().toJson(clientBean));
                }
                break;
        }
    }

    /**
     * 初始化数据
     */
    @Override
    public void initDatas() {
        String intentTag = getIntent().getStringExtra(Const.intentTag);
        if (intentTag != null) {
            if (intentTag.equals("newclient")) {
                clientid="";
            }else if (intentTag.equals("edit")){
                ClientDescBean.DataBean oriData= (ClientDescBean.DataBean) getIntent().getSerializableExtra("data");
                oriData.getClientRemark();
                clientname_edt.setText(oriData.getClientName());
                web_edt.setText(oriData.getClientWebsite());
                phone_edt.setText(oriData.getClientPhone());
                clientremark_edt.setText(oriData.getClientRemark());
                fax_edt.setText(oriData.getClientFax());
                address_edt.setText(oriData.getClientAddress());
                clientid=oriData.getClientId();
                //联系人
                ArrayList<ClientDescBean.DataBean.ContactBean> contactList = oriData.getContactList();
                for (ClientDescBean.DataBean.ContactBean contactBean:contactList) {
                    ClientBean.ContactBean bean=new ClientBean.ContactBean();
                    bean.setContactsName(contactBean.getContactsName());
                    bean.setContactRemark(contactBean.getContactRemark());
                    bean.setDuty(contactBean.getDuty());
                    bean.setEmail(contactBean.getEmail());
                    bean.setMobilePhone(contactBean.getMobilePhone());
                    contacts.add(bean);
                }
                contactAdapter.notifyDataSetChanged();
                // 谁可见
                ArrayList<ClientDescBean.DataBean.UserBean> userdata = oriData.getUserList();
                for (ClientDescBean.DataBean.UserBean userBean:userdata) {
                    MyFrendBean.DataBean bean=new MyFrendBean.DataBean();
                    bean.setFriendId(userBean.getUserId());
                    bean.setShowName(userBean.getUserName());
                    bean.setFriendName(userBean.getUserName());
                    bean.setHeadUrl(userBean.getHeadUrl());
                    selectdata.add(bean);
                }
                particepantAdapter.notifyDataSetChanged();


            }
        }
        clientname_edt.setSelection(clientname_edt.getText().length());
        web_edt.setSelection(web_edt.getText().length());
        phone_edt.setSelection(phone_edt.getText().length());
        clientremark_edt.setSelection(clientremark_edt.getText().length());
        address_edt.setSelection(address_edt.getText().length());
        fax_edt.setSelection(fax_edt.getText().length());
    }

    @Override
    public void eventListener() {

    }

    //填写联系人弹框
    private void showcontact(View v, final ClientBean.ContactBean contactBean, final String tag) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_contactinfo, null);
        final EditText name_edt = (EditText) contentView.findViewById(R.id.name_edt);
        final EditText phone_edt = (EditText) contentView.findViewById(R.id.phone_edt);
        final EditText email_edt = (EditText) contentView.findViewById(R.id.email_edt);
        final EditText duty_edt = (EditText) contentView.findViewById(R.id.duty_edt);
        name_edt.setText(contactBean.getContactsName());
        phone_edt.setText(contactBean.getMobilePhone());
        email_edt.setText(contactBean.getEmail());
        duty_edt.setText(contactBean.getDuty());
        name_edt.setSelection(name_edt.getText().length());
        phone_edt.setSelection(phone_edt.getText().length());
        email_edt.setSelection(email_edt.getText().length());
        duty_edt.setSelection(duty_edt.getText().length());
        // 获取编辑框焦点
        name_edt.setFocusable(true);
        //打开软键盘
        final InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //处理popWindow 显示内容和点击事件
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    // mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.save_relay:
                        imm.hideSoftInputFromWindow(name_edt.getWindowToken(), 0);
                        boolean cansubmit = true;
                        //  mCustomPopWindow.dissmiss();
                        String name = name_edt.getText().toString().trim();
                        String phone = phone_edt.getText().toString().trim();
                        String email = email_edt.getText().toString().trim();
                        String duty = duty_edt.getText().toString().trim();
                        contactBean.setContactRemark("");
                        if (!TextUtils.isEmpty(name)) {
                            contactBean.setContactsName(name);
                        } else {
                            cansubmit = false;
                            ToastUtils.Toast_short("姓名不能为空");
                            contactBean.setContactsName("");
                        }

                        if (TextUtils.isEmpty(phone)) {
                            cansubmit = false;
                            ToastUtils.Toast_short("电话不能为空");
                            contactBean.setMobilePhone("");
                        } else if (!CheckForAllUtils.isMobileNO(phone)) {
                            cansubmit = false;
                            ToastUtils.Toast_short("请输入正确的电话格式");
                        } else {
                            contactBean.setMobilePhone(phone);
                        }

                        if (TextUtils.isEmpty(email)) {
                            contactBean.setEmail("");
                        } else if (!CheckForAllUtils.isEmailAdd(email)) {
                            cansubmit = false;
                            ToastUtils.Toast_short("请输入正确的邮箱格式");
                        } else {
                            contactBean.setEmail(email);
                        }

                        if (!TextUtils.isEmpty(duty)) {
                            contactBean.setDuty(duty);
                        } else {
                            contactBean.setDuty("");
                        }

                        if (name.isEmpty() && phone.isEmpty() && email.isEmpty() && duty.isEmpty()) {
                            ToastUtils.Toast_short("请填写联系人信息");
                        } else if (cansubmit) {
                            if (tag.equals("new")) {
                                ImmUtils.hideImm(mActivity, imm);
                                mCustomPopWindow.dissmiss();
                                contacts.add(contactBean);
                                contactAdapter.notifyDataSetChanged();
                            } else if (tag.equals("edit")) {
                                ImmUtils.hideImm(mActivity, imm);
                                mCustomPopWindow.dissmiss();
                                contactAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    case R.id.cancel_relay:
                        imm.hideSoftInputFromWindow(name_edt.getWindowToken(), 0);
                        mCustomPopWindow.dissmiss();
                        break;

                }
                //Toast.makeText(HomeActivity.this,showContent,Toast.LENGTH_SHORT).show();
            }
        };
        contentView.findViewById(R.id.save_relay).setOnClickListener(listener);
        contentView.findViewById(R.id.cancel_relay).setOnClickListener(listener);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .setAnimationStyle(R.anim.pickerview_slide_in_bottom)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 数据刷新
     * @param dataBean
     */
    @Override
    public void showData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            ToastUtils.Toast_short("创建成功");
            CommonAction.refreshClients();
            finish();
        }
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(getResources().getString(R.string.failconnect));
    }

    @Override
    public void complete() {

    }

    //谁可见
    private class PeoPleAdapter extends CommonAdapter<MyFrendBean.DataBean> {

        public PeoPleAdapter(Context context, List<MyFrendBean.DataBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final MyFrendBean.DataBean item, final int position) {
            ImageView only_iv = helper.getView(R.id.only_iv);
            ImageView close_iv = helper.getView(R.id.close_iv);
            TextView only_tv = helper.getView(R.id.only_tv);
            only_tv.setMaxLines(1);
            only_tv.setEllipsize(TextUtils.TruncateAt.END);
            only_tv.setText(item.getFriendName());
            close_iv.setVisibility(View.VISIBLE);
            String headUrl = item.getHeadUrl();
            if (headUrl != null && !headUrl.isEmpty()) {
                helper.setImageByUrl(R.id.only_iv, headUrl, true);
            } else {
                only_iv.setImageResource(R.mipmap.niu);
            }
            close_iv.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    selectdata.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 155 && resultCode == 102 && data != null) {
            List<MyFrendBean.DataBean> datas = (List<MyFrendBean.DataBean>) data.getExtras().getSerializable("selectdata");
            selectdata.addAll(datas);
            particepantAdapter.notifyDataSetChanged();
        }
    }

    //联系人列表
    public class ContactAdapter extends CommonAdapter<ClientBean.ContactBean> {


        public ContactAdapter(Context context, List<ClientBean.ContactBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);

        }

        @Override
        public void convert(BaseViewHolder helper, final ClientBean.ContactBean item, final int position) {
            TextView name_duty_tv = helper.getView(R.id.name_duty_tv);
            TextView phone_tv = helper.getView(R.id.phone_tv);

            TextView email_tv = helper.getView(R.id.email_tv);
            RelativeLayout email_relay = helper.getView(R.id.email_relay);
            RelativeLayout phone_relay = helper.getView(R.id.phone_relay);
            phone_tv.setText(item.getMobilePhone());
            email_tv.setText(item.getEmail());
            if (!item.getDuty().isEmpty()){
                name_duty_tv.setText(item.getContactsName()+"—"+item.getDuty());
            }else {
                name_duty_tv.setText(item.getContactsName());
            }
            helper.setOnClickListener(R.id.edit_iv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    showcontact(view, item, "edit");
                }
            });
            if (TextUtils.isEmpty(item.getEmail())) {
                email_relay.setVisibility(View.GONE);
            } else {
                email_relay.setVisibility(View.VISIBLE);
            }
            helper.setOnClickListener(R.id.delete_iv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    contacts.remove(position);
                    contactAdapter.notifyDataSetChanged();
                }
            });

        }
    }


}
