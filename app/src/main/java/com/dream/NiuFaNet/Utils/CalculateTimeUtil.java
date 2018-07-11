package com.dream.NiuFaNet.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    /***
     * 计算两个日期间的工作日天数，除周六日
     *
     * @param sdate
     * @param edate
     * @return
     */
    public static long calcWorkdayTimeInMillis(Calendar sdate, Calendar edate, long start, long end) {
        // 获取开始时间的偏移量
        long scharge = 0;
        if (sdate.get(Calendar.DAY_OF_WEEK) != 1
                && sdate.get(Calendar.DAY_OF_WEEK) != 7) {
            // 只有在开始时间为非周末的时候才计算偏移量
            scharge += (sdate.get(Calendar.HOUR_OF_DAY) * 3600000);
            scharge += (sdate.get(Calendar.MINUTE) * 60000);
            scharge += (sdate.get(Calendar.SECOND) * 1000);
            scharge = ((24 * 3600000) - scharge);
            scharge += ((sdate.getTime().getTime() - start) - (3 * 24 * 3600000));
        }
        // (24*3600000=86400000)-((9*3600000+1800000)=34200000)+(3*24*3600000=259200000)-(2*24*3600000)=
        // 86400000-34200000=52200000
        // 获取结束时间的偏移量
        long echarge = 0;
        if (edate.get(Calendar.DAY_OF_WEEK) != 1
                && edate.get(Calendar.DAY_OF_WEEK) != (7)) {
            // 只有在结束时间为非周末的时候才计算偏移量
            echarge += (edate.get(Calendar.HOUR_OF_DAY) * 3600000);
            echarge += (edate.get(Calendar.MINUTE) * 60000);
            echarge += (edate.get(Calendar.SECOND) * 1000);
            echarge = ((24 * 3600000) - echarge);
            echarge += (edate.getTime().getTime() - end) - (24 * 3600000);
            echarge -= (2 * 24 * 3600000);
        }
        // (24*3600000=86400000)-(18*3600000=64800000)+(24*3=259200000)
        if (scharge < 0 || echarge < 0)
            scharge = echarge = 0;
        return scharge - echarge;
    }
    /**
     * 计算两个时间的工作日
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static int isWeekend(String startDate, String endDate)
            {
        int thisweek, lastweek;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date now = null;
                Date date = null;
                try {
                    now = df.parse(startDate);
                    date = df.parse(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int subdate = (int) ((date.getTime() - now.getTime()) / (24 * 60 * 60 * 1000));
        int nowtoday = now.getDay();
        int datetoday = date.getDay();
        if (nowtoday == 0 || nowtoday == 6) {
            thisweek = 5;
        } else {
            thisweek = 7 - nowtoday - 2;
        }
        if (datetoday == 0 || datetoday == 6) {
            lastweek = 0;
        } else {
            lastweek = datetoday;
        }
        int i;
        if (nowtoday == 0 || nowtoday == 6) {
            i = (subdate + 1 - thisweek - lastweek) / 7 * 5 + thisweek
                    + lastweek;
        } else {
            i = (subdate + 1 - thisweek - lastweek) / 7 * 5 + thisweek
                    + lastweek + 1;
        }
        return i;


    }
}
