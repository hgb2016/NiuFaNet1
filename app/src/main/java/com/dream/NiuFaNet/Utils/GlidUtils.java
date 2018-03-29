package com.dream.NiuFaNet.Utils;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.Other.Const;
import com.dream.NiuFaNet.Other.MyApplication;

/**
 * Created by Administrator on 2017/6/27 0027.
 */
public class GlidUtils {
    public static DrawableRequestBuilder<String> getCircleBitmap(String url){
        DrawableRequestBuilder<String> transform = Glide.with(MyApplication.getInstance()).load(Const.API_BASE_URL+url).transform(new GlideCircleTransform(MyApplication.getInstance()));
        return transform;
    }
}
