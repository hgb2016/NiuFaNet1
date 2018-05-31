package com.dream.NiuFaNet.Bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientDescBean {
  /**  “data”: {
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
               “contactList”:[   --------常用联系人
                   {
                   “id”:”1”,--联系人编号
                   “contactsName”:”张三”,----联系人名称
                   “mobilePhone”:”13800138000”,----联系人手机
                   “email”:”ouyang@niufa.cn”,----联系人邮箱
                   “duty”:”法务”,----联系人职务
                   “contactRemark”:”描述”,----联系人描述

                   },
                   {
                   “id”:”1”,--联系人编号
                   “contactsName”:”张三”,----联系人名称
                   “mobilePhone”:”13800138000”,----联系人手机
                   “email”:”ouyang@niufa.cn”,----联系人邮箱
                   “duty”:”法务”,----联系人职务
                   “contactRemark”:”描述”,----联系人描述

                   }
               ]
               “userList”:[      --------谁可见
                       {
                       “id”:”1”, ------编号
                       “userId”:”1”, ------用户编号
                       “userName”:”李四”, ------用户名称
                       “headUrl”:””, ------用户头像
                       },
                           {
                           “id”:”1”, ------编号
                           “userId”:”1”, ------用户编号
                           “userName”:”李四”, ------用户名称
                           “headUrl”:””, ------用户头像
                           }
               ]
              }
   */
    private String error;
    private String message;
    private ClientDescBean.DataBean data;
    private String isShow;

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
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
        private ArrayList<ClientDescBean.DataBean.ContactBean> contactList;
        private ArrayList<ClientDescBean.DataBean.UserBean>  userList;

        public ArrayList<ContactBean> getContactList() {
            return contactList;
        }

        public void setContactList(ArrayList<ContactBean> contactList) {
            this.contactList = contactList;
        }

        public ArrayList<UserBean> getUserList() {
            return userList;
        }

        public void setUserList(ArrayList<UserBean> userList) {
            this.userList = userList;
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

        public static class ContactBean implements Serializable{
            private String id;
            private String contactsName;
            private String mobilePhone;
            private String email;
            private String duty;
            private String contactRemark;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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
        public static class  UserBean implements Serializable{
            private String id;
            private String userId;
            private String userName;
            private String headUrl;
            private boolean isAdd;
            private boolean isDelete;
            private boolean isEmpty;
            private boolean isSouce;

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

            public boolean isEmpty() {
                return isEmpty;
            }

            public void setEmpty(boolean empty) {
                isEmpty = empty;
            }

            public boolean isSouce() {
                return isSouce;
            }

            public void setSouce(boolean souce) {
                isSouce = souce;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }



}
