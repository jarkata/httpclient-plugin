package com.jarkata.plugin.client.service;

import com.jarkata.plugin.client.domain.DubboConfigVo;
import com.jarkata.plugin.client.domain.DubboRequestVo;

import java.util.Map;

public interface DubboService {

    void execute();


    Object getService(DubboRequestVo requestVo, DubboConfigVo configVo) throws Exception;
}
