package com.jarkata.plugin.client.domain;

import com.alibaba.fastjson.JSON;
import com.jarkata.plugin.client.PluginConstants;
import com.jarkata.plugin.client.utils.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Dubbo配置
 */
public class DubboConfigVo implements Serializable {
    private String appname;
    private String contextPath;
    private String linkModel;
    private String registerAddress;

    public DubboConfigVo() {
    }

    public DubboConfigVo(String appname) {
        this.appname = appname;
    }

    public DubboConfigVo(String appname, String contextPath) {
        this.appname = appname;
        this.contextPath = contextPath;
    }

    public static DubboConfigVo getInstance() {
        String content = FileUtils.getConfig(PluginConstants.PLUGIN_DUBBO_CONFIG);
        if (StringUtils.isNotBlank(content)) {
            return JSON.parseObject(content, DubboConfigVo.class);
        }
        return new DubboConfigVo();
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

    public String getLinkModel() {
        return linkModel;
    }

    public void setLinkModel(String linkModel) {
        this.linkModel = linkModel;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }
}
