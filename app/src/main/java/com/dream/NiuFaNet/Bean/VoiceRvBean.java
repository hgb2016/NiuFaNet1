package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/10 0010.
 */
public class VoiceRvBean {

    /**
     * error : true
     * message :
     * body : [{"img":"","data":[{"name":"帮我查一下XXX公司的工商信息"},{"name":"帮我查一下XXX公司的工商信息"},{"name":"帮我查一下XXX公司的工商信息"}],"name":"查询","id":"1"},{"img":"","data":[{"name":"律师费计算"},{"name":"帮我算一下诉讼费"},{"name":"帮我算一下仲裁费"}],"name":"计算","id":"2"},{"img":"","data":[{"name":"提醒我明天上午开庭"},{"name":"创建日程星期五下午见当事人"},{"name":"添加日程下周三去法院调取资料"}],"name":"日程","id":"3"}]
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

    public static class BodyBean {
        /**
         * img :
         * data : [{"name":"帮我查一下XXX公司的工商信息"},{"name":"帮我查一下XXX公司的工商信息"},{"name":"帮我查一下XXX公司的工商信息"}]
         * name : 查询
         * id : 1
         */

        private String img;
        private String name;
        private String id;
        private List<DataBean> data;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * name : 帮我查一下XXX公司的工商信息
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
