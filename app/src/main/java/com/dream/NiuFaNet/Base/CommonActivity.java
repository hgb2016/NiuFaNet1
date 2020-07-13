package com.dream.NiuFaNet.Base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Window;
import android.view.WindowManager;

import com.dream.NiuFaNet.Contract.PermissionListener;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/7/007.
 */
public abstract class CommonActivity extends AppCompatActivity {
    protected ImmersionBar mImmersionBar;
    public Bundle savedInstanceState;
    public Activity mActivity;
    public Context mContext;
    public Dialog mLoadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(getLayoutId());
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();
         //XuniKeyWord.assistActivity(this);
        this.savedInstanceState = savedInstanceState;
        this.mActivity = this;
        this.mContext = this;
        mLoadingDialog = DialogUtils.initLoadingDialog(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        initDatas();
        eventListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ImmersionBar.with(this).destroy(); //必须调用该方法，防止内存泄漏
    }
    protected void initImmersionBar() {
        mImmersionBar= ImmersionBar.with(this).keyboardEnable(true).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .statusBarDarkFont(true, 0.2f) ;//原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        mImmersionBar.init();
    }
    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
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

    private PermissionListener mListener;
    private static final int PERMISSION_REQUESTCODE = 100;
    public void requestRunPermisssion(String[] permissions, PermissionListener listener){
        mListener = listener;
        List<String> permissionLists = new ArrayList<>();
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                permissionLists.add(permission);
            }
        }

        if(!permissionLists.isEmpty()){
            ActivityCompat.requestPermissions(this, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
        }else{
            //表示全都授权了
            mListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUESTCODE:
                if(grantResults.length > 0){
                    //存放没授权的权限
                    List<String> deniedPermissions = new ArrayList<>();
                    for(int i = 0; i < grantResults.length; i++){
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if(grantResult != PackageManager.PERMISSION_GRANTED){
                            deniedPermissions.add(permission);
                        }
                    }
                    if(deniedPermissions.isEmpty()){
                        //说明都授权了
                        mListener.onGranted();
                    }else{
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }
}
