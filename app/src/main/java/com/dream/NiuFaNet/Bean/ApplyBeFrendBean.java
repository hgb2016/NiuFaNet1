package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6 0006.
 */
public class ApplyBeFrendBean {

    /**
     * data : [{"id":"14","friendId":"29","friendName":"Toma"}]
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
         * id : 14
         * friendId : 29
         * friendName : Toma
         */

        private String id;
        private String friendId;
        private String friendName;
        private String method;
        private String eventStr;

        public String getEventStr() {
            return eventStr;
        }

        public void setEventStr(String eventStr) {
            this.eventStr = eventStr;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
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
