package com.dream.NiuFaNet.Ui.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.VersionBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.VersionUpdateContract;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.VersionUpdatePresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.GlideCircleTransform;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MineActivity1 extends CommonActivity implements VersionUpdateContract.View{
    @Bind(R.id.head_icon)
    ImageView head_icon;
    @Bind(R.id.username_tv)
    TextView mUsernameTv;
    //暂时展示电话号
    @Bind(R.id.desc_tv)
    TextView phone_tv;
    @Bind(R.id.nologin_lay)
    LinearLayout nologin_lay;
    @Bind(R.id.login_tv)
    TextView login_tv;
    @Bind(R.id.register_tv)
    TextView register_tv;
    @Bind(R.id.my_lv)
    MyListView my_lv;
    private List<ListBean> listBeans=new ArrayList<>();
    @Inject
    VersionUpdatePresenter versionUpdatePresenter;
    private Dialog loginDialog;
    private MyAdapter myAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_my;
    }

    @Override
    public void initView() {
        DaggerNFComponent.builder()
                .appComponent(MyApplication.getInstance().getAppComponent())
//                .mainActivityModule(new MainActivityMcodule(this))
                .build()
                .inject(this);
        versionUpdatePresenter.attachView(this);
        loginDialog = DialogUtils.getLoginTip(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLogin();
    }

    @Override
    public void initDatas() {
        versionUpdatePresenter.requestVersion("1","");
        //listBeans.add(new ListBean("消息",R.mipmap.icon_message,false));
        listBeans.add(new ListBean("我的客户",R.mipmap.icon_client,false));
        listBeans.add(new ListBean("谁可以看我的工作",R.mipmap.see,false));
        listBeans.add(new ListBean("导出我的日程",R.mipmap.export_1,true));
        listBeans.add(new ListBean("分享",R.mipmap.share,false));
        listBeans.add(new ListBean("意见反馈",R.mipmap.opinion,false));
        listBeans.add(new ListBean("关于我们",R.mipmap.about,false));
        listBeans.add(new ListBean("联系我们",R.mipmap.phone,false));
        listBeans.add(new ListBean("版本介绍",R.mipmap.version_introduction,false));
        myAdapter=new MyAdapter(mContext,listBeans,R.layout.view_mylist);
        my_lv.setAdapter(myAdapter);
        refreshLogin();
    }
    private void refreshLogin() {
        boolean isLogin = (boolean) SpUtils.getParam(Const.isLogin, false);
        if (isLogin) {
            toLogin();
        } else {
            noLogin();
        }
    }
    private void toLogin() {
        nologin_lay.setVisibility(View.GONE);
        mUsernameTv.setVisibility(View.VISIBLE);
        phone_tv.setVisibility(View.VISIBLE);
        String thirdType = (String) SpUtils.getParam(Const.thirdType, Const.bdUser);
        String itUserIcon = (String) SpUtils.getParam(Const.userHeadUrl, "");
        String phone = (String) SpUtils.getParam(Const.userPhone, "");
        String userName = (String) SpUtils.getParam(Const.userName, "");
        loginSet(itUserIcon, phone, userName);
       /* if (thirdType.equals("bduser")) {

        } else {
            String tdUserIcon = (String) SpUtils.getParam(Const.userHeadUrl, "");
            String tdUserName = (String) SpUtils.getParam(Const.userName, "");
            loginSet(tdUserIcon, "", tdUserName);
        }*/

    }
    private void noLogin() {
        head_icon.setImageResource(R.mipmap.niu);
        nologin_lay.setVisibility(View.VISIBLE);
        mUsernameTv.setVisibility(View.GONE);
        phone_tv.setVisibility(View.GONE);
        phone_tv.setText("");
        mUsernameTv.setText("");
        SpUtils.savaUserInfo(Const.isLogin, false);
    }
    private void loginSet(String headUrl, String phone, String userName) {
        if (phone.isEmpty()) {
            phone_tv.setVisibility(View.GONE);
        } else {
            phone_tv.setVisibility(View.VISIBLE);
            phone_tv.setText(phone);
        }
        phone_tv.setText(phone);
        if (userName.isEmpty()) {
            mUsernameTv.setVisibility(View.GONE);
        } else {
            mUsernameTv.setVisibility(View.VISIBLE);
            mUsernameTv.setText(userName);
        }

        if (!headUrl.isEmpty()) {
            Glide.with(this).load(headUrl).transform(new GlideCircleTransform(mActivity)).into(head_icon);
        }
        SpUtils.savaUserInfo(Const.isLogin, true);
    }
    @Override
    public void eventListener() {

    }
    @OnClick({R.id.back_relay,R.id.setting_relay,R.id.myinfo_relay,R.id.register_tv,R.id.login_tv})
    public void OnClick(View v){
        switch (v.getId()){
            //我的个人信息
            case R.id.myinfo_relay:
                if (CommonAction.getIsLogin()) {
                    IntentUtils.toActivity_result(UserInfoActivity1.class,mActivity,28);
                } else {
                    IntentUtils.toActivity_result(LoginActivity1.class,mActivity,21);
                }
                //startActivity(new Intent(getApplicationContext(),UserInfoActivity1.class));
                break;
            //返回
            case R.id.back_relay:
                finish();
                break;
            //设置
            case R.id.setting_relay:
                startActivityForResult(new Intent(mActivity, SettingActivity.class), 103);
                break;
            //去登陆
            case R.id.login_tv:
                startActivity(new Intent(getApplicationContext(),LoginActivity1.class));
                break;
            //去注册
            case R.id.register_tv:
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
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
    public void showData(VersionBean dataBean) {
        if (dataBean.getError().equals(Const.success)) {
            String url = dataBean.getUrl();
            String version = dataBean.getVersion();
            if (url != null && !url.isEmpty() && version != null && !version.isEmpty()) {//对url和version进行判断
                PackageInfo packageInfo = null;
                try {
                    packageInfo = MineActivity1.this.getPackageManager().getPackageInfo(MineActivity1.this.getPackageName(), 0);
                    final String localVersionName = packageInfo.versionName;
                    Log.e("tag", "localVersionName=" + localVersionName);
                    int localVersionCode = packageInfo.versionCode;
                    String[] currentNames = localVersionName.split("\\.");
                    String[] webNames = version.split("\\.");
                    if (Integer.parseInt(webNames[0]) > Integer.parseInt(currentNames[0])) {//有更高版本的apk
                        listBeans.get(7).setNew(true);
                        myAdapter.notifyDataSetChanged();
                    } else if (Integer.parseInt(webNames[1]) > Integer.parseInt(currentNames[1])) {//有更高版本的apk
                        listBeans.get(7).setNew(true);
                        myAdapter.notifyDataSetChanged();
                    } else if (Integer.parseInt(webNames[2]) > Integer.parseInt(currentNames[2])) {//有更高版本的apk
                        listBeans.get(7).setNew(true);
                        myAdapter.notifyDataSetChanged();
                    } else {

                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 103) {
            noLogin();
        }
    }
    public class MyAdapter extends CommonAdapter<ListBean>{


        public MyAdapter(Context context, List mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, ListBean item, final int position) {
            helper.setText(R.id.name,item.getName());
            helper.setImageResource(R.id.iv,item.getIcon());
            ImageView dot=helper.getView(R.id.dot);
            if (item.isNew){
                dot.setVisibility(View.VISIBLE);
            }else {
                dot.setVisibility(View.GONE);
            }
            helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    switch (position){
                        /*//系统消息
                        case 0:
                            startActivity(new Intent(mContext,MessageActivity.class));
                            break;*/
                        //我的客户
                        case 0:
                            if (CommonAction.getIsLogin()){
                                IntentUtils.toActivityWithTag(ClientsActivity.class,mActivity,"client");
                            }else {
                                loginDialog.show();
                            }
                            break;
                        //谁可以看我的工作
                        case 1:
                            if (CommonAction.getIsLogin()){
                                IntentUtils.toActivity(WorkVisibleFrendsActivity.class, mActivity);
                            }else {
                                loginDialog.show();
                            }
                            break;
                        //导出我的日程
                        case 2:
                            if (CommonAction.getIsLogin()){
                                Intent intent=new Intent();
                                intent.putExtra("type","2");
                                intent.setClass(mContext,DownScheduleActivity.class);
                                startActivity(intent);
                            }else {
                                loginDialog.show();
                            }
                            break;
                        //分享
                        case 3:
                            DialogUtils.showShareDialog(mActivity);
                            break;
                        //意见反馈
                        case 4:
                            startActivity(new Intent(getApplicationContext(),FeedBackActivity.class));
                            break;
                        //关于我们
                        case 5:
                            startActivity(new Intent(getApplicationContext(),AboutusActivity.class));
                            break;
                        //联系我们
                        case 6:
                            startActivity(new Intent(mActivity,ContactusActivity.class));
                            break;
                        //版本介绍
                        case 7:
                            startActivity(new Intent(mActivity,VersionDescActivity.class));
                            break;
                    }
                }
            });
        }
    }
    public class ListBean{
        public ListBean(String name, int icon,boolean isNew) {
            this.name = name;
            this.icon = icon;
            this.isNew=isNew;
        }

        private String name;
        private int icon;
        private boolean isNew;

        public boolean isNew() {
            return isNew;
        }

        public void setNew(boolean aNew) {
            isNew = aNew;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }
}
