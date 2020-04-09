package com.jarkata.plugin.client.domain;

import java.io.Serializable;

/**
 * Http 请求VO
 */
public class HttpRequestVo implements Serializable {

    private String httpUrl;
    private String httpHeader;
    private String httpParameter;

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public String getHttpParameter() {
        return httpParameter;
    }

    public void setHttpParameter(String httpParameter) {
        this.httpParameter = httpParameter;
    }
}
