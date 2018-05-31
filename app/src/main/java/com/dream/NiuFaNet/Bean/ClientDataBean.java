package com.dream.NiuFaNet.Bean;

import java.util.ArrayList;

public class ClientDataBean {
  /**  “data”:[
                {
                        “clientId”:”1”,---客户编号
                        “clientName”:”百度”,---客户名称
                        “clientPhone”:”13800138000”,---客户电话
                        “clientAddress”:”北京”,---客户地址
                        “clientWebsite”:”www.baidu.com”,---客户网站
                        “clientRemark”:”百度”,---客户描述
                        “clientFax”:””,---客户传真
                        “createTime”:”2018-05-22 18:00:00”,---创建时间
                        “userId”:”111”,---创建人编号
                        “userName”:”张三”,---创建人名称
                        “headUrl”:””,---创建人头像
                },
                {
                        “clientId”:”1”,---客户编号
                        “clientName”:”百度”,---客户名称
                        “clientPhone”:”13800138000”,---客户电话
                        “clientAddress”:”北京”,---客户地址
                        “clientWebsite”:”www.baidu.com”,---客户网站
                        “clientRemark”:”百度”,---客户描述
                        “clientFax”:””,---客户传真
                        “createTime”:”2018-05-22 18:00:00”,---创建时间
                        “userId”:”111”,---创建人编号
                        “userName”:”张三”,---创建人名称
                        “headUrl”:””,---创建人头像
                },

            ]*/
    private String error;
    private String message;
    private ArrayList<ClientDataBean.DataBean> data;

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

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean{
        private String clientId;
        private String clientName;
        private String clientPhone;
        private String clientAddress;
        private String clientWebsite;
        private String clientRemark;
        private String clientFax;
        private String createTime;
        private String userId;
        private String userName;
        private String headUrl;
        private boolean select;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String getClientPhone() {
            return clientPhone;
        }

        public void setClientPhone(String clientPhone) {
            this.clientPhone = clientPhone;
        }

        public String getClientAddress() {
            return clientAddress;
        }

        public void setClientAddress(String clientAddress) {
            this.clientAddress = clientAddress;
        }

        public String getClientWebsite() {
            return clientWebsite;
        }

        public void setClientWebsite(String clientWebsite) {
            this.clientWebsite = clientWebsite;
        }

        public String getClientRemark() {
            return clientRemark;
        }

        public void setClientRemark(String clientRemark) {
            this.clientRemark = clientRemark;
        }

        public String getClientFax() {
            return clientFax;
        }

        public void setClientFax(String clientFax) {
            this.clientFax = clientFax;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }
    }

}
