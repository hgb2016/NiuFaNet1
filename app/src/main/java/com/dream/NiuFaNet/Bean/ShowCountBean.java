package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by hou on 2018/4/24.
 */

public class ShowCountBean {
    /**
     * data : [{"userId":2163,"userName":"","mobilePhone":"13128861359"}]
     * error : false
     * message : 请求成功！
     */

    private String error;
    private String message;
    private String showCount;

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

    public String getShowCount() {
        return showCount;
    }

    public void setShowCount(String showCount) {
        this.showCount = showCount;
    }
}
