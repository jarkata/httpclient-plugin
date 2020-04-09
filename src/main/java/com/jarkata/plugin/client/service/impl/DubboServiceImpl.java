package com.jarkata.plugin.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.jarkata.plugin.client.PluginConstants;
import com.jarkata.plugin.client.domain.DubboConfigVo;
import com.jarkata.plugin.client.domain.DubboRequestVo;
import com.jarkata.plugin.client.enums.LinkModelEnum;
import com.jarkata.plugin.client.service.BeanFactory;
import com.jarkata.plugin.client.service.DubboService;
import com.jarkata.plugin.client.utils.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理dubbo接口调用
 */
public class DubboServiceImpl implements DubboService {

    private static final ReferenceConfig<GenericService> reference = new ReferenceConfig<>();

    /**
     * 调用远程服务处理
     *
     * @param requestVo
     * @return
     * @throws Exception
     */
    @Override
    public Object getService(DubboRequestVo requestVo) throws Exception {

        if (StringUtils.isBlank(requestVo.getClazzUrl())) {
            throw new IllegalArgumentException("className is Empty");
        }

        GenericService genericService = getObject(requestVo);

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
        return genericService.$invoke(requestVo.getClazzMethod(),
                parameterTypeList.toArray(new String[]{}),
                objects.toArray());

    }

    /**
     * 获取GenericService服务
     *
     * @param requestVo
     * @return
     */
    public GenericService getObject(DubboRequestVo requestVo) {
        DubboConfigVo instance = DubboConfigVo.getInstance();
        ApplicationConfig applicationConfig = BeanFactory.getBean(ApplicationConfig.class);
        applicationConfig.setName("idea-plugin-dubbo-client");
        reference.setId("testId");
        reference.setGeneric("true");
        reference.setApplication(applicationConfig);
        reference.setInterface(requestVo.getClazzUrl());
        if (LinkModelEnum.P2P.getCode().equals(instance.getLinkModel())) {
            reference.setUrl(requestVo.getUrl());
        } else {
            RegistryConfig registry = BeanFactory.getBean(RegistryConfig.class);
            registry.setAddress(instance.getRegisterAddress());
            registry.setId("ideaplugin-dubbo-register");
            reference.setRegistry(registry);
        }
        if (StringUtils.isNotBlank(requestVo.getVersion()) && !PluginConstants.DUBBO_VERSION_DEFAULT.equals(requestVo.getVersion())) {
            reference.setVersion(requestVo.getVersion());
        }
        return reference.get();
    }

}
