package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/9 0009.
 */
public class CalInviteBean {

    /**
     * data : [{"id":"85","userId":"2182","title":"司机口岸","beginTime":"2017-12-09 16:57:41","endTime":"2017-12-09 17:57:41","address":"","remark":"","userName":"宋大佬"}]
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
         * id : 85
         * userId : 2182
         * title : 司机口岸
         * beginTime : 2017-12-09 16:57:41
         * endTime : 2017-12-09 17:57:41
         * address :
         * remark :
         * userName : 宋大佬
         */

        private String id;
        private String userId;
        private String title;
        private String beginTime;
        private String endTime;
        private String address;
        private String remark;
        private String userName;
        private String eventStr;
        private String method;
        private int position;

        public String getEventStr() {
            return eventStr;
        }

        public void setEventStr(String eventStr) {
            this.eventStr = eventStr;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
