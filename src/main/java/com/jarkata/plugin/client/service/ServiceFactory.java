package com.jarkata.plugin.client.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务创建工厂
 */
public class ServiceFactory {

    private static final ConcurrentHashMap<String, Object> singleServiceFactoryMap = new ConcurrentHashMap<>();

    private static final Object lock = new Object();

    /**
     * 获取Bean
     *
     * @param beanNmae
     * @param clzz
     * @param <T>
     * @return
     */
    public static <T> T getService(String beanNmae, Class<T> clzz) {

        Object serviceBean = singleServiceFactoryMap.get(beanNmae);
        if (serviceBean == null) {
            synchronized (lock) {
                try {
                    serviceBean = clzz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
                singleServiceFactoryMap.put(beanNmae, serviceBean);
            }
        }
        return (T) serviceBean;
    }

}
