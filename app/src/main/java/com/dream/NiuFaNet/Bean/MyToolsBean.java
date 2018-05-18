package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by hou on 2018/5/5.
 */

public class MyToolsBean {

    /**
     * body : {"find":[{"url":"http://api.niufa.cn:9080/check.html","actionName":"工商查询","actionType":"1","actionPic":"http://api.niufa.cn:9080/img/functionioc/gongs.png"},{"url":"","actionName":"鉴定机构","actionType":"1","actionPic":"http://api.niufa.cn:9080/img/functionioc/jianding.png"},{"url":"http://api.niufa.cn:9080/laws.html","actionName":"法规检索","actionType":"1","actionPic":"http://api.niufa.cn:9080/img/functionioc/fagui.png"},{"url":"http://api.niufa.cn:9080/caselaw.html","actionName":"案例检索","actionType":"1","actionPic":"http://api.niufa.cn:9080/img/functionioc/anli.png"},{"url":"","actionName":"法院查询","actionType":"1","actionPic":"http://api.niufa.cn:9080/img/functionioc/fayuan.png"},{"url":"","actionName":"仲裁委查询","actionType":"1","actionPic":"http://api.niufa.cn:9080/img/functionioc/zccx.png"}],"calculate":[{"url":"http://api.niufa.cn:9080/Attorneys.html","actionName":"律师费计算","actionType":"2","actionPic":"http://api.niufa.cn:9080/img/functionioc/lvshi.png"},{"url":"http://api.niufa.cn:9080/legalfees.html","actionName":"诉讼费计算","actionType":"2","actionPic":"http://api.niufa.cn:9080/img/functionioc/susong.png"},{"url":"http://api.niufa.cn:9080/arbitrationfee.html","actionName":"仲裁费计算","actionType":"2","actionPic":"http://api.niufa.cn:9080/img/functionioc/zhongcai.png"},{"url":"http://api.niufa.cn:9080/interest.html","actionName":"违约金计算","actionType":"2","actionPic":"http://api.niufa.cn:9080/img/functionioc/weiyue.png"},{"url":"http://api.niufa.cn:9080/date.html","actionName":"天数日期计算","actionType":"2","actionPic":"http://api.niufa.cn:9080/img/functionioc/riqi.png"},{"url":"http://api.niufa.cn:9080/financingbd.html","actionName":"投资股权计算","actionType":"2","actionPic":"http://api.niufa.cn:9080/img/functionioc/guquan.png"}],"labor":[{"url":"http://api.niufa.cn:9080/find4.html","actionName":"合同模版大全","actionType":"3","actionPic":"http://api.niufa.cn:9080/img/functionioc/hetong.png"},{"url":"http://api.niufa.cn:9080/personal.html","actionName":"个税计算","actionType":"3","actionPic":"http://api.niufa.cn:9080/img/functionioc/geshui.png"},{"url":"http://api.niufa.cn:9080/departure.html","actionName":"离职补偿金","actionType":"3","actionPic":"http://api.niufa.cn:9080/img/functionioc/lizhi.png"},{"url":"http://api.niufa.cn:9080/maternity_leave.html","actionName":"产假计算","actionType":"3","actionPic":"http://api.niufa.cn:9080/img/functionioc/canjia.png"},{"url":"http://api.niufa.cn:9080/inductrial.jsp","actionName":"交通赔偿金","actionType":"3","actionPic":"http://api.niufa.cn:9080/img/functionioc/jiaotong.png"}]}
     * error : false
     * message : 请求成功！
     */

    private List<MyToolsBean.DataBean> data;
    private String error;
    private String message;

    public List<DataBean> getDataBean() {
        return data;
    }

    public void setDataBean(List<DataBean> dataBean) {
        this.data = dataBean;
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

    public static class DataBean {
        private String toolId;
        private String link;
        private String actionName;
        private String imgUrl;
        private boolean isEdited;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean isEdited() {
            return isEdited;
        }

        public void setEdited(boolean edited) {
            isEdited = edited;
        }

        public String getToolId() {
            return toolId;
        }

        public void setToolId(String toolId) {
            this.toolId = toolId;
        }

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
