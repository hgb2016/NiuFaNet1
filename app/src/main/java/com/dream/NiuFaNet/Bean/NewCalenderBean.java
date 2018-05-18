package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/20 0020.
 */
public class NewCalenderBean {
    private String userId;
    private String scheduleId;
    private String title;
    private String beginTime;
    private String endTime;
    private String type;
    private String address;
    private String remark;
    private List<ParticipantBean> participant;
    private List<RemindBean> remind;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ParticipantBean> getParticipant() {
        return participant;
    }

    public void setParticipant(List<ParticipantBean> participant) {
        this.participant = participant;
    }

    public List<RemindBean> getRemind() {
        return remind;
    }

    public void setRemind(List<RemindBean> remind) {
        this.remind = remind;
    }

    public static class ParticipantBean{
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
    public static class RemindBean {
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

}
