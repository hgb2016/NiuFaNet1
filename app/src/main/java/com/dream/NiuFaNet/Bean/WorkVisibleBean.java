package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/25 0025.
 */
public class WorkVisibleBean {

    /**
     * data : [{"userId":"305","userName":"欧阳","headUrl":"http://api.niufa.cn:9080/niufa_chatbot/upload/20171221231916142.jpg"}]
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
         * userId : 305
         * userName : 欧阳
         * headUrl : http://api.niufa.cn:9080/niufa_chatbot/upload/20171221231916142.jpg
         */

        private String userId;
        private String userName;
        private String headUrl;
        private boolean isSource;

        public boolean isSource() {
            return isSource;
        }

        public void setSource(boolean source) {
            isSource = source;
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
