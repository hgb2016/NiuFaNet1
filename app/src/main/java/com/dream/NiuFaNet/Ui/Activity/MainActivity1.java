package com.dream.NiuFaNet.Ui.Activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.NiuFaNet.Base.BaseActivityRelay;
import com.dream.NiuFaNet.Other.CommonAction;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Fragment.CalenderFragment;
import com.dream.NiuFaNet.Ui.Fragment.ContactFragment;
import com.dream.NiuFaNet.Ui.Fragment.MainFragment;
import com.dream.NiuFaNet.Ui.Fragment.ProgramFragment;
import com.dream.NiuFaNet.Utils.AppManager;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ImgUtil;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.SpUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ljq on 2018/3/23.
 */

public class MainActivity1 extends BaseActivityRelay {
    @Bind(R.id.project_img)
    ImageView project_img;
    @Bind(R.id.contact_img)
    ImageView contact_img;
    @Bind(R.id.main_img)
    ImageView main_img;
    @Bind(R.id.schedule_img)
    ImageView schedule_img;
    @Bind(R.id.schedule_tv)
    TextView schedule_tv;
    @Bind(R.id.main_tv)
    TextView main_tv;
    @Bind(R.id.project_tv)
    TextView project_tv;
    @Bind(R.id.contact_tv)
    TextView contact_tv;
    private Bitmap mainnormal,schedulenormal,projectnormal,contactnormal,mainselect,scheduleselect,projectselect,contactselect;
    private Fragment mainFra, scheduleFra, projectFra, contactFra;
    private FragmentManager mFragmentManager;
    private Fragment currentFragment;
    private long exitTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main1;
    }

    @Override
    public void initView() {
        mFragmentManager = this.getSupportFragmentManager();
        initBitmap();
        SpUtils.savaUserInfo(Const.mainStatu, "1");
    }

    @Override
    public void refreshView() {

    }

    @Override
    public void initDatas() {
        if (savedInstanceState != null) {
            mainFra = mFragmentManager.findFragmentByTag("main");
            scheduleFra = mFragmentManager.findFragmentByTag("schedule");
            projectFra = mFragmentManager.findFragmentByTag("project");
            contactFra = mFragmentManager.findFragmentByTag("contact");
        } else {
            initFragment();
        }
        addFragment();

    }
    private void initFragment() {
        mainFra = new MainFragment();
        scheduleFra = new CalenderFragment();
        contactFra = new ContactFragment();
        projectFra = new ProgramFragment();

    }

    private void addFragment() {
        mFragmentManager.beginTransaction()
                .add(R.id.main_fra, mainFra, "main")
                .show(mainFra)
                .add(R.id.main_fra, scheduleFra, "schedule")
                .hide(scheduleFra)
                .add(R.id.main_fra, contactFra, "contact")
                .hide(contactFra)
                .add(R.id.main_fra, projectFra, "project")
                .hide(projectFra)
                .commit();
        currentFragment = mainFra;
    }
    @Override
    public void eventListener() {

    }
    //初始化底部tab
    private void initbottomItem() {
        main_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
        schedule_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
        contact_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
        project_tv.setTextColor(ResourcesUtils.getColor(R.color.main_textn));
        project_img.setImageBitmap(projectnormal);
        contact_img.setImageBitmap(contactnormal);
        main_img.setImageBitmap(mainnormal);
        schedule_img.setImageBitmap(schedulenormal);
    }
    public void startFragment(String s) {
        switch (s) {
            case "main":
                if (currentFragment != mainFra) {
                    mFragmentManager.beginTransaction()
                            .show(mainFra)
                            .hide(currentFragment)
                            .commitAllowingStateLoss();
                    currentFragment = mainFra;
                    SpUtils.setParam(Const.mainStatu, "1");
                }

                break;
            case "schedule":
                if (currentFragment != scheduleFra) {
                    mFragmentManager.beginTransaction()
                            .show(scheduleFra)
                            .hide(currentFragment)
                            .commitAllowingStateLoss();
                    currentFragment = scheduleFra;
                }

                break;
            case "project":
                if (currentFragment != projectFra) {
                    mFragmentManager.beginTransaction()
                            .show(projectFra)
                            .hide(currentFragment)
                            .commitAllowingStateLoss();
                    currentFragment = projectFra;
                }
                break;
            case "contact":
                if (currentFragment != contactFra) {
                    mFragmentManager.beginTransaction()
                            .show(contactFra)
                            .hide(currentFragment)
                            .commitAllowingStateLoss();
                    currentFragment = contactFra;
                }
                break;
        }
    }

    private void initBitmap() {
        mainnormal = ImgUtil.readBitMap(this, R.mipmap.icon_bar01);
        mainselect = ImgUtil.readBitMap(this,  R.mipmap.icon_bar01_click);
        schedulenormal = ImgUtil.readBitMap(this, R.mipmap.icon_bar02);
        scheduleselect = ImgUtil.readBitMap(this,  R.mipmap.icon_bar02_click);
        projectnormal = ImgUtil.readBitMap(this, R.mipmap.icon_bar03);
        projectselect = ImgUtil.readBitMap(this,  R.mipmap.icon_bar03_click);
        contactnormal = ImgUtil.readBitMap(this, R.mipmap.icon_bar04);
        contactselect = ImgUtil.readBitMap(this,  R.mipmap.icon_bar04_click);
    }
    @SuppressLint("NewApi")
    @OnClick({ R.id.main_lay, R.id.schedule_lay, R.id.project_lay, R.id.contact_lay})
    public void onClick(View v) {
        String them = CommonAction.getThem();
        switch (v.getId()) {
            case R.id.project_lay:
                if (CommonAction.getIsLogin()) {
                    refreshProject(them);
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.contact_lay:
                if (CommonAction.getIsLogin()) {
                    refreshContact();
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            case R.id.main_lay:
                refreshMain();
                break;
            case R.id.schedule_lay:
                if (CommonAction.getIsLogin()) {
                    refreshSchedule();
                } else {
                    DialogUtils.getLoginTip(mActivity).show();
                }
                break;
            default:
                break;
        }
    }
    //刷新首页模块
    private void refreshMain() {
        initbottomItem();
        main_img.setImageBitmap(mainselect);
        main_tv.setTextColor(ResourcesUtils.getColor(R.color.table_textcolor));
        startFragment("main");
        //statutView.setBackgroundColor(getResources().getColor(R.color.statu_diwen));
        SpUtils.savaUserInfo(Const.mainStatu, "1");
    }
    //刷新日程模块
    private void refreshSchedule() {
        initbottomItem();
        schedule_img.setImageBitmap(scheduleselect);
        schedule_tv.setTextColor(ResourcesUtils.getColor(R.color.table_textcolor));
        startFragment("schedule");
        //statutView.setBackgroundColor(getResources().getColor(R.color.statu_diwen));
        SpUtils.savaUserInfo(Const.mainStatu, "2");
    }
    //刷新项目模块
    private void refreshProject(String them) {
        initbottomItem();
        project_img.setImageBitmap(projectselect);
        project_tv.setTextColor(ResourcesUtils.getColor(R.color.table_textcolor));
        startFragment("project");
        SpUtils.savaUserInfo(Const.mainStatu, "3");
    }
    //刷新联系人模块
    private void refreshContact() {
        initbottomItem();
        contact_img.setImageBitmap(contactselect);
        contact_tv.setTextColor(ResourcesUtils.getColor(R.color.table_textcolor));
        startFragment("contact");
        //statutView.setBackgroundColor(getResources().getColor(R.color.statu_diwen));
        SpUtils.savaUserInfo(Const.mainStatu, "4");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity1.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                AppManager.getAppManager().AppExit();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
