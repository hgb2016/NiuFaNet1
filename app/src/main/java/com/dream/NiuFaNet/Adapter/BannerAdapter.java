package com.dream.NiuFaNet.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Bean.BannerBean;
import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.Utils.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10 0010.
 */
public class BannerAdapter extends PagerAdapter {
    private List<ImageView> views = new ArrayList<>();
    private List<BannerBean.DataBean> dataList = new ArrayList<>();

    public BannerAdapter(List<ImageView> views, List<BannerBean.DataBean> data){
        if (views!=null&&data!=null){
            this.views.clear();
            this.views.addAll(views);

            dataList.clear();
            dataList.addAll(data);
        }
    }
    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //对超出范围的资源进行销毁
    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        // TODO Auto-generated method stub
        //super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }
    //对显示的资源进行初始化
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        //return super.instantiateItem(container, position);
        Glide.with(MyApplication.getInstance())
                .load(dataList.get(position).getImgUrl())
                .into(views.get(position));
        container.addView(views.get(position));
        return views.get(position);
    }
}
