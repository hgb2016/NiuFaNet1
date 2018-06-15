package com.dream.NiuFaNet.Bean;

public class EditCount {
    /**
     * error : false
     * message : 操作成功！
     */

    private String error;
    private String message;
    private String isEditCount;

    public String getIsEditCount() {
        return isEditCount;
    }

    public void setIsEditCount(String isEditCount) {
        this.isEditCount = isEditCount;
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
