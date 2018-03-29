package com.dream.NiuFaNet.Bean;

/**
 * Created by Administrator on 2017/8/14 0014.
 */
public class HeadPortraitBean {

    /**
     * error : false
     * message : 操作成功！
     */

    private String error;
    private String message;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
