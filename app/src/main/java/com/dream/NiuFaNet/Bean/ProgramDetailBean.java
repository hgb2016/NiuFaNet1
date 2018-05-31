package com.dream.NiuFaNet.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4 0004.
 */
public class ProgramDetailBean implements Serializable{

    /**
     * data : {"name":"项目标题","userId":"29","description":"km我落空扣扣口蘑41路哈苏","caseNo":"","participant":[]}
     * error : false
     * message :
     */

    private DataBean data;
    private String error;
    private String message;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean implements Serializable{
        /**
         * name : 项目标题
         * userId : 29
         * description : km我落空扣扣口蘑41路哈苏
         * caseNo :
         * participant : []
         */

        private String name;
        private String userId;
        private String userName;
        private String headUrl;
        private String description;
        private String caseNo;
        private String projectId;
        private String status;
        private String clientId;
        private String clientName;

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        private List<scheduleBean> schedule;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<scheduleBean> getSchedule() {
            return schedule;
        }

        public void setSchedule(List<scheduleBean> schedule) {
            this.schedule = schedule;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        private List<participantBean> participant;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCaseNo() {
            return caseNo;
        }

        public void setCaseNo(String caseNo) {
            this.caseNo = caseNo;
        }

        public List<participantBean> getParticipant() {
            return participant;
        }

        public void setParticipant(List<participantBean> participant) {
            this.participant = participant;
        }
        public static class scheduleBean implements Serializable{
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
        public static class participantBean implements Serializable{
            private String userId;
            private String status;
            private String userName;
            private String headUrl;
            private boolean isAdd;
            private boolean isDelete;
            private boolean isEmpty;
            private boolean isSource;

            public boolean isSource() {
                return isSource;
            }

            public void setSource(boolean source) {
                isSource = source;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public boolean isEmpty() {
                return isEmpty;
            }

            public void setEmpty(boolean empty) {
                isEmpty = empty;
            }

            public String getHeadUrl() {
                return headUrl;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }


            public boolean isDelete() {
                return isDelete;
            }

            public void setDelete(boolean delete) {
                isDelete = delete;
            }

            public boolean isAdd() {
                return isAdd;
            }

            public void setAdd(boolean add) {
                isAdd = add;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
