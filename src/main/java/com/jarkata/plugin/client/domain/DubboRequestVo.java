package com.jarkata.plugin.client.domain;

import java.io.Serializable;

/**
 * Dubbo请求对象
 */
public class DubboRequestVo implements Serializable {

    private String potocol;
    private String host;
    private String clazzUrl;
    private String clazzMethod;
    private String requestJson;
    private String version;

    public DubboRequestVo(String potocol, String host, String clazzUrl, String clazzMethod) {
        this.potocol = potocol;
        this.host = host;
        this.clazzUrl = clazzUrl;
        this.clazzMethod = clazzMethod;
    }

    public String getUrl() {
        return potocol.toLowerCase() + "://" + host;
    }


    public String getPotocol() {
        return potocol;
    }

    public void setPotocol(String potocol) {
        this.potocol = potocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClazzUrl() {
        return clazzUrl;
    }

    public void setClazzUrl(String clazzUrl) {
        this.clazzUrl = clazzUrl;
    }

    public String getClazzMethod() {
        return clazzMethod;
    }

    public void setClazzMethod(String clazzMethod) {
        this.clazzMethod = clazzMethod;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
