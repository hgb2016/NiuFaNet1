package com.dream.NiuFaNet.Bean;

/**
 * Created by Administrator on 2017/10/13 0013.
 */
public class VersionBean {

    /**
     * version : 1.1.2
     * url :
     * error : false
     * message :
     */

    private String version;
    private String url;
    private String error;
    private String message;
    private String versionRemark;

    public String getVersionRemark() {
        return versionRemark;
    }

    public void setVersionRemark(String versionRemark) {
        this.versionRemark = versionRemark;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
