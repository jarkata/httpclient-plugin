package com.jarkata.plugin.client.domain;

import java.io.Serializable;

/**
 * Dubbo配置
 */
public class DubboConfigVo implements Serializable {
    private String appname;
    private String contextPath;

    public DubboConfigVo(String appname) {
        this.appname = appname;
    }

    public DubboConfigVo(String appname, String contextPath) {
        this.appname = appname;
        this.contextPath = contextPath;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
