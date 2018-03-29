package com.dream.NiuFaNet.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.dream.NiuFaNet.CustomView.PopChildView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/27 0027.
 */
public class MainVpAdapterView extends PagerAdapter {
    private List<View> fragments = new ArrayList<>();
    public MainVpAdapterView(List<View> fragmentList) {
        if (fragmentList!=null){
            fragments.clear();
            fragments.addAll(fragmentList);        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(fragments.get(position));
        return fragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(fragments.get(position));
    }
}
