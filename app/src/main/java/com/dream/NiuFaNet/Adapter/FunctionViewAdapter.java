package com.dream.NiuFaNet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.NiuFaNet.R;


/**
 * Created by Administrator on 2017/7/11.
 */

public class FunctionViewAdapter extends BaseAdapter {
    //上下文
    private Context context;
    //布局填充器
    private LayoutInflater inflater;
    private String[] typeNames;
    private int[] typeImages={R.mipmap.icon_lsf,R.mipmap.icon_ssf,R.mipmap.icon_ht,R.mipmap.icon_more};
    public FunctionViewAdapter(Context context, String[] titles) {
        this.context=context;
        this.typeNames=titles;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return typeNames.length;
    }

    @Override
    public Object getItem(int position) {
        return typeNames[position];
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null) {
            convertView=inflater.inflate(R.layout.view_imgtext, null);
        }
        ImageView iv = (ImageView) convertView.findViewById(R.id.img_iv);
        TextView tv = (TextView) convertView.findViewById(R.id.content_tv);
        iv.setImageResource(typeImages[position]);
        tv.setText(typeNames[position]);
        return convertView;
        }
}
