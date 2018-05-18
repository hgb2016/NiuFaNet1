package com.dream.NiuFaNet.Bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/12 0012.
 */
public class FriendNoticeBean implements Serializable{
    /**
     * error : false
     * message : 操作成功！
     */

    private String error;
    private String message;
    private List<NoticeBean> data;
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

    public List<NoticeBean> getData() {
        return data;
    }

    public void setData(List<NoticeBean> data) {
        this.data = data;
    }

    public static class  NoticeBean{
        private String id;
        private String friendId;
        private String friendName;
        private String acceptStatus;
        private String isShow;
        private String headUrl;

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
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

        public String getAcceptStatus() {
            return acceptStatus;
        }

        public void setAcceptStatus(String acceptStatus) {
            this.acceptStatus = acceptStatus;
        }

        public String getIsShow() {
            return isShow;
        }

        public void setIsShow(String isShow) {
            this.isShow = isShow;
        }
    }

}
