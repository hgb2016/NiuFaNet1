package com.dream.NiuFaNet.Bean;

/**
 * Created by Administrator on 2017/8/14 0014.
 */
public class NewProResultBean {

    /**
     * error : false
     * message : 操作成功！
     */

    private String error;
    private String message;
    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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
        return "NewProResultBean{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }
}
