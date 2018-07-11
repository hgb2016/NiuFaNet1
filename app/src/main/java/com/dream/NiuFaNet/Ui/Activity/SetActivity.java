package com.dream.NiuFaNet.Ui.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Bean.BusBean.LoginBus;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
public class SetActivity extends CommonActivity {

    @Bind(R.id.exit_login)
    LinearLayout exit_login;
    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void initView() {

       if (CommonAction.getIsLogin()){
           exit_login.setVisibility(View.VISIBLE);
       }
    }

    @Override
    public void initDatas() {


    }

    @Override
    public void eventListener() {

    }

    @OnClick({R.id.back_relay,R.id.change_pwd,R.id.change_them,R.id.exit_login})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_relay:
                finish();
                break;
            case R.id.change_pwd:
                if (CommonAction.getIsLogin()){
                    Intent intent = new Intent(mContext,ChangePwdActivity.class);
                    startActivity(intent);
                }else {
//                    ToastUtils.Toast_short("您还未登录");
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.change_them:
                Intent intent1 = new Intent(mContext,ChangeThemActivity.class);
                startActivity(intent1);
                break;
            case R.id.exit_login:
                new AlertDialog.Builder(this)
                        .setTitle("温馨提示：")
                        .setMessage("您确定要退出登录吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CommonAction.clearUserData();
                                setResult(103);
                                CommonAction.refreshLogined();
                                finish();
                            }
                        })
                        .create().show();

                break;
        }

    }

}
