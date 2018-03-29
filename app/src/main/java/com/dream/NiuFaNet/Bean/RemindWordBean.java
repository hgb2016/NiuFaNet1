package com.dream.NiuFaNet.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24 0024.
 */
public class RemindWordBean {

    /**
     * data : ["9点到11点开会","明天拜访客户","星期五下午六点聚餐"]
     * error : false
     * message :
     */

    private String error;
    private String message;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
