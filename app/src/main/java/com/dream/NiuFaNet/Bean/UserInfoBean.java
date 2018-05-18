package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21 0021.
 */
public class UserInfoBean {
    private String error;
    private String message;
    private UserBean body;

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

    public UserBean getBody() {
        return body;
    }

    public void setBody(UserBean body) {
        this.body = body;
    }

    public static class UserBean {
        private String userId;
        private String userName;
        private String mobilePhone;
        private String email;
        private String headUrl;
        private String sex;
        private String company;
        private String duty;
        private String hourRate;
        private String friendRemark;
        private String friendId;
        private String address;
        public String getFriendRemark() {
            return friendRemark;
        }

        public void setFriendRemark(String friendRemark) {
            this.friendRemark = friendRemark;
        }

        public String getFriendId() {
            return friendId;
        }

        public void setFriendId(String friendId) {
            this.friendId = friendId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getHourRate() {
            return hourRate;
        }

        public void setHourRate(String hourRate) {
            this.hourRate = hourRate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }
    }
}
