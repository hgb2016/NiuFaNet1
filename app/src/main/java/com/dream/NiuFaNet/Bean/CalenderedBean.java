package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/21 0021.
 */
public class CalenderedBean {

    /**
     * data : [{"scheduleId":"7","title":"开会咯","beginTime":"\"null\"","endTime":"2017-11-21 17:21:00.0","createTime":"2017-11-21 15:22:24.0","address":"办公室","projectId":"0"},{"scheduleId":"10","title":"","beginTime":"\"null\"","endTime":"2017-11-22 17:28:00.0","createTime":"2017-11-21 16:29:29.0","address":"南山公园","projectId":"0"}]
     * error : false
     * message :
     */

    private String error;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * scheduleId : 7
         * title : 开会咯
         * beginTime : "null"
         * endTime : 2017-11-21 17:21:00
         * createTime : 2017-11-21 15:22:24
         * address : 办公室
         * projectId : 0
         *
         */

        private String scheduleId;
        private String title;
        private String beginTime;
        private String endTime;
        private String createTime;
        private String address;
        private String projectId;
        private String participantIDs;
        private String type;
        private String fileCount;

        public String getFileCount() {
            return fileCount;
        }

        public void setFileCount(String fileCount) {
            this.fileCount = fileCount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getParticipantIDs() {
            return participantIDs;
        }

        public void setParticipantIDs(String participantIDs) {
            this.participantIDs = participantIDs;
        }

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }
    }
}
