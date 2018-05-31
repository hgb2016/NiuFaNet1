package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.Contact;
import com.dream.NiuFaNet.Bean.MyFrendBean;
import com.dream.NiuFaNet.Bean.UserInfoBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.DeleteUserFriendsContract;
import com.dream.NiuFaNet.Contract.EditFriendRemarkContract;
import com.dream.NiuFaNet.Contract.GetUserInfoContract;
import com.dream.NiuFaNet.Contract.SearchFriendInfoContract;
import com.dream.NiuFaNet.Contract.SimFrendsContract;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.DeleteUserFridendsPresenter;
import com.dream.NiuFaNet.Presenter.EditFriendRemarkPresenter;
import com.dream.NiuFaNet.Presenter.GetUserInfoPresenter;
import com.dream.NiuFaNet.Presenter.SearchFriendInfoPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.GlideCircleTransform;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/3.
 */

public class FriendDetailActivity extends CommonActivity implements SearchFriendInfoContract.View, DeleteUserFriendsContract.View, EditFriendRemarkContract.View {
    @Bind(R.id.head_iv)
    ImageView head_iv;
    @Bind(R.id.username_tv)
    TextView username_tv;
    @Bind(R.id.duty_tv)
    TextView duty_tv;
    @Bind(R.id.phone_tv)
    TextView phone_tv;
    @Bind(R.id.email_tv)
    TextView email_tv;
    @Bind(R.id.hourate_tv)
    TextView hourate_tv;
    @Bind(R.id.note_tv)
    TextView note_tv;
    @Bind(R.id.company_tv)
    TextView company_tv;
    @Bind(R.id.location_tv)
    TextView address_tv;
    @Bind(R.id.location_relay)
    RelativeLayout address_realy;
    @Bind(R.id.company_relay)
    RelativeLayout company_relay;
    @Bind(R.id.duty_relay)
    RelativeLayout duty_relay;
    @Bind(R.id.phone_relay)
    RelativeLayout phone_relay;
    @Bind(R.id.email_relay)
    RelativeLayout email_relay;
    @Bind(R.id.hourate_relay)
    RelativeLayout hourate_relay;
    @Bind(R.id.more_iv)
    ImageView more_iv;
    @Bind(R.id.edit_iv)
    ImageView edit_iv;
    @Bind(R.id.note_lay)
    LinearLayout note_lay;
    @Bind(R.id.bg_relay)
    RelativeLayout bg_relay;
    @Bind(R.id.empty_lay)
    LinearLayout empty_lay;
    @Inject
    SearchFriendInfoPresenter searchFriendInfoPresenter;
    @Inject
    DeleteUserFridendsPresenter deleteUserFridendsPresenter;
    @Inject
    EditFriendRemarkPresenter editFriendRemarkPresenter;
    private CustomPopWindow mCustomPopWindow;
    private String friendid;
    private MyFrendBean.DataBean temData;
    private InputMethodManager imm;

    @Override
    public int getLayoutId() {
        return R.layout.activity_frienddetail;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
                .build()
                .inject(this);
        searchFriendInfoPresenter.attachView(this);
        deleteUserFridendsPresenter.attachView(this);
        editFriendRemarkPresenter.attachView(this);
        imm = ImmUtils.getImm(this);
        RandomBg();
    }

    //随机背景图
    private void RandomBg() {
        int i = (int) (Math.random() * 3);
        switch (i){
            case 0:
                bg_relay.setBackgroundResource(R.mipmap.bg1);
                break;
            case 1:
                bg_relay.setBackgroundResource(R.mipmap.bg2);
                break;
            case 2:
                bg_relay.setBackgroundResource(R.mipmap.bg3);
                break;
        }
    }

    @Override
    public void initDatas() {
        friendid = getIntent().getStringExtra("friendid");
        if (friendid != null) {
            mLoadingDialog.show();
            searchFriendInfoPresenter.SearchFriendInfoInfo(CommonAction.getUserId(), friendid);
        }
    }

