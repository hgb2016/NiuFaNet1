package com.dream.NiuFaNet.Ui.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonActivity;
import com.dream.NiuFaNet.Base.CommonAdapter;

import com.dream.NiuFaNet.Bean.TimeTipBean;
import com.dream.NiuFaNet.CustomView.MyListView;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.OnClick;


/**
 * 设置提醒时间
 * Created by hou on 2018/3/29.
 */

public class SetRemindActivity extends CommonActivity {
    @Bind(R.id.remindtime_rv)
    MyListView remindtime_rv;
    @Bind(R.id.notime_lay)
    LinearLayout notime_lay;
    @Bind(R.id.back)
    ImageView iv_back;
    @Bind(R.id.remindtip_tv)
    TextView remindtip_tv;
    @Bind(R.id.remind_iv1)
    ImageView remind_iv1;
    private RemindRvAdapter remindRvAdapter;
    private ArrayList<TimeTipBean> lists = new ArrayList();
    private String remind = "";
    private boolean isSelect;
    public static String[] tipStrs = new String[]{"准点提醒", "15分钟前", "30分钟前", "1小时前", "1天前","两天前"};
    public static int[] tipMinutes = new int[]{0, 15, 30, 60, 1440,2880};
    @Override
    public int getLayoutId() {
        return R.layout.activity_setremind;
    }
    @Override
    public void initView() {
        Intent intent = getIntent();
        remind = intent.getStringExtra("remind");
        if (!TextUtils.isEmpty(remind)) {
            switch (remind) {
                case "1":
                    for (int i = 0; i < tipStrs.length; i++) {
                        TimeTipBean bean = new TimeTipBean();
                        bean.setTimeStr(tipStrs[i]);
                        bean.setMinute(tipMinutes[i]);
                        if (i == 0) {
                            bean.setSelect(true);
                        }
                        lists.add(bean);
                    }
                    break;
                case "2":

                    break;
            }

        }
        remindRvAdapter = new RemindRvAdapter(getApplicationContext(), lists, R.layout.rvitem_remind);
        remindtime_rv.setAdapter(remindRvAdapter);

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void eventListener() {

    }

    @OnClick({R.id.back, R.id.sure_tv,R.id.notime_lay})
    public void OnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sure_tv:
                int count=0;
                for (int i=0;i<lists.size();i++){
                    if (lists.get(i).isSelect()){
                        count++;
                    }
                }
                if (count>3){
                    ToastUtils.Toast_short(mActivity,"最多只能选择3个时间段");
                }else {
                    StringBuffer buff = new StringBuffer();
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).isSelect()) {
                            buff.append(lists.get(i).getTimeStr());
                            buff.append("、");
                        }
                    }
                    Collections.sort(lists, new Comparator<TimeTipBean>() {
                        @Override
                        public int  compare(TimeTipBean lhs, TimeTipBean rhs) {
                            if (lhs.getMinute() < rhs.getMinute()) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    if (buff.toString().length()!=0){
                        intent.putExtra("result",buff.toString().substring(0, buff.toString().length() - 1));
                        intent.putExtra("tiplist",lists);
                        setResult(Const.REMIND,intent);
                        finish();
                    }else {
                        intent.putExtra("result","不提醒");
                        intent.putExtra("tiplist",lists);
                        setResult(Const.REMIND,intent);
                        finish();
                    }
                }

                break;
            case R.id.notime_lay:
                isSelect=!isSelect;
                if (isSelect) {
                    remind_iv1.setImageResource(R.mipmap.check_green);
                    for (TimeTipBean r :lists) {
                        r.setSelect(false);
                    }
                    remindRvAdapter.notifyDataSetChanged();
                }else {
                    remind_iv1.setImageResource(R.mipmap.emptycheck_2);
                }
                break;
    }
    }

    public class RemindRvAdapter extends CommonAdapter<TimeTipBean> {

        public RemindRvAdapter(Context context, List<TimeTipBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(final BaseViewHolder holder, final TimeTipBean remind, final int position) {
            TextView remind_tv = holder.getView(R.id.remind_tv);
            final ImageView remind_iv = holder.getView(R.id.remind_iv);
            holder.setText(R.id.remind_tv, remind.getTimeStr());
            if (remind.isSelect()) {
                remind_tv.setTextColor(ResourcesUtils.getColor(R.color.blue_title));
                remind_iv.setImageResource(R.mipmap.check_green);
            }else{
                remind_tv.setTextColor(ResourcesUtils.getColor(R.color.black));
                remind_iv.setImageResource(R.mipmap.emptycheck_2);
            }
            if (!isSelect) {
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remind_iv1.setImageResource(R.mipmap.emptycheck_2);
                        remind.setSelect(!remind.isSelect());
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

}
