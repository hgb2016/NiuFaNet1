package com.dream.NiuFaNet.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BaseBean;
import com.dream.NiuFaNet.Bean.VoiceRvBean;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Utils.ResourcesUtils;
import com.dream.NiuFaNet.Utils.RvUtils;
import com.dream.NiuFaNet.Utils.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14 0014.
 */
public class VoiceContentAdapter extends RVBaseAdapter<VoiceRvBean.BodyBean> {
    public VoiceContentAdapter(Context context, List<VoiceRvBean.BodyBean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void onBind(RVBaseHolder holder, VoiceRvBean.BodyBean baseBean, int position) {
        if (StringUtil.NoNullOrEmpty(baseBean.getImg())){
            holder.setImageByUrl(R.id.type_iv,baseBean.getImg(),false);
        }
        if (StringUtil.NoNullOrEmpty(baseBean.getName())){
            holder.setText(R.id.typeName_tv,baseBean.getName());
        }
        RecyclerView voicese_rv = holder.getView(R.id.voicese_rv);
        RvUtils.setOptionnoLine(voicese_rv);
        List<VoiceRvBean.BodyBean.DataBean> beanList = baseBean.getData();
        if (beanList!=null&&beanList.size()>0){
            voicese_rv.setAdapter(new VoiceTextAdapter(context,beanList,R.layout.rvitem_onlytext));
        }
    }

    private class VoiceTextAdapter extends RVBaseAdapter<VoiceRvBean.BodyBean.DataBean>{

        public VoiceTextAdapter(Context context, List<VoiceRvBean.BodyBean.DataBean> data, int layoutId) { 
            super(context, data, layoutId);
        }

        @Override
        public void onBind(RVBaseHolder holder, VoiceRvBean.BodyBean.DataBean dataBean, int position) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.topMargin = 15;
            TextView onlyTv = holder.getView(R.id.only_tv);
            onlyTv.setTextColor(ResourcesUtils.getColor(R.color.white));
            onlyTv.setTextSize(12);
            onlyTv.setText(StringUtil.checkNull(dataBean.getName(),""));
        }
    }
}
