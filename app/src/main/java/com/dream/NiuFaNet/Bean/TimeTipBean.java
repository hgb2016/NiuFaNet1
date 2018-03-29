package com.dream.NiuFaNet.Bean;

/**
 * Created by Administrator on 2017/11/18 0018.
 */
public class TimeTipBean {
    private boolean isSelect;
    private String timeStr;
    private int minute;

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
}
