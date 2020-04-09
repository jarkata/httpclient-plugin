package com.jarkata.plugin.client.service;

import com.jarkata.plugin.client.domain.DubboRequestVo;

public interface DubboService {

    Object getService(DubboRequestVo requestVo) throws Exception;
}
