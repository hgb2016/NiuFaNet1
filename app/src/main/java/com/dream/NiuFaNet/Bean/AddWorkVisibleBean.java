package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public class AddWorkVisibleBean {

    /**
     * visible : [{"user_id":"305"}]
     * userId : 2181
     */

    private String userId;
    private List<VisibleBean> visible;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<VisibleBean> getVisible() {
        return visible;
    }

    public void setVisible(List<VisibleBean> visible) {
        this.visible = visible;
    }

    public static class VisibleBean {
        /**
         * user_id : 305
         */

        private String user_id;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
