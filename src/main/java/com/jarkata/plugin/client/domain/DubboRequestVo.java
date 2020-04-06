package com.jarkata.plugin.client.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Dubbo请求对象
 */
@Getter
@Setter
@ToString
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


}
