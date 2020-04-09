package com.jarkata.plugin.client.service.impl;

import com.jarkata.plugin.client.domain.DubboRequestVo;
import com.jarkata.plugin.client.service.BeanFactory;
import com.jarkata.plugin.client.utils.ClassUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class DubboServiceImplTest {

    @Test
    public void getService() {

        DubboServiceImpl dubboService = BeanFactory.getBean("dubboService", DubboServiceImpl.class);
        System.out.println(dubboService);
        try {
            DubboRequestVo dubboRequestVo = new DubboRequestVo("dubbo", "127.0.0.1:20880", "com.jarkata.facade.DemoFacade", "invoke");
            Object test = dubboService.getService(dubboRequestVo);
            System.out.println(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testUrlClassLoader() throws Exception {
        Class<?> aClass = ClassUtils.getClass("com.jarkata.facade.DemoFacade");
        System.out.println(aClass
        );
    }


    @Test
    public void testClassLoader() {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        try {
            Enumeration<URL> resources = systemClassLoader.getResources("META-INF/dubbo/internal/org.apache.dubbo.common.extension.ExtensionFactory");
            System.out.println(resources);
            System.out.println(resources.nextElement());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}