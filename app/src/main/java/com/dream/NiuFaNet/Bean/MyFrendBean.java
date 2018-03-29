package com.dream.NiuFaNet.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class MyFrendBean implements Serializable{

    /**
     * data : [{"id":"20","friendId":"2169","friendName":""},{"id":"21","friendId":"2182","friendName":""},{"id":"23","friendId":"2184","friendName":""}]
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

    public static class DataBean implements Serializable{
        /**
         * id : 20
         * friendId : 2169
         * friendName :
         */

        private String id;
        private String friendId;
        private String friendName;
        private String headUrl;
        private boolean isSelect;

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFriendId() {
            return friendId;
        }

        public void setFriendId(String friendId) {
            this.friendId = friendId;
        }

        public String getFriendName() {
            return friendName;
        }

        public void setFriendName(String friendName) {
            this.friendName = friendName;
        }
    }
}
