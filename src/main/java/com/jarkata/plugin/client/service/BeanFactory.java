package com.jarkata.plugin.client.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务创建工厂
 */
public class BeanFactory {

    private static final ConcurrentHashMap<String, Object> STRING_OBJECT_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    private static final Object lock = new Object();

    /**
     * 获取Bean
     *
     * @param beanNmae
     * @param clzz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanNmae, Class<T> clzz) {
        Object serviceBean = STRING_OBJECT_CONCURRENT_HASH_MAP.get(beanNmae);
        if (serviceBean == null) {
            synchronized (lock) {
                try {
                    serviceBean = clzz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
                STRING_OBJECT_CONCURRENT_HASH_MAP.put(beanNmae, serviceBean);
            }
        }
        return (T) serviceBean;
    }

    /**
     * 仅根据类型获取Service
     *
     * @param clzz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clzz) {
        if (clzz == null) {
            return null;
        }
        String clzzName = clzz.getName();
        return getBean(clzzName, clzz);
    }

}
