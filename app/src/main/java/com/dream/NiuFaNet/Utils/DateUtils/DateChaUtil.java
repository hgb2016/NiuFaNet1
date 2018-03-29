package com.dream.NiuFaNet.Utils.DateUtils;

import android.widget.TextView;

/**
 * Created by Administrator on 2017/7/6 0006.
 */
public class DateChaUtil {
    public static String getDateCha(long fdCreateDate) {
        String chaStr = "";
        long currentTime = System.currentTimeMillis();
        //天数差
        long dateDays = (currentTime - fdCreateDate) / (1000 * 3600 * 24);
        if (dateDays < 1) {
            long hours = dateDays * 24;
            if (hours<0){
                long minute = hours * 60;
                chaStr = minute+"分钟前";
            }else {
                chaStr = hours+"小时前";
            }
        } else if (dateDays > 30) {
            long month = dateDays / 30;
            chaStr = month+"个月前";

        } else if (dateDays > 1 && dateDays <= 30) {
            chaStr= dateDays+"天前";
        }
        return chaStr;
    }
}
