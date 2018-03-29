package com.dream.NiuFaNet.Utils;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import com.dream.NiuFaNet.Other.MyApplication;
import com.dream.NiuFaNet.R;

/**
 * Created by Administrator on 2017/10/31 0031.
 */
public class AnimaCommonUtil {


    public static Animation getShunRotate(){
        Animation animation = AnimationUtils.loadAnimation(MyApplication.getInstance(), R.anim.arrow_anim);
        animation.setFillAfter(!animation.getFillAfter());
        return animation;
    }
    public static Animation getNiRotate(){
        Animation animation = AnimationUtils.loadAnimation(MyApplication.getInstance(), R.anim.arrow_anim_ni);
        animation.setFillAfter(!animation.getFillAfter());
        return animation;
    }
}
