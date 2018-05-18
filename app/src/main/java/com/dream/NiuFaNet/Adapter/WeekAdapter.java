package com.dream.NiuFaNet.Adapter;

import android.content.Context;
import android.widget.LinearLayout;

import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.WeekBean;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.DensityUtil;

import java.util.List;

public class WeekAdapter extends RVBaseAdapter<WeekBean> {

        public WeekAdapter(Context context, List<WeekBean> data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, WeekBean weekBean, int position) {
            int screenWidth = DensityUtil.getScreenWidth();
            int itemWidth = screenWidth / 7;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            params.width = itemWidth;
            holder.itemView.setLayoutParams(params);
            holder.setText(R.id.only_tv, weekBean.getWeek());
        }
    }