package com.dream.NiuFaNet.Base;

import android.content.Context;

import java.util.List;

public abstract class MultiLayoutsBaseAdapter<T> extends RVBaseAdapter<T> {

    public MultiLayoutsBaseAdapter(Context context, List<T> data, int[] layoutIds) {

        super(context, data, layoutIds);

    }

    @Override
    public int getItemViewType(int position) {
      //调用子类继承实现的获取指定布局类型的抽象方法
        return getItemType(position);
    }

    @Override
    public void onBind(RVBaseHolder holder, T itemBean, int position) {
      //调用子类继承实现的对加载后特定布局中控件操作的抽象方法，同时传入布局类型
        onBinds(holder, itemBean, position, getItemViewType(position));
    }
    //获取指定的布局类型的抽象方法，让子类继承实现
    public abstract int getItemType(int position);
    //对加载后的特定布局中的控件操作的抽象方法，让子类继承实现
    public abstract void onBinds(RVBaseHolder holder, T t, int position, int itemType);
}