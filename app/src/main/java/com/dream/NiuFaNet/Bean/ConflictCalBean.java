package com.dream.NiuFaNet.Bean;

import java.util.ArrayList;

public class ConflictCalBean {
    /**
     * error : false
     * message : 操作成功！
     */

    private String error;
    private String message;
    private boolean isConflict;
    private ArrayList<DataBean> data;
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isConflict() {
        return isConflict;
    }

    public void setConflict(boolean conflict) {
        isConflict = conflict;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean{
        private  String scheduleId;
        private  String title;
        private  String beginTime;
        private  String endTime;

        public String getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(String scheduleId) {
            this.scheduleId = scheduleId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

}
