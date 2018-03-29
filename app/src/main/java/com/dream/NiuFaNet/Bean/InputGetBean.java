package com.dream.NiuFaNet.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/24 0024.
 */
public class InputGetBean implements Serializable{


    /**
     * beginDate : 2017-11-24 15:00:00
     * endDate : 2017-11-24 16:00:00
     * content : 开会
     */

    private String beginDate;
    private String endDate;
    private String content;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
