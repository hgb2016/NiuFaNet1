package com.dream.NiuFaNet.Bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class CalendarBean implements Serializable{
    private String gDay;
    private String nDay;
    private String weekDay;
    private String tag;
    private Date date;

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private boolean isWork;

    public boolean isWork() {
        return isWork;
    }

    public void setWork(boolean work) {
        isWork = work;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getgDay() {
        return gDay;
    }

    public void setgDay(String gDay) {
        this.gDay = gDay;
    }

    public String getnDay() {
        return nDay;
    }

    public void setnDay(String nDay) {
        this.nDay = nDay;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    @Override
    public String toString() {
        return "CalendarBean{" +
                "gDay='" + gDay + '\'' +
                ", nDay='" + nDay + '\'' +
                ", weekDay='" + weekDay + '\'' +
                '}';
    }
}
