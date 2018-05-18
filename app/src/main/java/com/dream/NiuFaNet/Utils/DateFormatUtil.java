package com.dream.NiuFaNet.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/2 0002.
 */
public class DateFormatUtil {
   public static String getTime(long time,String formatStr){
        SimpleDateFormat format=new SimpleDateFormat(formatStr);
        Date d1=new Date(time);
        String t1=format.format(d1);
       return t1;
    }
   public static String getTime(Date date,String formatStr){
        SimpleDateFormat format=new SimpleDateFormat(formatStr);
        String t1=format.format(date);

       return t1;
    }
   public static Date getTime(String dateStr,String formatStr){
        SimpleDateFormat format=new SimpleDateFormat(formatStr);
       Date t1= null;
       try {
           t1 = format.parse(dateStr);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       return t1;
    }

    /**
     * 获取两个日期之间的间隔天数
     * @return
     */
    public static int getGapCount(long startTime, long endTime,String formatStr) {
        SimpleDateFormat format=new SimpleDateFormat(formatStr);
        String startStr = getTime(startTime, formatStr);
        String endStr = getTime(endTime, formatStr);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = format.parse(startStr);
            endDate = format.parse(endStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }
}
