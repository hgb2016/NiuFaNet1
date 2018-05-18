package com.dream.NiuFaNet.Utils;


import com.dream.NiuFaNet.Other.Const;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/24 0024.
 */
public class MapUtils{

    public static Map<String,String> getMap(){
        Map<String,String> map = new HashMap<>();
        return map;
    }
    public static Map<String,String> getMapInstance(){
        Map<String,String> map = new HashMap<>();
        map.put(Const.page,"1");
        return map;
    }
}
