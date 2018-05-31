package com.dream.NiuFaNet.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dream.NiuFaNet.R;
import com.example.zhouwei.library.CustomPopWindow;

public class PopUtils {
    private static CustomPopWindow mCustomPopWindow;

    //详情弹框
    public static void showDetailPop(View v, String content, Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_detail, null);
        TextView detail_tv= (TextView) contentView.findViewById(R.id.detail_tv);
        detail_tv.setText(content);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(contentView)
                .setBgDarkAlpha(0.7f)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .enableBackgroundDark(true)
                .setAnimationStyle(R.anim.actionsheet_dialog_in)
                .create()
                .showAsDropDown(v, 0, 0);
    }
}
