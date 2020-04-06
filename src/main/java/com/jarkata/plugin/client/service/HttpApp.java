package com.jarkata.plugin.client.service;

import com.intellij.openapi.components.ServiceManager;

public interface HttpApp {
    static HttpApp getInstance() {
        return ServiceManager.getService(HttpApp.class);
    }
}
