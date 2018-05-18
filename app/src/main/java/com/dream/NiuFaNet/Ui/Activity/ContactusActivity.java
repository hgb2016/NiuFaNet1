package com.dream.NiuFaNet.Ui.Activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.R;

import butterknife.OnClick;

/**
 * Created by hou on 2018/4/19.
 */

public class ContactusActivity extends CommonActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_contactus;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }
    @OnClick({R.id.back_relay,R.id.phone_relay})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.back_relay:
                finish();
                break;
            case R.id.phone_relay:
                diallPhone("0755-22672943");
                break;
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
