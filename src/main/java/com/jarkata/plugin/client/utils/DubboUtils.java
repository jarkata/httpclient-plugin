package com.jarkata.plugin.client.utils;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

public class DubboUtils {

    public static ReferenceConfig<GenericService> getReference() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-test");

   /*     RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://127.0.0.1:2181");
        registry.setId("dubbo-register");
*/
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setId("testId");
//        reference.setRegistry(registry);
        reference.setGeneric("true");
        reference.setApplication(applicationConfig);
        return reference;
    }
}
