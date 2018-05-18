package com.dream.NiuFaNet.Ui.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DateFormatUtil;
import com.dream.NiuFaNet.Utils.DateUtils.Week;
import com.dream.NiuFaNet.Utils.Dialog.DialogUtils;
import com.dream.NiuFaNet.Utils.ImmUtils;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.example.zhouwei.library.CustomPopWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *  新建待办
 * Created by hou on 2018/3/29.
 */

public class NewTodoActivity extends CommonActivity {

    @Bind(R.id.remind_tv)
    TextView remind_tv;
    @Bind(R.id.remindtype_tv)
    TextView remindtype_tv;
    @Bind(R.id.event_tv)
    EditText event_tv;
    @Bind(R.id.address_tv)
    EditText address_tv;
    @Bind(R.id.note_tv)
    EditText note_tv;
    @Bind(R.id.remindtype_lay)
    LinearLayout remindtype_lay;
    @Bind(R.id.apendix_lay)
    LinearLayout apendix_lay;
    @Bind(R.id.address_lay)
    LinearLayout address_lay;
    @Bind(R.id.note_lay)
    LinearLayout note_lay;
    @Bind(R.id.more_lay)
    LinearLayout more_lay;
    @Bind(R.id.addpicture)
    ImageView addpic_iv;
    @Bind(R.id.iv_more)
    ImageView iv_more;
    @Bind(R.id.starttime_lay)
    LinearLayout starttime_lay;
    @Bind(R.id.starttime_tv)
    TextView starttime_tv;
    @Bind(R.id.startdate_tv)
    TextView startdate_tv;
    @Bind(R.id.endtime_lay)
    LinearLayout endtime_lay;
    @Bind(R.id.endtime_tv)
    TextView endtime_tv;
    @Bind(R.id.enddate_tv)
    TextView enddate_tv;
    @Bind(R.id.add_iv)
    ImageView addcontact_iv;
    @Bind(R.id.work_iv)
    ImageView work_iv;
    @Bind(R.id.personal_iv)
    ImageView personal_iv;
    private boolean isup =false;
    private CustomPopWindow mCustomPopWindow;
    //提醒方式集合
    private  ArrayList<Remind> lists=new ArrayList();
    private TimePickerView dateDialog;
    private Dialog loadingDialog;
    private InputMethodManager imm;
    private long startDate;
    private long endDate;
    private int tempTag;
    @Override
    public int getLayoutId() {
        return R.layout.activity_addnewtodo;
    }

