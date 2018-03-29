package com.dream.NiuFaNet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dream.NiuFaNet.Base.BaseViewHolder;
import com.dream.NiuFaNet.Base.CommonAdapter;
import com.dream.NiuFaNet.Bean.FunctionBean;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.DragGridBaseAdapter;
import com.dream.NiuFaNet.R;
import com.dream.NiuFaNet.Ui.Activity.WebActivity;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class RecommandAdapter extends CommonAdapter <FunctionBean.BodyBean.FindBean> implements DragGridBaseAdapter{
    private int mHidePosition = -1;
    public RecommandAdapter(Context context, List<FunctionBean.BodyBean.FindBean> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(BaseViewHolder holder, FunctionBean.BodyBean.FindBean dataBean, int position) {

        holder.setImageByUrl(R.id.img_iv,dataBean.getActionPic(),false);
        holder.setImageResource(R.id.icon_dele, R.mipmap.icon_dele);
        //  holder.setText(R.id.content_tv,dataBean.getActionName());
        if (dataBean.isEdited()){
            holder.getView(R.id.icon_dele).setVisibility(View.VISIBLE);
            holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {

                }
            });
        }else{
            holder.getView(R.id.icon_dele).setVisibility(View.GONE);
            final String url = dataBean.getUrl();
            holder.getConvertView().setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    if (url != null && !url.isEmpty()) {
                        Intent intent = new Intent(mContext, WebActivity.class);
                        intent.putExtra(Const.webUrl, url);
                        mContext.startActivity(intent);
                    }
                }
            });
        }

    }

    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        FunctionBean.BodyBean.FindBean findBean = mDatas.get(oldPosition);
        if(oldPosition < newPosition){
            for(int i=oldPosition; i<newPosition; i++){
                Collections.swap(mDatas, i, i+1);
            }
        }else if(oldPosition > newPosition){
            for(int i=oldPosition; i>newPosition; i--){
                Collections.swap(mDatas, i, i-1);
            }
        }

        mDatas.set(newPosition, findBean);
    }

    @Override
    public void setHideItem(int hidePosition) {
        this.mHidePosition = hidePosition;
        notifyDataSetChanged();
    }
}
