package com.dream.NiuFaNet.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.LinearLayout;

import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.XuniKeyWord;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/7/007.
 */
public abstract class BaseActivityNoMarginLay extends AppCompatActivity {

    public Bundle savedInstanceState;
    public Activity mActivity;
    public Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(getLayoutId());
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        XuniKeyWord.setShiPeiNoMargin(this);
        this.savedInstanceState = savedInstanceState;
        this.mActivity = this;
        this.mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        initDatas();
        eventListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public abstract int getLayoutId();

    public abstract void initView();
    public abstract void initDatas();

    public abstract void eventListener();
    /**
     * Intent的简单跳转
     * @param cls
     * @param bundle
     */
    protected void GoToActivity(Class<?>cls,Bundle bundle){
        Intent intent=new Intent(this,cls);
        if (null!=bundle){
            intent.putExtras(bundle);
        }
        startActivity(intent);
//        overridePendingTransition(R.anim.dync_in_from_right,R.anim.dync_out_to_left);
    }

    /**
     * Intent简单的跳转关闭
     * @param cls
     * @param bundle
     */
    protected void GoToActivityFinish(Class<?>cls,Bundle bundle){
        Intent intent=new Intent(this,cls);
        if (null!=bundle){
            intent.putExtras(bundle);
        }
        startActivity(new Intent(this,cls));
//        overridePendingTransition(R.anim.slide_left_out,R.anim.slide_left_in);

        this.finish();
    }
}
