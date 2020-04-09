package com.jarkata.plugin.client.utils;

import com.jarkata.plugin.client.domain.DubboConfigVo;
import com.jarkata.plugin.client.enums.LinkModelEnum;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

public class DubboUtils {

    public static ReferenceConfig<GenericService> getReference() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-test");

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setId("testId");
        reference.setGeneric("true");
        reference.setApplication(applicationConfig);
        return reference;
    }
}
