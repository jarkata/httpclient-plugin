package com.jarkata.plugin.client.service.impl;

import com.jarkata.plugin.client.domain.DubboConfigVo;
import com.jarkata.plugin.client.domain.DubboRequestVo;
import com.jarkata.plugin.client.service.ServiceFactory;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class DubboServiceImplTest {

    @Test
    public void getService() {

        DubboServiceImpl dubboService = ServiceFactory.getService("dubboService", DubboServiceImpl.class);
        System.out.println(dubboService);
        try {
            DubboRequestVo dubboRequestVo = new DubboRequestVo("dubbo", "127.0.0.1:20880", "com.jarkata.facade.DemoFacade", "invoke");
            Object test = dubboService.getService(dubboRequestVo, new DubboConfigVo("test"));
            System.out.println(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}