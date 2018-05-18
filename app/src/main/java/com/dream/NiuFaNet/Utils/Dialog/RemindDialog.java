package com.dream.NiuFaNet.Utils.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.TimeTipBean;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.RvUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
public abstract class RemindDialog {

    private List<TimeTipBean> tipList = new ArrayList<>();
    public static String[] tipStrs = new String[]{"准点提醒", "15分钟前", "30分钟前", "1小时前", "1天前"};
    public static int[] tipMinutes = new int[]{0, 15, 30, 60, 120};
    private TimeTipAdapter tipAdapter;

    public RemindDialog(Activity mContext) {
        tipList.clear();
        for (int i = 0; i < tipStrs.length; i++) {
            TimeTipBean bean = new TimeTipBean();
            bean.setTimeStr(tipStrs[i]);
            bean.setMinute(tipMinutes[i]);
            if (i == 0) {
                bean.setSelect(true);
            }
            tipList.add(bean);
        }
        tipAdapter = new TimeTipAdapter(mContext, tipList, R.layout.rvitem_timetip);
        getTipDialog(mContext).show();
    }

    public Dialog getTipDialog(final Activity activity) {

        final Dialog tipDialog = new Dialog(activity, R.style.ActionSheetDialogStyle);
        View headView = LayoutInflater.from(activity).inflate(R.layout.dialog_calender_timetip, null);
        RecyclerView timetip_rv = (RecyclerView) headView.findViewById(R.id.timetip_rv);
        RvUtils.setOptionnoLine(timetip_rv, activity);
        TextView cancel_tv = (TextView) headView.findViewById(R.id.cancel_tv);
        TextView sure_tv = (TextView) headView.findViewById(R.id.sure_tv);
        timetip_rv.setAdapter(tipAdapter);
        tipDialog.setContentView(headView);
        cancel_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                tipDialog.dismiss();

            }
        });
        sure_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                StringBuffer buff = new StringBuffer();
                for (int i = 0; i < tipList.size(); i++) {
                    if (tipList.get(i).isSelect()) {
                        buff.append(tipList.get(i).getTimeStr());
                        buff.append("、");
                    }
                }
                Collections.sort(tipList, new Comparator<TimeTipBean>() {
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
                    selectResult(buff.toString().substring(0, buff.toString().length() - 1), tipList);
                }else {
                    selectResult("", tipList);
                }
                tipDialog.dismiss();

            }
        });
        return tipDialog;
    }

    private class TimeTipAdapter extends RVBaseAdapter<TimeTipBean> {

        public TimeTipAdapter(Context context, List<TimeTipBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(final RVBaseHolder holder, final TimeTipBean baseBean, final int position) {
            holder.setText(R.id.timetip_tv, baseBean.getTimeStr());

            final ImageView select_iv = holder.getView(R.id.select_iv);
            if (baseBean.isSelect()) {
                select_iv.setImageResource(R.mipmap.take);
            } else {
                select_iv.setImageResource(R.mipmap.taken);
            }

            holder.setOnClickListener(R.id.select_iv, new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (baseBean.isSelect()) {
                        select_iv.setImageResource(R.mipmap.take);
                        tipList.get(position).setSelect(false);
                    } else {
                        select_iv.setImageResource(R.mipmap.taken);
                        tipList.get(position).setSelect(true);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    public abstract void selectResult(String result, List<TimeTipBean> tipList);
}
