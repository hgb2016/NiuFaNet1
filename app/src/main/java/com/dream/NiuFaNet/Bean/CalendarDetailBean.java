package com.dream.NiuFaNet.Bean;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
public class CalendarDetailBean implements Serializable{

    /**
     * data : {"userId":"31","scheduleId":"22","userName":"轩辕墨翼","title":"挺酷","beginTime":"2017-12-05 18:15:00.0","endTime":"2017-12-05 19:15:00.0","type":"2","address":"","createTime":"2017-11-22 18:15:47.0","remark":"","participant":[],"remind":[{"remindTime":"2017-12-05 18:15:00.0","description":"准点提醒"}],"pic":[{"id":"24","imgUrl":"http://api.niufa.cn:9080/niufa_chatbot/upload/calendar20171122181547883.jpg"},{"id":"25","imgUrl":"http://api.niufa.cn:9080/niufa_chatbot/upload/calendar20171122181547883.jpg"},{"id":"26","imgUrl":"http://api.niufa.cn:9080/niufa_chatbot/upload/calendar20171122181547883.jpg"}]}
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
         * userId : 31
         * scheduleId : 22
         * userName : 轩辕墨翼
         * title : 挺酷
         * beginTime : 2017-12-05 18:15:00.0
         * endTime : 2017-12-05 19:15:00.0
         * type : 2
         * address :
         * createTime : 2017-11-22 18:15:47.0
         * remark :
         * participant : []
         * remind : [{"remindTime":"2017-12-05 18:15:00.0","description":"准点提醒"}]
         * pic : [{"id":"24","imgUrl":"http://api.niufa.cn:9080/niufa_chatbot/upload/calendar20171122181547883.jpg"},{"id":"25","imgUrl":"http://api.niufa.cn:9080/niufa_chatbot/upload/calendar20171122181547883.jpg"},{"id":"26","imgUrl":"http://api.niufa.cn:9080/niufa_chatbot/upload/calendar20171122181547883.jpg"}]
         */

        private String userId;
        private String createUserId;
        private String createUserName;
        private String createHeadUrl;
        private String scheduleId;
        private String userName;
        private String title;
        private String beginTime;
        private String endTime;
        private String type;
        private String address;
        private String createTime;
        private String remark;
        private String projectId;
        private String projectTitle;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateHeadUrl() {
            return createHeadUrl;
        }

        public void setCreateHeadUrl(String createHeadUrl) {
            this.createHeadUrl = createHeadUrl;
        }

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }

        public String getCreateUserName() {
            return createUserName;
        }

        public void setCreateUserName(String createUserName) {
            this.createUserName = createUserName;
        }

        public String getProjectTitle() {
            return projectTitle;
        }

        public void setProjectTitle(String projectTitle) {
            this.projectTitle = projectTitle;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        private List<participantBean> participant;
        private List<RemindBean> remind;
        private List<PicBean> pic;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(String scheduleId) {
            this.scheduleId = scheduleId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public List<participantBean> getParticipant() {
            return participant;
        }

        public void setParticipant(List<participantBean> participant) {
            this.participant = participant;
        }

        public List<RemindBean> getRemind() {
            return remind;
        }

        public void setRemind(List<RemindBean> remind) {
            this.remind = remind;
        }

        public List<PicBean> getPic() {
            return pic;
        }

        public void setPic(List<PicBean> pic) {
            this.pic = pic;
        }

        public static class RemindBean implements Serializable{
            /**
             * remindTime : 2017-12-05 18:15:00.0
             * description : 准点提醒
             */

            private String remindTime;
            private String description;

            public String getRemindTime() {
                return remindTime;
            }

            public void setRemindTime(String remindTime) {
                this.remindTime = remindTime;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public static class PicBean implements Serializable{
            /**
             * id : 24
             * imgUrl : http://api.niufa.cn:9080/niufa_chatbot/upload/calendar20171122181547883.jpg
             */

            private String id;
            private String imgUrl;
            private boolean isDelete;
            private boolean isAdd;
            private Bitmap bitmap;
            private File file;

            public Bitmap getBitmap() {
                return bitmap;
            }

            public void setBitmap(Bitmap bitmap) {
                this.bitmap = bitmap;
            }

            public File getFile() {
                return file;
            }

            public void setFile(File file) {
                this.file = file;
            }

            public boolean isAdd() {
                return isAdd;
            }

            public void setAdd(boolean add) {
                isAdd = add;
            }

            public boolean isDelete() {
                return isDelete;
            }

            public void setDelete(boolean delete) {
                isDelete = delete;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
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
            private boolean isSouce;

            public boolean isSouce() {
                return isSouce;
            }

            public void setSouce(boolean souce) {
                isSouce = souce;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
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

            public boolean isEmpty() {
                return isEmpty;
            }

            public void setEmpty(boolean empty) {
                isEmpty = empty;
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
        }
    }
}
