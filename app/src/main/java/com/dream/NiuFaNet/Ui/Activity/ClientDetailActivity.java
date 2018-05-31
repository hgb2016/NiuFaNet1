package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
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
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.SearchClientDescContract;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.SearchClientDescPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.CheckForAllUtils;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.PopUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hou on 2018/4/17.
 */

public class ClientDetailActivity extends CommonActivity implements SearchClientDescContract.View{
    @Bind(R.id.contact_lv)
    MyListView contact_lv;
    @Bind(R.id.particepant_gv)
    MyGridView particepant_gv;
    @Bind(R.id.clientname_tv)
    TextView clientname_tv;
    @Bind(R.id.web_tv)
    TextView web_tv;
    @Bind(R.id.address_tv)
    TextView address_tv;
    @Bind(R.id.phone_tv)
    TextView phone_tv;
    @Bind(R.id.fax_tv)
    TextView fax_tv;
    @Bind(R.id.clientremark_tv)
    TextView clientremark_tv;
    @Bind(R.id.web_relay)
    RelativeLayout web_relay;
    @Bind(R.id.address_relay)
    RelativeLayout address_relay;
    @Bind(R.id.phone_relay)
    RelativeLayout phone_relay;
    @Bind(R.id.fax_relay)
    RelativeLayout fax_relay;
    @Bind(R.id.clientremark_lay)
    LinearLayout clientremark_lay;
    @Bind(R.id.createdtime_tv)
    TextView createdtime_tv;
    private ContactAdapter contactAdapter;
    private ClientDescBean.DataBean tempData;
    @Inject
    SearchClientDescPresenter searchClientDescPresenter;
    private ArrayList<ClientDescBean.DataBean.ContactBean> contactList = new ArrayList<>();
    private ArrayList<ClientDescBean.DataBean.UserBean> userList = new ArrayList<>();
    private PeoPleAdapter peoPleAdapter;
    private CustomPopWindow mCustomPopWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_clientdetail;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        searchClientDescPresenter.attachView(this);
        contactAdapter = new ContactAdapter(mContext, contactList, R.layout.view_clientcontact1);
        peoPleAdapter = new PeoPleAdapter(mContext, userList, R.layout.gvitem_timg_btext);
        contact_lv.setAdapter(contactAdapter);
        particepant_gv.setAdapter(peoPleAdapter);

    }


    @Override
    public void initDatas() {
        String clientId = getIntent().getStringExtra("clientId");
        if (clientId != null)
            mLoadingDialog.show();
        searchClientDescPresenter.searchClientDesc(CommonAction.getUserId(), clientId);
    }

    @OnClick({R.id.back_relay, R.id.more_relay, R.id.addcontact_iv, R.id.addvisible_iv, R.id.clientremark_lay,R.id.phone_relay})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.more_relay:
                showMorePop(view);
                break;
            case R.id.addcontact_iv:
                ClientBean.ContactBean contactBean = new ClientBean.ContactBean();
                showcontact(view, contactBean, "new");
                break;
            case R.id.addvisible_iv:
                Bundle bundle = new Bundle();
                if (tempData != null) {
                    bundle.putSerializable("data", tempData);
                    Intent intent = new Intent(mActivity, WhoCanSeeClientActivity.class);
                    intent.putExtra("clientId", tempData.getClientId());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 222);
                }
                break;
            case R.id.clientremark_lay:
                String content = clientremark_tv.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    PopUtils.showDetailPop(view, content, mContext);
                }
                break;
            case R.id.phone_relay:
                diallPhone(tempData.getClientPhone());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void eventListener() {
        //谁可见的空白点击事件
        particepant_gv.setOnTouchBlankPositionListener(new MyGridView.OnTouchBlankPositionListener() {
            @Override
            public boolean onTouchBlankPosition() {
                Bundle bundle = new Bundle();
                if (tempData != null) {
                    bundle.putSerializable("data", tempData);
                    Intent intent = new Intent(mActivity, WhoCanSeeClientActivity.class);
                    intent.putExtra("clientId", tempData.getClientId());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 222);
                }
                return true;
            }
        });
    }

    @Override
    public void showData(ClientDescBean dataBean) {
        mLoadingDialog.dismiss();
        if (dataBean.getError().equals(Const.success)) {
            String isShow = dataBean.getIsShow();
            Log.i("newclient",dataBean.getIsShow()+"ishow");
            if (isShow.equals("Y")) {
                ClientDescBean.DataBean data = dataBean.getData();
                if (data != null) {
                    tempData = data;
                    clientname_tv.setText(data.getClientName());
                    web_tv.setText(data.getClientWebsite());
                    address_tv.setText(data.getClientAddress());
                    phone_tv.setText(data.getClientPhone());
                    fax_tv.setText(data.getClientFax());
                    clientremark_tv.setText(data.getClientRemark());
                    createdtime_tv.setText("该客户" + data.getUserName() + "在" + data.getCreateTime() + "创建");
                    ArrayList<ClientDescBean.DataBean.ContactBean> contactData = data.getContactList();
                    ArrayList<ClientDescBean.DataBean.UserBean> userData = data.getUserList();
                    contactList.clear();
                    userList.clear();
                    //把创建人加进谁可见队列
                    ClientDescBean.DataBean.UserBean userBean =new ClientDescBean.DataBean.UserBean();
                    userBean.setUserId(tempData.getUserId());
                    userBean.setUserName(tempData.getUserName());
                    userBean.setHeadUrl(tempData.getHeadUrl());
                    userList.add(userBean);
                    contactList.addAll(contactData);
                    userList.addAll(userData);
                    peoPleAdapter.notifyDataSetChanged();
                    contactAdapter.notifyDataSetChanged();
                    if (data.getClientWebsite().isEmpty()) {
                        web_relay.setVisibility(View.GONE);
                    } else {
                        web_relay.setVisibility(View.VISIBLE);
                    }
                    if (data.getClientAddress().isEmpty()) {
                        address_relay.setVisibility(View.GONE);
                    } else {
                        address_relay.setVisibility(View.VISIBLE);
                    }
                    if (data.getClientPhone().isEmpty()) {
                        phone_relay.setVisibility(View.GONE);
                    } else {

                        phone_relay.setVisibility(View.VISIBLE);
                    }
                    if (data.getClientFax().isEmpty()) {
                        fax_relay.setVisibility(View.GONE);
                    } else {
                        fax_relay.setVisibility(View.VISIBLE);
                    }
                    if (data.getClientRemark().isEmpty()) {
                        clientremark_lay.setVisibility(View.GONE);
                    } else {
                        clientremark_lay.setVisibility(View.VISIBLE);
                    }
                }
            } else if (isShow.equals("N")) {
                ToastUtils.Toast_short("你没有查看此客户的权利");
                finish();
            }
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }

    }

    @Override
    public void showDeleteResult(CommonBean commonBean) {
        if (commonBean.getError().equals(Const.success)) {
            ToastUtils.Toast_short("删除成功");
            CommonAction.refreshClients();
            finish();
        } else {
            ToastUtils.Toast_short(commonBean.getMessage());
        }

    }

    @Override
    public void showAddContactResult(CommonBean commonBean) {
        if (commonBean.getError().equals(Const.success)) {
            String clientId = getIntent().getStringExtra("clientId");
            if (clientId != null)
                mLoadingDialog.show();
            searchClientDescPresenter.searchClientDesc(CommonAction.getUserId(), clientId);
        }
    }

    @Override
    public void showDeleMyClientContactResult(CommonBean commonBean) {
        mLoadingDialog.dismiss();
        if (commonBean.getError().equals(Const.success)) {
            ToastUtils.Toast_short("删除成功");
            String clientId = getIntent().getStringExtra("clientId");
            if (clientId != null)
                mLoadingDialog.show();
            searchClientDescPresenter.searchClientDesc(CommonAction.getUserId(), clientId);
        } else {
            ToastUtils.Toast_short(commonBean.getMessage());
        }
    }

    @Override
    public void showError() {
        ToastUtils.Toast_short(getResources().getString(R.string.failconnect));
        mLoadingDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            String clientId = getIntent().getStringExtra("clientId");
            if (clientId != null)
                mLoadingDialog.show();
            searchClientDescPresenter.searchClientDesc(CommonAction.getUserId(), clientId);
        }
        if (requestCode == 222 && resultCode == 102) {
            String clientId = getIntent().getStringExtra("clientId");
            if (clientId != null)
                mLoadingDialog.show();
            searchClientDescPresenter.searchClientDesc(CommonAction.getUserId(), clientId);
        }
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }


    //联系人
    public class ContactAdapter extends CommonAdapter<ClientDescBean.DataBean.ContactBean> {

        public ContactAdapter(Context context, List<ClientDescBean.DataBean.ContactBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final ClientDescBean.DataBean.ContactBean item, int position) {
            TextView name_duty_tv = helper.getView(R.id.name_duty_tv);
            TextView phone_tv = helper.getView(R.id.phone_tv);
            phone_tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            TextView email_tv = helper.getView(R.id.email_tv);
            phone_tv.setText(item.getMobilePhone());
            email_tv.setText(item.getEmail());
            if (!TextUtils.isEmpty(item.getDuty())) {
                name_duty_tv.setText(item.getContactsName() + "—" + item.getDuty());
            } else {
                name_duty_tv.setText(item.getContactsName());
            }
            helper.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    DialogUtils.showDeleteDialog(mActivity, new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View view) {
                            mLoadingDialog.show();
                            searchClientDescPresenter.deleMyClientContact(CommonAction.getUserId(), tempData.getClientId(), item.getId());
                        }
                    });

                    return true;
                }
            });
            helper.getView(R.id.edit_iv).setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    ClientBean.ContactBean contactBean = new ClientBean.ContactBean();
                    contactBean.setId(item.getId());
                    contactBean.setMobilePhone(item.getMobilePhone());
                    contactBean.setEmail(item.getEmail());
                    contactBean.setDuty(item.getDuty());
                    contactBean.setContactRemark(item.getContactRemark());
                    contactBean.setContactsName(item.getContactsName());
                    showcontact(view, contactBean, "edit");
                }
            });
            helper.setOnClickListener(R.id.phone_tv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    diallPhone(item.getMobilePhone());
                }
            });
        }

    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    //谁可见
    private class PeoPleAdapter extends CommonAdapter<ClientDescBean.DataBean.UserBean> {

        public PeoPleAdapter(Context context, List<ClientDescBean.DataBean.UserBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, final ClientDescBean.DataBean.UserBean item, final int position) {
            ImageView only_iv = helper.getView(R.id.only_iv);
            ImageView close_iv = helper.getView(R.id.close_iv);
            TextView only_tv = helper.getView(R.id.only_tv);
            only_tv.setMaxLines(1);
            only_tv.setEllipsize(TextUtils.TruncateAt.END);
            only_tv.setText(item.getUserName());
            close_iv.setVisibility(View.GONE);
            String headUrl = item.getHeadUrl();
            if(item.getUserId().equals(CommonAction.getUserId())){
                only_tv.setText("我");
                only_tv.setTextColor(getResources().getColor(R.color.blue_title));
            }else {
                only_tv.setTextColor(getResources().getColor(R.color.black));
            }
            if (headUrl != null && !headUrl.isEmpty()) {
                helper.setImageByUrl(R.id.only_iv, headUrl, true);
            } else {
                only_iv.setImageResource(R.mipmap.niu);
            }
            close_iv.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    userList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
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
        final String id = contactBean.getId();
        name_edt.setSelection(name_edt.getText().length());
        phone_edt.setSelection(phone_edt.getText().length());
        email_edt.setSelection(email_edt.getText().length());
        duty_edt.setSelection(duty_edt.getText().length());
        /*// 获取编辑框焦点
        name_edt.setFocusable(true);
        //打开软键盘
        final InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    // mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.save_relay:
                        boolean cansubmit = true;
                        //  mCustomPopWindow.dissmiss();
                        String name = name_edt.getText().toString().trim();
                        String phone = phone_edt.getText().toString().trim();
                        String email = email_edt.getText().toString().trim();
                        String duty = duty_edt.getText().toString().trim();
                        contactBean.setUserId(CommonAction.getUserId());
                        contactBean.setClientId(tempData.getClientId());
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
                                mCustomPopWindow.dissmiss();
                                searchClientDescPresenter.addContact(new Gson().toJson(contactBean));
                            } else if (tag.equals("edit")) {
                                contactBean.setId(id);
                                mCustomPopWindow.dissmiss();
                                searchClientDescPresenter.addContact(new Gson().toJson(contactBean));
                            }
                        }
                        break;

                    case R.id.cancel_relay:
                        mCustomPopWindow.dissmiss();
                        break;
                }
            }
        };
        contentView.findViewById(R.id.save_relay).setOnClickListener(listener);
        contentView.findViewById(R.id.cancel_relay).setOnClickListener(listener);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .setAnimationStyle(R.anim.activity_open)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    //更多弹框
    private void showMorePop(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_more1, null);
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.editclient_tv:
                        Bundle bundle = new Bundle();
                        if (tempData != null) {
                            bundle.putSerializable("data", tempData);
                            Intent intent = new Intent(mActivity, NewClientActivity.class);
                            intent.putExtra(Const.intentTag, "edit");
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 111);
                        }
                        break;
                    case R.id.deleteclient_tv:
                        DialogUtils.showDeleteDialog(mContext, new NoDoubleClickListener() {
                            @Override
                            public void onNoDoubleClick(View view) {
                                mLoadingDialog.show();
                                searchClientDescPresenter.deleteMyClient(CommonAction.getUserId(), tempData.getClientId());
                            }
                        });
                        break;
                }
            }
        };
        contentView.findViewById(R.id.editclient_tv).setOnClickListener(listener);
        contentView.findViewById(R.id.deleteclient_tv).setOnClickListener(listener);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .enableBackgroundDark(true)
                .setAnimationStyle(R.anim.actionsheet_dialog_in)
                .create()
                .showAsDropDown(v, 0, 0);
    }

}
