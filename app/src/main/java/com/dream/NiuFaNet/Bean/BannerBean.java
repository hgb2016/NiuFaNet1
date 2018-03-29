package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10 0010.
 */
public class BannerBean {

    /**
     * data : [{"imgUrl":"http://api.niufa.cn:9080/niufa_chatbot/upload/appindex1.png","link":""},{"imgUrl":"http://api.niufa.cn:9080/niufa_chatbot/upload/appindex1.png","link":""},{"imgUrl":"http://api.niufa.cn:9080/niufa_chatbot/upload/appindex1.png","link":""}]
     * error : false
     * message : 请求成功
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
         * imgUrl : http://api.niufa.cn:9080/niufa_chatbot/upload/appindex1.png
         * link :
         */

        private String imgUrl;
        private String link;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
