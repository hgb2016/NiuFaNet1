package com.dream.NiuFaNet.Bean;

/**
 * Created by hou on 2018/5/8.
 */

public class CommonBean1 {
    /**
     * error : false
     * message : 操作成功！
     */

    private String error;
    private String message;
    private  String isShow;

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
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
