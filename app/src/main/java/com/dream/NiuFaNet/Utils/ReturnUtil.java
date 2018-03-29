package com.dream.NiuFaNet.Utils;

import com.dream.NiuFaNet.Bean.BusBean.ReturnBean;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/10/16 0016.
 */
public class ReturnUtil {

    public static void returnMain(){
        ReturnBean bean = new ReturnBean();
        bean.setEventStr("return");
        EventBus.getDefault().post(bean);
    }
}
