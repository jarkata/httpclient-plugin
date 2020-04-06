package com.jarkata.plugin.client.service;

import com.jarkata.plugin.client.domain.DubboConfigVo;
import com.jarkata.plugin.client.domain.DubboRequestVo;

public interface DubboService {

    void execute();


    Object getService(DubboRequestVo requestVo, DubboConfigVo configVo) throws Exception;
}
