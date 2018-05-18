package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5 0005.
 */
public class ProgramListBean {

    /**
     * data : [{"id":"6","name":"阿西吧我多客","description":"女魔头","status":"0","caseNo":"SZ8556856","schedule":[{"scheduleId":"27","title":"我市公安局","beginTime":"2017-12-05 19:44:48","endTime":"2017-12-08 19:44:00","createTime":"2017-12-05 19:46:26","address":"too哦咯哦哦"}]},{"id":"5","name":"项目标题","description":"km我落空扣扣口蘑41路哈苏","status":"0","caseNo":"","schedule":[{"scheduleId":"28","title":"什么鬼","beginTime":"2018-01-05 20:06:12","endTime":"2018-01-05 21:06:12","createTime":"2017-12-05 20:07:47","address":"诺呢JJ"}]}]
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
         * id : 6
         * name : 阿西吧我多客
         * description : 女魔头
         * status : 0
         * caseNo : SZ8556856
         * schedule : [{"scheduleId":"27","title":"我市公安局","beginTime":"2017-12-05 19:44:48","endTime":"2017-12-08 19:44:00","createTime":"2017-12-05 19:46:26","address":"too哦咯哦哦"}]
         */

        private String id;
        private String name;
        private String description;
        private String status;
        private String caseNo;
        private boolean isSelect;
        private boolean isExpand;

        public boolean isExpand() {
            return isExpand;
        }

        public void setExpand(boolean expand) {
            isExpand = expand;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        private List<ScheduleBean> schedule;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCaseNo() {
            return caseNo;
        }

        public void setCaseNo(String caseNo) {
            this.caseNo = caseNo;
        }

        public List<ScheduleBean> getSchedule() {
            return schedule;
        }

        public void setSchedule(List<ScheduleBean> schedule) {
            this.schedule = schedule;
        }

        public static class ScheduleBean {
            /**
             * scheduleId : 27
             * title : 我市公安局
             * beginTime : 2017-12-05 19:44:48
             * endTime : 2017-12-08 19:44:00
             * createTime : 2017-12-05 19:46:26
             * address : too哦咯哦哦
             */

            private String scheduleId;
            private String title;
            private String beginTime;
            private String endTime;
            private String createTime;
            private String address;
            private String fileCount;
            private String hourNum;
            private String createUserName;
            private String headUrl;

            public String getHourNum() {
                return hourNum;
            }

            public void setHourNum(String hourNum) {
                this.hourNum = hourNum;
            }

            public String getCreateUserName() {
                return createUserName;
            }

            public void setCreateUserName(String createUserName) {
                this.createUserName = createUserName;
            }

            public String getHeadUrl() {
                return headUrl;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }

            public String getFileCount() {
                return fileCount;
            }

            public void setFileCount(String fileCount) {
                this.fileCount = fileCount;
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
        }
    }
}
