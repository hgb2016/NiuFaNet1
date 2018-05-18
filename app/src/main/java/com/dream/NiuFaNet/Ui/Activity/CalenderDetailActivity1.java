package com.dream.NiuFaNet.Ui.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.awen.photo.photopick.controller.PhotoPagerConfig;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Adapter.CalDetailParticipantAdapter;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.Bean.CommonBean;
import com.dream.NiuFaNet.Bean.NewCalResultBean;
import com.dream.NiuFaNet.Bean.ProgramDetailBean;
import com.dream.NiuFaNet.Bean.TimeTipBean;
import com.dream.NiuFaNet.Component.DaggerNFComponent;
import com.dream.NiuFaNet.Contract.CalendarDetailContract;
import com.dream.NiuFaNet.Contract.ProgramDetailContract;
import com.dream.NiuFaNet.CustomView.MyGridView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Presenter.CalendarDetailPresenter;
import com.dream.NiuFaNet.Presenter.ProgramDetailPresenter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.BitmapUtils;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.DensityUtil;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.Dialog.RemindDialog;
import com.dream.NiuFaNet.Utils.HttpUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.IntentUtils;
import com.dream.NiuFaNet.Utils.PopWindowUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SpUtils;
import com.dream.NiuFaNet.Utils.SpannableStringUtil;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.wechat.friends.Wechat;

/**
 *日程详情
 * Created by Administrator on 2017/11/19 0019.
 */
public class CalenderDetailActivity1 extends CommonActivity {


    @Inject
    CalendarDetailPresenter detailPresenter;
    @Inject
    ProgramDetailPresenter programDetailPresenter;
    private CustomPopWindow mCustomPopWindow;
    @Bind(R.id.more_relay)
    RelativeLayout more_relay;
    @Override
    public int getLayoutId() {
        return R.layout.activity_calenderdetail1;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initDatas() {

    }

    private void setView() {

    }

    @Override
    public void eventListener() {
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Const.CAMERA) {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, Const.CAMERA);
        } else {
            Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(picture, Const.PICTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.PICTURE && data != null) {
            parseDate(data, Const.PICTURE, mActivity);

        }
        if (requestCode == Const.CAMERA && data != null) {
            parseDate(data, Const.CAMERA, mActivity);
        }
        if (requestCode==123&&resultCode==123){
            reloadData();
        }
        if (requestCode == 101 && resultCode == 102 ) {
            reloadData();
        }
        if (requestCode==006){
            reloadData();
        }
    }

    private void reloadData() {

    }

    private void parseDate(Intent data, int picture, Activity mActivity) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.more_relay})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_relay:
                showPopMenu();
                break;
        }
    }
    private void showPopMenu() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_menu,null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .enableBackgroundDark(true)
                .create()
                .showAsDropDown(more_relay,40,20);
    }

    /**
     * 处理添加按钮弹出显示内容、点击事件等逻辑
     * @param contentView
     */
    private void handleLogic(View contentView){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCustomPopWindow!=null){
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()){
                    case R.id.menu1_log:
                        startActivity(new Intent(getApplicationContext(),CalenderLogActivity.class));
                        break;
                    case R.id.menu2_update:
                        startActivity(new Intent(getApplicationContext(),EditCalenderActivity.class));
                        break;
                    case R.id.menu3_delete:

                        break;
                }
                //Toast.makeText(HomeActivity.this,showContent,Toast.LENGTH_SHORT).show();
            }
        };
        contentView.findViewById(R.id.menu1_log).setOnClickListener(listener);
        contentView.findViewById(R.id.menu2_update).setOnClickListener(listener);
        contentView.findViewById(R.id.menu3_delete).setOnClickListener(listener);
    }
}
