package com.dream.NiuFaNet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.MainFunctionBean;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.WebActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class MainFunctionAdpter extends CommonAdapter<MainFunctionBean.DataBean> {
    private Context context;
    public MainFunctionAdpter(Context context, List<MainFunctionBean.DataBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        this.context = context;
    }

    @Override
    public void convert(final BaseViewHolder helper, MainFunctionBean.DataBean item, int position) {
        helper.setImageByUrl(R.id.img_iv,item.getImgUrl(),false);
        helper.setText(R.id.content_tv,item.getActionName());
        final String url = item.getLink();
        helper.getConvertView().setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if (url != null && !url.isEmpty()) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra(Const.webUrl, url);
                    context.startActivity(intent);
                }

            }
        });
    }
}
