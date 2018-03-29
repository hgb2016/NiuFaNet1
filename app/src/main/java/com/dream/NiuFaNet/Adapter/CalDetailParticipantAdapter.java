package com.dream.NiuFaNet.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.CalendarDetailBean;
import com.dream.NiuFaNet.R;

import java.util.List;

public class CalDetailParticipantAdapter extends CommonAdapter<CalendarDetailBean.DataBean.participantBean> {
        public CalDetailParticipantAdapter(Context context, List<CalendarDetailBean.DataBean.participantBean> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(BaseViewHolder helper, CalendarDetailBean.DataBean.participantBean item, final int position) {
            ImageView only_iv = helper.getView(R.id.only_iv);
            ImageView close_iv = helper.getView(R.id.close_iv);
            TextView only_tv = helper.getView(R.id.only_tv);
            only_tv.setMaxLines(1);
            only_tv.setEllipsize(TextUtils.TruncateAt.END);
            String userName = item.getUserName();
            only_tv.setText(userName);
           /* if (item.isDelete()) {
                close_iv.setVisibility(View.VISIBLE);
            } else {
                close_iv.setVisibility(View.GONE);
            }*/

            String headUrl = item.getHeadUrl();
            if (!item.isEmpty()){
                if (headUrl!=null&&!headUrl.isEmpty()){
                    helper.setImageByUrl(R.id.only_iv,headUrl,true);
                }else {
                    only_iv.setImageResource(R.mipmap.niu);
                }
            }
        }
    }