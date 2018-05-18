package com.dream.NiuFaNet.Ui.Activity;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Presenter.CalendarDetailPresenter;
import com.dream.NiuFaNet.Presenter.ProgramDetailPresenter;
import com.dream.NiuFaNet.R;
import com.example.zhouwei.library.CustomPopWindow;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *待办详情
 *
 */
public class TodoDetailActivity extends CommonActivity {


    @Inject
    CalendarDetailPresenter detailPresenter;
    @Inject
    ProgramDetailPresenter programDetailPresenter;
    private CustomPopWindow mCustomPopWindow;
    @Bind(R.id.more_relay)
    RelativeLayout more_relay;
    @Override
    public int getLayoutId() {
        return R.layout.activity_tudodetail;
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
