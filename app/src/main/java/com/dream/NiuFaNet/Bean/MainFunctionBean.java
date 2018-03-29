package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10 0010.
 */
public class MainFunctionBean {

    /**
     * data : [{"link":"http://api.niufa.cn:9080/Attorneys.html","actionName":"律师费计算","imgUrl":"http://api.niufa.cn:9080/img/functionioc/lvshi.png"},{"link":"http://api.niufa.cn:9080/legalfees.html","actionName":"诉讼费计算","imgUrl":"http://api.niufa.cn:9080/img/functionioc/susong.png"},{"link":"http://api.niufa.cn:9080/check.html","actionName":"工商查询","imgUrl":"http://api.niufa.cn:9080/img/functionioc/gongs.png"},{"link":"http://api.niufa.cn:9080/judicialfind.html","actionName":"鉴定机构","imgUrl":"http://api.niufa.cn:9080/img/functionioc/jianding.png"},{"link":"http://api.niufa.cn:9080/laws.html","actionName":"法规检索","imgUrl":"http://api.niufa.cn:9080/img/functionioc/fagui.png"},{"link":"http://api.niufa.cn:9080/caselaw.html","actionName":"案例检索","imgUrl":"http://api.niufa.cn:9080/img/functionioc/anli.png"},{"link":"http://api.niufa.cn:9080/find4.html","actionName":"合同模版大全","imgUrl":"http://api.niufa.cn:9080/img/functionioc/hetong.png"},{"link":"http://api.niufa.cn:9080/arbitrationfee.html","actionName":"仲裁费计算","imgUrl":"http://api.niufa.cn:9080/img/functionioc/zhongcai.png"}]
     * error : false
     * message : 请求成功！
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
         * link : http://api.niufa.cn:9080/Attorneys.html
         * actionName : 律师费计算
         * imgUrl : http://api.niufa.cn:9080/img/functionioc/lvshi.png
         */

        private String link;
        private String actionName;
        private String imgUrl;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getActionName() {
            return actionName;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
