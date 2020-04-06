package com.jarkata.plugin.client.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Dubbo配置
 */
@Setter
@Getter
@ToString
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
}
