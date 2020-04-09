package com.jarkata.plugin.client.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * CLass工具类
 */
public class ClassUtils {

    /**
     * 根据className获取class对象
     *
     * @param clzzName
     * @return
     * @throws Exception
     */
    public static Class<?> getClass(String clzzName) throws Exception {
        if (StringUtils.isNotBlank(clzzName)) {
            return null;
        }
        ClassLoader classLoader = getUrlClassLoader();
        if (classLoader == null) {
            return null;
        }
        return classLoader.loadClass(clzzName);
    }

    /**
     * 获取选择的方法
     *
     * @param selectClazz
     * @param method
     * @return
     */
    public static Method getMethod(Class<?> selectClazz, String method) {
        if (selectClazz == null || StringUtils.isNotBlank(method)) {
            return null;
        }
        for (Method declaredMethod : selectClazz.getDeclaredMethods()) {
            if (method.equals(declaredMethod.getName())) {
                return declaredMethod;
            }
        }
        return null;
    }

    public static URLClassLoader getUrlClassLoader() throws Exception {
        String extPath = FileUtils.getExtPath(FileUtils.FILE_DUBBO);
        File directorys = new File(extPath);
        File[] jarFileList = directorys.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFileList == null) {
            return null;
        }
        List<URL> jarUrlList = new ArrayList<>();
        for (File file : jarFileList) {
            URL url = file.toURI().toURL();
            jarUrlList.add(url);
        }
        return URLClassLoader.newInstance(jarUrlList.toArray(new URL[]{}));
    }

}
