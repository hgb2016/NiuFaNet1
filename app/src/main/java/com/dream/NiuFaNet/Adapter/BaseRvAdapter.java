package com.dream.NiuFaNet.Adapter;

import android.content.Context;

import com.dream.NiuFaNet.Base.RVBaseAdapter;
import com.dream.NiuFaNet.Base.RVBaseHolder;
import com.dream.NiuFaNet.Bean.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14 0014.
 */
public class BaseRvAdapter extends RVBaseAdapter<BaseBean> {
    public BaseRvAdapter(Context context, List<BaseBean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void onBind(RVBaseHolder holder, BaseBean baseBean, int position) {

    }
}
