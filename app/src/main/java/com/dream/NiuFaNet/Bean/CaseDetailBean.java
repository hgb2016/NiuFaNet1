package com.dream.NiuFaNet.Bean;

import java.util.List;

public class CaseDetailBean {
    /**
     * “data”:
             {
             "caseId":”11”,--------案件编号
             "caseName"：”深圳知识产权”， ----------案件名称
             "price":”10-25万” ,--------案件标的
             "province":”广东”,-------省份
             "city":”深圳”,-------城市
             "endTime":”2018-07-04 10:00”,-------竞标结束时间
             "caseRemark ":”深圳”,-------案情详细描述
             "isBid ":”1”,------- 1为不需要竞标，2为可以竞标
             }
             “userList”:[
                 {
                 “userName”:”李*”, ------用户名称
                 “headUrl”:””, ------用户头像地址
                 “userPhone”:”135*******”, ------用户手机
                 “status”:”1”, ------1为待处理,2为选中,3为不合格

                 },

     ]

     */
    private String error;
    private String message;
    private CaseDetailBean.DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private String caseId;
        private String caseName;
        private String price;
        private String province;
        private String city;
        private String endTime;
        private String caseRemark;
        private String isBid;
        private List<DataBean.UserBean> userList;


        public List<DataBean.UserBean> getUserList() {
            return userList;
        }

        public void setUserList(List<DataBean.UserBean> userList) {
            this.userList = userList;
        }

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }

        public String getCaseName() {
            return caseName;
        }

        public void setCaseName(String caseName) {
            this.caseName = caseName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCaseRemark() {
            return caseRemark;
        }

        public void setCaseRemark(String caseRemark) {
            this.caseRemark = caseRemark;
        }

        public String getIsBid() {
            return isBid;
        }

        public void setIsBid(String isBid) {
            this.isBid = isBid;
        }

        public static class UserBean{
            private String userName;
            private String headUrl;
            private String userPhone;
            private String status;

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

            public String getUserPhone() {
                return userPhone;
            }

            public void setUserPhone(String userPhone) {
                this.userPhone = userPhone;
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
