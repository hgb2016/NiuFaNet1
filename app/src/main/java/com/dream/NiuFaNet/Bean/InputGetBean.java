package com.dream.NiuFaNet.Bean;

import java.io.Serializable;
import java.io.StreamTokenizer;

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
    private String isDrawTime;
    //private boolean isDrawTime;

  /*  public boolean isDrawTime() {
        return isDrawTime;
    }

    public void setDrawTime(boolean drawTime) {
        isDrawTime = drawTime;
    }*/
       public String getIsDrawTime() {
        return isDrawTime;
    }

    public void setIsDrawTime(String isDrawTime) {
        this.isDrawTime = isDrawTime;
    }


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
