package com.dream.NiuFaNet.Utils;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dream.NiuFaNet.CustomView.MyDecoration;
import com.dream.NiuFaNet.CustomView.RvDecoration;
import com.dream.NiuFaNet.Other.MyApplication;
import com.kevin.wraprecyclerview.WrapRecyclerView;


/**
 * Created by Administrator on 2017/5/18 0018.
 */
public class RvUtils {

    public static void setRvParam(RecyclerView rv){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin = 35;
        params.rightMargin = 35;
        rv.setLayoutParams(params);
    }
    public static void setRvParam_RelayParent(RecyclerView rv){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin = 25;
        params.rightMargin = 25;
        rv.setLayoutParams(params);
    }
    public static void setOption(RecyclerView rv, Activity activity){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        //添加分割线
//        rv.addItemDecoration(new MyDecoration(activity,MyDecoration.HORIZONTAL_LIST));
    }
    public static void setOptionDecoration(RecyclerView rv, Activity activity){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        //添加分割线
        rv.addItemDecoration(new MyDecoration(activity, RvDecoration.VERTICAL_LIST));
    }
    public static void setOptionnoLine(WrapRecyclerView rv, Activity activity){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
    }
    public static void setOptionnoLine(RecyclerView rv, Activity activity){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
    }
    public static void setOptionnoLine(RecyclerView rv){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyApplication.getInstance());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
    }
    public static void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }
}
