package com.jarkata.plugin.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.jarkata.plugin.client.domain.DubboConfigVo;
import com.jarkata.plugin.client.domain.DubboRequestVo;
import com.jarkata.plugin.client.service.DubboService;
import com.jarkata.plugin.client.utils.ClassUtils;
import com.jarkata.plugin.client.utils.DubboUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DubboServiceImpl implements DubboService {

    @Override
    public void execute() {

    }

    @Override
    public Object getService(DubboRequestVo requestVo, DubboConfigVo configVo) throws Exception {
        ReferenceConfig<GenericService> reference = DubboUtils.getReference();
        reference.setUrl("dubbo://127.0.0.1:20880");

        reference.setInterface(requestVo.getClazzUrl());
        GenericService object = reference.get();
        Class<?> requestClazz = ClassUtils.getClass(requestVo.getClazzUrl());
        Method method = ClassUtils.getMethod(requestClazz, requestVo.getClazzMethod());
        if (method == null) {
            return null;
        }

        List<String> parameterTypeList = new ArrayList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            parameterTypeList.add(parameterType.getName());
        }
        String requestJson = requestVo.getRequestJson();
        List<Object> objects = new ArrayList<>();
        if (StringUtils.isNotBlank(requestJson)) {
            objects = JSON.parseArray(requestJson, parameterTypes);
        } else {
            for (String parameter : parameterTypeList) {
                objects.add(null);
            }
        }
        return object.$invoke(requestVo.getClazzMethod(),
                parameterTypeList.toArray(new String[]{}),
                objects.toArray());

    }

}
