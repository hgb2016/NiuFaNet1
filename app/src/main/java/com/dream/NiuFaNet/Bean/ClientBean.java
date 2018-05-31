package com.dream.NiuFaNet.Bean;

import java.util.ArrayList;

public class ClientBean {
    /**
    “data”: {
			“clientId”:”1”,---客户编号
			“clientName”:”百度”,---客户名称
			“clientPhone”:”13800138000”,---客户电话
            “clientAddress”:”北京”,---客户地址
            “clientWebsite”:”www.baidu.com”,---客户网站
            “clientRemark”:”百度”,---客户描述
            “clientFax”:””,---客户传真
            “userId”:”111”,---创建人编号
            “contactList”:[   --------常用联系人
                    {
                    “contactsName”:”张三”,----联系人名称
                    “mobilePhone”:”13800138000”,----联系人手机
                    “email”:”ouyang@niufa.cn”,----联系人邮箱
                    “duty”:”法务”,----联系人职务
                    “contactRemark”:”描述”,----联系人描述

                    },
                    {
                    “contactsName”:”张三”,----联系人名称
                    “mobilePhone”:”13800138000”,----联系人手机
                    “email”:”ouyang@niufa.cn”,----联系人邮箱
                    “duty”:”法务”,----联系人职务
                    “contactRemark”:”描述”,----联系人描述

                    }
            ]
            “userList”:[      --------谁可见
                    {

            “userId”:”1”, ------用户编号

                    },
                    {
            “userId”:”1”, ------用户编号
                    }
            ]

                }
     */
    private String clientId;
    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private String clientWebsite;
    private String clientFax;
    private String clientRemark;
    private String userId;
    private ArrayList<ClientBean.ContactBean> contactData;
    private ArrayList<ClientBean.UserBean> userData;

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

    public String getClientFax() {
        return clientFax;
    }

    public void setClientFax(String clientFax) {
        this.clientFax = clientFax;
    }

    public String getClientRemark() {
        return clientRemark;
    }

    public void setClientRemark(String clientRemark) {
        this.clientRemark = clientRemark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public ArrayList<ContactBean> getContactData() {
        return contactData;
    }

    public void setContactData(ArrayList<ContactBean> contactData) {
        this.contactData = contactData;
    }

    public ArrayList<UserBean> getUserData() {
        return userData;
    }

    public void setUserData(ArrayList<UserBean> userData) {
        this.userData = userData;
    }

    public static class ContactBean{
        private String contactsName;
        private String mobilePhone;
        private String email;
        private String duty;
        private String contactRemark;
        private String id;
        private String clientId;
        private String userId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getContactsName() {
            return contactsName;
        }

        public void setContactsName(String contactsName) {
            this.contactsName = contactsName;
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

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }

        public String getContactRemark() {
            return contactRemark;
        }

        public void setContactRemark(String contactRemark) {
            this.contactRemark = contactRemark;
        }
    }
    public static class  UserBean{
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
