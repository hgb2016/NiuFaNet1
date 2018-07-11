package com.dream.NiuFaNet.Ui.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/3.
 */

public class SettingActivity extends CommonActivity {
  @Bind(R.id.exitlogin_relay)
  RelativeLayout exit_login;
    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
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
    @OnClick({R.id.changepwd_relay,R.id.back_relay,R.id.exitlogin_relay})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.changepwd_relay:
                if (CommonAction.getIsLogin()){
                    Intent intent = new Intent(mContext,ChangePwdActivity.class);
                    startActivity(intent);
                }else {
//                    ToastUtils.Toast_short("您还未登录");
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.exitlogin_relay:
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
            case R.id.back_relay:
                finish();
                break;
        }
    }
}
