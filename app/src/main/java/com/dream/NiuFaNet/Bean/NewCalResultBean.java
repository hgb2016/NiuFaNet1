package com.dream.NiuFaNet.Bean;

/**
 * Created by Administrator on 2017/8/14 0014.
 */
public class NewCalResultBean {

    /**
     * error : false
     * message : 操作成功！
     */

    private String error;
    private String message;
    private String scheduleId;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
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

    @Override
    public String toString() {
        return "NewCalResultBean{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", scheduleId='" + scheduleId + '\'' +
                '}';
    }
}
