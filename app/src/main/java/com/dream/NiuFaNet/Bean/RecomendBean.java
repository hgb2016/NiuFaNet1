package com.dream.NiuFaNet.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/9 0009.
 */
public class RecomendBean implements Serializable{

    /**
     * body : [{"id":1,"question":"算利息","url":""},{"id":2,"question":"算律师费","url":""},{"id":3,"question":"算诉讼费","url":""},{"id":4,"question":"帮我查工商","url":""},{"id":5,"question":"帮我查司法鉴定机构","url":""},{"id":6,"question":"帮我查法律法规","url":""},{"id":7,"question":"帮我查案例","url":""},{"id":8,"question":"帮我查法院","url":""}]
     * error : false
     * message : 请求成功！
     */

    private String error;
    private String message;
    private List<BodyBean> body;

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

    public List<BodyBean> getBody() {
        return body;
    }

    public void setBody(List<BodyBean> body) {
        this.body = body;
    }

    public static class BodyBean implements Serializable{
        /**
         * id : 1
         * question : 算利息
         * url :
         */

        private int id;
        private String question;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
