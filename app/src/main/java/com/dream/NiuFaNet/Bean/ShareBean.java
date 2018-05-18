package com.dream.NiuFaNet.Bean;

/**
 * Created by Administrator on 2017/9/13 0013.
 */
public class ShareBean {

    /**
     * title : 测试
     * content : 测试测试测试
     * picUrl : http://api.niufa.cn:9080/niufa_chatbot/upload/niufa.jpg
     * error : false
     * message :
     */

    private String title;
    private String content;
    private String picUrl;
    private String error;
    private String message;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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
}