    @Override
    public void eventListener() {

    }
    //点击事件
    @OnClick({R.id.username_tv,R.id.back_relay, R.id.note_lay, R.id.more_relay, R.id.friendcal_relay,R.id.phone_relay})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back_relay:
                finish();
                break;
            case R.id.note_lay:
                showinputnote(v);
                break;
            case R.id.more_relay:
                showMorePop(more_iv);
                break;
            case R.id.friendcal_relay:
                Intent intent = new Intent(mActivity, FriendCalenderActivity.class);
                Bundle bundle = new Bundle();
                if (temData != null) {
                    bundle.putSerializable("data", temData);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.phone_relay:
                String phone = phone_tv.getText().toString().trim();
                if (!phone.isEmpty()){
                    diallPhone(phone);
                }
                break;
            case R.id.username_tv:
                showinputnote(v);
                break;
        }
    }

    @Override
    public void showError() {
        mLoadingDialog.dismiss();
        ToastUtils.Toast_short("请求数据失败");
    }

    @Override
    public void complete() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showData(UserInfoBean dataBean) {
        mLoadingDialog.dismiss();
        if (dataBean.getError().equals(Const.success)) {
            UserInfoBean.UserBean userBean = dataBean.getBody();
            String headUrl = userBean.getHeadUrl();
            String userName = userBean.getUserName();
            String friendRemark = userBean.getFriendRemark();
            String company = userBean.getCompany();
            String duty = userBean.getDuty();
            String hourRate = userBean.getHourRate();
            String email = userBean.getEmail();
            String phone = userBean.getMobilePhone();
            String address = userBean.getAddress();
            if (company.isEmpty()&&duty.isEmpty()&&email.isEmpty()&&phone.isEmpty()&&address.isEmpty()&&hourRate.equals("0")){
                empty_lay.setVisibility(View.VISIBLE);
            }else {
                empty_lay.setVisibility(View.GONE);
            }
            temData = new MyFrendBean.DataBean();
            temData.setFriendId(friendid);
            temData.setFriendName(userName);
            temData.setFriendRemark(friendRemark);
            temData.setHeadUrl(headUrl);
            if (!headUrl.isEmpty()) {
                Glide.with(this).load(headUrl).transform(new GlideCircleTransform(mActivity)).into(head_iv);

            } else {
                head_iv.setImageResource(R.mipmap.niu);
            }
            if (!friendRemark.isEmpty()) {
                username_tv.setText(friendRemark);
                edit_iv.setVisibility(View.GONE);
                note_tv.setText(userName);
                note_lay.setClickable(false);
                username_tv.setClickable(true);
            } else {
                username_tv.setClickable(false);
                username_tv.setText(userName);
            }
            setUserInfo(company, company_tv, company_relay);
            setUserInfo(duty, duty_tv, duty_relay);
            setUserInfo(email, email_tv, email_relay);
            setUserInfo(phone, phone_tv, phone_relay);
            setUserInfo(address, address_tv, address_realy);
            if (hourRate.equals("0")) {
                hourate_relay.setVisibility(View.GONE);
            }else {
                hourate_relay.setVisibility(View.VISIBLE);
                hourate_tv.setText(hourRate+"元/小时");
            }

        }
    }

    public void setUserInfo(String userInfo, TextView tv, RelativeLayout relay) {
        if (!userInfo.isEmpty()) {
            tv.setText(userInfo);
            relay.setVisibility(View.VISIBLE);
        } else {

        }
    }


    //填写备注弹框
    private void showinputnote(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_inputenote, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.note_edit);
        editText.setText(username_tv.getText().toString().trim());
        editText.setSelection(editText.getText().length());
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel_tv:
                        mCustomPopWindow.dissmiss();
                        break;
                    case R.id.sure_tv:
                        String edit = editText.getText().toString().trim();
                        if (TextUtils.isEmpty(edit)) {
                            ToastUtils.Toast_short("备注不能为空");
                        }else if(edit.length()>10){
                            ToastUtils.Toast_short("备注不能超过10个字符");
                        }else {
                            editFriendRemarkPresenter.editFriendRemark(CommonAction.getUserId(), friendid, edit);
                        }
                        break;
                }
            }
        };

        contentView.findViewById(R.id.cancel_tv).setOnClickListener(listener);
        contentView.findViewById(R.id.sure_tv).setOnClickListener(listener);
        // 获取编辑框焦点
        editText.setFocusable(true);
        //打开软键盘
        InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .setAnimationStyle(R.style.ActionSheetDialogAnimation)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    //更多弹框
    private void showMorePop(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_more, null);
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.setnote_tv:
                        showinputnote(v);
                        break;
                    case R.id.deletefrident_tv:
                        DialogUtils.showDeleteDialog(mContext, new NoDoubleClickListener() {
                            @Override
                            public void onNoDoubleClick(View view) {
                                deleteUserFridendsPresenter.deleteUserFriends(CommonAction.getUserId(), friendid);
                            }
                        });

                        break;
                }
            }
        };
        contentView.findViewById(R.id.setnote_tv).setOnClickListener(listener);
        contentView.findViewById(R.id.deletefrident_tv).setOnClickListener(listener);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .setAnimationStyle(R.anim.actionsheet_dialog_in)
                .create()
                .showAsDropDown(v, 0, 0);
    }

    @Override
    public void showDeleteResult(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            ToastUtils.Toast_short("删除成功");
            if (mCustomPopWindow != null) {
                mCustomPopWindow.dissmiss();
            }
            finish();
            CommonAction.refreshContact();
        }
    }

    @Override
    public void showEditResult(CommonBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            ToastUtils.Toast_short("修改成功");
            if (mCustomPopWindow != null) {
                mCustomPopWindow.dissmiss();
            }
            searchFriendInfoPresenter.SearchFriendInfoInfo(CommonAction.getUserId(), friendid);
            CommonAction.refreshContact();
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
}
