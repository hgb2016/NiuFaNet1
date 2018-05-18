package com.dream.NiuFaNet.Utils;

/**
 * Created by Administrator on 2018/4/12.
 */

public class CalculateTimeUtil {
    public static String getTimeExpend(long longStart, long longEnd ){
        //传入字串类型 2016/06/28 08:30
        long longExpend = longEnd - longStart;  //获取时间差

        long longHours = longExpend / (60 * 60 * 1000); //根据时间差来计算小时数
        long longMinutes = (longExpend - longHours * (60 * 60 * 1000)) / (60 * 1000);   //根据时间差来计算分钟数

        return longHours+"时"+longMinutes+"分";
    }
    public static int getDayExpend(long longStart, long longEnd ){
        //传入字串类型 2016/06/28 08:30
        long longExpend = longEnd - longStart;  //获取时间差

        long longHours = longExpend / (60 * 60 * 1000); //根据时间差来计算小时数
        long longMinutes = (longExpend - longHours * (60 * 60 * 1000)) / (60 * 1000);   //根据时间差来计算分钟数

        return (int) (longHours/24);
    }
}
