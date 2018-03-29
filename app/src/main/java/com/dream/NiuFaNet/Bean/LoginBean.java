package com.dream.NiuFaNet.Bean;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
public class LoginBean {

    /**
     * body : {"userId":"26","userName":"","mobilePhone":"18277750576","email":"","headUrl":"","sex":"","company":"","duty":""}
     * error : false
     * message : 登录成功！
     */

    private BodyBean body;
    private String error;
    private String message;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
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

    public static class BodyBean {
        /**
         * userId : 26
         * userName :
         * mobilePhone : 18277750576
         * email :
         * headUrl :
         * sex :
         * company :
         * duty :
         */

        private String userId;
        private String userName;
        private String mobilePhone;
        private String email;
        private String headUrl;
        private String sex;
        private String company;
        private String duty;
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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
