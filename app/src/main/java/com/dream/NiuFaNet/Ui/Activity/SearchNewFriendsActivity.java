package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.os.AsyncTask;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.Contact;
import com.dream.NiuFaNet.Bean.SearchUserBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.SearchUserContract;
import com.dream.NiuFaNet.Contract.SimFrendsContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.SearchUserPresenter;
import com.dream.NiuFaNet.Presenter.SimFrendsPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.FrendsUtils;
import com.dream.NiuFaNet.Utils.LocalGroupSearch;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hou on 2018/4/18.
 */

public class SearchNewFriendsActivity extends CommonActivity implements SearchUserContract.View, SimFrendsContract.View {
    @Bind(R.id.search_edt)
    EditText search_edt;
    @Bind(R.id.users_rv)
    RecyclerView users_rv;
    @Bind(R.id.search_relay)
    RelativeLayout search_relay;
    @Bind(R.id.empty_lay)
    LinearLayout empty_lay;
    @Inject
    SearchUserPresenter searchUserPresenter;
    private UsersAdapter usersAdapter;
    private List<SearchUserBean.DataBean> dataList = new ArrayList<>();
    private List<Contact> contacts = new ArrayList<>();
    private List<Contact> contacts1 = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private String tag;
    @Inject
    SimFrendsPresenter mSimFrendsPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_searchfriends;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        searchUserPresenter.attachView(this);
        mSimFrendsPresenter.attachView(this);
        tag = getIntent().getStringExtra(Const.intentTag);
        RvUtils.setOptionnoLine(users_rv);
        // 获取编辑框焦点
        search_edt.setFocusable(true);
        search_edt.setFocusableInTouchMode(true);
        search_edt.requestFocus();
        search_edt.setHint("请输入手机号");
        search_edt.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        //打开软键盘
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        if (tag != null) {
            if (tag.equals("simfriend")) {
                try {
                    new DataTask().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                search_edt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        contacts.clear();
                        if (s != null && s.length() > 0) {
                            contacts = LocalGroupSearch.searchGroup(s, (ArrayList<Contact>) contacts1);
                            if (contacts.size() > 0 && contacts != null) {
                            } else {
                                //ToastUtils.Toast_short("没有搜索到联系人");
                            }
                        } else {

                        }
                        contactAdapter = new ContactAdapter(mContext, contacts, R.layout.rvitem_search_users);
                        users_rv.setAdapter(contactAdapter);
                        contactAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        } else {
            usersAdapter = new UsersAdapter(this, dataList, R.layout.rvitem_search_users);
            users_rv.setAdapter(usersAdapter);
        }
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }

    @OnClick({R.id.back_relay, R.id.search_relay,R.id.clear_iv,R.id.invite_tv})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.search_relay:
                if (tag!=null) {
                    if (tag.equals("simfriend")) {
                        contacts.clear();
                        String s = search_edt.getText().toString().trim();
                        if (s != null && s.length() > 0) {
                            contacts = LocalGroupSearch.searchGroup(s, (ArrayList<Contact>) contacts1);
                            if (contacts.size() > 0 && contacts != null) {
                            } else {
                                ToastUtils.Toast_short("没有搜索到联系人");
                            }
                        } else {

                        }
                        contactAdapter = new ContactAdapter(mContext, contacts, R.layout.rvitem_search_users);
                        users_rv.setAdapter(contactAdapter);
                        contactAdapter.notifyDataSetChanged();
                    }
                } else {
                    String keyWord = search_edt.getText().toString();
                    if (keyWord.isEmpty()) {
                        ToastUtils.Toast_short(getString(R.string.pleaseinputkeyword));
                    } else if (keyWord.matches("^[1][34578]\\d{9}")){
                        searchUserPresenter.searchUser(keyWord);
                    }else {
                        ToastUtils.Toast_short("请输入正确的手机号");
                    }
                }
                break;
            case R.id.clear_iv:
                search_edt.setText("");
                break;
            case R.id.invite_tv:
                mSimFrendsPresenter.sendMsg(search_edt.getText().toString().trim(),CommonAction.getUserId(),CommonAction.getUserName());
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
    public void showData(SearchUserBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            List<SearchUserBean.DataBean> data = dataBean.getData();
            if (data != null) {
                dataList.clear();
                if (data.size() > 0) {
                    empty_lay.setVisibility(View.GONE);
                    users_rv.setVisibility(View.VISIBLE);
                    dataList.addAll(data);
                }else {
                    users_rv.setVisibility(View.GONE);
                    empty_lay.setVisibility(View.VISIBLE);
                }
                usersAdapter.notifyDataSetChanged();
            }else {
                ToastUtils.Toast_short(dataBean.getMessage());
            }
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showAddFrendData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            ToastUtils.Toast_short("添加好友请求已发送");
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    @Override
    public void showData(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            ToastUtils.Toast_short("推荐成功");
        } else {
            ToastUtils.Toast_short(dataBean.getMessage());
        }
    }

    private class UsersAdapter extends RVBaseAdapter<SearchUserBean.DataBean> {

        public UsersAdapter(Context context, List<SearchUserBean.DataBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, final SearchUserBean.DataBean dataBean, int position) {
            holder.setText(R.id.username_tv, dataBean.getUserName());
            holder.setText(R.id.phone_tv, dataBean.getMobilePhone());
            holder.setOnClickListener(R.id.addfrends_tv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (dataBean.getUserId() != null) {
                        if (dataBean.getUserId().equals(CommonAction.getUserId())) {
                            ToastUtils.Toast_short("自己不可以添加自己哦~");
                        } else {
                            if (CommonAction.getIsLogin()) {
                                searchUserPresenter.addRends(CommonAction.getUserId(), dataBean.getUserId());
                                finish();
                            } else {
                                DialogUtils.getLoginTip(mContext).show();
                            }
                        }
                    }
                }
            });
        }
    }

    private class ContactAdapter extends RVBaseAdapter<Contact> {

        public ContactAdapter(Context context, List<Contact> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder helper, final Contact item, int position) {
            helper.setText(R.id.username_tv, item.getName());
            helper.setText(R.id.phone_tv, item.getPhoneNumber());
            TextView addfrends_tv = helper.getView(R.id.addfrends_tv);
            if (item.isSend()) {
                addfrends_tv.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
                addfrends_tv.setTextColor(ResourcesUtils.getColor(R.color.textcolor_blue2));
                addfrends_tv.setText("已推荐");
            } else {
                addfrends_tv.setText("推荐给TA");
                addfrends_tv.setBackgroundResource(R.drawable.shape_blue);
                addfrends_tv.setTextColor(ResourcesUtils.getColor(R.color.textcolor_blue2));
            }

            addfrends_tv.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    mLoadingDialog.show();
                    mSimFrendsPresenter.sendMsg(item.getPhoneNumber(), CommonAction.getUserId(), CommonAction.getUserName());
                    item.setSend(true);
                }
            });
        }
    }

    private class DataTask extends AsyncTask<String, String, List<Contact>> {
        @Override
        protected List<Contact> doInBackground(String... strings) {
            List<Contact> contacts2 = new ArrayList<>();
            try {
                contacts2 = FrendsUtils.loadContacts(mActivity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return contacts2;
        }

        @Override
        protected void onPostExecute(List<Contact> contact) {
            super.onPostExecute(contact);
            if (contact.size() > 0) {
                contacts1.clear();
                contacts1.addAll(contact);
            }
        }
    }
}