    @Override
    public void initView() {
        //更多是否展开
        isup();
        //时间选择框
        dateDialog = new TimePickerBuilder(this, new OnTimeSelectListener() {


            @Override
            public void onTimeSelect(Date date, View v) {
                long time = date.getTime();
                String dateStr = DateFormatUtil.getTime(time, "MM月dd日  HH:mm");
                Log.e("tag", "dateStr=" + dateStr);
                String[] strings = null;
                if (dateStr.contains("  ")) {
                    strings = dateStr.split("  ");
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(time));
                String weekDay = Week.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
                if (dateStr != null) {
                    switch (tempTag) {
                        case 1://开始日期
                            if (null != strings) {
                                startdate_tv.setText(strings[0] + "(周" + weekDay+")");
                                starttime_tv.setText(strings[1]);
                                startDate = time;
                              /*  remindList.clear();
                                remindList.addAll(getReminds(startDate));*/
                            }
                            break;
                        case 2://结束日期
                            if (null != strings) {
                                enddate_tv.setText(strings[0] +"(周" + weekDay+")");
                                endtime_tv.setText(strings[1]);
                                endDate = time;
                            }
                            break;
                    }
                }
                Log.e("tag", "dateStr=" + dateStr);
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setContentTextSize(16)//滚轮文字大小
                .setTitleSize(13)//标题文字大小
                .setCancelText("取消")//取消按钮文字
                .setLabel(" 年", "月", "日", "时", "分", "秒")
                .isCyclic(true)//是否循环滚动
                .setLineSpacingMultiplier(2.0f)
                .build();
        loadingDialog = DialogUtils.initLoadingDialog(this);
        //projectDialog = initProjectDialog(this);
        imm = ImmUtils.getImm(this);

    }



    @Override
    public void initDatas() {
        lists.add(new  Remind("无", false));
        lists.add(new  Remind("应用内", true));
        lists.add(new  Remind("短信", false));
        lists.add(new  Remind("电话", false));
    }

    @Override
    public void eventListener() {

    }
    @OnClick({R.id.remind_lay,R.id.remindtype_lay,R.id.back_relay,R.id.project_lay,R.id.add_iv,R.id.more_lay,R.id.addpicture,R.id.starttime_lay,R.id.endtime_lay,R.id.work_iv,R.id.personal_iv})
    public void onClick(View v) {
        Intent intent=new Intent();
        String event=event_tv.getText().toString().trim();
        String address=address_tv.getText().toString().trim();
        String note=note_tv.getText().toString().trim();
        switch (v.getId()){
            //开始时间
            case R.id.starttime_lay:
                ImmUtils.hideImm(mActivity, imm);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate);
                dateDialog.setDate(calendar);
                dateDialog.show();
                tempTag = 1;
                break;
            //结束时间
            case R.id.endtime_lay:
                ImmUtils.hideImm(mActivity, imm);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(endDate);
                dateDialog.setDate(calendar1);
                dateDialog.show();
                tempTag = 2;
                break;
            //选择工作性质
            case R.id.work_iv:
                work_iv.setImageResource(R.mipmap.check_green);
                personal_iv.setImageResource(R.mipmap.icon_checkempty);
                break;
            //选择私人性质
            case R.id.personal_iv:
                work_iv.setImageResource(R.mipmap.icon_checkempty);
                personal_iv.setImageResource(R.mipmap.check_green);
                break;
            //参与人
            case R.id.add_iv:
                startActivity(new Intent(getApplicationContext(),AddFriendsActivity.class));
                break;
            //提醒时间
            case R.id.remind_lay:
                intent.putExtra("remind","1");
                intent.setClass(getApplicationContext(),SetRemindActivity.class);
                startActivityForResult(intent, Const.REMIND);
                break;
            //提醒方式
            case R.id.remindtype_lay:
                showChooseRemind(v);
                break;
            //选择项目
            case R.id.project_lay:
                startActivity(new Intent(getApplicationContext(),AddProjectActivity.class));
                break;
            //更多
            case R.id.more_lay:
                isup=!isup;
                 isup();
                break;
            //添加图片
            case R.id.addpicture:
                showPopChooseApendix(v);
                break;
            case R.id.back_relay:
                finish();
                break;
        }
    }


    //返回结果显示
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Const.EVENT:
                   event_tv.setText(data.getStringExtra("result"));
                break;
            case Const.REMIND:
                   remind_tv.setText(data.getStringExtra("remind"));
                break;
            case Const.REMINDTYPE:
                   remindtype_tv.setText(data.getStringExtra("remindtype"));
                break;
            case Const.ADDRESS:
                   address_tv.setText(data.getStringExtra("result"));
                break;
            case Const.NOTE:
                   note_tv.setText(data.getStringExtra("result"));
                break;
        }
    }
    //更多 是收起还是展开
    private void isup() {
        if (!isup){
            iv_more.setImageResource(R.mipmap.up);
            address_lay.setVisibility(View.GONE);
            apendix_lay.setVisibility(View.GONE);
            note_lay.setVisibility(View.GONE);
            remindtype_lay.setVisibility(View.GONE);
        }else {
            iv_more.setImageResource(R.mipmap.down);
            address_lay.setVisibility(View.VISIBLE);
            apendix_lay.setVisibility(View.VISIBLE);
            note_lay.setVisibility(View.VISIBLE);
            remindtype_lay.setVisibility(View.VISIBLE);
        }
    }
    //选择附件 弹框
    private void showPopChooseApendix(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_chooseapendix,null);
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCustomPopWindow!=null){
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()){
                    case R.id.back_relay:
                        mCustomPopWindow.dissmiss();
                        break;
                    case R.id.picture_lay:

                        break;
                    case R.id.photo_lay:

                        break;
                }
                //Toast.makeText(HomeActivity.this,showContent,Toast.LENGTH_SHORT).show();
            }
        };
        contentView.findViewById(R.id.back_relay).setOnClickListener(listener);
        contentView.findViewById(R.id.picture_lay).setOnClickListener(listener);
        contentView.findViewById(R.id.photo_lay).setOnClickListener(listener);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(v, Gravity.BOTTOM,0,0);
    }
    //选择提醒 弹框
    private void showChooseRemind(View v) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_remind,null);
        //处理popWindow 显示内容
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.sure_relay:

                        break;
                }
            }
        };
        contentView.findViewById(R.id.sure_relay).setOnClickListener(listener);
        MyListView remind_lv = (MyListView) contentView.findViewById(R.id.remind_lv);

        RemindRvAdapter remindRvAdapter = new RemindRvAdapter(getApplicationContext(), lists, R.layout.rvitem_remind);
        remind_lv.setAdapter(remindRvAdapter);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                .enableBackgroundDark(true)
                .create()
                .showAtLocation(v, Gravity.BOTTOM,0,0);
    }



    public class RemindRvAdapter extends CommonAdapter<Remind> {

        public RemindRvAdapter(Context context, List<Remind> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(final BaseViewHolder holder, final Remind remind, final int position) {
            TextView remind_tv = holder.getView(R.id.remind_tv);
            final ImageView remind_iv = holder.getView(R.id.remind_iv);
            holder.setText(R.id.remind_tv, remind.getRemindTime());
            if (remind.isSelected) {
                remind_tv.setTextColor(ResourcesUtils.getColor(R.color.blue_title));
                remind_iv.setImageResource(R.mipmap.check_green);
            } else {
                remind_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
                remind_iv.setImageResource(R.mipmap.emptycheck_2);
            }
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    remind.setSelected(!remind.isSelected);
                    if (position == 0) {
                        for (int i = 0; i < mDatas.size(); i++) {
                            if (i != position) {
                                lists.get(i).setSelected(false);
                            }
                        }
                    } else {
                        lists.get(0).setSelected(false);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    class Remind {
        private String remindTime;
        private boolean isSelected;

        public Remind(String remindTime, boolean isSelected) {
            this.remindTime = remindTime;
            this.isSelected = isSelected;
        }

        public String getRemindTime() {
            return remindTime;
        }

        public void setRemindTime(String remindTime) {
            this.remindTime = remindTime;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
