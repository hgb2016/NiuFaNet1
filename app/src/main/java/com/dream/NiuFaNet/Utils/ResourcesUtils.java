package com.dream.NiuFaNet.Utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.dream.NiuFaNet.Other.MyApplication;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
public class ResourcesUtils {

    public static int getColor(int resId){
        return MyApplication.getInstance().getResources().getColor(resId);
    }
    public static Drawable getDrable(int resId){
        return MyApplication.getInstance().getResources().getDrawable(resId);
    }
    public static String getString(int resId){
        return MyApplication.getInstance().getResources().getString(resId);
    }
}
