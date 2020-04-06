package com.jarkata.plugin.client.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Http 请求VO
 */
@Setter
@Getter
@ToString
public class HttpRequestVo implements Serializable {

    private String httpUrl;
    private String httpHeader;
    private String httpParameter;
}
