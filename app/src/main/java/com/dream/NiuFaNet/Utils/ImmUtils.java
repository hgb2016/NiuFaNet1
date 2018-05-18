package com.dream.NiuFaNet.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dream.NiuFaNet.Other.MyApplication;

/**
 * Created by Administrator on 2017/11/9 0009.
 */
public class ImmUtils {
    public static InputMethodManager getImm(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm;
    }
    public static void hideImm(Activity activity){
        getImm(activity).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    public static void hideImm(Activity activity,InputMethodManager imm){
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    public static void showImm(Activity activity,View v){
        getImm(activity).showSoftInput(v,InputMethodManager.SHOW_FORCED);
    }
    public static void showImm(Activity activity,View v,InputMethodManager imm){
        imm.showSoftInput(v,InputMethodManager.SHOW_FORCED);
    }
}
