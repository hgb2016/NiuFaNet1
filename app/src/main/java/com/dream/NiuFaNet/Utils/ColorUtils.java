package com.dream.NiuFaNet.Utils;

import com.dream.NiuFaNet.Other.MyApplication;

/**
 * Created by Administrator on 2017/9/13 0013.
 */
public class ColorUtils  {

    public static int getColor(int resId){
        return MyApplication.getInstance().getResources().getColor(resId);
    }
}
