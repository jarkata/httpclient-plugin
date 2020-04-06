package com.jarkata.plugin.client.service.impl;

import com.jarkata.plugin.client.domain.DubboConfigVo;
import com.jarkata.plugin.client.domain.DubboRequestVo;
import com.jarkata.plugin.client.service.DubboService;
import com.jarkata.plugin.client.utils.ClassUtils;
import com.jarkata.plugin.client.utils.DubboUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

public class DubboServiceImpl implements DubboService {

    @Override
    public void execute() {

    }

    @Override
    public Object getService(DubboRequestVo requestVo, DubboConfigVo configVo) throws Exception {
        ReferenceConfig<?> reference = DubboUtils.getReference();
        reference.setUrl("dubbo://127.0.0.1:20880");
        Class<?> requestClazz = ClassUtils.getClass(requestVo.getClazzUrl());
        reference.setInterface(requestClazz);
        return reference.get();
    }

}
